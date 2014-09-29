/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.ws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple HTTP Client for IHC / ELKO LS Controller connection purposes.
 * 
 * Controller accepts only HTTPS connections and because normally IP address are
 * used on home network rather than DNS names, class accepts all host names on
 * TLS handshake.
 * 
 * @author Pauli Anttila
 * @since 1.1.0
 */
public abstract class IhcHttpsClient {

	private static final Logger logger = LoggerFactory
			.getLogger(IhcHttpsClient.class);

	HttpsURLConnection conn = null;
	private int timeout = 5000;
	
	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * Open HTTP connection.
	 * 
	 * @param url
	 *            Url to connect.
	 */
	protected void openConnection(String url)
			throws IhcExecption {

		// For debugging purposes
		//System.setProperty("javax.net.debug","all");

		try {
			conn = (HttpsURLConnection) new URL(url).openConnection();
		} catch (MalformedURLException e) {
			throw new IhcExecption(e);
		} catch (IOException e) {
			throw new IhcExecption(e);
		}

		conn.setHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				 logger.trace( "HostnameVerifier: arg0 = " + arg0 );
				 logger.trace( "HostnameVerifier: arg1 = " + arg1 );
				return true;
			}
		});

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("accept-charset", "UTF-8");
		conn.setRequestProperty("content-type", "text/xml");
		conn.setConnectTimeout(timeout);
	}

	protected void closeConnection() {
		//conn.disconnect();
	}
	
	@SuppressWarnings("unused")
	private void trustEveryone() {

		// Create a trust manager that does not validate certificate chains,
		// but accept all.
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
				logger.debug( "checkServerTrusted: certs = " +
				 certs.toString() );
				 logger.debug( "checkServerTrusted: authType = " + authType );
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");

			sslContext.init(null, trustAllCerts,
					new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
					.getSocketFactory());

		} catch (NoSuchAlgorithmException e) {
			logger.warn("Exception", e);
		} catch (KeyManagementException e) {
			logger.warn("Exception", e);
		}

	}

	/**
	 * Send HTTP request and wait response from the server.
	 * 
	 * @param query
	 *            Data to send.
	 * @param timeoutInMilliseconds
	 *            Timeout in milliseconds to wait response.
	 * @return Response from server.
	 */
	protected String sendQuery(String query)
			throws IhcExecption {
		
		conn.setReadTimeout(timeout);

		try {
			OutputStreamWriter writer = new OutputStreamWriter(
					conn.getOutputStream(), "UTF-8");

			logger.trace("Send query: {}", query);
			writer.write(query);
			writer.flush();
			writer.close();
	
			InputStreamReader reader = new InputStreamReader(conn.getInputStream(),
					"UTF-8");
			String response = readInputStreamAsString(reader);
			logger.trace("Receive response: {}", response);
			return response;
		
		} catch (UnsupportedEncodingException e) {
			throw new IhcExecption(e);
		} catch (IOException e) {
			throw new IhcExecption(e);
		}
	}

	/**
	 * Get cookies values from last response.
	 * 
	 * @return List of cookie values.
	 */
	public List<String> getCookies() {
		return conn.getHeaderFields().get("set-cookie");
	}

	/**
	 * Set cookie values to use in next query.
	 * 
	 * @param cookies
	 *            List of cookie values.
	 * @return
	 */
	public void setCookies(List<String> cookies) {
		for (String cookie : cookies) {
			logger.trace("Use cookie value '{}'", cookie.split(";", 2)[0]);
			conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
		}
	}

	/**
	 * Set request properties.
	 * 
	 * @param List
	 *            of request property values.
	 * @return
	 */
	public void setRequestProperties(Map<String, String> listOfProperties) {

		for (Map.Entry<String, String> entry : listOfProperties.entrySet()) {
			conn.addRequestProperty(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Set request property.
	 * 
	 * @param key
	 *            property key.
	 * @param value
	 *            property value.
	 */
	public void setRequestProperty(String key, String value) {

		conn.addRequestProperty(key, value);
	}

	static String readInputStreamAsString(InputStreamReader in)
			throws IOException {

		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result = in.read();

		while (result != -1) {
			byte b = (byte) result;
			buf.write(b);
			result = in.read();
		}

		return buf.toString();
	}
}
