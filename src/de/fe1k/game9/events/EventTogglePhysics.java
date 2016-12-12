package de.fe1k.game9.events;

public class EventTogglePhysics implements Event {
	public boolean enabled;

	public EventTogglePhysics(boolean enabled) {
		this.enabled = enabled;
	}
}
