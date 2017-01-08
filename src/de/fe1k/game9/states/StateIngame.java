package de.fe1k.game9.states;

import de.fe1k.game9.Game;
import de.fe1k.game9.map.MapLoader;

public class StateIngame extends GameState {

	@Override
	public void enter() {
		MapLoader.loadMap(Game.renderer, "res/map/map_dungeon0");
	}

	@Override
	public void leave() {

	}
}
