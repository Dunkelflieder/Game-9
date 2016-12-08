package de.fe1k.game9;

import de.nerogar.noise.render.*;
import de.nerogar.noise.render.deferredRenderer.DeferredContainer;
import de.nerogar.noise.util.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DeferredContainerBank {

	private static Map<String, DeferredContainer> containers = new HashMap<>();

	private static final Shader transparentShader;

	private static String getBasePath(String name) {
		return "res/sprites/" + name + "/";
	}

	private static Mesh createMesh() {
		float offX = 0f;
		float offY = 0f;
		return new Mesh(
				6,
				4,
				new int[]{0, 1, 3, 1, 2, 3},
				new float[]{
						0f-offX, 0f-offY, 0f,
						1f-offX, 0f-offY, 0f,
						1f-offX, 1f-offY, 0f,
						0f-offX, 1f-offY, 0f
				},
				new float[]{
						0f, 0f,
						1f, 0f,
						1f, 1f,
						0f, 1f,
				}
		);
	}

	private static DeferredContainer createContainer(String name, Mesh mesh) {
		if (mesh == null) {
			mesh = createMesh();
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
		return new DeferredContainer(mesh, transparentShader, colorTexture, normalTexture, lightTexture);
	}

	public static DeferredContainer getContainer(String name, Mesh mesh) {
		if (!containers.containsKey(name)) {
			containers.put(name, createContainer(name, mesh));
		}
		return containers.get(name);
	}

	static {
		transparentShader = DeferredContainer.createSurfaceShader("res/shaders/spriteTransparent.vert", "res/shaders/spriteTransparent.frag");
	}

}
