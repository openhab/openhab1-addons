/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.net.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Some common methods to be used in both HTTP-In-Binding and HTTP-Out-Binding
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @since 0.6.0
 */
public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	/** {@link Pattern} which matches the credentials out of an URL */ 
	private static final Pattern URL_CREDENTIALS_PATTERN = Pattern.compile("http://(.*?):(.*?)@.*");
	

	/**
	 * Executes the given <code>url</code> with the given <code>httpMethod</code>.
	 * Furthermore the <code>http.proxyXXX</code> System variables are read and
	 * set into the {@link HttpClient}.
	 * 
	 * @param httpMethod the HTTP method to use
	 * @param url the url to execute (in milliseconds)
	 * @param timeout the socket timeout to wait for data
	 * 
	 * @return the response body or <code>NULL</code> when the request went wrong
	 */
	public static String executeUrl(String httpMethod, String url, int timeout) {
		return executeUrl(httpMethod, url, null, null, timeout);
	}
	
	/**
	 * Executes the given <code>url</code> with the given <code>httpMethod</code>.
	 * Furthermore the <code>http.proxyXXX</code> System variables are read and
	 * set into the {@link HttpClient}.
	 * 
	 * @param httpMethod the HTTP method to use
	 * @param url the url to execute (in milliseconds)
	 * @param content the content to be send to the given <code>url</code> or 
	 * <code>null</code> if no content should be send.
	 * @param contentType the content type of the given <code>content</code>
	 * @param timeout the socket timeout to wait for data
	 * 
	 * @return the response body or <code>NULL</code> when the request went wrong
	 */
	public static String executeUrl(String httpMethod, String url, InputStream content, String contentType, int timeout) {
		
		return executeUrl(httpMethod, url, null, content, contentType, timeout);
	}
	
	/**
	 * Executes the given <code>url</code> with the given <code>httpMethod</code>.
	 * Furthermore the <code>http.proxyXXX</code> System variables are read and
	 * set into the {@link HttpClient}.
	 * 
	 * @param httpMethod the HTTP method to use
	 * @param url the url to execute (in milliseconds)
	 * @param httpHeaders optional http request headers which has to be sent within request 
	 * @param content the content to be send to the given <code>url</code> or 
	 * <code>null</code> if no content should be send.
	 * @param contentType the content type of the given <code>content</code>
	 * @param timeout the socket timeout to wait for data
	 * 
	 * @return the response body or <code>NULL</code> when the request went wrong
	 */
	public static String executeUrl(String httpMethod, String url, Properties httpHeaders, InputStream content, String contentType, int timeout) {
		String proxySet = System.getProperty("http.proxySet");
		
		String proxyHost = null;
		int proxyPort = 80;
		String proxyUser = null;
		String proxyPassword = null;
		String nonProxyHosts = null;
		
		if ("true".equalsIgnoreCase(proxySet)) {
			proxyHost = System.getProperty("http.proxyHost");
			String proxyPortString = System.getProperty("http.proxyPort");
			if (StringUtils.isNotBlank(proxyPortString)) {
				try {
					proxyPort = Integer.valueOf(proxyPortString);
				} catch(NumberFormatException e) {
					logger.warn("'{}' is not a valid proxy port - using port 80 instead");
				}
			}
			proxyUser = System.getProperty("http.proxyUser");
			proxyPassword = System.getProperty("http.proxyPassword");
			nonProxyHosts = System.getProperty("http.nonProxyHosts");
		}
		
		return executeUrl(httpMethod, url, httpHeaders, content, contentType, timeout, proxyHost, proxyPort, proxyUser, proxyPassword, nonProxyHosts);

	}
	
	/**
	 * Executes the given <code>url</code> with the given <code>httpMethod</code>
	 * 
	 * @param httpMethod the HTTP method to use
	 * @param url the url to execute (in milliseconds)
	 * @param httpHeaders optional HTTP headers which has to be set on request
	 * @param content the content to be send to the given <code>url</code> or 
	 * <code>null</code> if no content should be send.
	 * @param contentType the content type of the given <code>content</code>
	 * @param timeout the socket timeout to wait for data
	 * @param proxyHost the hostname of the proxy
	 * @param proxyPort the port of the proxy
	 * @param proxyUser the username to authenticate with the proxy
	 * @param proxyPassword the password to authenticate with the proxy
	 * @param nonProxyHosts the hosts that won't be routed through the proxy
	 * @return the response body or <code>NULL</code> when the request went wrong
	 */
	public static String executeUrl(String httpMethod, String url, Properties httpHeaders, InputStream content, String contentType, int timeout, String proxyHost, Integer proxyPort, String proxyUser, String proxyPassword, String nonProxyHosts) {
		
		HttpClient client = new HttpClient();
		
		// only configure a proxy if a host is provided
		if (StringUtils.isNotBlank(proxyHost) && proxyPort != null && shouldUseProxy(url, nonProxyHosts)) {
			client.getHostConfiguration().setProxy(proxyHost, proxyPort);
			if (StringUtils.isNotBlank(proxyUser)) {
				client.getState().setProxyCredentials(AuthScope.ANY,
					new UsernamePasswordCredentials(proxyUser, proxyPassword));
			}
		}
		  
		HttpMethod method = HttpUtil.createHttpMethod(httpMethod, url);
        method.getParams().setSoTimeout(timeout);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		if(httpHeaders != null){
			for(String httpHeaderKey: httpHeaders.stringPropertyNames()){
				method.addRequestHeader(new Header(httpHeaderKey,httpHeaders.getProperty(httpHeaderKey)));
			}
		}
		// add content if a valid method is given ...
		if (method instanceof EntityEnclosingMethod && content != null ) {
			EntityEnclosingMethod eeMethod = (EntityEnclosingMethod) method;
			eeMethod.setRequestEntity(new InputStreamRequestEntity(content, contentType));
		}

		Credentials credentials = extractCredentials(url);
		if (credentials != null) {
			client.getParams().setAuthenticationPreemptive(true); 
			client.getState().setCredentials(AuthScope.ANY, credentials);			
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
			if (statusCode != HttpStatus.SC_OK) {
				logger.warn("Method failed: " + method.getStatusLine());
			}

			String responseBody = IOUtils.toString(method.getResponseBodyAsStream());
			if (!responseBody.isEmpty()) {
				logger.debug(responseBody);
			}
			
			return responseBody;
		}
		catch (HttpException he) {
			logger.error("Fatal protocol violation: {}", he.toString());
		}
		catch (IOException ioe) {
			logger.error("Fatal transport error: {}", ioe.toString());
		}
		finally {
			method.releaseConnection();
		}
		
		return null;
	}

	/**
	 * Determines whether the list of <code>nonProxyHosts</code> contains the
	 * host (which is part of the given <code>urlString</code> or not.
	 * 
	 * @param urlString
	 * @param nonProxyHosts
	 * 
	 * @return <code>false</code> if the host of the given <code>urlString</code>
	 * is contained in <code>nonProxyHosts</code>-list and <code>true</code>
	 * otherwise
	 */
	private static boolean shouldUseProxy(String urlString, String nonProxyHosts) {
		
		if (StringUtils.isNotBlank(nonProxyHosts)) {
			String givenHost = urlString;
			
			try {
				URL url = new URL(urlString);
				givenHost = url.getHost();
			} catch (MalformedURLException e) {
				logger.error("the given url {} is malformed", urlString);
			}
			
			String[] hosts = nonProxyHosts.split("\\|");
			for (String host : hosts) {
				if (host.contains("*")) {
					// the nonProxyHots-pattern allows wildcards '*' which must
					// be masked to be used with regular expressions
					String hostRegexp = host.replaceAll("\\.", "\\\\.");
					hostRegexp = hostRegexp.replaceAll("\\*", ".*");
					if (givenHost.matches(hostRegexp)) {
						return false;
					}
				} else {
					if (givenHost.equals(host)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}

	/**
	 * Extracts username and password from the given <code>url</code>. A valid
	 * url to extract {@link Credentials} from looks like:
	 * <pre>
	 * http://username:password@www.domain.org
	 * </pre>
	 *  
	 * @param url the URL to extract {@link Credentials} from
	 * 
	 * @return the exracted Credentials or <code>null</code> if the given 
	 * <code>url</code> does not contain credentials
 	 */
	protected static Credentials extractCredentials(String url) {
		
		Matcher matcher = URL_CREDENTIALS_PATTERN.matcher(url);
		
		if (matcher.matches()) {
			
			matcher.reset();

			String username = "";
			String password = "";

			while (matcher.find()) {
				username = matcher.group(1);
				password = matcher.group(2);
			}
			
			Credentials credentials =
				 new UsernamePasswordCredentials(username, password);
			return credentials;
		}
		
		return null;
	}

	/**
	 * Factory method to create a {@link HttpMethod}-object according to the 
	 * given String <code>httpMethod</codde>
	 * 
	 * @param httpMethodString the name of the {@link HttpMethod} to create
	 * @param url
	 * 
	 * @return an object of type {@link GetMethod}, {@link PutMethod}, 
	 * {@link PostMethod} or {@link DeleteMethod}
	 * @throws IllegalArgumentException if <code>httpMethod</code> is none of
	 * <code>GET</code>, <code>PUT</code>, <code>POST</POST> or <code>DELETE</code>
	 */
	public static HttpMethod createHttpMethod(String httpMethodString, String url) {
		
		if ("GET".equals(httpMethodString)) {
			return new GetMethod(url);
		}
		else if ("PUT".equals(httpMethodString)) {
	        return new PutMethod(url);
		}
		else if ("POST".equals(httpMethodString)) {
	        return new PostMethod(url);
		}
		else if ("DELETE".equals(httpMethodString)) {
	        return new DeleteMethod(url);
		}
		else {
			throw new IllegalArgumentException("given httpMethod '" + httpMethodString + "' is unknown");
		}
	}

}
