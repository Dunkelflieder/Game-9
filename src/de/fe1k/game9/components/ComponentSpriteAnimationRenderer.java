package de.fe1k.game9.components;

import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventBeforeRender;
import de.nerogar.noise.render.Shader;
import de.nerogar.noise.render.deferredRenderer.DeferredContainer;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;

public class ComponentSpriteAnimationRenderer extends ComponentSpriteRenderer {

	private final Shader animationShader;

	private final int   frames;
	private final float delay;

	private int   currentFrame;
	private float lastUpdate;

	public ComponentSpriteAnimationRenderer(DeferredRenderer renderer, String sprite, int frames, float delay) {
		super(renderer, sprite);
		this.frames = frames;
		this.delay = delay;
		Event.register(EventBeforeRender.class, this::beforeRender);

		animationShader = DeferredContainer.createSurfaceShader("res/shaders/spriteAnimation.vert", "res/shaders/spriteAnimation.frag");

		renderable.getContainer().setSurfaceShader(animationShader);

		// no, there is no other way to set shader parameters
		animationShader.activate();
		animationShader.setUniform1f("frames", frames);
		animationShader.deactivate();

	}

	private void beforeRender(EventBeforeRender event) {
		lastUpdate += event.deltaTime;

		if (lastUpdate > delay) {

			int newFrames = (int) (lastUpdate / delay);
			currentFrame += newFrames;
			currentFrame %= frames;

			lastUpdate %= delay;

		}

		// no, there is no other way to set shader parameters
		animationShader.activate();
		animationShader.setUniform1f("currentFrame", currentFrame);
		animationShader.deactivate();

	}

	@Override
	public void destroy() {
		super.destroy();

		animationShader.cleanup();
	}
}
