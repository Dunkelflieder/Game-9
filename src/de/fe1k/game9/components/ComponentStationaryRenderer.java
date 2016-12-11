package de.fe1k.game9.components;

import de.nerogar.noise.render.deferredRenderer.DeferredRenderable;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;

public class ComponentStationaryRenderer extends ComponentRenderer {

	private DeferredRenderable renderable;
	private DeferredRenderer   renderer;

	/**
	 * This component causes a renderable to be rendered by the given renderer.
	 *
	 * @param renderer   renderer to render the renderable in
	 * @param renderable renderable to render
	 */
	public ComponentStationaryRenderer(DeferredRenderer renderer, DeferredRenderable renderable) {
		this.renderer = renderer;
		this.renderable = renderable;

		renderer.addObject(renderable);
	}

	@Override
	public void destroy() {
		renderer.removeObject(renderable);
	}
}
