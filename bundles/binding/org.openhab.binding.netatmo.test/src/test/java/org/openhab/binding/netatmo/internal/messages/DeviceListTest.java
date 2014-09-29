package org.openhab.binding.netatmo.internal.messages;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.openhab.binding.netatmo.internal.messages.DeviceListRequestStub.ACCESS_TOKEN;
import static org.openhab.binding.netatmo.internal.messages.DeviceListRequestStub.createRequest;

import java.util.List;

import org.junit.Test;
import org.openhab.binding.netatmo.internal.messages.DeviceListResponse.Device;
import org.openhab.binding.netatmo.internal.messages.DeviceListResponse.Module;

/**
 * @author Andreas Brenk
 * @since 1.4.0
 */
public class DeviceListTest {

	@Test
	public void testError() throws Exception {
		final DeviceListRequestStub request = createRequest("/error-2.json");
		final DeviceListResponse response = request.execute();

		assertTrue(response.isError());
	}

	@Test
	public void testSuccess() throws Exception {
		final DeviceListRequestStub request = createRequest("/devicelist.json");
		final DeviceListResponse response = request.execute();

		assertFalse(response.isError());
		assertEquals("http://api.netatmo.net/api/devicelist?access_token="
				+ ACCESS_TOKEN, request.getQuery());
		assertEquals("ok", response.getStatus());

		final List<Device> devices = response.getDevices();
		final List<Module> modules = response.getModules();

		assertEquals(1, devices.size());
		assertEquals(1, modules.size());

		final Device device = devices.get(0);
		final Module module = modules.get(0);

		assertTrue(device.isPublicData());
		assertTrue(module.isPublicData());
	}

}
