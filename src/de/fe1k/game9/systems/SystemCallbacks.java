package de.fe1k.game9.systems;

import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventCallback;
import de.fe1k.game9.events.EventUpdate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SystemCallbacks implements GameSystem {
	private class CallbackEntry {
		float time;
		EventCallback.Callback callback;
		CallbackEntry(float time, EventCallback.Callback callback) {
			this.time = time;
			this.callback = callback;
		}
	}

	private List<CallbackEntry> callbacks = new ArrayList<>();

	@Override
	public void start() {
		Event.register(EventCallback.class, this::addCallback);
		Event.register(EventUpdate.class, this::update);
	}

	private void addCallback(EventCallback event) {
		int pos = 0;
		// find spot to insert. TODO: binary search
		while(pos < callbacks.size() && callbacks.get(pos).time < event.time) {
			pos++;
		}
		callbacks.add(pos, new CallbackEntry(event.time, event.callback));
	}

	private void update(EventUpdate event) {
		Iterator<CallbackEntry> iter = callbacks.iterator();
		List<EventCallback.Callback> toCall = new ArrayList<>();
		while (iter.hasNext()) {
			CallbackEntry entry = iter.next();
			entry.time -= event.deltaTime;
			if (entry.time <= 0) {
				iter.remove();
				toCall.add(entry.callback);
			}
		}
		// call after iteration to avoid concurrent modifications in the callbacks
		toCall.forEach(EventCallback.Callback::call);
	}

	@Override
	public void stop() {
		Event.unregister(EventCallback.class, this::addCallback);
		Event.unregister(EventUpdate.class, this::update);
	}
}
