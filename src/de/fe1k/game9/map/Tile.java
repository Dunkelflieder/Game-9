package de.fe1k.game9.map;

import de.fe1k.game9.entities.*;
import de.nerogar.noise.util.Vector2f;

import java.util.Arrays;
import java.util.stream.Stream;

public enum Tile {

	MARKER(0xFFFFFF, null, false, new EntityBuilderMarker()),
	PLAYER(0x00AA00, null, false, new EntityBuilderPlayer()),
	GROUND(0x000000, "ground", true, new EntityBuilderBlock()),
	FLOOR(0x111111, "floor", true, new EntityBuilderBlock()),
	PILLAR(0xFFBB00, "pillar", true, new EntityBuilderBlock()),
	BARRIER(0xFFFF00, "barrier", true, new EntityBuilderBlock()),
	LAMP(0xFFAA00, "lamp_ceiling", true, new EntityBuilderLamp()),
	LAVA(0x800000, "lava", false, new EntityBuilderLava()),
	FIRE(0xFF0000, "fire", false, new EntityBuilderFire());

	public interface EntityBuilder {

		void createEntity(Entity entity, Tile tile, int markerColor);
	}

	public final  int           color;
	public final  String        texname;
	public final  boolean       stationary;
	private final EntityBuilder entityBuilder;

	Tile(int color, String texname, boolean stationary, EntityBuilder entityBuilder) {
		this.color = color;
		this.texname = texname;
		this.stationary = stationary;
		this.entityBuilder = entityBuilder;
	}

	public Entity createEntity(Vector2f position, int markerColor) {
		Entity entity = Entity.spawn(position);

		if (entityBuilder != null) {
			entityBuilder.createEntity(entity, this, markerColor);
		}

		return entity;
	}

	public static Tile fromColor(int color) {
		for (Tile tile : values()) {
			if (tile.color == (color)) {
				return tile;
			}
		}
		return null;
	}

	public static Stream<Tile> getStaticTiles() {
		return Arrays.stream(values()).filter(tile -> tile.stationary);
	}
}
