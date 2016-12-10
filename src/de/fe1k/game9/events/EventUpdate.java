package de.fe1k.game9.events;

public class EventUpdate implements Event {
	public float deltaTime;
	public boolean server;
	public EventUpdate(float deltaTime, boolean server) {
		this.deltaTime = deltaTime;
		this.server = server;
	}
}
