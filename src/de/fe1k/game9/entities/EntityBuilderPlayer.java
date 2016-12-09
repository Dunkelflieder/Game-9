package de.fe1k.game9.entities;

import de.fe1k.game9.Game;
import de.fe1k.game9.components.*;
import de.fe1k.game9.map.Tile;
import de.fe1k.game9.utils.Bounding;
import de.nerogar.noise.util.Color;

public class EntityBuilderPlayer implements Tile.EntityBuilder {

	@Override
	public void createEntity(Entity entity, Tile tile, int markerColor) {

		entity.addComponent(new ComponentPlayer());

		entity.getScale().set(1.0f);
		entity.addComponent(new ComponentSpriteRenderer(Game.renderer, "man"));
		entity.addComponent(new ComponentMoving());
		entity.addComponent(new ComponentBounding(new Bounding(0.2f, 0, 0.8f, 0.95f), ComponentBounding.LAYER_PLAYER, ComponentBounding.LAYER_ALL));
		entity.addComponent(new ComponentLight(Game.renderer, new Color(1.0f, 0.8f, 0.8f, 0.0f), 20, 3));
		entity.addComponent(new ComponentDeathAnimation());
		ComponentControllable control = new ComponentControllable(Game.window.getInputHandler());
		entity.addComponent(control);
		control.resetPosition();

	}

}
