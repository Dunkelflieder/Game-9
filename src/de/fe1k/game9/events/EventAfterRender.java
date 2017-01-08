package de.fe1k.game9.events;

import de.nerogar.noise.render.GLWindow;

public class EventAfterRender extends EventBeforeRender {
	public EventAfterRender(GLWindow window, float deltaTime, double runTime) {
		super(window, deltaTime, runTime);
	}
}
