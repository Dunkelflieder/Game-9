package de.fe1k.game9.map;

import de.fe1k.game9.DeferredContainerBank;
import de.fe1k.game9.components.ComponentMarker;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.events.Event;
import de.fe1k.game9.events.EventMapLoaded;
import de.nerogar.noise.render.Mesh;
import de.nerogar.noise.render.RenderProperties3f;
import de.nerogar.noise.render.VertexList;
import de.nerogar.noise.render.deferredRenderer.DeferredContainer;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderable;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;
import de.nerogar.noise.util.Logger;
import de.nerogar.noise.util.Vector2f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapLoader {

	public static void loadMap(DeferredRenderer renderer, String foldername) {

		Map<Tile, List<Entity>> entitiesPerTile = new HashMap<>();
		for (Tile tile : Tile.values()) {
			entitiesPerTile.put(tile, new ArrayList<>());
		}

		try {
			BufferedImage levelImg = ImageIO.read(new File(foldername + "/level.png"));
			BufferedImage metaImg = ImageIO.read(new File(foldername + "/meta.png"));
			BufferedImage markerImg = ImageIO.read(new File(foldername + "/marker.png"));

			int width = levelImg.getWidth();
			int height = levelImg.getHeight();

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int blockColor = levelImg.getRGB(x, height - y - 1);
					int metaColor = metaImg.getRGB(x, height - y - 1) & 0xFFFFFF;
					int markerColor = markerImg.getRGB(x, height - y - 1);

					if ((blockColor & 0xFF000000) != 0) {
						blockColor &= 0xFFFFFF;

						Tile tile = Tile.fromColor(blockColor);
						if (tile == null) {
							Logger.getWarningStream().printf("Unrecognized tile for color: 0x%06x", blockColor);
							tile = Tile.GROUND;
						}

						Entity entity = tile.createEntity(new Vector2f(x, y), metaColor);
						entitiesPerTile.get(tile).add(entity);
					}
					if ((markerColor & 0xFF000000) != 0) {
						markerColor &= 0xFFFFFF;
						Entity markerEntity = Entity.spawn(new Vector2f(x, y));
						markerEntity.addComponent(new ComponentMarker(markerColor));
					}
				}
			}
		} catch (IOException e) {
			Logger.getErrorStream().printf("Could not load map file: %s", foldername);
			e.printStackTrace();
		}

		// build mesh for all stationary tiles
		for (Map.Entry<Tile, List<Entity>> entry : entitiesPerTile.entrySet()) {
			Tile tile = entry.getKey();
			List<Entity> entities = entry.getValue();

			if (tile.stationary) {
				DeferredContainer cont = DeferredContainerBank.getContainer(tile.texname, buildMesh(entities));
				renderer.addObject(new DeferredRenderable(cont, new RenderProperties3f()));
			}
		}
		// add background
		DeferredContainer cont = DeferredContainerBank.getContainer("background", buildBackgroundMesh());
		renderer.addObject(new DeferredRenderable(cont, new RenderProperties3f()));

		Event.trigger(new EventMapLoaded(foldername));
	}

	private static Mesh buildBackgroundMesh() {
		VertexList vl = new VertexList();
		float x = -1e5f;
		int p1 = vl.addVertex(-x, -x, -1f, -x, -x, 0, 0, 0);
		int p2 = vl.addVertex(+x, -x, -1f, +x, -x, 0, 0, 0);
		int p3 = vl.addVertex(+x, +x, -1f, +x, +x, 0, 0, 0);
		int p4 = vl.addVertex(-x, +x, -1f, -x, +x, 0, 0, 0);
		vl.addIndex(p1, p3, p4);
		vl.addIndex(p1, p2, p3);
		return new Mesh(vl.getIndexCount(), vl.getVertexCount(), vl.getIndexArray(), vl.getPositionArray(), vl.getUVArray());
	}

	private static Mesh buildMesh(List<Entity> entities){
		VertexList vl = new VertexList();
		for (Entity e : entities) {
			Vector2f pos = e.getPosition();
			float x = pos.getX();
			float y = pos.getY();
			int p0 = vl.addVertex(x + 0, y + 0, 0, 0, 0, 0, 0, 0);
			int p1 = vl.addVertex(x + 1, y + 0, 0, 1, 0, 0, 0, 0);
			int p2 = vl.addVertex(x + 1, y + 1, 0, 1, 1, 0, 0, 0);
			int p3 = vl.addVertex(x + 0, y + 1, 0, 0, 1, 0, 0, 0);
			vl.addIndex(p0, p1, p3);
			vl.addIndex(p1, p2, p3);
		}
		return new Mesh(vl.getIndexCount(), vl.getVertexCount(), vl.getIndexArray(), vl.getPositionArray(), vl.getUVArray());
	}
}
