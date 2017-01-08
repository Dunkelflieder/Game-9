package de.fe1k.game9.events;

import de.nerogar.noise.render.GLWindow;

public class EventBeforeRender implements Event {
	public GLWindow window;
	public float deltaTime;
	public double runTime;
	public EventBeforeRender(GLWindow window, float deltaTime, double runTime) {
		this.window = window;
		this.deltaTime = deltaTime;
		this.runTime = runTime;
	}
}
