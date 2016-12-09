package de.fe1k.game9.entities;

import de.fe1k.game9.components.ComponentStartMarker;
import de.fe1k.game9.map.Tile;

public class EntityBuilderMarker implements Tile.EntityBuilder {

	@Override
	public void createEntity(Entity entity, Tile tile, int markerColor) {
		if (markerColor == 0x00FF00) {
			entity.addComponent(new ComponentStartMarker());
		} else if (markerColor == 0xFF0000) {
			//entity.addComponent(new ComponentStopMarker());
		}

	}
}
