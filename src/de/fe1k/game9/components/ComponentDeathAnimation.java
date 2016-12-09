package de.fe1k.game9.components;

import de.nerogar.noise.util.Color;

public class ComponentDeathAnimation extends ComponentRenderer {

	public String sprite;
	public Color  lightColor;

	public float velocity;
	public float scale;
	public float lifetime;

	public ComponentDeathAnimation() {
		this.sprite = "blood";

		this.lightColor = new Color(1.0f, 0.2f, 0.2f, 0.0f);

		this.velocity = 10;
		this.scale = 0.1f;
		this.lifetime = 0.3f;
	}

}
