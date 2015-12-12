/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.openhab.binding.netatmo.internal.NetatmoMeasureType.HUMIDITY;
import static org.openhab.binding.netatmo.internal.NetatmoMeasureType.TEMPERATURE;
import static org.openhab.binding.netatmo.internal.messages.MeasurementRequestStub.ACCESS_TOKEN;
import static org.openhab.binding.netatmo.internal.messages.MeasurementRequestStub.DEVICE_ID;
import static org.openhab.binding.netatmo.internal.messages.MeasurementRequestStub.MODULE_ID;
import static org.openhab.binding.netatmo.internal.messages.MeasurementRequestStub.createRequest;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.openhab.binding.netatmo.internal.messages.MeasurementResponse.Body;

/**
 * @author Andreas Brenk
 * @author Rob Nielsen
 * @since 1.4.0
 */
public class MeasurementTest {

	@Test
	public void testError() throws Exception {
		final MeasurementRequestStub request = createRequest("/error-2.json");
		final MeasurementResponse response = request.execute();

		assertTrue(response.isError());

		final NetatmoError error = response.getError();

		assertNotNull(error);
		assertEquals(2, error.getCode());
		assertEquals("Invalid access token", error.getMessage());
	}

	@Test
	public void testSuccess() throws Exception {
		final MeasurementRequestStub request = createRequest("/getmeasure.json");
		request.addMeasure(TEMPERATURE);
		request.addMeasure(HUMIDITY);

		final MeasurementResponse response = request.execute();

		assertFalse(response.isError());
		assertNull(response.getError());

		assertEquals("access_token=" + ACCESS_TOKEN
				+ "&scale=max&date_end=last&device_id=" + DEVICE_ID
				+ "&module_id=" + MODULE_ID + "&type=Humidity,Temperature",
				request.getContent());

		final List<Body> bodyList = response.getBody();

		assertNotNull(bodyList);
		assertEquals(1, bodyList.size());

		final Body body = bodyList.get(0);
		final List<List<BigDecimal>> valuesList = body.getValues();

		assertNotNull(valuesList);
		assertEquals(1, valuesList.size());

		final List<BigDecimal> values = valuesList.get(0);

		assertNotNull(values);
		assertEquals(2, values.size());
		assertEquals(new BigDecimal("77"), values.get(0));
		assertEquals(new BigDecimal("19.3"), values.get(1));

	}

}
