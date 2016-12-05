package de.fe1k.game9.map;

import de.fe1k.game9.DeferredContainerBank;
import de.fe1k.game9.components.ComponentBounding;
import de.fe1k.game9.components.ComponentRenderer;
import de.fe1k.game9.components.ComponentSpriteRenderer;
import de.fe1k.game9.entities.Entity;
import de.fe1k.game9.utils.Bounding;
import de.nerogar.noise.render.Mesh;
import de.nerogar.noise.render.RenderProperties3f;
import de.nerogar.noise.render.VertexList;
import de.nerogar.noise.render.deferredRenderer.DeferredContainer;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderable;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;
import de.nerogar.noise.util.Logger;
import de.nerogar.noise.util.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapLoader {

	public static void loadMap(DeferredRenderer renderer, String filename) {

		Map<Tile, List<Entity>> entitiesPerTile = new HashMap<>();
		for (Tile tile : Tile.values()) {
			entitiesPerTile.put(tile, new ArrayList<>());
		}

		try {
			BufferedImage img = ImageIO.read(new File(filename));
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					int color = img.getRGB(x, img.getHeight() - y - 1);
					if (color == 0xffffffff) {
						continue;  // air
					}
					Tile tile = Tile.fromColor(color);
					if (tile == null) {
						Logger.getErrorStream().printf("Unrecognized tile for color: 0x%06x", color);
						continue;
					}
					Entity entity = Entity.spawn();
					entity.teleport(new Vector3f(x, y, 0));
					entitiesPerTile.get(tile).add(entity);
				}
			}
		} catch (IOException e) {
			Logger.getErrorStream().printf("Could not loading map file: %s", filename);
			e.printStackTrace();
		}

		for (Map.Entry<Tile, List<Entity>> entry : entitiesPerTile.entrySet()) {
			Tile tile = entry.getKey();
			List<Entity> entities = entry.getValue();

			for (Entity entity : entities) {
				Bounding bounding = new Bounding(0, 0, 1, 1);
				entity.addComponent(new ComponentBounding(bounding));
			}

			////// Build how to render //////

			if (!tile.static_) {
				for (Entity entity : entities) {
					entity.addComponent(new ComponentSpriteRenderer(renderer, tile.texname));
				}
				continue;
			}

			// static tile

			for (Entity entity : entities) {
				// just to mark it. it's rendered externally anyway
				entity.addComponent(new ComponentRenderer());
			}

			VertexList vl = new VertexList();
			for (Entity e : entities) {
				Vector3f pos = e.getPosition();
				float x = pos.getX();
				float y = pos.getY();
				int p0 = vl.addVertex(x + 0, y + 0, 0, 0, 0, 0, 0, 0);
				int p1 = vl.addVertex(x + 1, y + 0, 0, 1, 0, 0, 0, 0);
				int p2 = vl.addVertex(x + 1, y + 1, 0, 1, 1, 0, 0, 0);
				int p3 = vl.addVertex(x + 0, y + 1, 0, 0, 1, 0, 0, 0);
				vl.addIndex(p0, p1, p3);
				vl.addIndex(p1, p2, p3);
			}
			Mesh m = new Mesh(vl.getIndexCount(), vl.getVertexCount(), vl.getIndexArray(), vl.getPositionArray(), vl.getUVArray());
			DeferredContainer cont = DeferredContainerBank.getContainer(tile.texname, m);
			renderer.addObject(new DeferredRenderable(cont, new RenderProperties3f()));
		}
	}
}
