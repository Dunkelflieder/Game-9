package de.fe1k.game9.components;

import de.nerogar.noise.util.Color;
import de.nerogar.noise.util.Vector2f;

import java.util.Random;

public class ComponentParticleSystem extends ComponentRenderer {

	public String sprite;

	// spawn timing
	public float lastSpawn;

	public  float spawnDelay;
	private float spawnDelayRand;
	public  int   spawnCount;
	private int   spawnCountRand;

	// spawn parameters
	public Vector2f velocity, velocityRand;
	public Vector2f offset, offsetRand;
	public Vector2f scaleMin, scaleRandDelta;
	public float lifetimeMin;
	public float lifetimeRand;

	// light
	public boolean hasLight;
	public Color   lightColor;
	public float   lightReach;
	public float   lightIntensity;

	// physics
	public boolean colliding;

	public ComponentParticleSystem(String sprite, float spawnDelay, float spawnDelayRand, int spawnCount, int spawnCountRand, float lifetimeMin) {
		this.sprite = sprite;
		this.spawnDelay = spawnDelay;
		this.spawnDelayRand = spawnDelayRand;
		this.spawnCount = spawnCount;
		this.spawnCountRand = spawnCountRand;
		this.lifetimeMin = lifetimeMin;

		this.velocity = new Vector2f();
		this.velocityRand = new Vector2f();
		this.offset = new Vector2f();
		this.offsetRand = new Vector2f();
		this.scaleMin = new Vector2f(1);
		this.scaleRandDelta = new Vector2f();
	}

	public int getSpawnCount(Random rand, float deltaTime) {
		lastSpawn += deltaTime;

		int spawns = 0;

		float randomSpawnDelay = rand.nextFloat() * spawnCountRand;

		if (lastSpawn > spawnDelay + randomSpawnDelay) {
			spawns = spawnCount + rand.nextInt(spawnCountRand + 1);
			lastSpawn = 0;
		}

		return spawns;
	}

	public void setLight(Color lightColor, float lightReach, float lightIntensity) {
		hasLight = true;

		this.lightColor = lightColor;
		this.lightReach = lightReach;
		this.lightIntensity = lightIntensity;
	}

}
