package de.fe1k.game9;

import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventBeforeRender;
import de.nerogar.noise.render.*;
import de.nerogar.noise.render.deferredRenderer.DeferredContainer;
import de.nerogar.noise.util.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DeferredContainerBank {

	private static class AnimationProperties {

		private final int   frames;
		private final float delay;
		private       int   currentFrame;
		private       float lastUpdate;

		public AnimationProperties(int frames, float delay) {
			this.frames = frames;
			this.delay = delay;
			currentFrame = 0;
			lastUpdate = 0;
		}
	}

	private static class SpriteProperties {

		private final boolean drawBackside;

		public SpriteProperties(boolean drawBackside) {
			this.drawBackside = drawBackside;
		}
	}

	private static Map<String, DeferredContainer> containers = new HashMap<>();

	private static Map<String, AnimationProperties> animationPropertiesMap = new HashMap<>();
	private static Map<String, SpriteProperties>    spritePropertiesMap    = new HashMap<>();

	private static final Shader transparentShader;

	private static String getBasePath(String name) {
		return "res/sprites/" + name + "/";
	}

	private static Mesh createMesh(boolean drawBackside) {
		VertexList vertexList = new VertexList();

		int p0 = vertexList.addVertex(0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f);
		int p1 = vertexList.addVertex(1f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
		int p2 = vertexList.addVertex(1f, 1f, 0f, 1f, 1f, 0f, 0f, 1f);
		int p3 = vertexList.addVertex(0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f);

		vertexList.addIndex(p0, p1, p3);
		vertexList.addIndex(p1, p2, p3);

		if (drawBackside) {
			p0 = vertexList.addVertex(-1f, 0f, 0f, 0f, 0f, 0f, 0f, 1f);
			p1 = vertexList.addVertex(0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
			p2 = vertexList.addVertex(0f, 1f, 0f, 1f, 1f, 0f, 0f, 1f);
			p3 = vertexList.addVertex(-1f, 1f, 0f, 0f, 1f, 0f, 0f, 1f);

			vertexList.addIndex(p0, p3, p1);
			vertexList.addIndex(p1, p3, p2);
		}

		return new Mesh(
				vertexList.getIndexCount(),
				vertexList.getVertexCount(),
				vertexList.getIndexArray(),
				vertexList.getPositionArray(),
				vertexList.getUVArray(),
		        vertexList.getNormalArray()
		);
	}

	private static void update(EventBeforeRender event) {
		for (Map.Entry<String, DeferredContainerBank.AnimationProperties> entry : animationPropertiesMap.entrySet()) {
			DeferredContainerBank.AnimationProperties properties = entry.getValue();
			DeferredContainer container = containers.get(entry.getKey());

			properties.lastUpdate += event.deltaTime;

			if (properties.lastUpdate > properties.delay) {

				int newFrames = (int) (properties.lastUpdate / properties.delay);
				properties.currentFrame += newFrames;
				properties.currentFrame %= properties.frames;

				properties.lastUpdate %= properties.delay;

				if (container != null) {
					// no, there is no other way to set shader parameters
					container.getSurfaceShader().activate();
					container.getSurfaceShader().setUniform1f("currentFrame", properties.currentFrame);
					container.getSurfaceShader().deactivate();
				}

			}

		}

	}

	private static DeferredContainer createContainer(String name, Mesh mesh) {
		SpriteProperties spriteProperties = spritePropertiesMap.get(name);
		AnimationProperties animationProperties = animationPropertiesMap.get(name);

		if (mesh == null) {
			boolean drawBackside = false;
			if (spriteProperties != null) {
				drawBackside = spriteProperties.drawBackside;
			}
			mesh = createMesh(drawBackside);
		}

		String colorPath = getBasePath(name) + "color.png";
		Texture2D colorTexture;
		if (new File(colorPath).exists()) {
			colorTexture = Texture2DLoader.loadTexture(colorPath, Texture2D.InterpolationType.NEAREST);
		} else {
			colorTexture = Texture2DLoader.loadTexture("<white.png>", Texture2D.InterpolationType.NEAREST);
			Logger.getErrorStream().printf("Color map not found: %s", colorPath);
		}

		String normalPath = getBasePath(name) + "normal.png";
		Texture2D normalTexture;
		if (new File(normalPath).exists()) {
			normalTexture = Texture2DLoader.loadTexture(normalPath, Texture2D.InterpolationType.NEAREST);
		} else {
			normalTexture = Texture2DLoader.loadTexture("<normal.png>", Texture2D.InterpolationType.NEAREST);
		}

		String lightPath = getBasePath(name) + "red.png";
		Texture2D lightTexture;
		if (new File(lightPath).exists()) {
			lightTexture = Texture2DLoader.loadTexture(lightPath, Texture2D.InterpolationType.NEAREST);
		} else {
			lightTexture = Texture2DLoader.loadTexture("<red.png>", Texture2D.InterpolationType.NEAREST);
		}

		Shader shader = transparentShader;
		if (animationProperties != null) {
			shader = DeferredContainer.createSurfaceShader("res/shaders/spriteAnimation.vert", "res/shaders/spriteAnimation.frag");

			shader.activate();
			shader.setUniform1f("frames", animationProperties.frames);
			shader.deactivate();

		}

		return new DeferredContainer(mesh, shader, colorTexture, normalTexture, lightTexture);
	}

	public static DeferredContainer getContainer(String name, Mesh mesh) {
		if (!containers.containsKey(name)) {
			containers.put(name, createContainer(name, mesh));
		}
		return containers.get(name);
	}

	static {
		Event.register(EventBeforeRender.class, DeferredContainerBank::update);

		transparentShader = DeferredContainer.createSurfaceShader("res/shaders/spriteTransparent.vert", "res/shaders/spriteTransparent.frag");

		animationPropertiesMap.put("man", new AnimationProperties(6, 0.07f));
		animationPropertiesMap.put("lava", new AnimationProperties(8, 0.15f));
		animationPropertiesMap.put("fire", new AnimationProperties(8, 0.3f));

		spritePropertiesMap.put("man", new SpriteProperties(true));
	}

}
