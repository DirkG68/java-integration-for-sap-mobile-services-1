package com.sap.mobile.services.client.push;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.mobile.services.client.RestTemplateResponseErrorHandler;

class PushRestTemplateResponseErrorHandler extends RestTemplateResponseErrorHandler {
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void handleServiceSpecificErrors(ClientHttpResponse response) throws IOException {
		switch (response.getStatusCode()) {
			case NOT_FOUND:
				try {
					PushResponse pushResponse = mapper.readValue(getResponseBodyText(), DTOPushResponse.class);
					throw new NoMessageSentException(pushResponse.getStatus().getMessage(), getResponseBodyText(),
							response.getHeaders(), pushResponse);
				} catch (JsonProcessingException e) {
					// NOOP
				}
				throw new NoMessageSentException(getResponseBodyText(), response.getHeaders());
			case UNPROCESSABLE_ENTITY:
				try {
					PushResponse pushResponse = mapper.readValue(getResponseBodyText(), DTOPushResponse.class);
					throw new MessageErrorException(getResponseBodyText(), response.getHeaders(), pushResponse);
				} catch (JsonProcessingException e) {
					// NOOP
				}
				throw new MessageErrorException(getResponseBodyText(), response.getHeaders());
			default:
				try {
					PushResponse pushResponse = mapper.readValue(getResponseBodyText(), DTOPushResponse.class);
					throw new PushClientException(response.getStatusCode().name(), getResponseBodyText(),
							response.getHeaders(), pushResponse);
				} catch (JsonProcessingException e) {
					// NOOP
				}
		}
	}
}
