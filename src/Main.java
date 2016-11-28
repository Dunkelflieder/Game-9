import de.nerogar.noise.Noise;
import de.nerogar.noise.render.*;
import de.nerogar.noise.render.deferredRenderer.DeferredContainer;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderable;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;
import de.nerogar.noise.util.Logger;
import de.nerogar.noise.util.Timer;

public class Main {
	private static TextureCubeMap getSkyboxTexture() {
		return TextureCubeMapLoader.loadTexture(
				"res/skybox/front.png",
				"res/skybox/back.png",
				"res/skybox/top.png",
				"res/skybox/bottom.png",
				"res/skybox/left.png",
				"res/skybox/right.png"
		);
	}

	private static DeferredRenderable getTruck() {
		Mesh mesh = WavefrontLoader.loadObject("res/truck/mesh.obj");
		Texture2D colorTexture = Texture2DLoader.loadTexture("res/truck/color.png");
		Texture2D normalTexture = Texture2DLoader.loadTexture("noiseEngine/textures/normal.png");
		Texture2D lightTexture = Texture2DLoader.loadTexture("res/truck/light.png");
		DeferredContainer container = new DeferredContainer(mesh, null, colorTexture, normalTexture, lightTexture);
		return new DeferredRenderable(container, new RenderProperties3f());
	}

	public static void main(String[] args) {
		Logger.addStream(Logger.DEBUG, System.out);
		Noise.init();
		GLWindow window = new GLWindow("Game-9", 800, 600, true, 0, null, null);
		DeferredRenderer renderer = new DeferredRenderer(window.getWidth(), window.getHeight());
		renderer.setAmbientOcclusionEnabled(false);  // already baked in light maps
		renderer.setReflectionTexture(getSkyboxTexture());
		DeferredRenderable truck = getTruck();
		renderer.addObject(truck);
		PerspectiveCamera cam = new PerspectiveCamera(90, (float) window.getWidth()/window.getHeight(), 0.01f, 10000.0f);
		cam.setPitch(-0.4f);
		cam.setXYZ(0, 1, 2);
		window.setSizeChangeListener((int width, int height) -> {
			renderer.setFrameBufferResolution(width, height);
			cam.setAspect((float) width/height);
		});
		Timer timer = new Timer();
		while (!window.shouldClose()) {
			timer.update(1/60f);
			GLWindow.updateAll();
			renderer.render(cam);
			window.bind();
			RenderHelper.blitTexture(renderer.getColorOutput());
			truck.getRenderProperties().setYaw((float) timer.getRuntime());
		}
	}
}
