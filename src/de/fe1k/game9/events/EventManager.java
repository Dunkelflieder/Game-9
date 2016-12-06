package de.fe1k.game9.events;

import java.util.HashMap;

public class EventManager {

	private class EventListenerMap extends HashMap<EventListener<? extends Event>, Class<? extends Event>> {}

	private EventListenerMap listeners;
	private EventListenerMap listenersOnce;

	public EventManager() {
		listeners = new EventListenerMap();
		listenersOnce = new EventListenerMap();
	}

	public <T extends Event> void register(Class<T> eventClass, EventListener<? super T> listener) {
		listeners.put(listener, eventClass);
	}

	public <T extends Event> void registerOnce(Class<T> eventClass, EventListener<? super T> listener) {
		listenersOnce.put(listener, eventClass);
	}

	public void unregister(EventListener<? extends Event> listener) {
		listeners.remove(listener);
	}

	@SuppressWarnings("unchecked")
	public <T extends Event> void trigger(T event) {
		EventListenerMap listeners = new EventListenerMap();
		listeners.putAll(this.listeners);
		EventListenerMap listenersOnce = new EventListenerMap();
		listenersOnce.putAll(this.listenersOnce);
		this.listenersOnce.clear();

		listeners.entrySet().stream()
				.filter(entry -> entry.getValue().isInstance(event))
				.forEach(entry -> ((EventListener<T>) entry.getKey()).onEvent(event));
		listenersOnce.entrySet().stream()
				.filter(entry -> entry.getValue().isInstance(event))
				.forEach(entry -> ((EventListener<T>) entry.getKey()).onEvent(event));
		listenersOnce.entrySet().removeIf(entry -> entry.getValue().isInstance(event));
		this.listenersOnce.putAll(listenersOnce);
	}

}
