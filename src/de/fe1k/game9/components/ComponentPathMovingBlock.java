package de.fe1k.game9.components;

import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.events.EventUpdate;
import de.fe1k.game9.utils.Vector2i;
import de.nerogar.noise.util.Logger;
import de.nerogar.noise.util.Vector2f;

import java.util.HashSet;
import java.util.Set;

public class ComponentPathMovingBlock extends Component {

	private EventListener<EventUpdate> eventUpdate = this::update;
	private Vector2i previousPosition;
	private Vector2i currentPosition;
	private Vector2i targetPosition;
	private float speed = 1f;

	public ComponentPathMovingBlock() {
		Event.register(EventUpdate.class, eventUpdate);

	}

	@Override
	public void init() {
		currentPosition = new Vector2i(
				Math.round(getOwner().getPosition().getX()),
				Math.round(getOwner().getPosition().getY())
		);
		previousPosition = currentPosition.clone();
		targetPosition = currentPosition.clone();
	}

	private void update(EventUpdate event) {
		Vector2f dist = new Vector2f(targetPosition.getX() - getOwner().getPosition().getX(),
		                             targetPosition.getY() - getOwner().getPosition().getY()
		);
		if (dist.getValue() < speed * event.deltaTime) {
			previousPosition.set(currentPosition);
			currentPosition.set(targetPosition);
			setNextTargetPosition();
		} else {
			dist.setValue(speed * event.deltaTime);
		}
		getOwner().move(dist.getX(), dist.getY());
	}

	private void setNextTargetPosition() {
		Set<Entity> pathEntities = new HashSet<>();
		pathEntities.addAll(Entity.getAt(currentPosition.getX() - 1, currentPosition.getY()));
		pathEntities.addAll(Entity.getAt(currentPosition.getX() + 1, currentPosition.getY()));
		pathEntities.addAll(Entity.getAt(currentPosition.getX(), currentPosition.getY() - 1));
		pathEntities.addAll(Entity.getAt(currentPosition.getX(), currentPosition.getY() + 1));
		pathEntities.removeIf(entity -> {
			ComponentMarker marker = entity.getComponent(ComponentMarker.class);
			return marker == null || marker.getMarker() != ComponentMarker.MARKER_PATH;
		});
		if (pathEntities.isEmpty()) {
			Logger.getWarningStream().println("path-moving block couldn't find next position, previous position: " + currentPosition);
			return;
		}
		if (pathEntities.size() > 1) {
			pathEntities.removeIf(entity -> previousPosition.getX() == (int) Math.floor(entity.getPosition().getX())
					&& previousPosition.getY() == (int) Math.floor(entity.getPosition().getY()));
		}
		Vector2f nextPosition = pathEntities.iterator().next().getPosition();
		targetPosition.setX((int) Math.floor(nextPosition.getX()));
		targetPosition.setY((int) Math.floor(nextPosition.getY()));
	}

	@Override
	public void destroy() {
		Event.unregister(EventUpdate.class, eventUpdate);
	}
}
