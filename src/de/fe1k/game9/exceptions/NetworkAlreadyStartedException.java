package de.fe1k.game9.exceptions;

public class NetworkAlreadyStartedException extends IllegalStateException {

	public NetworkAlreadyStartedException() {
		super();
	}

	public NetworkAlreadyStartedException(String message) {
		super(message);
	}

	public NetworkAlreadyStartedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NetworkAlreadyStartedException(Throwable cause) {
		super(cause);
	}
}
