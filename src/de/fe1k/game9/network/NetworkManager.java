package de.fe1k.game9.network;

import de.fe1k.game9.events.*;
import de.fe1k.game9.exceptions.BadNetworkingException;
import de.fe1k.game9.exceptions.NetworkAlreadyStartedException;
import de.fe1k.game9.exceptions.NetworkNotStartedException;
import de.nerogar.noise.network.Connection;
import de.nerogar.noise.network.Packets;
import de.nerogar.noise.network.ServerThread;
import de.nerogar.noise.network.packets.Packet;

import java.io.IOException;
import java.net.BindException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class NetworkManager {
	private static final int CHANNEL_EVENTS = 0x4200;
	private static final int CHANNEL_META   = 0x4201;

	private boolean server;
	private static boolean started;

	private ServerThread     serverThread;
	private Connection       serverConnection;
	private List<Connection> clients;
	private int              clientId;

	private EventListener<EventUpdate>    eventUpdateServer = this::updateServer;
	private EventListener<EventUpdate>    eventUpdateClient = this::updateClient;
	private EventListener<EventToClients> eventToClients    = this::broadcastEvent;
	private EventListener<EventToServer>  eventToServer     = this::sendEvent;

	static {
		Packets.addPacket(CHANNEL_EVENTS, PacketNetworkedEvent.class);
		Packets.addPacket(CHANNEL_META, PacketSetClientId.class);
	}

	public NetworkManager() {
		clients = new ArrayList<>();
	}

	private void handleEventPackets(Packet packet) {
		if (!(packet instanceof PacketNetworkedEvent)) {
			throw new BadNetworkingException();
		}
		PacketNetworkedEvent eventPacket = (PacketNetworkedEvent) packet;
		Event.trigger(eventPacket.event);
	}

	private void updateServer(EventUpdate event) {
		boolean clientIdsDirty = false;
		// check for disconnected clients
		Iterator<Connection> clientIter = clients.iterator();
		while (clientIter.hasNext()) {
			Connection client = clientIter.next();
			if (client.isClosed()) {
				clientIter.remove();
				clientIdsDirty = true;
				Event.trigger(new EventClientDisconnected(client));
			}
		}
		// accept new clients
		for (Connection client : serverThread.getNewConnections()) {
			clients.add(client);
			clientIdsDirty = true;
			Event.trigger(new EventClientConnected(client));
		}
		if (clientIdsDirty) updateClientIds();
		// retrieve and process data from clients
		for (Connection client : clients) {
			client.pollPackets(false);
			client.getPackets(CHANNEL_EVENTS).forEach(this::handleEventPackets);
			client.flushPackets();
		}
	}

	private void updateClient(EventUpdate event) {
		if (serverConnection.isClosed()) {
			shutdown();
			Event.trigger(new EventDisconnected(serverConnection));
			return;
		}
		serverConnection.pollPackets(true);
		serverConnection.getPackets(CHANNEL_EVENTS).forEach(this::handleEventPackets);
		for (Packet packet : serverConnection.getPackets(CHANNEL_META)) {
			if (packet instanceof PacketSetClientId) {
				clientId = ((PacketSetClientId) packet).clientId;
			}
		}
		serverConnection.flushPackets();
	}

	private void broadcastEvent(EventToClients event) {
		serverThread.broadcast(new PacketNetworkedEvent(event));
	}

	private void sendEvent(EventToServer event) {
		serverConnection.send(new PacketNetworkedEvent(event));
	}

	public void startServer(int port) throws BindException {
		if (started) throw new NetworkAlreadyStartedException();

		serverThread = new ServerThread(port);
		server = true;
		started = true;
		clientId = -1;
		Event.register(EventUpdate.class, eventUpdateServer);
		Event.register(EventToClients.class, eventToClients);
	}

	public void startClient(String host, int port) throws IOException {
		if (started) throw new NetworkAlreadyStartedException();

		serverConnection = new Connection(new Socket(host, port));
		server = false;
		started = true;
		Event.register(EventUpdate.class, eventUpdateClient);
		Event.register(EventToServer.class, eventToServer);
		Event.trigger(new EventConnected(serverConnection));
	}

	public boolean isServer() {
		if (!started) throw new NetworkNotStartedException();
		return server;
	}

	public void shutdown() {
		if (!started) throw new NetworkNotStartedException();

		if (server) {
			serverThread.stopThread();
			clients.forEach(Connection::close);
			clients.clear();
		} else {
			serverConnection.close();
		}
		started = false;
		Event.unregister(EventUpdate.class, eventUpdateServer);
		Event.unregister(EventUpdate.class, eventUpdateClient);
		Event.unregister(EventToServer.class, eventToServer);
		Event.unregister(EventToClients.class, eventToClients);
	}

	public boolean isStarted() {
		return started;
	}

	public List<Connection> getClients() {
		if (!isServer()) throw new IllegalStateException("No server running.");
		return clients;
	}

	public Connection getServer() {
		if (isServer()) throw new IllegalStateException("Is server itself.");
		return serverConnection;
	}

	private void updateClientIds() {
		for (int i = 0; i < clients.size(); i++) {
			Connection client = clients.get(i);
			client.send(new PacketSetClientId(i));
		}
	}

}
