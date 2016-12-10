package de.fe1k.game9.systems;

import de.fe1k.game9.components.ComponentControllable;
import de.fe1k.game9.components.ComponentKillOnCollision;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventCollision;

public class SystemKillOnCollision implements GameSystem {

	public SystemKillOnCollision() {
	}

	@Override
	public void start() {
		Event.register(EventCollision.class, this::entityCollision);
	}

	private void entityCollision(EventCollision event) {
		if (!event.obstacle.getOwner().hasComponent(ComponentKillOnCollision.class)) return;

		Entity movingEntity = event.movingComponent.getOwner();
		if (movingEntity.hasComponent(ComponentControllable.class)) {
			movingEntity.getComponent(ComponentControllable.class).resetPosition();
		} else {
			Entity.despawn(movingEntity.getId());
		}

	}

	@Override
	public void stop() {
		Event.unregister(EventCollision.class, this::entityCollision);
	}
}
