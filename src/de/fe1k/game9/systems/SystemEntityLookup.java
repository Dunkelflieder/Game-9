package de.fe1k.game9.systems;

import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.*;
import de.fe1k.game9.utils.Vector2i;
import de.nerogar.noise.util.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemEntityLookup implements GameSystem {

	private Map<Vector2i, List<Entity>> entityLookup = new HashMap<>();

	private EventListener<EventEntityMoved>     eventEntityMoved;
	private EventListener<EventEntitySpawned>   eventEntitySpawned;
	private EventListener<EventEntityDestroyed> eventEntityDestroyed;

	@Override
	public void start() {
		eventEntityMoved = this::entityMoved;
		eventEntitySpawned = this::entitySpawned;
		eventEntityDestroyed = this::entityDestroyed;
		Event.register(EventEntityMoved.class, eventEntityMoved);
		Event.register(EventEntitySpawned.class, eventEntitySpawned);
		Event.register(EventEntityDestroyed.class, eventEntityDestroyed);
		Entity.getAll().forEach(entity -> addEntity(entity, vector2fTo2i(entity.getPosition())));
	}

	private void addEntity(Entity entity, Vector2i position) {
		if (!entityLookup.containsKey(position)) {
			entityLookup.put(position, new ArrayList<>());
		}
		entityLookup.get(position).add(entity);
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
		removeEntity(event.entity, vector2fTo2i(event.from));
		addEntity(event.entity, vector2fTo2i(event.to));
	}

	private void entitySpawned(EventEntitySpawned event) {
		addEntity(event.entity, vector2fTo2i(event.entity.getPosition()));
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
		Event.unregister(EventEntityMoved.class, eventEntityMoved);
		Event.unregister(EventEntitySpawned.class, eventEntitySpawned);
		Event.unregister(EventEntityDestroyed.class, eventEntityDestroyed);
	}
}
