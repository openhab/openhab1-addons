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
 * The application will need to request access and refresh tokens once the user has 
 * authorized the application within the ecobee Web Portal.
 * 
 * @see TokenResponse
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/auth/pin-api-authorization.shtml">Requesting Tokens (Access & Refresh)</a>
 * @author John Cocula
 * @author Ecobee
 */
public class TokenRequest extends AbstractRequest {

	private static final String RESOURCE_URL = "https://api.ecobee.com/token";

	private String authToken;
	private String appKey;
	
	/**
	 * Construct a token request.
	 * 
	 * @param authToken
	 * 			the authorization token you were issued
	 * @param appKey
	 * 			the application key for your application (this binding)
	 */
	public TokenRequest(final String authToken,
						final String appKey ) {
		assert authToken != null : "authToken must not be null!";
		assert appKey != null : "appKey must not be null!";

		this.authToken = authToken;
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
			throw newException("Could not get auth token.", e, url, json);
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("authToken", this.authToken);
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
			urlBuilder.append("?grant_type=ecobeePin");
			urlBuilder.append("&code=");
			urlBuilder.append(authToken);
			urlBuilder.append("&client_id=");
			urlBuilder.append(appKey);
			return URIUtil.encodeQuery(urlBuilder.toString());
		} catch (final Exception e) {
			throw new EcobeeException(e);
		}
	}
}
