package de.fe1k.game9.events;

import de.fe1k.game9.entities.Entity;
import de.nerogar.noise.util.Vector2f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EventEntityMoved implements EventNetworked {

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

	@Override
	public void fromStream(DataInputStream in) throws IOException {
		entity = Entity.getById(in.readLong());
		from = new Vector2f(in.readFloat(), in.readFloat());
		to = new Vector2f(in.readFloat(), in.readFloat());
	}

	@Override
	public void toStream(DataOutputStream out) throws IOException {
		out.writeLong(entity.getId());
		out.writeFloat(from.getX());
		out.writeFloat(from.getY());
		out.writeFloat(to.getX());
		out.writeFloat(to.getY());
	}
}
