package de.fe1k.game9.events;

import de.fe1k.game9.entities.Entity;
import de.nerogar.noise.util.Vector3f;

public class EventEntityMoved implements Event {
	public Entity entity;
	public Vector3f from;
	public Vector3f to;
	public EventEntityMoved(Entity entity, Vector3f from, Vector3f to) {
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
