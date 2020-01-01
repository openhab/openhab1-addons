/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.netatmo.internal.camera;

import static org.openhab.io.net.http.HttpUtil.executeUrl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.netatmo.internal.messages.AbstractRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The method GetHomeData returns information about user's homes and camera's.
 * 
 * @author Ing. Peter Weiss
 * @since 1.8.0
 * @see <a href="https://api.netatmo.com/api/gethomedata">gethomedata</a>
 */
public class GetHomeDataRequest extends AbstractRequest {

	private static final String URL = API_BASE_URL + "gethomedata";

	private static final String CONTENT = "access_token=%s";

	private static final Logger logger = LoggerFactory.getLogger(GetHomeDataRequest.class);

	private final String accessToken;

	/**
	 * The method GetHomeData returns information about user's homes and camera's.
	 * 
	 * @param accessToken
	 *            mandatory, must not be <code>null</code>
	 */
	public GetHomeDataRequest(final String accessToken) {
		assert accessToken != null : "accessToken must not be null!";

		this.accessToken = accessToken;
	}

	@Override
	public GetHomeDataResponse execute() {
		final String content = String.format(CONTENT, this.accessToken);

		String json = null;
		try {
			json = executeQuery(content);

			return JSON.readValue(json, GetHomeDataResponse.class);
		} catch (final Exception e) {
			throw newException("Could not execute get home data request!", e, URL, content, json);
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
