package de.fe1k.game9.events;

import de.nerogar.noise.network.Connection;

public class EventClientConnected implements Event {
	public Connection client;
	public EventClientConnected(Connection client) {
		this.client = client;
	}
}
