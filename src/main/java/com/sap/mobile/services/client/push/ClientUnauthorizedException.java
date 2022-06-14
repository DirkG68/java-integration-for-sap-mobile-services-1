package com.sap.mobile.services.client.push;

/**
 * Thrown when authorization failed. Please check and update the client configuration.
 */
public class ClientUnauthorizedException extends ClientException {

	ClientUnauthorizedException() {
		super("Authorization failed.");
	}
}
