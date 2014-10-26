/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

import static org.openhab.io.net.http.HttpUtil.executeUrl;

import java.util.Properties;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.openhab.binding.ecobee.internal.EcobeeException;

/**
 * Queries the Ecobee API for thermostats.
 * 
 * @author John Cocula
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/operations/get-thermostats.shtml">GET Thermostats</a>
 */
public class ThermostatRequest extends AbstractRequest {

	private static final String RESOURCE_URL = "https://api.ecobee.com/1/thermostat";

	@JsonIgnore
	private final String accessToken;

	@JsonProperty("selection")
	private final Selection selection;

	@JsonProperty("page")
	private final Page page;

	/**
	 * Creates a request for the measurements of a device or module.
	 * 
	 * If you don't specify a moduleId you will retrieve the device's
	 * measurements. If you do specify a moduleId you will retrieve the module's
	 * measurements.
	 * 
	 * @param accessToken
	 * @param selection
	 * @param page
	 *            optional, may be <code>null</code>
	 */
	public ThermostatRequest(final String accessToken, final Selection selection,
			final Page page) {
		assert accessToken != null : "accessToken must not be null!";
		assert selection != null : "selection must not be null!";

		this.accessToken = accessToken;
		this.selection = selection;
		this.page = page;
	}

	@Override
	public ThermostatResponse execute() {
		final String url = buildQueryString();
		String json = null;

		try {
			json = executeQuery(url);

			final ThermostatResponse response = JSON.readValue(json,
					ThermostatResponse.class);

			return response;
		} catch (final Exception e) {
			throw newException("Could not get thermostats.", e, url, json);
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("accessToken", this.accessToken);
		builder.append("selection", this.selection);
		if (this.page != null) {
			builder.append("page", this.page);
		}

		return builder.toString();
	}

	protected String executeQuery(final String url) {
		Properties headers = new Properties();
		headers.putAll(HTTP_HEADERS);
		headers.put("Authorization", "Bearer " + this.accessToken);
		return executeUrl(HTTP_GET, url, headers, null, null,
				HTTP_REQUEST_TIMEOUT);
	}

	private String buildQueryString() {
		final StringBuilder urlBuilder = new StringBuilder(RESOURCE_URL);

		try {
			urlBuilder.append("?json=");
			urlBuilder.append(JSON.writeValueAsString(this));
			return URIUtil.encodeQuery(urlBuilder.toString());
		} catch (final Exception e) {
			throw new EcobeeException(e);
		}
	}
}
