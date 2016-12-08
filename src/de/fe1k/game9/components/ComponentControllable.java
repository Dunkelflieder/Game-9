package de.fe1k.game9.components;

import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventCollision;
import de.fe1k.game9.events.EventUpdate;
import de.fe1k.game9.utils.Direction;
import de.nerogar.noise.input.InputHandler;
import de.nerogar.noise.util.Logger;

import java.util.Optional;

@Depends(components={ComponentMoving.class})
public class ComponentControllable extends Component {
	private InputHandler inputs;
	private float jumpPower = 0;
	private boolean wasKeyDown = false;

	public ComponentControllable(InputHandler inputHandler) {
		this.inputs = inputHandler;
		Event.register(EventUpdate.class, this::update);
		Event.register(EventCollision.class, this::collision);
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
		if (event.collisionDirection.isHorizontal()) {
			// death
			resetPosition();
		}
	}

	public void resetPosition() {
		Optional<ComponentStartMarker> start = Entity.getComponents(ComponentStartMarker.class).findFirst();
		if (!start.isPresent()) {
			Logger.getErrorStream().printf("No start marker found, can't teleport to start!");
			return;
		}
		getOwner().teleport(start.get().getOwner().getPosition());
	}

	@Override
	public void destroy() {
		Event.unregister(EventUpdate.class, this::update);
		Event.unregister(EventCollision.class, this::collision);
	}

}
