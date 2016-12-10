package de.fe1k.game9.events;

import de.nerogar.noise.network.Connection;

public class EventClientDisconnected implements Event {
	public Connection client;
	public EventClientDisconnected(Connection client) {
		this.client = client;
	}
}
