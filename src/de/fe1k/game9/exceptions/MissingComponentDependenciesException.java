package de.fe1k.game9.exceptions;

public class MissingComponentDependenciesException extends RuntimeException {

	public MissingComponentDependenciesException() {
		super();
	}

	public MissingComponentDependenciesException(String message) {
		super(message);
	}

	public MissingComponentDependenciesException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingComponentDependenciesException(Throwable cause) {
		super(cause);
	}
}
