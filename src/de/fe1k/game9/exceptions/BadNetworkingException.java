package de.fe1k.game9.exceptions;

/**
 * Thrown when errors occur, that are caused by unexpected behaviour from a peer connection.
 * The connection causing this should be closed.
 */
public class BadNetworkingException extends RuntimeException {

	public BadNetworkingException() {
		super();
	}

	public BadNetworkingException(String message) {
		super(message);
	}

	public BadNetworkingException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadNetworkingException(Throwable cause) {
		super(cause);
	}
}
