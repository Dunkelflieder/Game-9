package de.fe1k.game9.commands;

import de.fe1k.game9.network.Network;
import de.nerogar.noise.util.Logger;

import java.io.IOException;
import java.util.function.Consumer;

public class CommandConnect implements Consumer<String[]> {

	@Override
	public void accept(String[] strings) {
		if (strings.length != 3) {
			Logger.log(Logger.ERROR, "must provide IP and port for command " + strings[0]);
			return;
		}
		String host = strings[1];
		int port = Integer.parseInt(strings[2]);
		try {
			if (Network.isStarted()) Network.shutdown();
			Network.startClient(host, port);
		} catch (IOException e) {
			Logger.log(Logger.ERROR, "unable to connect to " + host + ":" + port);
		}
	}
}
