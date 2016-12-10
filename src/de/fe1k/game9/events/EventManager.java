package de.fe1k.game9.events;

import de.fe1k.game9.debug.EventProfiler;
import de.nerogar.noise.Noise;

import java.util.*;

public class EventManager {

	private class EventListenerMap extends HashMap<Class<? extends Event>, Set<EventListener<? extends Event>>> {}

	private EventListenerMap listeners;
	private EventListenerMap listenersOnce;

	private Queue<Event> eventQueue;
	private boolean      isAlreadyInvoking;

	private EventProfiler profiler;

	public EventManager() {
		listeners = new EventListenerMap();
		listenersOnce = new EventListenerMap();
		eventQueue = new ArrayDeque<>();

		profiler = new EventProfiler();
		Noise.getDebugWindow().addProfiler(profiler);
	}

	public <T extends Event> boolean register(Class<T> eventClass, EventListener<? super T> listener) {
		profiler.incrementValue(EventProfiler.LISTENER_REGISTERED);
		profiler.incrementValue(EventProfiler.LISTENER_COUNT);

		if (!listeners.containsKey(eventClass)) {
			listeners.put(eventClass, new HashSet<>());
		}
		return listeners.get(eventClass).add(listener);
	}

	public <T extends Event> boolean registerOnce(Class<T> eventClass, EventListener<? super T> listener) {
		profiler.incrementValue(EventProfiler.LISTENER_REGISTERED);
		profiler.incrementValue(EventProfiler.LISTENER_COUNT);

		if (!listenersOnce.containsKey(eventClass)) {
			listenersOnce.put(eventClass, new HashSet<>());

		}
		return listenersOnce.get(eventClass).add(listener);
	}

	public <T extends Event> boolean unregister(Class<T> eventClass, EventListener<? super T> listener) {
		boolean removed = listeners.containsKey(eventClass) && listeners.get(eventClass).remove(listener);

		if(removed){
			profiler.incrementValue(EventProfiler.LISTENER_UNREGISTERED);
			profiler.decrementValue(EventProfiler.LISTENER_COUNT);
		}

		return removed;

	}

	@SuppressWarnings("unchecked")
	public void trigger(Event event) {
		eventQueue.add(event);

		profiler.incrementValue(EventProfiler.EVENT_TRIGGERED);

		if (isAlreadyInvoking) {
			return;
		}
		isAlreadyInvoking = true;

		long timerStart = System.nanoTime();

		while (!eventQueue.isEmpty()) {
			triggerOne(eventQueue.poll());
		}

		long timerEnd = System.nanoTime();
		long executionTime = timerEnd - timerStart;

		profiler.addValue(EventProfiler.TIME_TRIGGER, (int) executionTime);

		isAlreadyInvoking = false;
	}

	@SuppressWarnings("unchecked")
	private <T extends Event> void triggerOne(T event) {
		Set<EventListener<T>> toCall = new HashSet<>();

		for (EventListenerMap listenerMap : new EventListenerMap[] { listeners, listenersOnce }) {
			Iterator<Map.Entry<Class<? extends Event>, Set<EventListener<? extends Event>>>> iter = listenerMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<Class<? extends Event>, Set<EventListener<? extends Event>>> entry = iter.next();
				if (!entry.getKey().isInstance(event)) {
					continue;
				}
				for (EventListener<? extends Event> eventListener : entry.getValue()) {
					toCall.add((EventListener<T>) eventListener);
				}
				if (listenerMap == listenersOnce) {
					iter.remove();
					profiler.incrementValue(EventProfiler.LISTENER_UNREGISTERED);
					profiler.decrementValue(EventProfiler.LISTENER_COUNT);
				}
			}
		}

		for (EventListener<T> listener : toCall) {
			listener.onEvent(event);
		}

		profiler.addValue(EventProfiler.LISTENER_EXECUTED, toCall.size());
	}

}
