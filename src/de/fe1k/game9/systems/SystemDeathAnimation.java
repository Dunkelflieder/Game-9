package de.fe1k.game9.systems;

import de.fe1k.game9.components.*;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventEntityDestroyed;
import de.fe1k.game9.utils.Bounding;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;

import java.util.Random;

public class SystemDeathAnimation implements GameSystem {

	private DeferredRenderer renderer;

	public SystemDeathAnimation(DeferredRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public void start() {
		Event.register(EventEntityDestroyed.class, this::entityDestroyed);
	}

	private void entityDestroyed(EventEntityDestroyed event) {
		if (!event.entity.hasComponent(ComponentDeathAnimation.class)) return;

		ComponentDeathAnimation component = event.entity.getComponent(ComponentDeathAnimation.class);

		Random rand = new Random();

		for (int i = 0; i < 50; i++) {
			Entity particle = Entity.spawn(event.entity.getPosition().clone().addX(0.5f).addY(0.5f));

			particle.setRotation((float) (rand.nextFloat() * Math.PI * 2));
			particle.getScale().setX(rand.nextFloat() * component.scale + component.scale);
			particle.getScale().setY(rand.nextFloat() * component.scale + component.scale);

			ComponentMoving componentMoving = new ComponentMoving();
			componentMoving.velocity.setX((float) (rand.nextGaussian() * component.velocity));
			componentMoving.velocity.setY((float) (rand.nextGaussian() * component.velocity + component.velocity));
			particle.addComponent(componentMoving);

			particle.addComponent(new ComponentBounding(new Bounding(0, 0, 0.1f, 0.1f), ComponentBounding.LAYER_PARTICLES, ComponentBounding.LAYER_MAP));

			particle.addComponent(new ComponentSpriteRenderer(renderer, component.sprite, 1));

			particle.addComponent(new ComponentDespawn((float) (Math.random() * component.lifetime + component.lifetime)));

			particle.addComponent(new ComponentLight(renderer, component.lightColor, 2.5f, 0.3f));
		}

	}

	@Override
	public void stop() {
		Event.unregister(EventEntityDestroyed.class, this::entityDestroyed);
	}
}
