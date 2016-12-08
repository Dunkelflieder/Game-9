package de.fe1k.game9.systems;

import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventEntityDestroyed;
import de.fe1k.game9.events.EventEntityMoved;
import de.fe1k.game9.events.EventEntitySpawned;
import de.fe1k.game9.utils.Vector2i;
import de.nerogar.noise.util.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemEntityLookup implements GameSystem {
	private Map<Vector2i, List<Entity>> entityLookup = new HashMap<>();

	@Override
	public void start() {
		Event.register(EventEntityMoved.class, this::entityMoved);
		Event.register(EventEntitySpawned.class, this::entitySpawned);
		Event.register(EventEntityDestroyed.class, this::entityDestroyed);
		Entity.getAll().forEach(this::addEntity);
	}

	private void addEntity(Entity entity) {
		Vector2i pos = vector2fTo2i(entity.getPosition());
		if (!entityLookup.containsKey(pos)) {
			entityLookup.put(pos, new ArrayList<>());
		}
		entityLookup.get(pos).add(entity);
	}

	private void removeEntity(Entity entity) {
		removeEntity(entity, vector2fTo2i(entity.getPosition()));
	}

	private void removeEntity(Entity entity, Vector2i position) {
		if (entityLookup.containsKey(position)) {
			entityLookup.get(position).remove(entity);
		}
	}

	private Vector2i vector2fTo2i(Vector2f v) {
		return new Vector2i((int) Math.floor(v.getX()), (int) Math.floor(v.getY()));
	}

	private void entityMoved(EventEntityMoved event) {
		Vector2i oldPos = vector2fTo2i(event.from);
		removeEntity(event.entity, oldPos);
		addEntity(event.entity);
	}

	private void entitySpawned(EventEntitySpawned event) {
		addEntity(event.entity);
	}

	private void entityDestroyed(EventEntityDestroyed event) {
		removeEntity(event.entity);
	}

	public List<Entity> getAt(Vector2i pos) {
		List<Entity> result = entityLookup.get(pos);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}

	public List<Entity> getAt(int x, int y) {
		return getAt(new Vector2i(x, y));
	}

	@Override
	public void stop() {
		Event.unregister(EventEntityMoved.class, this::entityMoved);
		Event.unregister(EventEntitySpawned.class, this::entitySpawned);
		Event.unregister(EventEntityDestroyed.class, this::entityDestroyed);
	}
}
