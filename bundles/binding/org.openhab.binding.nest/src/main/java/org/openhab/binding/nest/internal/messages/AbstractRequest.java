/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal.messages;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.openhab.binding.nest.internal.NestException;
import org.openhab.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all Nest API requests.
 * 
 * @author John Cocula
 * @since 1.7.0
 */
public abstract class AbstractRequest extends AbstractMessage implements Request {

	private static final Logger logger = LoggerFactory.getLogger(AbstractRequest.class);

	private static final Properties HTTP_HEADERS;

	private static final int DEFAULT_HTTP_REQUEST_TIMEOUT = 10000;

	protected static final String HTTP_GET = "GET";

	protected static final String HTTP_POST = "POST";

	protected static final String HTTP_PUT = "PUT";

	protected static final String API_BASE_URL = "https://developer-api.nest.com/";

	protected static final ObjectMapper JSON = new ObjectMapper();

	protected static int httpRequestTimeout = DEFAULT_HTTP_REQUEST_TIMEOUT;

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

	public static void setHttpRequestTimeout(int timeout) {
		httpRequestTimeout = timeout;
	}

	protected final RuntimeException newException(final String message, final Exception cause, final String url,
			final String json) {
		if (cause instanceof JsonMappingException) {
			return new NestException("Could not parse JSON from URL '" + url + "': " + json, cause);
		}

		return new NestException(message, cause);
	}

	/**
	 * Executes the given <code>url</code> with the given <code>httpMethod</code>. In the case of httpMethods that do
	 * not support automatic redirection, manually handle the HTTP temporary redirect (307) and retry with the new URL.
	 * 
	 * @param httpMethod
	 *            the HTTP method to use
	 * @param url
	 *            the url to execute (in milliseconds)
	 * @param contentString
	 *            the content to be sent to the given <code>url</code> or <code>null</code> if no content should be
	 *            sent.
	 * @param contentType
	 *            the content type of the given <code>contentString</code>
	 * @return the response body or <code>NULL</code> when the request went wrong
	 */
	protected final String executeUrl(final String httpMethod, final String url, final String contentString,
			final String contentType) {

		HttpClient client = new HttpClient();

		HttpMethod method = HttpUtil.createHttpMethod(httpMethod, url);
		method.getParams().setSoTimeout(httpRequestTimeout);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

		for (String httpHeaderKey : HTTP_HEADERS.stringPropertyNames()) {
			method.addRequestHeader(new Header(httpHeaderKey, HTTP_HEADERS.getProperty(httpHeaderKey)));
		}

		// add content if a valid method is given ...
		if (method instanceof EntityEnclosingMethod && contentString != null) {
			EntityEnclosingMethod eeMethod = (EntityEnclosingMethod) method;
			InputStream content = new ByteArrayInputStream(contentString.getBytes());
			eeMethod.setRequestEntity(new InputStreamRequestEntity(content, contentType));
		}

		if (logger.isDebugEnabled()) {
			try {
				logger.trace("About to execute '" + method.getURI().toString() + "'");
			} catch (URIException e) {
				logger.trace(e.getMessage());
			}
		}

		try {

			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_NO_CONTENT || statusCode == HttpStatus.SC_ACCEPTED) {
				// perfectly fine but we cannot expect any answer...
				return null;
			}

			// Manually handle 307 redirects with a little tail recursion
			if (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT) {
				Header[] headers = method.getResponseHeaders("Location");
				String newUrl = headers[headers.length - 1].getValue();
				return executeUrl(httpMethod, newUrl, contentString, contentType);
			}

			if (statusCode != HttpStatus.SC_OK) {
				logger.warn("Method failed: " + method.getStatusLine());
			}

			InputStream tmpResponseStream = method.getResponseBodyAsStream();
			Header encodingHeader = method.getResponseHeader("Content-Encoding");
			if (encodingHeader != null) {
				for (HeaderElement ehElem : encodingHeader.getElements()) {
					if (ehElem.toString().matches(".*gzip.*")) {
						tmpResponseStream = new GZIPInputStream(tmpResponseStream);
						logger.trace("GZipped InputStream from {}", url);
					} else if (ehElem.toString().matches(".*deflate.*")) {
						tmpResponseStream = new InflaterInputStream(tmpResponseStream);
						logger.trace("Deflated InputStream from {}", url);
					}
				}
			}

			String responseBody = IOUtils.toString(tmpResponseStream);
			if (!responseBody.isEmpty()) {
				logger.trace(responseBody);
			}

			return responseBody;
		} catch (HttpException he) {
			logger.error("Fatal protocol violation: {}", he.toString());
		} catch (IOException ioe) {
			logger.error("Fatal transport error: {}", ioe.toString());
		} finally {
			method.releaseConnection();
		}

		return null;
	}
}
