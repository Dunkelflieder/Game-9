package de.fe1k.game9.systems;

import de.fe1k.game9.components.*;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventListener;
import de.fe1k.game9.events.EventUpdate;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;
import de.nerogar.noise.util.Vector2f;

import java.util.Collection;
import java.util.Random;

public class SystemParticles implements GameSystem {

	private DeferredRenderer           renderer;
	private EventListener<EventUpdate> eventUpdate;

	public SystemParticles(DeferredRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public void start() {
		eventUpdate = this::update;
		Event.register(EventUpdate.class, eventUpdate);
	}

	private void update(EventUpdate event) {
		Collection<ComponentParticleSystem> components = Entity.getComponents(ComponentParticleSystem.class);

		Random rand = new Random();

		for (ComponentParticleSystem component : components) {

			int spawns = component.getSpawnCount(rand, event.deltaTime);

			if (spawns > 0) {

				for (int pID = 0; pID < spawns; pID++) {
					Vector2f spawnPosition = component.getOwner().getPosition().clone()
							.add(component.offset)
							.addX((float) (rand.nextGaussian() * component.offsetRand.getX()))
							.addY((float) (rand.nextGaussian() * component.offsetRand.getY()));

					Vector2f spawnVelocity = component.velocity.clone()
							.addX((float) (rand.nextGaussian() * component.velocityRand.getX()))
							.addY((float) (rand.nextGaussian() * component.velocityRand.getY()));

					Vector2f spawnScale = component.scaleMin.clone()
							.add(component.scaleRandDelta.multiplied(rand.nextFloat()));

					Entity particle = Entity.spawn(spawnPosition);
					particle.getScale().set(spawnScale);

					ComponentMoving componentMoving = new ComponentMoving();
					componentMoving.velocity.set(spawnVelocity);
					particle.addComponent(componentMoving);

					particle.addComponent(new ComponentSpriteRenderer(renderer, component.sprite, 1));

					particle.addComponent(new ComponentDespawn(component.lifetimeMin + rand.nextFloat() * component.lifetimeRand));

					if (component.hasLight) {
						particle.addComponent(new ComponentLight(renderer, component.lightColor, component.lightReach, component.lightIntensity));
					}
				}

			}

		}
	}

	@Override
	public void stop() {
		Event.unregister(EventUpdate.class, eventUpdate);
	}
}
