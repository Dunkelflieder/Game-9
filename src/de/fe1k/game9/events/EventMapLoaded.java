package de.fe1k.game9.events;

public class EventMapLoaded implements Event {
	public String mapname;
	public EventMapLoaded(String mapname) {
		this.mapname = mapname;
	}
}
