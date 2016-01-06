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
import static junit.framework.Assert.assertTrue;
import static org.openhab.binding.netatmo.internal.messages. GetStationsDataRequestStub.ACCESS_TOKEN;
import static org.openhab.binding.netatmo.internal.messages. GetStationsDataRequestStub.createRequest;

import java.util.List;

import org.junit.Test;
import org.openhab.binding.netatmo.internal.messages. GetStationsDataResponse.Device;
import org.openhab.binding.netatmo.internal.messages. GetStationsDataResponse.Module;

/**
 * @author Rob Nielsen
 * @since 1.8.0
 */
public class GetStationsDataTest {

	@Test
	public void testError() throws Exception {
		final  GetStationsDataRequestStub request = createRequest("/error-2.json");
		final  GetStationsDataResponse response = request.execute();

		assertTrue(response.isError());
	}

	@Test
	public void testSuccess() throws Exception {
		final  GetStationsDataRequestStub request = createRequest("/getstationsdata.json");
		final  GetStationsDataResponse response = request.execute();

		assertFalse(response.isError());
		assertEquals("access_token=" + ACCESS_TOKEN, request.getContent());
		assertEquals("ok", response.getStatus());

		final List<Device> devices = response.getDevices();
		assertEquals(1, devices.size());
		
		final Device device = devices.get(0);
		
		final List<Module> modules = device.getModules();
		assertEquals(4, modules.size());
	}

}
