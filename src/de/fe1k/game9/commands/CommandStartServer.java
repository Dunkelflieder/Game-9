package de.fe1k.game9.commands;

import de.fe1k.game9.network.Network;
import de.nerogar.noise.util.Logger;

import java.io.IOException;
import java.util.function.Consumer;

public class CommandStartServer implements Consumer<String[]> {

	@Override
	public void accept(String[] strings) {
		if (strings.length != 2) {
			Logger.log(Logger.ERROR, "must provide port for command " + strings[0]);
			return;
		}
		int port = Integer.parseInt(strings[1]);
		try {
			if (Network.isStarted()) Network.shutdown();
			Network.startServer(port);
		} catch (IOException e) {
			Logger.log(Logger.ERROR, "unable to start server at port " + port);
		}
	}
}
