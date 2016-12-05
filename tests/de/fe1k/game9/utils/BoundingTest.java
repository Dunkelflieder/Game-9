package de.fe1k.game9.utils;

import de.nerogar.noise.util.Vector2f;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundingTest {
	@Test
	void getEscapeVector1() {
		Bounding b1 = new Bounding(
				new Vector2f(0),
				new Vector2f(2)
		);
		Bounding b2 = new Bounding(
				new Vector2f(1),
				new Vector2f(3)
		);
		Optional<Vector2f> escape = b1.getEscapeVector(b2);
		assertTrue(escape.isPresent());
		assertEquals(new Vector2f(-1), escape.get());
	}

	@Test
	void getEscapeVector2() {
		Bounding b1 = new Bounding(
				new Vector2f(0, -0.1f),
				new Vector2f(1)
		);
		Bounding b2 = new Bounding(
				new Vector2f(-1),
				new Vector2f(1, 0)
		);
		Optional<Vector2f> escape = b1.getEscapeVector(b2);
		assertTrue(escape.isPresent());
		assertEquals(new Vector2f(1, 0.1f), escape.get());
	}

	@Test
	void translated() {
		Bounding b1 = new Bounding(
				new Vector2f(-1, -2),
				new Vector2f(5, 4)
		);
		Bounding b2 = b1.translated(new Vector2f(2, -7));

		assertEquals(new Bounding(
				new Vector2f(1, -9),
				new Vector2f(7, -3)
		), b2);
	}

}
