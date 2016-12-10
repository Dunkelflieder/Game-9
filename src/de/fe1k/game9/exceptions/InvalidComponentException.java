package de.fe1k.game9.exceptions;

public class InvalidComponentException extends RuntimeException {

	public InvalidComponentException() {
		super();
	}

	public InvalidComponentException(String message) {
		super(message);
	}

	public InvalidComponentException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidComponentException(Throwable cause) {
		super(cause);
	}
}
