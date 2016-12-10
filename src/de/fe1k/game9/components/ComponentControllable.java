package de.fe1k.game9.components;

import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.*;
import de.fe1k.game9.utils.Direction;
import de.nerogar.noise.input.InputHandler;
import de.nerogar.noise.util.Logger;

import java.util.List;

@Depends(components = { ComponentMoving.class })
public class ComponentControllable extends Component {

	private InputHandler inputs;
	private float   jumpPower  = 0;
	private boolean wasKeyDown = false;

	private EventListener<EventUpdate>    eventUpdate;
	private EventListener<EventCollision> eventCollision;

	public ComponentControllable(InputHandler inputHandler) {
		this.inputs = inputHandler;
		eventUpdate = this::update;
		eventCollision = this::collision;
		Event.register(EventUpdate.class, eventUpdate);
		Event.register(EventCollision.class, eventCollision);
	}

	private void update(EventUpdate event) {
		ComponentMoving moving = getOwner().getComponent(ComponentMoving.class);
		boolean isKeyDown = inputs.isKeyDown(' ');
		boolean onGround = moving.touching[Direction.DOWN.val];
		if (onGround) {
			jumpPower = 1;
		}
		if (isKeyDown && !(onGround && wasKeyDown)) {
			moving.velocity.add(moving.gravity.multiplied(-0.09f * jumpPower));
			jumpPower *= 1 - (10 * event.deltaTime);
		}
		moving.velocity.setX(10f);
		wasKeyDown = isKeyDown;
	}

	private void collision(EventCollision event) {
		if (!event.movingComponent.getOwner().equals(getOwner())) {
			return;
		}
		if (event.collisionDirection.isHorizontal()) {
			// death
			Event.trigger(new EventEntityDestroyed(getOwner()));
			resetPosition();
		}
	}

	public void resetPosition() {
		List<ComponentStartMarker> starts = Entity.getComponents(ComponentStartMarker.class);
		if (starts.isEmpty()) {
			Logger.getErrorStream().printf("No start marker found, can't teleport to start!");
			return;
		}
		getOwner().teleport(starts.get(0).getOwner().getPosition());
	}

	@Override
	public void destroy() {
		Event.unregister(EventUpdate.class, eventUpdate);
		Event.unregister(EventCollision.class, eventCollision);
	}

}
