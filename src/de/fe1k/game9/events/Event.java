package de.fe1k.game9.events;

public interface Event {

	////////////////// STATIC STUFF //////////////////

	EventManager globalEventManager = new EventManager();

	static <T extends Event> boolean register(Class<T> eventClass, EventListener<? super T> listener) {
		return globalEventManager.register(eventClass, listener);
	}

	static <T extends Event> boolean registerOnce(Class<T> eventClass, EventListener<? super T> listener) {
		return globalEventManager.registerOnce(eventClass, listener);
	}

	static <T extends Event> boolean unregister(Class<T> eventClass, EventListener<? super T> listener) {
		return globalEventManager.unregister(eventClass, listener);
	}

	static <T extends Event> void trigger(T event) {
		globalEventManager.trigger(event);
	}

}
