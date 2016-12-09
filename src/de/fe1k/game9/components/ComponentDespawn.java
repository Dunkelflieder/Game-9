package de.fe1k.game9.components;

import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventCallback;

public class ComponentDespawn extends ComponentRenderer {

	private float delay;

	/**
	 * This component causes the entity to despawn after {@code delay} seconds
	 *
	 * @param delay the despawn delay in seconds
	 */
	public ComponentDespawn(float delay) {
		this.delay = delay;

		Event.trigger(new EventCallback(delay, this::despawn));
	}

	private void despawn() {
		Entity.despawn(getOwner().getId());
	}

	@Override
	public void destroy() {
	}

}
