package org.openhab.binding.netatmo.internal.messages;

import java.io.IOException;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * @author Andreas Brenk
 * @since 1.4.0
 */
public class DeviceListRequestStub extends DeviceListRequest {

	protected static final String ACCESS_TOKEN = "test-access-token";

	public static DeviceListRequestStub createRequest(final String resource)
			throws Exception {
		return new DeviceListRequestStub(resource);
	}

	private final String response;

	private String query;

	private DeviceListRequestStub(final String response) throws Exception {
		super(ACCESS_TOKEN);

		final URL resource = getClass().getResource(response);

		if (resource == null) {
			throw new IOException("Resource '" + response + "' not found!");
		}

		this.response = Resources.toString(resource, Charsets.UTF_8);
	}

	public String getQuery() {
		return this.query;
	}

	@Override
	protected String executeQuery(final String query) {
		this.query = query;

		return this.response;
	}

}