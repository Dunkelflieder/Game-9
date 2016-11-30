package de.fe1k.game9.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventManager {

	private Map<EventListener<? extends Event>, Class<? extends Event>> listeners;
	private Map<EventListener<? extends Event>, Class<? extends Event>> listenersToAdd;
	private Set<EventListener<? extends Event>> listenersToRemove;

	public EventManager() {
		listeners = new HashMap<>();
		listenersToAdd = new HashMap<>();
		listenersToRemove = new HashSet<>();
	}

	public <T extends Event> void register(Class<T> eventClass, EventListener<? super T> listener) {
		listenersToAdd.put(listener, eventClass);
	}

	public void unregister(EventListener<? extends Event> listener) {
		listenersToRemove.add(listener);
	}

	@SuppressWarnings("unchecked")
	public <T extends Event> void trigger(T event) {
		listenersToRemove.forEach(listeners::remove);
		listenersToRemove.clear();
		listeners.putAll(listenersToAdd);
		listenersToAdd.clear();
		listeners.entrySet().stream()
				.filter(entry -> entry.getValue().isInstance(event))
				.forEach(entry -> ((EventListener<T>) entry.getKey()).onEvent(event));
	}

}
