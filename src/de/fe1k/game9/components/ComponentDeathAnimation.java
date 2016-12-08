package de.fe1k.game9.components;

import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventEntityDestroyed;
import de.fe1k.game9.utils.Bounding;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;
import de.nerogar.noise.util.Color;

import java.util.Random;

public class ComponentDeathAnimation extends ComponentRenderer {

	protected DeferredRenderer renderer;
	private   String           sprite;

	/**
	 * This component causes a 2D-sprite to be rendered by the given renderer.
	 *
	 * @param renderer renderer to render the sprite in
	 */
	public ComponentDeathAnimation(DeferredRenderer renderer) {
		this.renderer = renderer;
		this.sprite = "blood";

		Event.register(EventEntityDestroyed.class, this::destroyEvent);
	}

	private void destroyEvent(EventEntityDestroyed event) {
		if (!event.entity.hasComponent(ComponentDeathAnimation.class)) return;

		float velocity = 10;
		float scale = 0.1f;
		float lifetime = 0.3f;

		Random rand = new Random();

		for (int i = 0; i < 50; i++) {
			Entity particle = Entity.spawn(getOwner().getPosition().clone().addX(0.5f).addY(0.5f));

			particle.setRotation((float) (rand.nextFloat() * Math.PI * 2));
			particle.getScale().setX(rand.nextFloat() * scale + scale);
			particle.getScale().setY(rand.nextFloat() * scale + scale);

			ComponentMoving componentMoving = new ComponentMoving();
			componentMoving.velocity.setX((float) (rand.nextGaussian() * velocity));
			componentMoving.velocity.setY((float) (rand.nextGaussian() * velocity + velocity));
			particle.addComponent(componentMoving);

			// crashes the game
			particle.addComponent(new ComponentBounding(new Bounding(0, 0, 0.1f, 0.1f), ComponentBounding.LAYER_PARTICLES, ComponentBounding.LAYER_MAP));

			particle.addComponent(new ComponentSpriteRenderer(renderer, "blood", 1));

			particle.addComponent(new ComponentDespawn((float) (Math.random() * lifetime + lifetime)));

			particle.addComponent(new ComponentLight(renderer, new Color(1.0f, 0.2f, 0.2f, 0.0f), 2.5f, 0.3f));
		}
	}

	@Override
	public void destroy() {
		Event.unregister(EventEntityDestroyed.class, this::destroyEvent);
	}
}
