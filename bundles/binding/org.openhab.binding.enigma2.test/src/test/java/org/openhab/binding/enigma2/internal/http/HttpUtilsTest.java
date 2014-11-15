package org.openhab.binding.enigma2.internal.http;

import java.io.IOException;

import org.junit.Test;

/**
 * Test class for HttpUtils
 * 
 * @author Sebastian Kutschbach
 * @since 1.6.0
 */
public class HttpUtilsTest {

	private static final String DEFAULT_HOSTNAME = "localhost";
	private static final String DEFAULT_SUFFIX = "/web/about";
	private static final String DEFAULT_USER = "user";
	private static final String DEFAULT_PASSWORD = "password";

	@Test
	public void testGetGetResponse() throws IOException {
		HttpUtils.getGetResponse(DEFAULT_HOSTNAME, DEFAULT_SUFFIX,
				DEFAULT_USER, DEFAULT_PASSWORD);
	}

	@Test(expected = IOException.class)
	public void testGetGetResponseInvalidHost() throws IOException {
		HttpUtils.getGetResponse("invalidHostName", DEFAULT_SUFFIX,
				DEFAULT_USER, DEFAULT_PASSWORD);
	}
}
