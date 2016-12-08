package de.fe1k.game9.components;

import de.fe1k.game9.DeferredContainerBank;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventBeforeRender;
import de.nerogar.noise.render.RenderProperties3f;
import de.nerogar.noise.render.deferredRenderer.DeferredContainer;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderable;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;
import de.nerogar.noise.util.Vector2f;

public class ComponentSpriteRenderer extends ComponentRenderer {
	protected DeferredRenderable renderable;
	protected DeferredRenderer renderer;
	private String sprite;

	/**
	 * This component causes a 2D-sprite to be rendered by the given renderer.
	 * @param renderer renderer to render the sprite in
	 * @param sprite filepath of the sprite
	 */
	public ComponentSpriteRenderer(DeferredRenderer renderer, String sprite) {
		this.renderer = renderer;
		this.sprite = sprite;
		rebuildRenderable();
		Event.register(EventBeforeRender.class, this::beforeRender);
	}

	private void rebuildRenderable() {
		if (renderable != null) {
			renderer.removeObject(renderable);
		}
		DeferredContainer container = DeferredContainerBank.getContainer(sprite, null);
		renderable = new DeferredRenderable(container, new RenderProperties3f());
		renderer.addObject(renderable);
	}

	public String getSprite() {
		return sprite;
	}

	public void setSprite(String sprite) {
		this.sprite = sprite;
		rebuildRenderable();
	}

	private void beforeRender(EventBeforeRender event) {
		Vector2f pos = getOwner().getPosition();
		Vector2f scale = getOwner().getScale();
		float rot = getOwner().getRotation();
		renderable.getRenderProperties().setXYZ(pos.getX(), pos.getY(), 0);
		renderable.getRenderProperties().setScale(scale.getX(), scale.getY(), 1);
		renderable.getRenderProperties().setPitch(0);
		renderable.getRenderProperties().setYaw(0);
		renderable.getRenderProperties().setRoll(rot);
	}

	@Override
	public void destroy() {
		renderer.removeObject(renderable);
		Event.unregister(EventBeforeRender.class, this::beforeRender);
	}
}
