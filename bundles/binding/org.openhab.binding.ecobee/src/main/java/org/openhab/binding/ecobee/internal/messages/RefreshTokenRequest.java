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

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ecobee.internal.EcobeeException;

/**
 * All access tokens must be refreshed periodically. 
 * Token refresh reduces the potential and benefit of token theft. 
 * Since all tokens expire, stolen tokens may only be used for a limited time. 
 * A token refresh immediately expires the previously issued access and 
 * refresh tokens and issues brand new tokens.
 * 
 * @see TokenResponse
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/auth/token-refresh.shtml">Refreshing Your Tokens</a>
 * @author John Cocula
 * @author Ecobee
 */
public class RefreshTokenRequest extends AbstractRequest {

	private static final String RESOURCE_URL = "https://api.ecobee.com/token";

	private String refreshToken;
	private String appKey;
	
	/**
	 * Construct a refresh token request.
	 * 
	 * @param refreshToken
	 * 			the refresh token you were issued
	 * @param appKey
	 * 			the application key for your application (this binding)
	 */
	public RefreshTokenRequest( final String refreshToken,
								final String appKey ) {
		assert refreshToken != null : "refreshToken must not be null!";
		assert appKey != null : "appKey must not be null!";

		this.refreshToken = refreshToken;
		this.appKey = appKey;
	}

	@Override
	public TokenResponse execute() {
		final String url = buildQueryString();
		String json = null;

		try {
			json = executeQuery(url);

			final TokenResponse response = JSON.readValue(json,
					TokenResponse.class);

			return response;
		} catch (final Exception e) {
			throw newException("Could not get refresh token.", e, url, json);
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("refreshToken", this.refreshToken);
		builder.append("appKey", this.appKey);
		return builder.toString();
	}

	protected String executeQuery(final String url) {
		return executeUrl(HTTP_POST, url, HTTP_HEADERS, null, null,
				HTTP_REQUEST_TIMEOUT);
	}

	private String buildQueryString() {
		final StringBuilder urlBuilder = new StringBuilder(RESOURCE_URL);

		try {
			urlBuilder.append("?grant_type=refresh_token");
			urlBuilder.append("&code=");
			urlBuilder.append(refreshToken);
			urlBuilder.append("&client_id=");
			urlBuilder.append(appKey);
			return URIUtil.encodeQuery(urlBuilder.toString());
		} catch (final Exception e) {
			throw new EcobeeException(e);
		}
	}
}
