package org.openhab.binding.stiebelheatpump.internal;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.stiebelheatpump.protocol.Request;

public class ConfigLocatorTest {

	public ConfigLocatorTest() {
	}

	@Test
	public void LoadResourceFiles() throws StiebelHeatPumpException {
		ConfigLocator configLocator = new ConfigLocator("2.06.xml");
		
		List<Request> configuration = configLocator.getConfig();

		Request firstRequest = configuration.get(0);
		Assert.assertEquals("Version",firstRequest.getName());
		Assert.assertEquals((byte) 0xfd, firstRequest.getRequestByte());
		
	}
}
