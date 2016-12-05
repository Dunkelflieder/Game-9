package de.fe1k.game9;

import de.fe1k.game9.components.ComponentBounding;
import de.fe1k.game9.components.ComponentMoving;
import de.fe1k.game9.components.ComponentSpriteAnimationRenderer;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventBeforeRender;
import de.fe1k.game9.events.EventUpdate;
import de.fe1k.game9.map.MapLoader;
import de.fe1k.game9.systems.SystemCollision;
import de.fe1k.game9.systems.SystemEntityLookup;
import de.fe1k.game9.systems.SystemMoving;
import de.fe1k.game9.utils.Bounding;
import de.nerogar.noise.Noise;
import de.nerogar.noise.render.GLWindow;
import de.nerogar.noise.render.OrthographicCamera;
import de.nerogar.noise.render.RenderHelper;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;
import de.nerogar.noise.util.Timer;
import de.nerogar.noise.util.Vector3f;

public class Game {
	private GLWindow window;
	private DeferredRenderer renderer;
	private OrthographicCamera camera;
	private Timer timer;
	private long lastFpsUpdate;

	public Game() {
		Noise.init();
		setUpWindow();
		setUpCamera();
		setUpRenderer();
		setUpSystems();
		//for (int i = 0; i < 50; i++) {
			MapLoader.loadMap(renderer, "res/map/map0.png");
		//}
		makeRenderableEntity();
		timer = new Timer();
	}

	private void setUpSystems() {
		SystemMoving systemMoving = new SystemMoving();
		SystemEntityLookup systemEntityLookup = new SystemEntityLookup();
		SystemCollision systemCollision = new SystemCollision(systemEntityLookup);

		systemMoving.start();
		systemEntityLookup.start();
		systemCollision.start();
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
		camera = new OrthographicCamera(20, (float) window.getWidth()/window.getHeight(), 10, -10);
		camera.setXYZ(10, 10, 10);
	}

	private void displayFPS() {
		if (System.nanoTime() - lastFpsUpdate > 1_000_000_000 / 5) {
			lastFpsUpdate = System.nanoTime();

			float fps = Math.round(timer.getFrequency() * 10f) / 10f;
			float time = Math.round(timer.getCalcTime() * 1000000f) / 1000f;

			window.setTitle("FPS: " + fps + " -> frame time: " + time);
		}
	}

	private void mainloop() {
		GLWindow.updateAll();
		float targetDelta = 1/60f;
		timer.update(targetDelta);
		displayFPS();
		Event.trigger(new EventUpdate(targetDelta));
		Event.trigger(new EventBeforeRender(targetDelta, timer.getRuntime()));
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
		entity.getScale().set(1.0f);
		entity.teleport(new Vector3f(10, 10, 0));
		entity.addComponent(new ComponentSpriteAnimationRenderer(renderer, "man", 6, 0.07f));
		entity.addComponent(new ComponentMoving());
		entity.addComponent(new ComponentBounding(new Bounding(0, 0, 1, 1)));
	}
}
