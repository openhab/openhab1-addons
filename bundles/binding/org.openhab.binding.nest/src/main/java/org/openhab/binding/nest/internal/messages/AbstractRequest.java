/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.TimeZone;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.openhab.binding.nest.internal.NestException;

/**
 * Base class for all Nest API requests.
 * 
 * @author John Cocula
 * @since 1.7.0
 */
public abstract class AbstractRequest extends AbstractMessage implements Request {

	protected static final String HTTP_GET = "GET";

	protected static final String HTTP_PUT = "PUT";

	protected static final String HTTP_POST = "POST";

	protected static final String API_BASE_URL = "https://developer-api.nest.com/";

	protected static final Properties HTTP_HEADERS;

	protected static final int HTTP_REQUEST_TIMEOUT = 10000;

	protected static final ObjectMapper JSON = new ObjectMapper();

	static {
		HTTP_HEADERS = new Properties();
		HTTP_HEADERS.put("Accept", "application/json");

		// do not serialize null values
		JSON.setSerializationInclusion(Inclusion.NON_NULL);

		// dates in the JSON are in ISO 8601 format
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);
		JSON.setDateFormat(df);
	}

	protected final RuntimeException newException(final String message, final Exception cause, final String url,
			final String json) {
		if (cause instanceof JsonMappingException) {
			return new NestException("Could not parse JSON from URL '" + url + "': " + json, cause);
		}

		return new NestException(message, cause);
	}
}
