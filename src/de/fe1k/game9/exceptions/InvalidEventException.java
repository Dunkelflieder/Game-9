package de.fe1k.game9.exceptions;

public class InvalidEventException extends RuntimeException {

	public InvalidEventException() {
		super();
	}

	public InvalidEventException(String message) {
		super(message);
	}

	public InvalidEventException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidEventException(Throwable cause) {
		super(cause);
	}
}
