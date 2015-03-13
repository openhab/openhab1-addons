/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal.messages;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.nest.internal.NestException;

/**
 * 
 * @see AccessTokenResponse
 * @author John Cocula
 * @since 1.7.0
 */
public class AccessTokenRequest extends AbstractRequest {

	private static final String RESOURCE_URL = "https://api.home.nest.com/oauth2/access_token";

	private String clientId;
	private String clientSecret;
	private String pinCode;

	/**
	 * Construct a request to obtain an access code to the Nest API.
	 * 
	 * @param client_id
	 *            the <b>Client ID</b> shown in you Nest Clients screen and you pasted into openhab.cfg at
	 *            nest:client_id
	 * @param client_secret
	 *            the <b>Client Secret</b> shown in you Nest Clients screen and you pasted into openhab.cfg at
	 *            nest:client_secret
	 * @param pincode
	 *            the PIN that Nest showed on-screen and you pasted into openhab.cfg at nest:pincode
	 * 
	 * @see <a href="https://developer.nest.com/clients">Clients screen</a>
	 */
	public AccessTokenRequest(final String clientId, final String clientSecret, final String pinCode) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.pinCode = pinCode;
	}

	@Override
	public AccessTokenResponse execute() {
		final String url = buildQueryString();
		String json = null;

		try {
			json = executeQuery(url);

			final AccessTokenResponse response = JSON.readValue(json, AccessTokenResponse.class);

			return response;
		} catch (final Exception e) {
			throw newException("Could not get access token.", e, url, json);
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("pinCode", this.pinCode);
		builder.append("clientId", this.clientId);
		builder.append("clientSecret", this.clientSecret);
		return builder.toString();
	}

	protected String executeQuery(final String url) {
		return executeUrl(HTTP_POST, url, null, null);
	}

	private String buildQueryString() {
		final StringBuilder urlBuilder = new StringBuilder(RESOURCE_URL);

		try {
			urlBuilder.append("?code=");
			urlBuilder.append(this.pinCode);
			urlBuilder.append("&client_id=");
			urlBuilder.append(this.clientId);
			urlBuilder.append("&client_secret=");
			urlBuilder.append(this.clientSecret);
			urlBuilder.append("&grant_type=authorization_code");
			return URIUtil.encodeQuery(urlBuilder.toString());
		} catch (final Exception e) {
			throw new NestException(e);
		}
	}
}
