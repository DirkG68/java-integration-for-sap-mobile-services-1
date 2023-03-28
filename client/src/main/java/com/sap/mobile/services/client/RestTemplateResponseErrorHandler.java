package com.sap.mobile.services.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import lombok.AccessLevel;
import lombok.Getter;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	@Getter(AccessLevel.PROTECTED)
	private String responseBodyText;

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		try {
		responseBodyText = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			//noop
		}
		return response.getStatusCode().isError();
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		final HttpStatus status = response.getStatusCode();
		this.handleServiceSpecificErrors(response);

		switch (status) {
			case UNAUTHORIZED:
				throw new ClientUnauthorizedException(responseBodyText, response.getHeaders());
			case TOO_MANY_REQUESTS:
				throw new TrialLimitExceededException(responseBodyText, response.getHeaders());
			default:
		}
		// Generic error handlers for undefined 4** and 5**
		if (status.is4xxClientError()) {
			throw new ClientErrorException(status.name(), responseBodyText, response.getHeaders());
		}
		if (status.is5xxServerError()) {
			throw new ServerErrorException(status.name(), responseBodyText, response.getHeaders());
		}
	}

	protected void handleServiceSpecificErrors(ClientHttpResponse response) throws IOException {
	}
}
