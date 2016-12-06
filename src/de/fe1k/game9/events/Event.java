package de.fe1k.game9.events;

public interface Event {

	////////////////// STATIC STUFF //////////////////

	EventManager globalEventManager = new EventManager();

	static <T extends Event> void register(Class<T> eventClass, EventListener<? super T> listener) {
		globalEventManager.register(eventClass, listener);
	}

	static <T extends Event> void registerOnce(Class<T> eventClass, EventListener<? super T> listener) {
		globalEventManager.registerOnce(eventClass, listener);
	}

	static void unregister(EventListener<? extends Event> listener) {
		globalEventManager.unregister(listener);
	}

	static <T extends Event> void trigger(T event) {
		globalEventManager.trigger(event);
	}

}
