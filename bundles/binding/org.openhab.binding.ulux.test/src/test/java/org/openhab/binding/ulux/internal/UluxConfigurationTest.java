package org.openhab.binding.ulux.internal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Hashtable;

import org.junit.Test;

public class UluxConfigurationTest {

	@Test
	public void testDefaults() throws Exception {
		final Hashtable<String, String> properties = new Hashtable<String, String>();

		final UluxConfiguration configuration = new UluxConfiguration();
		configuration.updated(properties);

		assertThat(configuration.getBindAddress(), equalTo(new InetSocketAddress("0.0.0.0", UluxBinding.PORT)));
		assertThat(configuration.getProjectId(), equalTo((short) 1));
		assertThat(configuration.getDesignId(), equalTo((short) 1));
	}

	@Test
	public void testConfiguration() throws Exception {
		final Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("bind_address", "192.168.1.1");
		properties.put("projectId", "5");
		properties.put("designId", "8");
		properties.put("switch.1", "192.168.1.101");

		final UluxConfiguration configuration = new UluxConfiguration();
		configuration.updated(properties);

		assertThat(configuration.getBindAddress(), equalTo(new InetSocketAddress("192.168.1.1", UluxBinding.PORT)));
		assertThat(configuration.getProjectId(), equalTo((short) 5));
		assertThat(configuration.getDesignId(), equalTo((short) 8));
		assertThat(configuration.getSwitchAddress((short) 1), equalTo(InetAddress.getByName("192.168.1.101")));
		assertThat(configuration.getSwitchId(InetAddress.getByName("192.168.1.101")), equalTo((short) 1));
	}

}
