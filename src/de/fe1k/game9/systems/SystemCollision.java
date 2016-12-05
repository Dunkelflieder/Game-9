package de.fe1k.game9.systems;

import de.fe1k.game9.components.ComponentBounding;
import de.fe1k.game9.components.ComponentMoving;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.events.EventUpdate;
import de.fe1k.game9.utils.Bounding;
import de.nerogar.noise.util.Vector2f;
import de.nerogar.noise.util.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SystemCollision implements GameSystem {
	private SystemEntityLookup entityLookup;

	public SystemCollision(SystemEntityLookup entityLookup) {
		this.entityLookup = entityLookup;
	}

	@Override
	public void start() {
		Event.register(EventUpdate.class, this::update);
	}

	private void update(EventUpdate event) {
		Entity.getAllWithComponents(ComponentBounding.class, ComponentMoving.class).forEach(entity -> {
			ComponentBounding boundingComponent = entity.getComponent(ComponentBounding.class);
			Bounding bounding = boundingComponent.bounding.translated(new Vector2f(entity.getPosition().getX(), entity.getPosition().getY()));
			ComponentMoving movingComponent = entity.getComponent(ComponentMoving.class);
			int x = (int) Math.floor(entity.getPosition().getX());
			int y = (int) Math.floor(entity.getPosition().getY());

			// get all entities in close proximity (assuming they are max. 1 unit big)
			List<Entity> possiblyColliding = new ArrayList<>();
			possiblyColliding.addAll(entityLookup.getAt(x-1, y-1));
			possiblyColliding.addAll(entityLookup.getAt(x+0, y-1));
			possiblyColliding.addAll(entityLookup.getAt(x+1, y-1));
			possiblyColliding.addAll(entityLookup.getAt(x-1, y+0));
			possiblyColliding.addAll(entityLookup.getAt(x+0, y+0));
			possiblyColliding.addAll(entityLookup.getAt(x+1, y+0));
			possiblyColliding.addAll(entityLookup.getAt(x-1, y+1));
			possiblyColliding.addAll(entityLookup.getAt(x+0, y+1));
			possiblyColliding.addAll(entityLookup.getAt(x+1, y+1));

			for (Entity collidingEntity : possiblyColliding) {
				if (collidingEntity.equals(entity)) {
					continue;  // no self collision
				}
				ComponentBounding compBounding = collidingEntity.getComponent(ComponentBounding.class);
				if (compBounding == null) {
					continue;  // has no bounding
				}
				Bounding otherBounding = compBounding.bounding.translated(new Vector2f(
						collidingEntity.getPosition().getX(),
						collidingEntity.getPosition().getY()
				));
				Optional<Vector2f> escape = bounding.getEscapeVector(otherBounding);
				if (escape.isPresent()) {
					Vector2f vector2f = escape.get();
					if (Math.abs(vector2f.getX()) < Math.abs(vector2f.getY())) {
						entity.move(new Vector3f(vector2f.getX(), 0, 0));
						movingComponent.velocity.setX(0);
					} else {
						entity.move(new Vector3f(0, vector2f.getY(), 0));
						movingComponent.velocity.setY(0);
					}
				}
			}
		});
	}

	@Override
	public void stop() {
		Event.unregister((EventListener<EventUpdate>) this::update);
	}
}
