package de.fe1k.game9.entities;

import de.fe1k.game9.Game;
import de.fe1k.game9.components.ComponentLight;
import de.fe1k.game9.map.Tile;
import de.nerogar.noise.util.Color;

public class EntityBuilderLamp implements Tile.EntityBuilder {

	@Override
	public void createEntity(Entity entity, Tile tile, int markerColor) {
		entity.addComponent(new ComponentLight(Game.renderer, new Color(1.0f, 0.66f, 0.0f, 0.0f), 10, 2));
	}
}
