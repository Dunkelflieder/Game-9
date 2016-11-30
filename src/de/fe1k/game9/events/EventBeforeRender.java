package de.fe1k.game9.events;

public class EventBeforeRender implements Event {
	public float deltaTime;
	public double runTime;
	public EventBeforeRender(float deltaTime, double runTime) {
		this.deltaTime = deltaTime;
		this.runTime = runTime;
	}
}
