package de.fe1k.game9.exceptions;

public class NetworkNotStartedException extends IllegalStateException {

	public NetworkNotStartedException() {
		super();
	}

	public NetworkNotStartedException(String message) {
		super(message);
	}

	public NetworkNotStartedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NetworkNotStartedException(Throwable cause) {
		super(cause);
	}
}
