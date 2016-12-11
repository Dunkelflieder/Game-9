package de.fe1k.game9.map;

import de.nerogar.noise.util.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapCache {
	public static class MapFileContainer {
		public BufferedImage levelImg;
		public BufferedImage metaImg;
		public BufferedImage markerImg;
		public MapFileContainer(BufferedImage levelImg, BufferedImage metaImg, BufferedImage markerImg) {
			this.levelImg = levelImg;
			this.metaImg = metaImg;
			this.markerImg = markerImg;
		}
	}

	private static Map<String, MapFileContainer> mapContainers;

	static {
		mapContainers = new HashMap<>();
	}

	public static MapFileContainer getMapContainer(String mapName) {
		MapFileContainer container = mapContainers.get(mapName);
		if (container == null) {
			try {
				BufferedImage levelImg = ImageIO.read(new File(mapName + "/level.png"));
				BufferedImage metaImg = ImageIO.read(new File(mapName + "/meta.png"));
				BufferedImage markerImg = ImageIO.read(new File(mapName + "/marker.png"));
				container = new MapFileContainer(levelImg, metaImg, markerImg);
				mapContainers.put(mapName, container);
			} catch (IOException e) {
				Logger.getErrorStream().printf("Could not load map file: %s", mapName);
				e.printStackTrace();
			}
		}
		return container;
	}
}
