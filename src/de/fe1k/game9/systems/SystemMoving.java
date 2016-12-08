package de.fe1k.game9.systems;

import de.fe1k.game9.components.ComponentMoving;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventCollision;
import de.fe1k.game9.events.EventUpdate;
import de.nerogar.noise.util.Vector2f;

public class SystemMoving implements GameSystem {
	public static final Vector2f GRAVITY = new Vector2f(0f, -100);  // feels more responsive!

	@Override
	public void start() {
		Event.register(EventUpdate.class, this::update);
		Event.register(EventCollision.class, this::collision);
	}

	private void update(EventUpdate event) {
		Entity.getComponents(ComponentMoving.class).forEach(comp -> updateOne(event.deltaTime, comp));
	}

	private void collision(EventCollision event) {
		if (event.collisionDirection.isHorizontal()) {
			event.movingComponent.velocity.setX(0);
		} else {
			event.movingComponent.velocity.setY(0);
		}
		event.movingComponent.touching[event.collisionDirection.val] = true;
	}

	private void updateOne(float deltaTime, ComponentMoving comp) {
		comp.velocity.add(comp.gravity.multiplied(deltaTime));
		boolean touchingAny = false;
		for (int i = 0; i < comp.touching.length; i++) {
			if (comp.touching[i]) {
				touchingAny = true;
			}
			comp.touching[i] = false;
		}
		float friction = touchingAny ? comp.friction : comp.airFriction;
		comp.velocity.multiply(1 - (friction *deltaTime));
		comp.getOwner().move(comp.velocity.multiplied(deltaTime));
	}

	@Override
	public void stop() {
		Event.unregister(EventUpdate.class, this::update);
		Event.unregister(EventCollision.class, this::collision);
	}

}
