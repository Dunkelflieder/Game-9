package de.fe1k.game9.states;

import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventClientConnected;
import de.fe1k.game9.events.EventClientDisconnected;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.network.Network;

public class StateWaitingForPlayers extends GameState {

	private static final int PLAYERS_REQUIRED = 2;

	private EventListener<EventClientConnected>    eventClientConnected    = event -> checkReady();
	private EventListener<EventClientDisconnected> eventClientDisconnected = event -> checkReady();

	@Override
	public void enter() {
		if (!Network.isServer()) return;  // server decides when to start. no need to track as client
		Event.register(EventClientConnected.class, eventClientConnected);
		Event.register(EventClientDisconnected.class, eventClientDisconnected);
		checkReady();
	}

	@Override
	public void leave() {
		Event.unregister(EventClientConnected.class, eventClientConnected);
		Event.unregister(EventClientDisconnected.class, eventClientDisconnected);
	}

	private void checkReady() {
		int numPlayers = Network.getClients().size() + 1;
		if (numPlayers < PLAYERS_REQUIRED) return;
		GameState.transition(new StateIngame());
	}

}
