package de.fe1k.game9.components;

import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.events.EventMapLoaded;
import de.fe1k.game9.events.EventUpdate;
import de.fe1k.game9.utils.Vector2i;
import de.nerogar.noise.util.Logger;
import de.nerogar.noise.util.Vector2f;

import java.util.*;

public class ComponentPathMovingBlock extends Component {

	private EventListener<EventUpdate> eventUpdate = this::update;
	private Vector2i previousPosition;
	private Vector2i currentPosition;
	private Vector2i targetPosition;
	private float speed = 1f;

	private List<ComponentPathMovingBlock> slaves;
	private ComponentPathMovingBlock       master;
	private Vector2f     masterOffset;

	public ComponentPathMovingBlock() {
		masterOffset = new Vector2f();
		slaves = new ArrayList<>();
		Event.registerOnce(EventMapLoaded.class, this::prepare);
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

	private void prepare(EventMapLoaded event) {
		// find out whether this is a master or a slave
		if (isMaster()) {
			master = null;
		} else {
			// find master
			master = findMaster();
			if (master == null) {
				Logger.getWarningStream().println("path-moving block did not find master, current position: " + currentPosition);
				return;
			}
			master.slaves.add(this);
			masterOffset = getOwner().getPosition().subtracted(master.getOwner().getPosition());
		}
	}

	private boolean isMaster() {
		Entity marker = Entity.getFirstAt(currentPosition.getX(), currentPosition.getY(), this::isPath);
		return marker != null;
	}

	private Set<Entity> getNeighbors(int x, int y) {
		Set<Entity> entities = new HashSet<>();
		entities.addAll(Entity.getAt(x - 1, y));
		entities.addAll(Entity.getAt(x + 1, y));
		entities.addAll(Entity.getAt(x, y - 1));
		entities.addAll(Entity.getAt(x, y + 1));
		return entities;
	}

	private ComponentPathMovingBlock findMaster() {
		Set<Entity> candidates = getNeighbors(currentPosition.getX(), currentPosition.getY());
		Queue<Entity> notChecked = new ArrayDeque<>(candidates);
		while (!notChecked.isEmpty()) {
			Entity candidate = notChecked.poll();
			ComponentPathMovingBlock comp = candidate.getComponent(ComponentPathMovingBlock.class);
			if (comp == null) {
				continue;
			}
			if (comp.isMaster()) {
				return comp;
			}
			for (Entity next : getNeighbors(comp.currentPosition.getX(), comp.currentPosition.getY())) {
				if (candidates.add(next)) {
					notChecked.add(next);
				}
			}
		}
		return null;
	}

	private boolean isPath(Entity entity) {
		ComponentMarker marker = entity.getComponent(ComponentMarker.class);
		return marker != null && marker.getMarker() == ComponentMarker.MARKER_PATH;
	}

	private void updateAsSlave() {
		float x = master.getOwner().getPosition().getX() + masterOffset.getX();
		float y = master.getOwner().getPosition().getY() + masterOffset.getY();
		getOwner().teleport(x, y);
	}

	private void update(EventUpdate event) {
		if (master != null) {
			return;
		}
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
		slaves.forEach(ComponentPathMovingBlock::updateAsSlave);
	}

	private void setNextTargetPosition() {
		Set<Entity> pathEntities = getNeighbors(currentPosition.getX(), currentPosition.getY());
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
