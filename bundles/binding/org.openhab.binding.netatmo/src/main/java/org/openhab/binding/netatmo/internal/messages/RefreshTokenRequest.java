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
 * Gets a renewed refresh token from the Netatmo API to use in future
 * measurement requests.
 * 
 * @author Andreas Brenk
 * @author Rob Nielsen
 * @since 1.4.0
 * @see <a
 *      href="http://dev.netatmo.com/doc/authentication/refreshtoken">refreshtoken</a>
 */
public class RefreshTokenRequest extends AbstractRequest {
	private static final String URL = "https://api.netatmo.net/oauth2/token";

	private static final String CONTENT = "grant_type=refresh_token&refresh_token=%s&client_id=%s&client_secret=%s&scope=read_station";

	private static final Logger logger = LoggerFactory.getLogger(RefreshTokenRequest.class);

	private final String clientId;

	private final String clientSecret;

	private final String refreshToken;

	public RefreshTokenRequest(final String clientId,
			final String clientSecret, final String refreshToken) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.refreshToken = refreshToken;
	}

	@Override
	public RefreshTokenResponse execute() {
		final String content = String.format(CONTENT, this.refreshToken,
				this.clientId, this.clientSecret);

		String json = null;
		try {
			json = executeQuery(content);
			if (json == null) {
				return null;
			}

			final RefreshTokenResponse response = JSON.readValue(json,
					RefreshTokenResponse.class);

			return response;
		} catch (final Exception e) {
			throw newException("Could not refresh access token!", e, URL, content, json);
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("clientId", this.clientId);
		builder.append("clientSecret", this.clientSecret);
		builder.append("refreshToken", this.refreshToken);

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
