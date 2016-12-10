package de.fe1k.game9.exceptions;

public class ComponentAlreadyExistsException extends RuntimeException {

	public ComponentAlreadyExistsException() {
		super();
	}

	public ComponentAlreadyExistsException(String message) {
		super(message);
	}

	public ComponentAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComponentAlreadyExistsException(Throwable cause) {
		super(cause);
	}
}
