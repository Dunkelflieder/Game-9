package de.fe1k.game9.entities;

import de.fe1k.game9.components.ComponentMarker;
import de.fe1k.game9.map.Tile;

public class EntityBuilderMarker implements Tile.EntityBuilder {

	@Override
	public void createEntity(Entity entity, Tile tile, int markerColor) {
		entity.addComponent(new ComponentMarker(markerColor));
	}
}
