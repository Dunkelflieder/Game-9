package de.fe1k.game9.systems;

import de.fe1k.game9.events.*;
import de.fe1k.game9.network.PacketNetworkedEvent;
import de.nerogar.noise.network.Connection;
import de.nerogar.noise.network.Packets;
import de.nerogar.noise.network.ServerThread;

import java.net.BindException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SystemNetwork implements GameSystem {

	private ServerThread     server;
	private List<Connection> clients;

	private EventListener<EventUpdate>    eventUpdate;
	private EventListener<EventNetworked> eventNetworked;

	@Override
	public void start() {
		clients = new ArrayList<>();
		try {
			server = new ServerThread(4200);
		} catch (BindException e) {
			e.printStackTrace();
		}
		eventUpdate = this::update;
		eventNetworked = (event) -> server.broadcast(new PacketNetworkedEvent(event));
		Event.register(EventUpdate.class, eventUpdate);
		Event.register(EventNetworked.class, eventNetworked);

		Packets.addPacket(1, PacketNetworkedEvent.class);
	}

	private void update(EventUpdate event) {
		for (Connection connection : server.getNewConnections()) {
			clients.add(connection);
			Event.trigger(new EventClientConnected(connection));
		}
		Iterator<Connection> iterClients = clients.iterator();
		while (iterClients.hasNext()) {
			Connection client = iterClients.next();
			if (client.isClosed()) {
				iterClients.remove();
				Event.trigger(new EventClientDisconnected(client));
			}
		}
		for (Connection client : clients) {
			client.pollPackets(true);
		}
	}

	@Override
	public void stop() {
		Event.unregister(EventUpdate.class, eventUpdate);
		Event.unregister(EventNetworked.class, eventNetworked);
		server.stopThread();
	}
}
