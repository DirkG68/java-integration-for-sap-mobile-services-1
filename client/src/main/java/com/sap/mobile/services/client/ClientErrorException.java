package com.sap.mobile.services.client;

/**
 * An 4xx Error was reported.
 */
public class ClientErrorException extends ClientException {
	ClientErrorException(String msg, String responseBodyText, HttpHeaders responseHeaders) {
		super(msg, responseBodyText, responseHeaders);
	}
}
