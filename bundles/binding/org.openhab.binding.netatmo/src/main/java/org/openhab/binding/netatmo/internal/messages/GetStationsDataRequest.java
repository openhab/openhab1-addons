/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

import static org.openhab.io.net.http.HttpUtil.executeUrl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A getstationsdata request returns information about user's weather
 * stations such as last measures, state, etc.
 * 
 * @author Rob Nielsen
 * @since 1.8.0
 * @see <a href="https://api.netatmo.com/api/getstationsdata">getstationsdata</a>
 */
public class GetStationsDataRequest extends AbstractRequest {

	private static final String URL = API_BASE_URL + "getstationsdata";

	private static final String CONTENT = "access_token=%s";

	private static final Logger logger = LoggerFactory.getLogger(GetStationsDataRequest.class);

	private final String accessToken;

	/**
	 * Creates a request for information about user's weather stations
	 * such as last measures, state, etc.
	 * 
	 * @param accessToken
	 *            mandatory, must not be <code>null</code>
	 */
	public GetStationsDataRequest(final String accessToken) {
		assert accessToken != null : "accessToken must not be null!";

		this.accessToken = accessToken;
	}

	@Override
	public GetStationsDataResponse execute() {
		final String content = String.format(CONTENT, this.accessToken);

		String json = null;
		try {
			json = executeQuery(content);

			return JSON.readValue(json, GetStationsDataResponse.class);
		} catch (final Exception e) {
			throw newException("Could not execute get stations data request!", e, URL, content, json);
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("accessToken", this.accessToken);

		return builder.toString();
	}

	protected String executeQuery(final String content) throws Exception {
		final InputStream stream = new ByteArrayInputStream(
				content.getBytes(CHARSET));

		logger.debug("HTTP Post url='{}' content='{}'", URL, content);

		return executeUrl(HTTP_POST, URL, HTTP_HEADERS, stream,
				HTTP_CONTENT_TYPE, HTTP_REQUEST_TIMEOUT);
	}
}
