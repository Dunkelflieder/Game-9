package de.fe1k.game9.systems;

import de.fe1k.game9.components.ComponentBounding;
import de.fe1k.game9.components.ComponentMoving;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.*;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.utils.Bounding;
import de.fe1k.game9.utils.Direction;
import de.nerogar.noise.util.Vector2f;

import java.util.*;

public class SystemPhysics implements GameSystem {

	public static final Vector2f GRAVITY = new Vector2f(0f, -100);  // feels more responsive!

	private EventListener<EventUpdate> eventUpdate = this::update;

	public SystemPhysics() {
		Event.register(EventTogglePhysics.class, event -> {
			if (event.enabled) start();
			else stop();
		});
	}

	@Override
	public void start() {
		Event.register(EventUpdate.class, eventUpdate);
	}

	@Override
	public void stop() {
		Event.unregister(EventUpdate.class, eventUpdate);
	}

	private void update(EventUpdate event) {
		for (ComponentMoving componentMoving : Entity.getComponents(ComponentMoving.class)) {
			updateOne(event.deltaTime, componentMoving);
		}
	}

	private List<ComponentBounding> getPossibleColliders(ComponentBounding bounding, int x, int y) {
		// get all entities in close proximity (assuming they are max. 1 unit big)
		Set<Entity> possiblyCollidingSet = new HashSet<>();
		possiblyCollidingSet.addAll(Entity.getAt(x - 1, y - 1));
		possiblyCollidingSet.addAll(Entity.getAt(x + 0, y - 1));
		possiblyCollidingSet.addAll(Entity.getAt(x + 1, y - 1));
		possiblyCollidingSet.addAll(Entity.getAt(x - 1, y + 0));
		possiblyCollidingSet.addAll(Entity.getAt(x + 0, y + 0));
		possiblyCollidingSet.addAll(Entity.getAt(x + 1, y + 0));
		possiblyCollidingSet.addAll(Entity.getAt(x - 1, y + 1));
		possiblyCollidingSet.addAll(Entity.getAt(x + 0, y + 1));
		possiblyCollidingSet.addAll(Entity.getAt(x + 1, y + 1));

		List<ComponentBounding> possiblyColliding = new ArrayList<>();
		for (Entity candidate : possiblyCollidingSet) {
			ComponentBounding otherBounding = candidate.getComponent(ComponentBounding.class);
			if (otherBounding == null) {
				continue;  // must have bounding
			}
			if (otherBounding.equals(bounding)) {
				continue;  // no self collisions please
			}
			if ((bounding.layerCollides & otherBounding.layerSelf) == 0 || (bounding.layerSelf & otherBounding.layerCollides) == 0) {
				continue;  // boundings' layers must allow collision
			}
			possiblyColliding.add(otherBounding);
		}

		// sort by distance to entity being checked. Fixes movement stuttering when moving along flat surfaces,
		// because for example when moving on a flat ground, a neighboring bounding might think you collided with
		// it's side because the bounding you're standing on right now didn't push you upwards yet.
		possiblyColliding.sort((o1, o2) -> {
			float dist1 = o1.getOwner().getPosition().subtracted(bounding.getOwner().getPosition()).getSquaredValue();
			float dist2 = o2.getOwner().getPosition().subtracted(bounding.getOwner().getPosition()).getSquaredValue();
			return (int) Math.signum(dist1 - dist2);
		});

		return possiblyColliding;
	}

	private void updateOne(float deltaTime, ComponentMoving comp) {
		Entity entity = comp.getOwner();
		// work on copy of position, and trigger a moved event at the end
		Vector2f newPosition = comp.getOwner().getPosition().clone();
		// reset all touching flags now and set them again if a collision happened
		boolean touchingAny = false;
		for (int i = 0; i < comp.touching.length; i++) {
			if (comp.touching[i]) {
				touchingAny = true;
			}
			comp.touching[i] = false;
		}

		// apply forces
		comp.velocity.add(comp.gravity.multiplied(deltaTime));
		float friction = touchingAny ? comp.friction : comp.airFriction;
		comp.velocity.multiply(1 - (friction * deltaTime));
		newPosition.add(comp.velocity.multiplied(deltaTime));

		// continue with collision detection
		List<EventCollision> collisions = new ArrayList<>();
		ComponentBounding boundingComponent = entity.getComponent(ComponentBounding.class);
		if (boundingComponent != null) {
			int x = (int) Math.floor(entity.getPosition().getX());
			int y = (int) Math.floor(entity.getPosition().getY());

			List<ComponentBounding> possiblyColliding = getPossibleColliders(boundingComponent, x, y);

			for (ComponentBounding colliding : possiblyColliding) {
				Bounding bounding = boundingComponent.bounding.translated(new Vector2f(newPosition.getX(), newPosition.getY()));
				Bounding otherBounding = colliding.getTranslatedBounding();
				Vector2f deltaMoved = newPosition.subtracted(entity.getPosition());
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
				Direction collisionDirection;
				if (horizontal) {
					escapeVector.setY(0);
					collisionDirection = escapeVector.getX() < 0 ? Direction.RIGHT : Direction.LEFT;
				} else {
					escapeVector.setX(0);
					collisionDirection = escapeVector.getY() < 0 ? Direction.UP : Direction.DOWN;
				}
				collisions.add(new EventCollision(comp, colliding, collisionDirection));
				newPosition.add(escapeVector);
			}
		}

		// apply the changes
		entity.teleport(newPosition);
		for (EventCollision collision : collisions) {
			comp.touching[collision.collisionDirection.val] = true;
			if (collision.collisionDirection.isHorizontal()) {
				comp.velocity.setX(0);
			} else {
				comp.velocity.setY(0);
			}
			Event.trigger(collision);
		}
	}

}
