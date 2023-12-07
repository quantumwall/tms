package org.quantum.exception;

public class InsufficientRightsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsufficientRightsException(String message) {
		super(message);
	}

}
