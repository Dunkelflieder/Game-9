package de.fe1k.game9.systems;

import de.fe1k.game9.Game;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventCallback;
import de.fe1k.game9.events.EventUpdate;

import java.util.PriorityQueue;
import java.util.Queue;

public class SystemCallbacks implements GameSystem {
	private class CallbackEntry implements Comparable<CallbackEntry> {
		double time;
		EventCallback.Callback callback;
		CallbackEntry(double time, EventCallback.Callback callback) {
			this.time = time;
			this.callback = callback;
		}
		@Override
		public int compareTo(CallbackEntry o) {
			return (int) Math.signum(time - o.time);
		}
	}

	private Queue<CallbackEntry> callbacks = new PriorityQueue<>();

	@Override
	public void start() {
		Event.register(EventCallback.class, this::addCallback);
		Event.register(EventUpdate.class, this::update);
	}

	private void addCallback(EventCallback event) {
		double now = Game.getRunTime();
		callbacks.add(new CallbackEntry(now+event.time, event.callback));
	}

	private void update(EventUpdate event) {
		double now = Game.getRunTime();
		CallbackEntry entry;
		while ((entry = callbacks.peek()) != null && entry.time <= now) {
			callbacks.poll();
			entry.callback.call();
		}
	}

	@Override
	public void stop() {
		Event.unregister(EventCallback.class, this::addCallback);
		Event.unregister(EventUpdate.class, this::update);
	}
}
