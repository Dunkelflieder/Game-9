package de.fe1k.game9.components;

import de.fe1k.game9.systems.SystemPhysics;
import de.nerogar.noise.util.Vector2f;

public class ComponentMoving extends Component {
	public Vector2f velocity;
	public Vector2f gravity;
	public float friction;
	public float airFriction;
	public boolean[] touching;
	public ComponentMoving() {
		this.velocity = new Vector2f();
		this.gravity = SystemPhysics.GRAVITY;
		this.airFriction = 5;
		this.friction = 10;
		touching = new boolean[4];
	}
}
