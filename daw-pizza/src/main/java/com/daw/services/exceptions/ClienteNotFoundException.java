package com.daw.services.exceptions;

public class ClienteNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7239101262919209494L;

	public ClienteNotFoundException(String message) {
		super(message);
	}

}
