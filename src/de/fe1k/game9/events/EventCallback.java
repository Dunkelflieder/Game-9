package de.fe1k.game9.events;

public class EventCallback implements Event {
	public interface Callback {
		void call();
	}
	public float time;
	public Callback callback;
	public EventCallback(float time, Callback callback) {
		this.time = time;
		this.callback = callback;
	}
}
