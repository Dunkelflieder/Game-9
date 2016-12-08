package de.fe1k.game9.map;

import java.util.Arrays;
import java.util.stream.Stream;

public enum Tile {
	START(0xff0000, "man", false),
	STOP(0x00ff00, "man", false),
	GROUND(0x000000, "ground", true),
	FLOOR(0x111111, "floor", true),
	PILLAR(0xffbb00, "pillar", true),
	BARRIER(0xffff00, "barrier", true);

	public final int     color;
	public final String  texname;
	public final boolean stationary;

	Tile(int color, String texname, boolean stationary) {
		this.color = color;
		this.texname = texname;
		this.stationary = stationary;
	}
	
	public static Tile fromColor(int color) {
		for (Tile tile : values()) {
			if (tile.color == (color & 0x00ffffff)) {
				return tile;
			}
		}
		return null;
	}

	public static Stream<Tile> getStaticTiles() {
		return Arrays.stream(values()).filter(tile -> tile.stationary);
	}
}
