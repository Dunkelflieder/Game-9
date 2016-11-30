package de.fe1k.game9.events;

public class EventUpdate implements Event {
	public float deltaTime;
	public EventUpdate(float deltaTime) {
		this.deltaTime = deltaTime;
	}
}
