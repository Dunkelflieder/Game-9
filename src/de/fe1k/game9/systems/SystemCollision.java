package de.fe1k.game9.systems;

import de.fe1k.game9.components.ComponentBounding;
import de.fe1k.game9.components.ComponentMoving;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventEntityMoved;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.utils.Bounding;
import de.nerogar.noise.util.Vector2f;

import java.util.*;

public class SystemCollision implements GameSystem {
	private SystemEntityLookup entityLookup;
	private Set<Entity> skipEntities = new HashSet<>();

	public SystemCollision(SystemEntityLookup entityLookup) {
		this.entityLookup = entityLookup;
	}

	@Override
	public void start() {
		Event.register(EventEntityMoved.class, this::entityMoved);
	}

	private void entityMoved(EventEntityMoved event) {
		Entity entity = event.entity;
		if (skipEntities.remove(entity)) {
			return;
		}
		ComponentBounding boundingComponent = entity.getComponent(ComponentBounding.class);
		if (boundingComponent == null) {
			return;  // entity that moved has no bounding
		}
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

		possiblyColliding.removeIf(e -> !e.hasComponent(ComponentBounding.class));  // must have bounding
		possiblyColliding.remove(entity);  // no self collisions please

		// sort by distance to entity being checked. Fixes movement stuttering when moving along flat surfaces,
		// because for example when moving on a flat ground, a neighboring bounding might think you collided with
		// it's side because the bounding you're standing on right now didn't push you upwards yet.
		possiblyColliding.sort((o1, o2) ->
				(int) Math.signum(o1.getPosition().subtracted(entity.getPosition()).getSquaredValue()
						        - o2.getPosition().subtracted(entity.getPosition()).getSquaredValue())
		);

		Vector2f newPosition = entity.getPosition().clone();
		for (Entity collidingEntity : possiblyColliding) {
			ComponentBounding compBounding = collidingEntity.getComponent(ComponentBounding.class);
			Bounding bounding = boundingComponent.bounding.translated(new Vector2f(newPosition.getX(), newPosition.getY()));
			Bounding otherBounding = compBounding.bounding.translated(new Vector2f(
					collidingEntity.getPosition().getX(),
					collidingEntity.getPosition().getY()
			));
			Vector2f deltaMoved = newPosition.subtracted(event.from);
			Optional<Vector2f> escape = bounding.getEscapeVector(otherBounding);
			if (!escape.isPresent()) {
				continue;  // no collision
			}
			Vector2f escapeVector = escape.get();
			/* Determine whether the collision happened in horizontal direction (left and right sides touching),
			 * or not (top and bottom touching):
			 * 1.) If the delta movement and the escape vector point in the same direction in either dimension,
			 *     assume the collision is orthogonal to that.
			 * 2.) If the delta movement's (positive) slope is steeper than the escape vector's (positive)
			 *     slope, the collision was vertical, otherwise horizontal.
			 *     This can also be expressed as whether slope1/slope2 has an incline of > 100%
			 *     (aka the x component is bigger than the y-component).
			 */
			boolean horizontal;
			if (deltaMoved.getX() * escapeVector.getX() > 0) {
				horizontal = false;  // collision "from inside", can't be horizontal
			} else if (deltaMoved.getY() * escapeVector.getY() > 0) {
				horizontal = true;  // collision "from inside", can't be vertical
			} else {
				horizontal = Math.abs(deltaMoved.getX() / escapeVector.getX())
						   > Math.abs(deltaMoved.getY() / escapeVector.getY());
			}
			if (horizontal) {
				escapeVector.setY(0);
				movingComponent.velocity.setX(0);
			} else {
				escapeVector.setX(0);
				movingComponent.velocity.setY(0);
			}
			newPosition.add(escapeVector);
		}
		// prevent infinite event loops
		skipEntities.add(entity);
		entity.teleport(newPosition);
	}

	@Override
	public void stop() {
		Event.unregister((EventListener<EventEntityMoved>) this::entityMoved);
	}
}
