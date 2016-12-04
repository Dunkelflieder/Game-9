package de.fe1k.game9.map;

import de.fe1k.game9.components.ComponentSpriteRenderer;
import de.fe1k.game9.entities.Entity;
import de.nerogar.noise.render.deferredRenderer.DeferredRenderer;
import de.nerogar.noise.util.Logger;
import de.nerogar.noise.util.Vector2f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {

	public static List<Entity> loadMap(DeferredRenderer renderer, String filename) {

		List<Entity> entities = new ArrayList<>();

		try {
			BufferedImage img = ImageIO.read(new File(filename));
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y_img = 0; y_img < img.getHeight(); y_img++) {
					int y = img.getHeight() - y_img - 1;
					int color = img.getRGB(x, y);
					if (color == 0xffffffff) {
						continue;  // air
					}
					Tile tile = Tile.fromColor(color);
					if (tile == null) {
						Logger.getErrorStream().printf("Unrecognized tile for color: 0x%06x", color);
						continue;
					}
					Entity entity = Entity.spawn();
					entity.addComponent(new ComponentSpriteRenderer(renderer, tile.texname, new Vector2f(x, y)));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return entities;

	}

}
