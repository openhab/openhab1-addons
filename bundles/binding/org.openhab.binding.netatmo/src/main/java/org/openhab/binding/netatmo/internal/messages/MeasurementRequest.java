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

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.netatmo.internal.NetatmoException;

/**
 * Queries the Netatmo API for the measures of a single device or module.
 * 
 * @author Andreas Brenk
 * @since 1.4.0
 * @see <a href="http://dev.netatmo.com/doc/restapi/getmeasure">getmeasure</a>
 */
public class MeasurementRequest extends AbstractRequest {

	private static final String RESOURCE_URL = "http://api.netatmo.net/api/getmeasure";

	/**
	 * @param deviceId
	 *            mandatory, must not be <code>null</code>
	 * @param moduleId
	 *            optional, may be <code>null</code>
	 * @return a unique key suitable to store a request in a map
	 */
	public static String createKey(final String deviceId, final String moduleId) {
		if (moduleId == null) {
			return "device:" + deviceId;

		} else {
			return "module:" + moduleId;
		}
	}

	private final String accessToken;

	private final String deviceId;

	private final String moduleId;

	private final SortedSet<String> measures = new TreeSet<String>();

	/**
	 * Creates a request for the measurements of a device or module.
	 * 
	 * If you don't specify a moduleId you will retrieve the device's
	 * measurements. If you do specify a moduleId you will retrieve the module's
	 * measurements.
	 * 
	 * @param accessToken
	 * @param deviceId
	 * @param moduleId
	 *            optional, may be <code>null</code>
	 */
	public MeasurementRequest(final String accessToken, final String deviceId,
			final String moduleId) {
		assert accessToken != null : "accessToken must not be null!";
		assert deviceId != null : "deviceId must not be null!";

		this.accessToken = accessToken;
		this.deviceId = deviceId;
		this.moduleId = moduleId;
	}

	/**
	 * @param measure
	 *            the name of a supported measure, e.g. "Temperature" or
	 *            "Humidity"
	 */
	public void addMeasure(final String measure) {
		this.measures.add(measure);
	}

	@Override
	public MeasurementResponse execute() {
		try {
			final String url = buildQueryString();
			final String json = executeQuery(url);

			final MeasurementResponse response = JSON.readValue(json,
					MeasurementResponse.class);

			return response;
		} catch (final Exception e) {
			throw new NetatmoException("Could not get measurements!", e);
		}
	}

	/**
	 * @see #createKey(String, String)
	 */
	public String getKey() {
		return createKey(this.deviceId, this.moduleId);
	}

	public SortedSet<String> getMeasures() {
		return this.measures;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("accessToken", this.accessToken);
		builder.append("deviceId", this.deviceId);
		if (this.moduleId != null) {
			builder.append("moduleId", this.moduleId);
		}
		builder.append("measures", this.measures);

		return builder.toString();
	}

	protected String executeQuery(final String url) {
		return executeUrl(HTTP_GET, url, HTTP_HEADERS, null, null,
				HTTP_REQUEST_TIMEOUT);
	}

	private String buildQueryString() {
		final StringBuilder urlBuilder = new StringBuilder(RESOURCE_URL);
		urlBuilder.append("?access_token=");
		urlBuilder.append(this.accessToken);
		urlBuilder.append("&scale=max");
		urlBuilder.append("&date_end=last");
		urlBuilder.append("&device_id=");
		urlBuilder.append(this.deviceId);
		if (this.moduleId != null) {
			urlBuilder.append("&module_id=");
			urlBuilder.append(this.moduleId);
		}
		urlBuilder.append("&type=");
		for (final Iterator<String> i = this.measures.iterator(); i.hasNext();) {
			urlBuilder.append(i.next());
			if (i.hasNext()) {
				urlBuilder.append(",");
			}
		}

		try {
			return URIUtil.encodeQuery(urlBuilder.toString());
		} catch (final URIException e) {
			throw new NetatmoException(e);
		}
	}
}
