package de.fe1k.game9.events;

import de.nerogar.noise.render.GLWindow;

public class EventAfterRender implements Event {
	public GLWindow window;
	public float deltaTime;
	public double runTime;
	public EventAfterRender(GLWindow window, float deltaTime, double runTime) {
		this.window = window;
		this.deltaTime = deltaTime;
		this.runTime = runTime;
	}
}
