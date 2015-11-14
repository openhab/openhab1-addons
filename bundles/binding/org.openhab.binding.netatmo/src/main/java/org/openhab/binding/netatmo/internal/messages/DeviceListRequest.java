/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

import static org.apache.commons.httpclient.util.URIUtil.encodeQuery;
import static org.openhab.io.net.http.HttpUtil.executeUrl;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.netatmo.internal.NetatmoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A devicelist request returns the list of devices owned by the user, and their
 * modules.
 * 
 * @author Andreas Brenk
 * @author Rob Nielsen
 * @since 1.4.0
 * @see <a href="http://dev.netatmo.com/doc/restapi/devicelist">devicelist</a>
 */
public class DeviceListRequest extends AbstractRequest {

	private static final String RESOURCE_URL = API_BASE_URL + "devicelist";

	private static final Logger logger = LoggerFactory.getLogger(DeviceListRequest.class);

	private final String accessToken;

	/**
	 * Creates a request for the list of devices owned by the user, and their
	 * modules.
	 * 
	 * @param accessToken
	 *            mandatory, must not be <code>null</code>
	 */
	public DeviceListRequest(final String accessToken) {
		assert accessToken != null : "accessToken must not be null!";

		this.accessToken = accessToken;
	}

	@Override
	public DeviceListResponse execute() {
		final String url = prepare();

		logger.debug(url);

		String json = null;

		try {
			json = executeQuery(url);

			final DeviceListResponse response = JSON.readValue(json,
					DeviceListResponse.class);

			return response;
		} catch (final Exception e) {
			throw newException("Could not execute device list request!", e, url, json);
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("accessToken", this.accessToken);

		return builder.toString();
	}

	protected String executeQuery(final String url) {
		return executeUrl(HTTP_GET, url, HTTP_HEADERS, null, null,
				HTTP_REQUEST_TIMEOUT);
	}

	private String prepare() {
		final StringBuilder urlBuilder = new StringBuilder(RESOURCE_URL);
		urlBuilder.append("?access_token=");
		urlBuilder.append(this.accessToken);

		try {
			return encodeQuery(urlBuilder.toString());
		} catch (final URIException e) {
			throw new NetatmoException(
					"Could not prepare device list request!", e);
		}
	}
}
