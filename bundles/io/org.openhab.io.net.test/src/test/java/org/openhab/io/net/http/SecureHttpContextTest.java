package org.openhab.io.net.http;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Test;
import org.osgi.service.cm.ConfigurationException;

public class SecureHttpContextTest {
	@Test
	public void ipv4() throws UnknownHostException, ConfigurationException {
		init("192.168.0.0/24");
		assertFalse(ctx.isExternalIp("127.0.0.1"));
		assertFalse(ctx.isExternalIp("192.168.0.100"));
		assertTrue(ctx.isExternalIp("1.1.1.1"));
	}

	@Test
	public void ipv6() throws UnknownHostException, ConfigurationException {
		init("fe80::/64");
		assertFalse(ctx.isExternalIp("::1"));
		assertFalse(ctx.isExternalIp("fe80:0000:0000:0000:0000:0000:0000:0001"));
		assertTrue(ctx.isExternalIp("fe80:0000:0000:0001:0000:0000:0000:0000"));
	}

	@Test
	public void dualstack() throws UnknownHostException, ConfigurationException {
		init("192.168.0.0/24,fe80::/64");
		assertFalse(ctx.isExternalIp("127.0.0.1"));
		assertFalse(ctx.isExternalIp("192.168.0.100"));
		assertTrue(ctx.isExternalIp("1.1.1.1"));
		assertFalse(ctx.isExternalIp("::1"));
		assertFalse(ctx.isExternalIp("fe80:0000:0000:0000:0000:0000:0000:0001"));
		assertTrue(ctx.isExternalIp("fe80:0000:0000:0001:0000:0000:0000:0000"));
	}

	@Test
	public void undef() throws UnknownHostException, ConfigurationException {
		init(null);
		assertFalse(ctx.isExternalIp("127.0.0.1"));
		assertFalse(ctx.isExternalIp("::1"));
		assertFalse(ctx.isExternalIp("192.168.1.111"));
		assertTrue(ctx.isExternalIp("192.168.100.111"));
		assertTrue(ctx.isExternalIp("fe80:0000:0000:0000:0000:0000:0000:0001"));
		assertTrue(ctx.isExternalIp("fe80:0000:0000:0001:0000:0000:0000:0000"));
	}

	private SecureHttpContext ctx;
	public void init(String netmask) throws ConfigurationException {
		ctx = new SecureHttpContext();
		Dictionary<String, String> cfg = new Hashtable<String, String>();
		if (netmask != null)
			cfg.put("netmask", netmask);
		ctx.updated(cfg);
	}
}