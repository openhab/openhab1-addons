/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

import java.util.Properties;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Base class for all Netatmo API requests.
 * 
 * @author Andreas Brenk
 * @since 1.4.0
 */
public abstract class AbstractRequest extends AbstractMessage implements
		Request {

	protected static final String HTTP_GET = "GET";

	protected static final String HTTP_POST = "POST";

	protected static final Properties HTTP_HEADERS;

	protected static final int HTTP_REQUEST_TIMEOUT = 10000;

	protected static final ObjectMapper JSON = new ObjectMapper();

	static {
		HTTP_HEADERS = new Properties();
		HTTP_HEADERS.put("Accept", "application/json");
	}

}
