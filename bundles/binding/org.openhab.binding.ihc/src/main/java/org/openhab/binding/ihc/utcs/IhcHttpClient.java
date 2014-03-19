/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.utcs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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
public class IhcHttpClient {

	private static final Logger logger = LoggerFactory
			.getLogger(IhcHttpClient.class);

	HttpsURLConnection conn = null;

	/**
	 * Open HTTP connection.
	 * 
	 * @param url
	 *            Url to connect.
	 * @param timeout
	 *            Timeout in milliseconds.
	 * @return
	 */
	public void openConnection(String url, int timeout)
			throws UnsupportedEncodingException, IOException {

		// System.setProperty("javax.net.debug","all");

		// trustEveryone();

		conn = (HttpsURLConnection) new URL(url).openConnection();

		conn.setHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				// logger.debug( "HostnameVerifier: arg0 = " + arg0 );
				// logger.debug( "HostnameVerifier: arg1 = " + arg1 );
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

	@SuppressWarnings("unused")
	private void trustEveryone() {

		// Create a trust manager that does not validate certificate chains
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
				// logger.debug( "checkServerTrusted: certs = " +
				// certs.toString() );
				// logger.debug( "checkServerTrusted: authType = " + authType );
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
	public String sendQuery(String query, int timeoutInMilliseconds)
			throws IOException {
		conn.setReadTimeout(timeoutInMilliseconds);
		OutputStreamWriter writer = new OutputStreamWriter(
				conn.getOutputStream(), "UTF-8");
		// logger.debug("Send query: {}", query);
		writer.write(query);
		writer.flush();
		writer.close();

		InputStreamReader reader = new InputStreamReader(conn.getInputStream(),
				"UTF-8");
		String response = readInputStreamAsString(reader);
		// logger.debug("Receive response: {}", response);
		return response;
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
			// logger.debug("Use cookie value '{}'", cookie.split(";", 2)[0]);
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
