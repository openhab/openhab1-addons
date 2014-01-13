/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
import org.openhab.binding.netatmo.internal.NetatmoException;

/**
 * Gets a renewed refresh token from the Netatmo API to use in future
 * measurement requests.
 * 
 * @author Andreas Brenk
 * @since 1.4.0
 * @see <a
 *      href="http://dev.netatmo.com/doc/authentication/refreshtoken">refreshtoken</a>
 */
public class RefreshTokenRequest extends AbstractRequest {

	private static final String CHARSET = "UTF-8";

	private static final String HTTP_CONTENT_TYPE = "application/x-www-form-urlencoded;charset="
			+ CHARSET;

	private static final String URL = "https://api.netatmo.net/oauth2/token";
	private static final String CONTENT = "grant_type=refresh_token&refresh_token=%s&client_id=%s&client_secret=%s";

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
		try {
			final String content = String.format(CONTENT, this.refreshToken,
					this.clientId, this.clientSecret);

			final String json = executeQuery(content);

			final RefreshTokenResponse response = JSON.readValue(json,
					RefreshTokenResponse.class);

			return response;
		} catch (final Exception e) {
			throw new NetatmoException("Could not refresh access token!", e);
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

		return executeUrl(HTTP_POST, URL, HTTP_HEADERS, stream,
				HTTP_CONTENT_TYPE, HTTP_REQUEST_TIMEOUT);
	}
}
