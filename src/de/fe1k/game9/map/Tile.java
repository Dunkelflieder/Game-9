package de.fe1k.game9.map;

public enum Tile {
	START(0x00ff00, "man"),
	STOP(0xff0000, "man"),
	GROUND(0x000000, "ground"),
	FLOOR(0x111111, "floor"),
	PILLAR(0xffff00, "pillar"),
	BARRIER(0xffbb00, "barrier");

	public final int color;
	public final String texname;

	Tile(int color, String texname) {
		this.color = color;
		this.texname = texname;
	}
	
	public static Tile fromColor(int color) {
		for (Tile tile : values()) {
			if (tile.color == (color & 0x00ffffff)) {
				return tile;
			}
		}
		return null;
	}
}
