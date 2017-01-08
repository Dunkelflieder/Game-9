package de.fe1k.game9.systems;

import de.fe1k.game9.components.ComponentControllable;
import de.fe1k.game9.components.ComponentKillOnCollision;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventCollision;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.network.Network;

public class SystemKillOnCollision implements GameSystem {

	private EventListener<EventCollision> eventCollision;

	public SystemKillOnCollision() {
	}

	@Override
	public void start() {
		eventCollision = this::entityCollision;
		Event.register(EventCollision.class, eventCollision);
	}

	private void entityCollision(EventCollision event) {
		// only server does this logic
		if (!Network.isStarted() || !Network.isServer()) return;
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
		Event.unregister(EventCollision.class, eventCollision);
	}
}
