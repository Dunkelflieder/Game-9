package de.fe1k.game9.components;

import de.fe1k.game9.DeferredContainerBank;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventBeforeRender;
import de.fe1k.game9.events.EventListener;
import de.nerogar.noise.render.RenderProperties3f;
import de.nerogar.noise.render.deferredRenderer.DeferredContainer;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderable;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;
import de.nerogar.noise.util.Vector2f;
import de.nerogar.noise.util.Vector3f;

public class ComponentSpriteRenderer extends Component {
	protected DeferredRenderable renderable;
	protected DeferredRenderer renderer;
	private Vector2f anchor;
	private String sprite;

	/**
	 * This component causes a 2D-sprite to be rendered by the given renderer.
	 * @param renderer renderer to render the sprite in
	 * @param sprite filepath of the sprite
	 * @param anchor a 2D-point describing where the sprite is anchored at. Defaults to [0.5, 0.5] if null.
	 */
	public ComponentSpriteRenderer(DeferredRenderer renderer, String sprite, Vector2f anchor) {
		if (anchor == null) {
			anchor = new Vector2f(0.5f, 0.5f);
		}
		this.anchor = anchor;
		this.renderer = renderer;
		this.sprite = sprite;
		rebuildRenderable();
		Event.register(EventBeforeRender.class, this::beforeRender);
	}

	private void rebuildRenderable() {
		if (renderable != null) {
			renderer.removeObject(renderable);
		}
		DeferredContainer container = DeferredContainerBank.getContainer(sprite);
		renderable = new DeferredRenderable(container, new RenderProperties3f());
		renderer.addObject(renderable);
	}

	public Vector2f getAnchor() {
		return anchor;
	}

	public void setAnchor(Vector2f anchor) {
		this.anchor = anchor;
		rebuildRenderable();
	}

	public String getSprite() {
		return sprite;
	}

	public void setSprite(String sprite) {
		this.sprite = sprite;
		rebuildRenderable();
	}

	private void beforeRender(EventBeforeRender event) {
		if (!getOwner().isPresent()) {
			return;
		}
		Entity owner = getOwner().get();
		Vector3f pos = owner.getPosition();
		Vector3f scale = owner.getScale();
		Vector3f rot = owner.getRotation();
		renderable.getRenderProperties().setXYZ(pos.getX(), pos.getY(), pos.getZ());
		renderable.getRenderProperties().setScale(scale.getX(), scale.getY(), scale.getZ());
		renderable.getRenderProperties().setPitch(rot.getX());
		renderable.getRenderProperties().setYaw(rot.getY());
		renderable.getRenderProperties().setRoll(rot.getZ());
	}

	@Override
	public void destroy() {
		renderer.removeObject(renderable);
		Event.unregister((EventListener<EventBeforeRender>) this::beforeRender);
	}
}
