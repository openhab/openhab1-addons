/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mojio.messages;

import static org.openhab.io.net.http.HttpUtil.executeUrl;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.mojio.internal.MojioException;

/**
 * The AuthorizeRequest class implements authorization request using MojIO RESTful interface.
 * Takes application key, application secret and the username with the password.
 * So you should create an application in mojio web interface and use the generated key/id.
 *
 * @see AuthorizeResponse
 * @author Vladimir Pavluk
 * @since 1.0
 */
public class AuthorizeRequest extends AbstractRequest {

	private static final String RESOURCE_URL = API_BASE_URL + "v1/Login/";

	private String appKey;
	private String appSecret;
	private String username;
	private String password;

	/**
	 * Construct an authorization request.
	 * 
	 * @param appKey
	 *            the application key for your application (this binding)
	 */
	public AuthorizeRequest(final String appKey, final String appSecret, final String username, final String password) {
		assert appKey != null : "appKey must not be null!";
		assert appSecret != null : "appSecret must not be null!";
		assert username != null : "username must not be null!";
		assert password != null : "password must not be null!";

		this.appKey = appKey;
		this.appSecret = appSecret;
		this.username = username;
		this.password = password;
	}

	@Override
	public AuthorizeResponse execute() {
		final String url = buildQueryString();
		String json = null;

		try {
			json = executeQuery(url);

			final AuthorizeResponse response = JSON.readValue(json, AuthorizeResponse.class);

			return response;
		} catch (final Exception e) {
			throw newException("Could not get authorization.", e, url, json);
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("appKey", this.appKey);
		builder.append("appSecret", this.appSecret);
		builder.append("username", this.username);
		builder.append("password", this.password);
		return builder.toString();
	}

	protected String executeQuery(final String url) {
		return executeUrl(HTTP_POST, url, HTTP_HEADERS, null, null, HTTP_REQUEST_TIMEOUT);
	}

	private String buildQueryString() {
		final StringBuilder urlBuilder = new StringBuilder(RESOURCE_URL);

		try {
			urlBuilder.append(appKey);
			urlBuilder.append("?secretKey=");
			urlBuilder.append(appSecret);
      urlBuilder.append("&minutes=43829&userOrEmail=");
			urlBuilder.append(username);
			urlBuilder.append("&password=");
			urlBuilder.append(password);
			return URIUtil.encodeQuery(urlBuilder.toString());
		} catch (final Exception e) {
			throw new MojioException(e);
		}
	}
}
