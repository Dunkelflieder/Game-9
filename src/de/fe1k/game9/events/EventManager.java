package de.fe1k.game9.events;

import java.util.*;

public class EventManager {

	private class EventListenerMap extends HashMap<Class<? extends Event>, Set<EventListener<? extends Event>>> {}

	private EventListenerMap listeners;
	private EventListenerMap listenersOnce;

	private Queue<Event>     eventQueue;
	private boolean          isAlreadyInvoking;

	public EventManager() {
		listeners = new EventListenerMap();
		listenersOnce = new EventListenerMap();
		eventQueue = new LinkedList<>();
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
		return listeners.containsKey(eventClass) && listeners.get(eventClass).remove(listener);
	}

	@SuppressWarnings("unchecked")
	public void trigger(Event event) {
		eventQueue.add(event);
		if (isAlreadyInvoking) {
			return;
		}
		isAlreadyInvoking = true;
		while (!eventQueue.isEmpty()) {
			triggerOne(eventQueue.poll());
		}
		isAlreadyInvoking = false;
	}

	@SuppressWarnings("unchecked")
	private <T extends Event> void triggerOne(T event) {
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
