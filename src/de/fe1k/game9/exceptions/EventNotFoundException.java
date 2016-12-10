package de.fe1k.game9.exceptions;

public class EventNotFoundException extends RuntimeException {

	public EventNotFoundException() {
		super();
	}

	public EventNotFoundException(String message) {
		super(message);
	}

	public EventNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventNotFoundException(Throwable cause) {
		super(cause);
	}
}
