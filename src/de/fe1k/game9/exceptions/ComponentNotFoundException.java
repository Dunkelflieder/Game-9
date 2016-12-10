package de.fe1k.game9.exceptions;

public class ComponentNotFoundException extends RuntimeException {

	public ComponentNotFoundException() {
		super();
	}

	public ComponentNotFoundException(String message) {
		super(message);
	}

	public ComponentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComponentNotFoundException(Throwable cause) {
		super(cause);
	}
}
