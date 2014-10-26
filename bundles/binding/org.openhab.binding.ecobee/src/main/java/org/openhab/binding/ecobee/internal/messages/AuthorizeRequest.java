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
 * The ecobee PIN authorization method is designed to support any 3rd party device, 
 * be it a mobile phone, tablet, desktop widget or remote server. This authorization 
 * method allows a 3rd party application to obtain an authorization code and a 4 byte 
 * alphabetic string which can be displayed to the user. The user then logs into the 
 * ecobee Portal and registers the application using the PIN provided. Once this step 
 * is completed, the 3rd party application is able to request the access and 
 * refresh tokens.
 * 
 * @see AuthorizeResponse
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/auth/pin-api-authorization.shtml">PIN Authorization Strategy</a>
 * @author John Cocula
 * @author Ecobee
 */
public class AuthorizeRequest extends AbstractRequest {

	private static final String RESOURCE_URL = "https://api.ecobee.com/authorize";

	private String appKey;
	private String scope;
	
	/**
	 * Construct an authorization request.
	 * 
	 * @param appKey
	 * 			the application key for your application (this binding)
	 * @param scope
	 * 			the scope the application requests from the user
	 */
	public AuthorizeRequest( final String appKey,
							 final String scope ) {
		assert appKey != null : "appKey must not be null!";
		assert scope != null : "scope must not be null!";

		this.appKey = appKey;
		this.scope = scope;
	}

	@Override
	public AuthorizeResponse execute() {
		final String url = buildQueryString();
		String json = null;

		try {
			json = executeQuery(url);

			final AuthorizeResponse response = JSON.readValue(json,
					AuthorizeResponse.class);

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
		builder.append("scope", this.scope);
		return builder.toString();
	}

	protected String executeQuery(final String url) {
		return executeUrl(HTTP_GET, url, HTTP_HEADERS, null, null,
				HTTP_REQUEST_TIMEOUT);
	}

	private String buildQueryString() {
		final StringBuilder urlBuilder = new StringBuilder(RESOURCE_URL);

		try {
			urlBuilder.append("?response_type=ecobeePin");
			urlBuilder.append("&client_id=");
			urlBuilder.append(appKey);
			urlBuilder.append("&scope=");
			urlBuilder.append(scope);
			return URIUtil.encodeQuery(urlBuilder.toString());
		} catch (final Exception e) {
			throw new EcobeeException(e);
		}
	}
}
