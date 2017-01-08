package de.fe1k.game9.events;

import de.fe1k.game9.entities.Entity;
import de.nerogar.noise.util.Vector2f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EventEntityUpdatePositionNetworked implements EventToClients {

	public Entity   entity;
	public Vector2f to;

	public EventEntityUpdatePositionNetworked() {
	}

	public EventEntityUpdatePositionNetworked(Entity entity, Vector2f to) {
		this.entity = entity;
		this.to = to;
	}

	@Override
	public String toString() {
		return "EventEntityUpdatePositionNetworked{" +
				"entity=" + entity +
				", to=" + to +
				'}';
	}

	@Override
	public void fromStream(DataInputStream in) throws IOException {
		entity = Entity.getById(in.readLong());
		to = new Vector2f(in.readFloat(), in.readFloat());
	}

	@Override
	public void toStream(DataOutputStream out) throws IOException {
		out.writeLong(entity.getId());
		out.writeFloat(to.getX());
		out.writeFloat(to.getY());
	}
}
