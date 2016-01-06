/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enphaseenergy.internal.messages;

import static org.apache.commons.httpclient.util.URIUtil.encodeQuery;
import static org.openhab.io.net.http.HttpUtil.executeUrl;

import java.util.Properties;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import org.openhab.binding.enphaseenergy.internal.EnphaseenergyException;


/**
 * A devicelist request returns the list of systems controlled by the user, and their
 * modules.
 * 
 * @author Markus Fritze
 * @since 1.7.0
 * @see <a href="https://developer.enphase.com/docs#api-systems">systems</a>
 */
public class SystemsRequest extends AbstractMessage {

	protected static final String HTTP_GET = "GET";

	protected static final String API_BASE_URL = "https://api.enphaseenergy.com/api/v2/";

	protected static final String RESOURCE_URL = API_BASE_URL + "systems";

	protected static final Properties HTTP_HEADERS;

	protected static final int HTTP_REQUEST_TIMEOUT = 10000;

	protected static final ObjectMapper JSON = new ObjectMapper();

	static {
		HTTP_HEADERS = new Properties();
		HTTP_HEADERS.put("Accept", "application/json");
	}

	protected final RuntimeException newException(
			final String message, final Exception cause,
			final String url, final String json) {
		if(cause instanceof JsonMappingException) {
			return new EnphaseenergyException("Could not parse JSON from URL '" + url + "': " + json, cause);
		}

		return new EnphaseenergyException(message, cause);
	}

	private final String key;
	private final String user_id;
	private final Integer system_id;

	/**
	 * Creates a request for the list of devices owned by the user
	 * 
	 * @param key
	 *            mandatory, must not be <code>null</code>
	 * @param user_id
	 *            mandatory, must not be <code>null</code>
	 */
	public SystemsRequest(final String key, final String user_id, final Integer system_id) {
		assert key != null : "key must not be null!";
		assert user_id != null : "user_id must not be null!";
		assert system_id != null : "system_id must not be null!";

		this.key = key;
		this.user_id = user_id;
		this.system_id = system_id;
	}

	public SystemsResponse execute() {
		final String url = prepare();
		String json = null;

		try {
			json = executeQuery(url);

			final SystemsResponse response = JSON.readValue(json, SystemsResponse.class);
			return response;
		} catch (final Exception e) {
			throw newException("Could not execute system summary request!", e, url, json);
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("key", this.key);
		builder.append("user_id", this.user_id);
		builder.append("system_id", this.system_id);
		return builder.toString();
	}

	protected String executeQuery(final String url) {
		return executeUrl(HTTP_GET, url, HTTP_HEADERS, null, null, HTTP_REQUEST_TIMEOUT);
	}

	public String prepare() {
		final StringBuilder urlBuilder = new StringBuilder(RESOURCE_URL);
		urlBuilder.append("/");
		urlBuilder.append(this.system_id);
		urlBuilder.append("/summary");
		urlBuilder.append("?key=");
		urlBuilder.append(this.key);
		urlBuilder.append("&user_id=");
		urlBuilder.append(this.user_id);

		try {
			return encodeQuery(urlBuilder.toString());
		} catch (final URIException e) {
			throw new EnphaseenergyException("Could not prepare systems request!", e);
		}
	}
}
