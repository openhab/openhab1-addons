/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal.messages;

//import static org.openhab.io.net.http.HttpUtil.executeUrl;

import java.io.IOException;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.openhab.binding.nest.internal.NestException;

/**
 * Updates the data model in the Nest API.
 * 
 * @author John Cocula
 * @since 1.7.0
 */
public class UpdateDataModelRequest extends AbstractRequest {

	private static final String RESOURCE_URL = API_BASE_URL;

	@JsonIgnore
	private final String accessToken;

	private final DataModel dataModel;

	/**
	 * Creates a request for the measurements of a device or module.
	 * 
	 * If you don't specify a moduleId you will retrieve the device's measurements. If you do specify a moduleId you
	 * will retrieve the module's measurements.
	 * 
	 * @param accessToken
	 *            the access token that permits this API call
	 * @param selection
	 *            which thermostats will be updated
	 * @param functions
	 *            optional, a list of functions to send
	 * @param thermostat
	 *            optional, a thermostat that has writable properties specified
	 */
	public UpdateDataModelRequest(final String accessToken, final DataModel dataModel) {
		assert accessToken != null : "accessToken must not be null!";

		this.accessToken = accessToken;
		this.dataModel = dataModel;
	}

	@Override
	public DataModelResponse execute() {
		final String url = buildQueryString();
		String json = null;

		try {
			json = executeQuery(url);

			final DataModelResponse response = JSON.readValue(json, DataModelResponse.class);

			return response;
		} catch (final Exception e) {
			throw newException("Could not update data model.", e, url, json);
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("accessToken", this.accessToken);
		if (this.dataModel != null) {
			builder.append("dataModel", this.dataModel);
		}

		return builder.toString();
	}

	protected String executeQuery(final String url) throws JsonGenerationException, JsonMappingException, IOException {
		return executeUrl(HTTP_PUT, url, JSON.writeValueAsString(this.dataModel), "application/json");
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
