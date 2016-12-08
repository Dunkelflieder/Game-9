package de.fe1k.game9.events;

import de.fe1k.game9.components.ComponentBounding;
import de.fe1k.game9.components.ComponentMoving;
import de.fe1k.game9.utils.Direction;

public class EventCollision implements Event {
	public ComponentMoving movingComponent;
	public ComponentBounding obstacle;
	public Direction collisionDirection;
	public EventCollision(ComponentMoving movingComponent, ComponentBounding obstacle, Direction collisionDirection) {
		this.movingComponent = movingComponent;
		this.obstacle = obstacle;
		this.collisionDirection = collisionDirection;
	}
}
