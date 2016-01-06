/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal.messages;

import java.util.Date;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.openhab.binding.nest.internal.NestException;

/**
 * Queries the Nest API for the entire data model.
 * 
 * @author John Cocula
 * @since 1.7.0
 */
public class DataModelRequest extends AbstractRequest {

	private static final String RESOURCE_URL = API_BASE_URL;

	@JsonIgnore
	private final String accessToken;

	/**
	 * Creates a request for the measurements of a device or module.
	 * 
	 * If you don't specify a moduleId you will retrieve the device's measurements. If you do specify a moduleId you
	 * will retrieve the module's measurements.
	 * 
	 * @param accessToken
	 * @param selection
	 * @param page
	 *            optional, may be <code>null</code>
	 */
	public DataModelRequest(final String accessToken) {
		assert accessToken != null : "accessToken must not be null!";

		this.accessToken = accessToken;
	}

	@Override
	public DataModelResponse execute() {
		final String url = buildQueryString();
		String json = null;

		try {
			json = executeQuery(url);

			final DataModelResponse response = JSON.readValue(json, DataModelResponse.class);

			// note the time we received the data model
			response.setLast_connection(new Date());

			// sync the returned data model so its internal pointers are pointing
			response.sync();

			return response;
		} catch (final Exception e) {
			throw newException("Could not get data model.", e, url, json);
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
		return executeUrl(HTTP_GET, url, null, null);
	}

	private String buildQueryString() {
		final StringBuilder urlBuilder = new StringBuilder(RESOURCE_URL);

		try {
			urlBuilder.append("?auth=");
			urlBuilder.append(this.accessToken);
			return URIUtil.encodeQuery(urlBuilder.toString());
		} catch (final Exception e) {
			throw new NestException(e);
		}
	}
}
