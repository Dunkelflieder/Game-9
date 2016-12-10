package de.fe1k.game9;

import de.fe1k.game9.components.Component;
import de.fe1k.game9.components.ComponentPlayer;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventBeforeRender;
import de.fe1k.game9.events.EventUpdate;
import de.fe1k.game9.map.MapLoader;
import de.fe1k.game9.systems.*;
import de.nerogar.noise.Noise;
import de.nerogar.noise.render.GLWindow;
import de.nerogar.noise.render.OrthographicCamera;
import de.nerogar.noise.render.RenderHelper;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;
import de.nerogar.noise.util.Timer;

import java.util.List;

public class Game {

	public static  GLWindow           window;
	public static  DeferredRenderer   renderer;
	private        OrthographicCamera camera;
	private static Timer              timer;  // TODO properly distinguish between static and non-static stuff
	private        long               lastFpsUpdate;

	public Game() {
		Noise.init();
		setUpWindow();
		setUpCamera();
		setUpRenderer();
		setUpSystems();
		MapLoader.loadMap(renderer, "res/map/map1");
		timer = new Timer();
	}

	private void setUpSystems() {
		SystemMoving systemMoving = new SystemMoving();
		SystemEntityLookup systemEntityLookup = new SystemEntityLookup();
		SystemCollision systemCollision = new SystemCollision(systemEntityLookup);
		SystemCallbacks systemCallbacks = new SystemCallbacks();
		SystemDeathAnimation systemDeathAnimation = new SystemDeathAnimation(renderer);
		SystemKillOnCollision systemKillOnCollision = new SystemKillOnCollision();

		systemMoving.start();
		systemEntityLookup.start();
		systemCollision.start();
		systemCallbacks.start();
		systemDeathAnimation.start();
		systemKillOnCollision.start();
	}

	private void setUpRenderer() {
		renderer = new DeferredRenderer(window.getWidth(), window.getHeight());

		renderer.setSunLightBrightness(0);
		renderer.setAmbientOcclusionEnabled(false);
		renderer.setMinAmbientBrightness(0.1f);
	}

	private void setUpWindow() {
		window = new GLWindow("Game-9", 1280, 720, true, 0, null, null);
		window.setSizeChangeListener((int width, int height) -> {
			renderer.setFrameBufferResolution(width, height);
			camera.setAspect((float) width / height);
			camera.setHeight((float) height / 32);
		});
	}

	private void setUpCamera() {
		camera = new OrthographicCamera((float) window.getHeight() / 32, (float) window.getWidth() / window.getHeight(), 100, -100);
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
		float targetDelta = 1 / 60f;
		timer.update(targetDelta);
		displayFPS();
		Event.trigger(new EventUpdate(targetDelta));
		Event.trigger(new EventBeforeRender(targetDelta, timer.getRuntime()));

		List<ComponentPlayer> playerList = Entity.getComponents(ComponentPlayer.class);
		if (!playerList.isEmpty()) {
			Entity player = playerList.get(0).getOwner();

			camera.setX(player.getPosition().getX());
			camera.setY(player.getPosition().getY());
		}

		renderer.render(camera);
		window.bind();
		RenderHelper.blitTexture(renderer.getColorOutput());
	}

	public void run() {
		while (!window.shouldClose()) {
			mainloop();
		}
	}

	public static double getRunTime() {
		return timer.getRuntime();
	}
}
