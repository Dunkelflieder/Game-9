package de.fe1k.game9.events;

import de.nerogar.noise.network.Connection;

public class EventDisconnected implements Event {
	public Connection server;
	public EventDisconnected(Connection server) {
		this.server = server;
	}
}
