package de.fe1k.game9.utils;

import de.nerogar.noise.util.Vector2f;

import java.util.Optional;

// 2D-AABB
public class Bounding {
	private Vector2f min;
	private Vector2f max;

	public Bounding() {
		this(new Vector2f(), new Vector2f(1));
	}

	public Bounding(Vector2f min, Vector2f max) {
		this.min = min;
		this.max = max;
	}

	public Vector2f getMin() {
		return min;
	}

	public void setMin(Vector2f min) {
		this.min = min;
	}

	public Vector2f getMax() {
		return max;
	}

	public void setMax(Vector2f max) {
		this.max = max;
	}

	public Optional<Vector2f> getEscapeVector(Bounding bounding) {
		float gapXR = bounding.getMin().getX() - getMax().getX();
		float gapXL = getMin().getX() - bounding.getMax().getX();
		float gapYR = bounding.getMin().getY() - getMax().getY();
		float gapYL = getMin().getY() - bounding.getMax().getY();
		if (gapXL > 0 || gapXR > 0 || gapYL > 0 || gapYR > 0) {
			return Optional.empty();
		}
		Vector2f escape = new Vector2f();
		escape.setX(Math.min(gapXL, gapXR));
		escape.setY(Math.min(gapYL, gapYR));
		return Optional.of(escape);
	}
}
