/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.openwebif.internal.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.openhab.action.openwebif.internal.impl.config.OpenWebIfConfig;

/**
 * Builds a get url with the specified parameters.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class UrlBuilder {
	private static final String ENCODING = "UTF-8";
	private OpenWebIfConfig config;
	private String path;
	private Map<String, String> queryParams = new HashMap<String, String>();

	/**
	 * Creates a UrlBuilder with the specified config and path.
	 */
	public UrlBuilder(OpenWebIfConfig config, String path) {
		this.config = config;
		this.path = path;
	}

	/**
	 * Adds a name/value parameter.
	 */
	public UrlBuilder addParameter(String param, String value) {
		queryParams.put(param, value);
		return this;
	}

	/**
	 * Builds the complete get url.
	 */
	public String build() throws UnsupportedEncodingException {
		final StringBuilder sb = new StringBuilder();
		sb.append("http");
		if (config.isHttps()) {
			sb.append("s");
		}
		sb.append("://");
		sb.append(config.getHost());
		if (config.getPort() != 0) {
			sb.append(":").append(config.getPort());
		}
		sb.append(path);

		if (queryParams.size() > 0) {
			sb.append("?").append(buildParameter());
		}

		return sb.toString();
	}

	/**
	 * Builds an encodes all parameters.
	 */
	private String buildParameter() throws UnsupportedEncodingException {
		final StringBuilder result = new StringBuilder();
		for (String param : queryParams.keySet()) {
			String name = URLEncoder.encode(param, ENCODING);
			String value = URLEncoder.encode(queryParams.get(param), ENCODING);

			if (result.length() > 0) {
				result.append("&");
			}
			result.append(name);
			if (value != null) {
				result.append("=");
				result.append(value);
			}
		}
		return result.toString();
	}
}
