package de.fe1k.game9.entities;

import de.fe1k.game9.Game;
import de.fe1k.game9.components.ComponentKillOnCollision;
import de.fe1k.game9.components.ComponentLight;
import de.fe1k.game9.components.ComponentParticleSystem;
import de.fe1k.game9.map.Tile;
import de.nerogar.noise.util.Color;

public class EntityBuilderLava extends EntityBuilderBlock {

	@Override
	public void createEntity(Entity entity, Tile tile, int markerColor) {
		super.createEntity(entity, tile, markerColor);

		entity.addComponent(new ComponentLight(Game.renderer, new Color(1.0f, 0.2f, 0.0f, 0.0f), 10, 1.0f));
		entity.addComponent(new ComponentKillOnCollision());

		// particle system
		ComponentParticleSystem particleSystem = new ComponentParticleSystem("blood", 1.5f, 0.5f, 3, 2, 0.2f);

		particleSystem.lifetimeRand = 0.2f;

		particleSystem.velocity.set(0f, 30f);
		particleSystem.velocityRand.set(5f, 5f);

		particleSystem.scaleMin.set(0.1f);
		particleSystem.scaleRandDelta.set(0.2f);

		particleSystem.setLight(new Color(1.0f, 0.2f, 0.2f, 0.0f), 2.5f, 1.0f);

		entity.addComponent(particleSystem);

	}

}
