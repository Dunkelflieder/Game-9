package de.fe1k.game9.events;

import de.fe1k.game9.entities.Entity;

public class EventEntityDestroyed implements Event {
	public Entity entity;
	public EventEntityDestroyed(Entity entity) {
		this.entity = entity;
	}

	@Override
	public String toString() {
		return "EventEntityDestroyed{" +
				"entity=" + entity +
				'}';
	}
}
