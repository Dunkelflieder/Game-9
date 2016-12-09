package de.fe1k.game9.entities;

import de.fe1k.game9.Game;
import de.fe1k.game9.components.ComponentLight;
import de.fe1k.game9.components.ComponentSpriteRenderer;
import de.fe1k.game9.map.Tile;
import de.nerogar.noise.util.Color;

public class EntityBuilderLava implements Tile.EntityBuilder {

	@Override
	public void createEntity(Entity entity, Tile tile, int markerColor) {
		entity.addComponent(new ComponentLight(Game.renderer, new Color(1.0f, 0.2f, 0.0f, 0.0f), 10, 1.0f));
		entity.addComponent(new ComponentSpriteRenderer(Game.renderer, tile.texname));
	}

}
