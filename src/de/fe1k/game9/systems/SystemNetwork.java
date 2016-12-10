package de.fe1k.game9.systems;

import de.fe1k.game9.events.*;
import de.fe1k.game9.network.PacketNetworkedEvent;
import de.nerogar.noise.network.Connection;
import de.nerogar.noise.network.ServerThread;

import java.net.BindException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SystemNetwork implements GameSystem {
	private ServerThread     server;
	private List<Connection> clients;

	@Override
	public void start() {
		clients = new ArrayList<>();
		try {
			server = new ServerThread(4200);
		} catch (BindException e) {
			e.printStackTrace();
		}
		Event.register(EventUpdate.class, this::update);
		Event.register(EventNetworked.class, this::broadcastEvent);
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

	private void broadcastEvent(EventNetworked event) {
		server.broadcast(new PacketNetworkedEvent(event));
	}

	@Override
	public void stop() {
		Event.unregister(EventUpdate.class, this::update);
		Event.unregister(EventNetworked.class, this::broadcastEvent);
		server.stopThread();
	}
}
