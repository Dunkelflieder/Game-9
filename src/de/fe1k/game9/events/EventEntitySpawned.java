package de.fe1k.game9.events;

import de.fe1k.game9.entities.Entity;

public class EventEntitySpawned implements Event {
	public Entity entity;
	public EventEntitySpawned(Entity entity) {
		this.entity = entity;
	}

	@Override
	public String toString() {
		return "EventEntitySpawned{" +
				"entity=" + entity +
				'}';
	}
}
