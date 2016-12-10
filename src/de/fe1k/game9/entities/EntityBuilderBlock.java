package de.fe1k.game9.entities;

import de.fe1k.game9.Game;
import de.fe1k.game9.components.ComponentBounding;
import de.fe1k.game9.components.ComponentRenderer;
import de.fe1k.game9.components.ComponentSpriteRenderer;
import de.fe1k.game9.map.Tile;
import de.fe1k.game9.utils.Bounding;

public class EntityBuilderBlock implements Tile.EntityBuilder {

	@Override
	public void createEntity(Entity entity, Tile tile, int markerColor) {
		entity.addComponent(new ComponentBounding(new Bounding(), ComponentBounding.LAYER_MAP, ComponentBounding.LAYER_ALL));
		if (tile.texname != null) {
			if (tile.stationary) {
				entity.addComponent(new ComponentRenderer());
			} else {
				entity.addComponent(new ComponentSpriteRenderer(Game.renderer, tile.texname));
			}
		}
	}
}
