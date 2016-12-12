package de.fe1k.game9.events;

public class EventToggleFlymode implements Event {
	public boolean enabled;

	public EventToggleFlymode(boolean enabled) {
		this.enabled = enabled;
	}
}
