/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netaqua.internal.messages;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.httpclient.util.URIUtil.encodeQuery;
import static org.openhab.io.net.http.HttpUtil.executeUrl;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.openhab.binding.netaqua.internal.NetAQUAException;

/**
 * A devicelist request returns the list of systems controlled by the user, and their
 * modules.
 * 
 * @author Markus Fritze
 * @since 1.7.0
 * @see <a href="https://developer.enphase.com/docs#api-systems">systems</a>
 */
public class SystemsRequest extends AbstractMessage {

	protected static final String HTTP_GET = "GET";

	protected static final String API_BASE_URL = "http://";

	protected static final int HTTP_REQUEST_TIMEOUT = 10000;

	private final String server;
	private final String request;

	protected final RuntimeException newException(
			final String message, final Exception cause,
			final String url, final String json) {

		return new NetAQUAException(message, cause);
	}
	
	/**
	 * Creates a request
	 * 
	 * @param request
	 *            mandatory, must not be <code>null</code>
	 */
	public SystemsRequest(final String server, final String request) {
		assert server != null : "server must not be null!";
		assert request != null : "key must not be null!";

		this.server = server;
		this.request = request;
	}

	public Map<String, String> execute() {
		final String url = prepare();
		String response = null;

		try {
			response = executeQuery(url);
			Map<String, String> responseMap = new HashMap<String, String>();
			for(final String line : response.split("\\r?\\n")) {
				final String[]	keyValue = line.split("\\|");
				responseMap.put(keyValue[0], StringEscapeUtils.unescapeHtml(keyValue[1]));
			}
			return responseMap;
		} catch (final Exception e) {
			throw newException("Could not execute system summary request!", e, url, response);
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append(this.request);
		return builder.toString();
	}

	protected String executeQuery(final String url) {
		Properties headers = new Properties();
		return executeUrl(HTTP_GET, url, headers, null, null, HTTP_REQUEST_TIMEOUT);
	}

	public String prepare() {
		final StringBuilder urlBuilder = new StringBuilder(API_BASE_URL);
		urlBuilder.append(this.server);
		urlBuilder.append("/");
		urlBuilder.append(this.request);

		try {
			return encodeQuery(urlBuilder.toString());
		} catch (final URIException e) {
			throw new NetAQUAException("Could not prepare systems request!", e);
		}
	}
}
