package de.fe1k.game9.entities;

import de.fe1k.game9.components.ComponentPathMovingBlock;
import de.fe1k.game9.map.Tile;

public class EntityBuilderPathMovingBlock extends EntityBuilderBlock {

	@Override
	public void createEntity(Entity entity, Tile tile, int markerColor) {
		super.createEntity(entity, tile, markerColor);

		entity.addComponent(new ComponentPathMovingBlock());
	}
}
