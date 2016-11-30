package de.fe1k.game9.components;

import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.events.EventUpdate;
import de.nerogar.noise.util.Vector2f;

public class ComponentMoving extends Component {
	public static final Vector2f GRAVITY = new Vector2f(0f, -10f);
	private Vector2f velocity;
	private Vector2f gravity;
	private float friction;
	public ComponentMoving() {
		this.velocity = new Vector2f();
		this.gravity = GRAVITY;
		this.friction = 0.1f;
		Event.register(EventUpdate.class, this::update);
	}
	private void update(EventUpdate event) {
		velocity.add(gravity.multiplied(event.deltaTime));
		velocity.multiply(1 - (friction*event.deltaTime));
		getOwner().ifPresent(
				owner -> owner.getPosition().add(velocity.multiplied(event.deltaTime))
		);
	}
	@Override
	public void destroy() {
		Event.unregister((EventListener<EventUpdate>) this::update);
	}
}
