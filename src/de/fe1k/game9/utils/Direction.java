package de.fe1k.game9.utils;

/**
 * Enum to represent the 4 directions UP, RIGHT, DOWN and LEFT (= 0, 1, 2, 3).
 */
public enum Direction {
	UP(0),
	RIGHT(1),
	DOWN(2),
	LEFT(3);

	public final int val;

	Direction(int val) {
		this.val = val;
	}

	/**
	 * Returns the Direction Enum, which corresponds to {@param val}, or null if no match.
	 * @return UP, RIGHT, DOWN, LEFT for 0, 1, 2, 3. Otherwise null
	 */
	public static Direction fromVal(int val) {
		for (Direction d : values()) {
			if (d.val == val) {
				return d;
			}
		}
		return null;
	}

	/**
	 * Returns the Direction enum, which represents the current one rotated by
	 * {@param quarterTurnsCW} 90 degree clockwise rotations.
	 * @param quarterTurnsCW number of 90 degree clockwise rotations
	 * @return the Enum representing the new Direction
	 */
	public Direction rotated(int quarterTurnsCW) {
		int newVal = (((val + quarterTurnsCW) % 4) + 4) % 4;
		return Direction.fromVal(newVal);
	}

	/**
	 * Returns a Vector2i with one component offset by 1, basically "moving" the input vector into
	 * the direction this enum represents.
	 * @param start the Vector2i to offset
	 * @return a Vector2i with one of the componentd offset by 1, depending on this durection enum.
	 */
	public Vector2i offset(Vector2i start) {
		int x = start.getX();
		int y = start.getY();
		if (this == Direction.UP) y--;
		else if (this == Direction.DOWN) y++;
		else if (this == Direction.LEFT) x--;
		else if (this == Direction.RIGHT) x++;
		return new Vector2i(x, y);
	}

	/**
	 * @return true if this direction is horizontal (LEFT or RIGHT), false otherwise (UP or DOWN)
	 */
	public boolean isHorizontal() {
		return this == Direction.LEFT || this == Direction.RIGHT;
	}
}
