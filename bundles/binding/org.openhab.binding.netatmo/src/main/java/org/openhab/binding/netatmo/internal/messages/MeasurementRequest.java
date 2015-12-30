/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.netatmo.internal.NetatmoMeasureType;
import org.openhab.binding.netatmo.internal.NetatmoScale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Queries the Netatmo API for the measures of a single device or module.
 * 
 * @author Andreas Brenk
 * @author Gaël L'hopital
 * @author Rob Nielsen
 * @since 1.4.0
 * @see <a href="http://dev.netatmo.com/doc/restapi/getmeasure">getmeasure</a>
 */
public class MeasurementRequest extends AbstractRequest {

	private static final String URL = API_BASE_URL + "getmeasure";

	private static final Logger logger = LoggerFactory.getLogger(MeasurementRequest.class);

	/**
	 * @param deviceId
	 *            mandatory, must not be <code>null</code>
	 * @param moduleId
	 *            optional, may be <code>null</code>
	 * @return a unique key suitable to store a request in a map
	 */
	public static String createKey(final String deviceId, final String moduleId, final NetatmoScale scale) {
		final String s =  ":" + scale.getScale();
		if (moduleId == null) {
			return "device:" + deviceId + s;

		} else {
			return "module:" + moduleId + s;
		}
	}

	private final String accessToken;

	private final String deviceId;

	private final String moduleId;

	private final NetatmoScale scale;

	private final SortedSet<String> measures = new TreeSet<String>();

	/**
	 * Creates a request for the measurements of a device or module
	 * using the default scale.
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
		this(accessToken, deviceId, moduleId, NetatmoScale.MAX);
	}

	/**
	 * Creates a request for the measurements of a device or module
	 * using the scale specified.
	 *
	 * If you don't specify a moduleId you will retrieve the device's
	 * measurements. If you do specify a moduleId you will retrieve the module's
	 * measurements.
	 *
	 * @param accessToken
	 * @param deviceId
	 * @param moduleId
	 *            optional, may be <code>null</code>
	 * @param scale
	 */
	public MeasurementRequest(final String accessToken, final String deviceId,
			final String moduleId, final NetatmoScale scale) {
		assert accessToken != null : "accessToken must not be null!";
		assert deviceId != null : "deviceId must not be null!";
		assert scale != null : "scale must not be null!";

		this.accessToken = accessToken;
		this.deviceId = deviceId;
		this.moduleId = moduleId;
		this.scale = scale;
	}

	/**
	 * @param measure
	 *            the name of a supported measure, e.g. "Temperature" or
	 *            "Humidity"
	 */
	public void addMeasure(final NetatmoMeasureType measureType) {
		this.measures.add(measureType.getMeasure());
	}

	@Override
	public MeasurementResponse execute() {
		final String content = buildContentString();

		String json = null;
		try {
			json = executeQuery(content);
			
			return JSON.readValue(json, MeasurementResponse.class);
		} catch (final Exception e) {
			throw newException("Could not get measurements!", e, URL, content, json);
		}
	}

	/**
	 * @see #createKey(String, String)
	 */
	public String getKey() {
		return createKey(this.deviceId, this.moduleId, this.scale);
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

	protected String executeQuery(final String content) throws Exception {
		final InputStream stream = new ByteArrayInputStream(
				content.getBytes(CHARSET));

		logger.debug("HTTP Post url='{}' content='{}'", URL, content);

		return executeUrl(HTTP_POST, URL, HTTP_HEADERS, stream,
				HTTP_CONTENT_TYPE, HTTP_REQUEST_TIMEOUT);
	}

	private String buildContentString() {
		final StringBuilder contentBuilder = new StringBuilder();
		contentBuilder.append("access_token=");
		contentBuilder.append(this.accessToken);
		contentBuilder.append("&scale=" + scale.getScale());
		contentBuilder.append("&date_end=last");
		contentBuilder.append("&device_id=");
		contentBuilder.append(this.deviceId);
		if (this.moduleId != null) {
			contentBuilder.append("&module_id=");
			contentBuilder.append(this.moduleId);
		}
		contentBuilder.append("&type=");
		for (final Iterator<String> i = this.measures.iterator(); i.hasNext();) {
			contentBuilder.append(i.next());
			if (i.hasNext()) {
				contentBuilder.append(",");
			}
		}

		return contentBuilder.toString();
	}
}
