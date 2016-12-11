package de.fe1k.game9.events;

import de.nerogar.noise.network.Connection;

public class EventConnected implements Event {
	public Connection server;
	public EventConnected(Connection server) {
		this.server = server;
	}
}
