package de.fe1k.game9.events;

@FunctionalInterface
public interface EventListener<T extends Event> {
	void onEvent(T event);
}
