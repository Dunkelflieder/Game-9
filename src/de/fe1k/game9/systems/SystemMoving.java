package de.fe1k.game9.systems;

import de.fe1k.game9.components.ComponentMoving;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.events.EventUpdate;
import de.nerogar.noise.util.Vector2f;

public class SystemMoving implements GameSystem {
	public static final Vector2f GRAVITY = new Vector2f(0f, -10f);

	@Override
	public void start() {
		Event.register(EventUpdate.class, this::update);
	}

	private void update(EventUpdate event) {
		Entity.getComponents(ComponentMoving.class).forEach(comp -> updateOne(event.deltaTime, comp));
	}

	private void updateOne(float deltaTime, ComponentMoving comp) {
		comp.velocity.add(comp.gravity.multiplied(deltaTime));
		comp.velocity.multiply(1 - (comp.friction*deltaTime));
		comp.getOwner().move(comp.velocity.multiplied(deltaTime));
	}

	@Override
	public void stop() {
		Event.unregister((EventListener<EventUpdate>) this::update);
	}

}
