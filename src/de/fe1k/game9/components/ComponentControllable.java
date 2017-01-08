package de.fe1k.game9.components;

import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.*;
import de.fe1k.game9.network.Network;
import de.fe1k.game9.utils.Direction;
import de.nerogar.noise.input.InputHandler;
import de.nerogar.noise.input.KeyboardKeyEvent;
import de.nerogar.noise.util.Logger;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

@Depends(components = { ComponentMoving.class })
public class ComponentControllable extends Component {

	private InputHandler inputs;
	private float   jumpPower  = 0;
	private boolean wasKeyDown = false;
	private boolean flymode    = false;

	private int   targetMoveDirection = 1;
	private float moveDirection       = targetMoveDirection;

	private EventListener<EventUpdate>        eventUpdate        = this::update;
	private EventListener<EventCollision>     eventCollision     = this::collision;
	private EventListener<EventToggleFlymode> eventToggleFlymode = event -> flymode = event.enabled;
	private EventListener<EventTurnAround>    eventTurnAround    = this::turnAround;

	public ComponentControllable(InputHandler inputHandler) {
		this.inputs = inputHandler;
		Event.register(EventUpdate.class, eventUpdate);
		Event.register(EventCollision.class, eventCollision);
		Event.register(EventToggleFlymode.class, eventToggleFlymode);
		Event.register(EventTurnAround.class, eventTurnAround);
	}

	private void updateFlymode(EventUpdate event) {

	}

	private void update(EventUpdate event) {
		ComponentMoving moving = getOwner().getComponent(ComponentMoving.class);

		if (!Network.isStarted()) return;

		if (Network.isServer()) {
			if (flymode) {
				moving.velocity.set(0);
				float speed = 20 * event.deltaTime;
				if (inputs.isKeyDown(GLFW_KEY_UP)) getOwner().move(0, speed);
				if (inputs.isKeyDown(GLFW_KEY_DOWN)) getOwner().move(0, -speed);
				if (inputs.isKeyDown(GLFW_KEY_RIGHT)) getOwner().move(speed, 0);
				if (inputs.isKeyDown(GLFW_KEY_LEFT)) getOwner().move(-speed, 0);
			} else {
				boolean isKeyDown = inputs.isKeyDown(GLFW_KEY_SPACE);
				boolean onGround = moving.touching[Direction.DOWN.val];

				if (onGround) {
					jumpPower = 1;
				}

				if (isKeyDown && !(onGround && wasKeyDown)) {
					moving.velocity.add(moving.gravity.multiplied(-0.09f * jumpPower));
					jumpPower *= 1 - (10 * event.deltaTime);
				}

				moving.velocity.setX(10f * moveDirection);
				wasKeyDown = isKeyDown;
			}
		} else {
			for (KeyboardKeyEvent keyboardKeyEvent : inputs.getKeyboardKeyEvents()) {
				if (keyboardKeyEvent.action == GLFW.GLFW_PRESS && keyboardKeyEvent.key == GLFW.GLFW_KEY_ENTER) {
					Event.trigger(new EventTurnAround());
				}
			}
		}

		if (moveDirection < targetMoveDirection) moveDirection += 10 * event.deltaTime;
		if (moveDirection > targetMoveDirection) moveDirection -= 10 * event.deltaTime;
		getOwner().getScale().setX(moveDirection);

	}

	private void turnAround(EventTurnAround event) {
		targetMoveDirection *= -1;
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
		ComponentMarker start = Entity.getFirstComponent(ComponentMarker.class, marker -> marker.getMarker() == ComponentMarker.MARKER_START);
		if (start == null) {
			Logger.getErrorStream().printf("No start marker found, can't teleport to start!");
			return;
		}
		getOwner().teleport(start.getOwner().getPosition());
	}

	@Override
	public void destroy() {
		Event.unregister(EventUpdate.class, eventUpdate);
		Event.unregister(EventCollision.class, eventCollision);
		Event.unregister(EventToggleFlymode.class, eventToggleFlymode);
		Event.unregister(EventTurnAround.class, eventTurnAround);
	}

}
