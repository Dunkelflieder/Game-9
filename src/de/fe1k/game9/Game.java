package de.fe1k.game9;

import de.fe1k.game9.components.ComponentMoving;
import de.fe1k.game9.components.ComponentSpriteRenderer;
import de.fe1k.game9.components.ComponentTestRotation;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventBeforeRender;
import de.fe1k.game9.events.EventUpdate;
import de.nerogar.noise.Noise;
import de.nerogar.noise.render.GLWindow;
import de.nerogar.noise.render.PerspectiveCamera;
import de.nerogar.noise.render.RenderHelper;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;
import de.nerogar.noise.util.Timer;

public class Game {
	private GLWindow window;
	private DeferredRenderer renderer;
	private PerspectiveCamera camera;
	private Timer timer;

	public Game() {
		Noise.init();
		setUpWindow();
		setUpCamera();
		setUpRenderer();
		makeRenderableEntity();
		timer = new Timer();
	}

	private void setUpRenderer() {
		renderer = new DeferredRenderer(window.getWidth(), window.getHeight());
	}

	private void setUpWindow() {
		window = new GLWindow("Game-9", 800, 600, true, 0, null, null);
		window.setSizeChangeListener((int width, int height) -> {
			renderer.setFrameBufferResolution(width, height);
			camera.setAspect((float) width/height);
		});
	}

	private void setUpCamera() {
		camera = new PerspectiveCamera(80, (float) window.getWidth()/window.getHeight(), 0.01f, 10000.0f);
		camera.setXYZ(0, 0, 2);
	}

	private void mainloop() {
		GLWindow.updateAll();
		float targetDelta = 1/60f;
		timer.update(targetDelta);
		Event.trigger(new EventUpdate(targetDelta));
		Event.trigger(new EventBeforeRender());
		renderer.render(camera);
		window.bind();
		RenderHelper.blitTexture(renderer.getColorOutput());
	}

	public void run() {
		while (!window.shouldClose()) {
			mainloop();
		}
	}

	private void makeRenderableEntity() {
		Entity entity = Entity.spawn();
		entity.getScale().set(2.0f);
		entity.addComponent(new ComponentSpriteRenderer(renderer, "<unit.png>", null));
		entity.addComponent(new ComponentTestRotation());
		entity.addComponent(new ComponentMoving());
	}
}
