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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
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
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.openhab.binding.nest.internal.NestException;
import org.openhab.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Updates the data model in the Nest API.
 * 
 * @author John Cocula
 * @since 1.7.0
 */
public class UpdateDataModelRequest extends AbstractRequest {

	private static final Logger logger = LoggerFactory.getLogger(UpdateDataModelRequest.class);

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
		Properties headers = new Properties();
		headers.putAll(HTTP_HEADERS);
		String json = JSON.writeValueAsString(this.dataModel);
		return executeUrl(HTTP_PUT, url, headers, json, "application/json", HTTP_REQUEST_TIMEOUT);
	}

	/**
	 * Executes the given <code>url</code> with the given <code>httpMethod</code>
	 * 
	 * @param httpMethod
	 *            the HTTP method to use
	 * @param url
	 *            the url to execute (in milliseconds)
	 * @param httpHeaders
	 *            optional HTTP headers which has to be set on request
	 * @param content
	 *            the content to be send to the given <code>url</code> or <code>null</code> if no content should be
	 *            send.
	 * @param contentType
	 *            the content type of the given <code>content</code>
	 * @param timeout
	 *            the socket timeout to wait for data
	 * @param proxyHost
	 *            the hostname of the proxy
	 * @param proxyPort
	 *            the port of the proxy
	 * @param proxyUser
	 *            the username to authenticate with the proxy
	 * @param proxyPassword
	 *            the password to authenticate with the proxy
	 * @param nonProxyHosts
	 *            the hosts that won't be routed through the proxy
	 * @param followRedirects
	 *            follow redirects
	 * @return the response body or <code>NULL</code> when the request went wrong
	 */
	public static String executeUrl(final String httpMethod, final String url, final Properties httpHeaders,
			final String contentString, final String contentType, final int timeout) {

		HttpClient client = new HttpClient();

		HttpMethod method = HttpUtil.createHttpMethod(httpMethod, url);
		method.getParams().setSoTimeout(timeout);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

		if (httpHeaders != null) {
			for (String httpHeaderKey : httpHeaders.stringPropertyNames()) {
				method.addRequestHeader(new Header(httpHeaderKey, httpHeaders.getProperty(httpHeaderKey)));
			}
		}
		// add content if a valid method is given ...
		if (method instanceof EntityEnclosingMethod && contentString != null) {
			EntityEnclosingMethod eeMethod = (EntityEnclosingMethod) method;
			InputStream content = new ByteArrayInputStream(contentString.getBytes());
			eeMethod.setRequestEntity(new InputStreamRequestEntity(content, contentType));
		}

		if (logger.isDebugEnabled()) {
			try {
				logger.debug("About to execute '" + method.getURI().toString() + "'");
			} catch (URIException e) {
				logger.debug(e.getMessage());
			}
		}

		try {

			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_NO_CONTENT || statusCode == HttpStatus.SC_ACCEPTED) {
				// perfectly fine but we cannot expect any answer...
				return null;
			}

			if (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT) {
				Header[] headers = method.getResponseHeaders("Location");
				String newUrl = headers[headers.length - 1].getValue();
				return executeUrl(httpMethod, newUrl, httpHeaders, contentString, contentType, timeout);
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
						logger.debug("GZipped InputStream from {}", url);
					} else if (ehElem.toString().matches(".*deflate.*")) {
						tmpResponseStream = new InflaterInputStream(tmpResponseStream);
						logger.debug("Deflated InputStream from {}", url);
					}
				}
			}

			String responseBody = IOUtils.toString(tmpResponseStream);
			if (!responseBody.isEmpty()) {
				logger.debug(responseBody);
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
