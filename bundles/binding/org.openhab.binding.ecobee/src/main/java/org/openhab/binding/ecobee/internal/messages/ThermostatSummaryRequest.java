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
 * This request retrieves a list of thermostat configuration and state revisions. 
 * This request is a light-weight polling method which will only return the revision 
 * numbers for the significant portions of the thermostat data. It is the 
 * responsibility of the caller to store these revisions for future determination 
 * whether changes occurred at the next poll interval.
 * 
 * <p>
 * The intent is to permit the caller to determine whether a thermostat has changed 
 * since the last poll. Retrieval of a whole thermostat including runtime data is 
 * expensive and impractical for large amounts of thermostat such as a management set 
 * hierarchy, especially if nothing has changed. By storing the retrieved revisions, 
 * the caller may determine whether to get a thermostat and which sections of the 
 * thermostat should be retrieved.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/operations/get-thermostat-summary.shtml">GET Thermostat Summary (Polling)</a>
 * @author John Cocula
 * @author Ecobee
 */
public class ThermostatSummaryRequest extends AbstractRequest {

	private static final String RESOURCE_URL = "https://api.ecobee.com/1/thermostatSummary";

	@JsonIgnore
	private final String accessToken;

	@JsonProperty("selection")
	private final Selection selection;

	/**
	 * Creates a request for a summary of thermostats as specified in the <code>selection</code>.
	 * 
	 * @param accessToken the access token that permits this query
	 * @param selection a {@link Selection} object that specifies 
	 * which thermostats to return in the response.
	 */
	public ThermostatSummaryRequest(final String accessToken, final Selection selection) {
		assert accessToken != null : "accessToken must not be null!";
		assert selection != null : "selection must not be null!";

		this.accessToken = accessToken;
		this.selection = selection;
	}

	@Override
	public ThermostatSummaryResponse execute() {
		final String url = buildQueryString();
		String json = null;

		try {
			json = executeQuery(url);

			final ThermostatSummaryResponse response = JSON.readValue(json,
					ThermostatSummaryResponse.class);

			return response;
		} catch (final Exception e) {
			throw newException("Could not get thermostat summary.", e, url, json);
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("accessToken", this.accessToken);
		builder.append("selection", this.selection);

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
