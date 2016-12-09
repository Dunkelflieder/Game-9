package de.fe1k.game9;

import de.nerogar.noise.util.Logger;

public class Main {
	public static void main(String[] args) {
		Logger.addStream(Logger.DEBUG, Logger.INFO, System.out);
		Logger.addStream(Logger.WARNING, System.err);

		new Game().run();
	}
}
