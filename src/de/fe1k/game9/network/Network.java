package de.fe1k.game9.network;

import de.nerogar.noise.util.Logger;

import java.io.IOException;
import java.net.BindException;

public class Network {

	////////////////// STATIC STUFF //////////////////

	private static final NetworkManager globalNetwork = new NetworkManager();

	public static void startServer(int port) throws BindException {
		globalNetwork.startServer(port);
		Logger.getInfoStream().println("Started Server at port " + port);
	}

	public static void startClient(String host, int port) throws IOException {
		globalNetwork.startClient(host, port);
		Logger.getInfoStream().println("Connected as client to " + host + ":" + port);
	}

	public static boolean isServer() {
		return globalNetwork.isServer();
	}

	public static void shutdown() {
		globalNetwork.shutdown();
		Logger.getInfoStream().println("Networking shut down.");
	}

}
