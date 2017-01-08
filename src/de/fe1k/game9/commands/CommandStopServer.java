package de.fe1k.game9.commands;

import de.fe1k.game9.network.Network;
import de.nerogar.noise.util.Logger;

import java.util.function.Consumer;

public class CommandStopServer implements Consumer<String[]> {

	@Override
	public void accept(String[] strings) {
		if (!Network.isStarted() || !Network.isServer()) {
			Logger.log(Logger.ERROR, "Not running a server.");
			return;
		}
		Network.shutdown();
	}
}
