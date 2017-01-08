package de.fe1k.game9.events;

public class EventToggleCollisions implements Event {
	public boolean enabled;

	public EventToggleCollisions(boolean enabled) {
		this.enabled = enabled;
	}
}
