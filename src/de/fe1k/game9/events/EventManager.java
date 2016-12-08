package de.fe1k.game9.events;

import java.util.*;

public class EventManager {

	private class EventListenerMap extends HashMap<Class<? extends Event>, Set<EventListener<? extends Event>>> {}

	private EventListenerMap listeners;
	private EventListenerMap listenersOnce;

	public EventManager() {
		listeners = new EventListenerMap();
		listenersOnce = new EventListenerMap();
	}

	public <T extends Event> boolean register(Class<T> eventClass, EventListener<? super T> listener) {
		if (!listeners.containsKey(eventClass)) {
			listeners.put(eventClass, new HashSet<>());
		}
		return listeners.get(eventClass).add(listener);
	}

	public <T extends Event> boolean registerOnce(Class<T> eventClass, EventListener<? super T> listener) {
		if (!listenersOnce.containsKey(eventClass)) {
			listenersOnce.put(eventClass, new HashSet<>());
		}
		return listenersOnce.get(eventClass).add(listener);
	}

	public <T extends Event> boolean unregister(Class<T> eventClass, EventListener<? super T> listener) {
		if (!listeners.containsKey(eventClass)) {
			return false;
		}
		return listeners.get(eventClass).remove(listener);
	}

	@SuppressWarnings("unchecked")
	public <T extends Event> void trigger(T event) {
		Set<EventListener<T>> toCall = new HashSet<>();

		for (EventListenerMap listenerMap : new EventListenerMap[] {listeners, listenersOnce}) {
			Iterator<Map.Entry<Class<? extends Event>, Set<EventListener<? extends Event>>>> iter = listenerMap.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<Class<? extends Event>, Set<EventListener<? extends Event>>> entry = iter.next();
				if (!entry.getKey().isInstance(event)) {
					continue;
				}
				for (EventListener<? extends Event> eventListener : entry.getValue()) {
					toCall.add((EventListener<T>) eventListener);
				}
				if (listenerMap == listenersOnce) {
					iter.remove();
				}
			}
		}
		for (EventListener<T> listener : toCall) {
			listener.onEvent(event);
		}
	}

}
