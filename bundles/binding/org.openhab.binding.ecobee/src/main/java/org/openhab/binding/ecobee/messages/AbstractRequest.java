/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.messages;

import java.text.SimpleDateFormat;
import java.util.Properties;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.openhab.binding.ecobee.internal.EcobeeException;

/**
 * Base class for all Ecobee API requests.
 * 
 * @author John Cocula
 * @since 1.7.0
 */
public abstract class AbstractRequest extends AbstractMessage implements Request {

	protected static final String HTTP_GET = "GET";

	protected static final String HTTP_POST = "POST";

	protected static final String API_BASE_URL = "https://api.ecobee.com/";

	protected static final Properties HTTP_HEADERS;

	protected static int HTTP_REQUEST_TIMEOUT = 20000;

	protected static final ObjectMapper JSON = new ObjectMapper();

	static {
		HTTP_HEADERS = new Properties();
		HTTP_HEADERS.put("Content-Type", "application/json;charset=UTF-8");
		HTTP_HEADERS.put("User-Agent", "ecobee-openhab-api/1.0");

		// do not serialize null values
		JSON.setSerializationInclusion(Inclusion.NON_NULL);

		// *most* dates in the JSON are in this format
		JSON.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	public static void setHttpRequestTimeout(int timeout) {
		HTTP_REQUEST_TIMEOUT = timeout;
	}

	protected final RuntimeException newException(final String message, final Exception cause, final String url,
			final String json) {
		if (cause instanceof JsonMappingException) {
			return new EcobeeException("Could not parse JSON from URL '" + url + "': " + json, cause);
		}

		return new EcobeeException(message, cause);
	}
}
