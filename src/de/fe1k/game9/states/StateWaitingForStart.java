package de.fe1k.game9.states;

import de.fe1k.game9.Game;
import de.fe1k.game9.events.*;
import de.fe1k.game9.map.MapLoader;
import de.fe1k.game9.network.Network;

public class StateWaitingForStart extends GameState {

	private static final int PLAYERS_REQUIRED = 2;

	private EventListener<EventClientConnected>    eventClientConnected    = event -> checkReady();
	private EventListener<EventClientDisconnected> eventClientDisconnected = event -> checkReady();
	private EventListener<EventLoadMap>            eventLoadMap = this::loadMap;

	@Override
	public void enter() {
		Event.register(EventLoadMap.class, eventLoadMap);
		if (!Network.isServer()) return;  // server decides when to start. no need to track as client
		Event.register(EventClientConnected.class, eventClientConnected);
		Event.register(EventClientDisconnected.class, eventClientDisconnected);
		checkReady();
	}

	@Override
	public void leave() {
		Event.unregister(EventLoadMap.class, eventLoadMap);
		Event.unregister(EventClientConnected.class, eventClientConnected);
		Event.unregister(EventClientDisconnected.class, eventClientDisconnected);
	}

	private void checkReady() {
		int numPlayers = Network.getClients().size() + 1;
		if (numPlayers >= PLAYERS_REQUIRED) {
			Event.trigger(new EventLoadMap("res/map/map_dungeon0"));
		}
	}

	private void loadMap(EventLoadMap event) {
		MapLoader.loadMap(Game.renderer, event.mapName);
		GameState.transition(new StateIngame());
	}

}
