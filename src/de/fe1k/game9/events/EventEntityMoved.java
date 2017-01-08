package de.fe1k.game9.events;

import de.fe1k.game9.entities.Entity;
import de.nerogar.noise.util.Vector2f;

public class EventEntityMoved implements Event {

	public Entity   entity;
	public Vector2f from;
	public Vector2f to;

	public EventEntityMoved() {
	}

	public EventEntityMoved(Entity entity, Vector2f from, Vector2f to) {
		this.entity = entity;
		this.from = from;
		this.to = to;
	}

	@Override
	public String toString() {
		return "EventEntityMoved{" +
				"entity=" + entity +
				", from=" + from +
				", to=" + to +
				'}';
	}

}
