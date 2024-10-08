package com.sap.mobile.services.client.push;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;

public class DTOTest {
	@Test
	public void testDTOGcmNotificationTllFormat() throws Exception {
		Assert.assertNull(DTOGcmNotification.ttlFormat(null));
		Assert.assertEquals("3s", DTOGcmNotification.ttlFormat(Duration.ofSeconds(3)));
		Assert.assertEquals("3.000000001s",
				DTOGcmNotification.ttlFormat(Duration.ofSeconds(3).plus(Duration.ofNanos(1))));
		Assert.assertEquals("3.010000001s",
				DTOGcmNotification
						.ttlFormat(Duration.ofSeconds(3).plus(Duration.ofMillis(10)).plus(Duration.ofNanos(1))));
	}

	@Test
	public void testDTONotificationStatusResponse() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		DTOGetNotificationStatusResponse response = mapper.readValue(
				DTOTest.class.getResourceAsStream("/payloads/response-get-notification-status.json"),
				DTOGetNotificationStatusResponse.class);
		Assert.assertNotNull(response.getStatusDetails().getCaller());
	}

	@Test
	public void testDTOPushRegistrationsResponse() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		DTOPushRegistrationsResponse response = mapper.readValue(
				DTOTest.class.getResourceAsStream("/payloads/response-get-registrations-with-single-registration.json"),
				DTOPushRegistrationsResponse.class);
		Assert.assertNotNull(response.getValue().get(0).getUsername());
	}

	@Test
	public void testDTOApnsNotificationInterruptionLevel() throws Exception {
		ApnsNotification apnsNotification = ApnsNotification.builder().interruptionLevel(InterruptionLevel.CRITICAL)
				.build();
		DTOApnsNotification dtoApnsNotification = new DTOApnsNotification(apnsNotification);
		Assert.assertEquals("critical", dtoApnsNotification.getInterruptionLevel());
	}

	@Test
	public void testDTOApnsNotificationInterruptionLevelNull() throws Exception {
		ApnsNotification apnsNotification = ApnsNotification.builder().build();
		DTOApnsNotification dtoApnsNotification = new DTOApnsNotification(apnsNotification);
		Assert.assertNull(dtoApnsNotification.getInterruptionLevel());
	}

	@Test
	public void testDTOApnsNotificationRelevanceScoreWhenSetNotNull() {
		ApnsNotification apnsNotification = ApnsNotification.builder().relevanceScore(1.0F).build();
		DTOApnsNotification dtoApnsNotification = new DTOApnsNotification(apnsNotification);
		Assert.assertEquals(1.0F, dtoApnsNotification.getRelevanceScore().floatValue(), 0.01F);
	}

	@Test
	public void testDTOApnsNotificationRelevanceScoreWhenNotSetExpectNull() {
		ApnsNotification apnsNotification = ApnsNotification.builder().build();
		DTOApnsNotification dtoApnsNotification = new DTOApnsNotification(apnsNotification);
		Assert.assertNull(dtoApnsNotification.getRelevanceScore());
	}

	@Test
	public void testDTOApnsNotificationTargetContentIdWhenSetTargetContentIdIsNotNull() {
		ApnsNotification apnsNotification = ApnsNotification.builder().targetContentId("some value").build();
		DTOApnsNotification dtoApnsNotification = new DTOApnsNotification(apnsNotification);
		Assert.assertEquals("some value", dtoApnsNotification.getTargetContentId());
	}

	@Test
	public void testDTOApnsNotificationTargetContentIdWhenNotSetExpectNull() {
		ApnsNotification apnsNotification = ApnsNotification.builder().build();
		DTOApnsNotification dtoApnsNotification = new DTOApnsNotification(apnsNotification);
		Assert.assertNull(dtoApnsNotification.getTargetContentId());
	}
}
