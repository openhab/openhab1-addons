/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.ws;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom HTTP connection pool, which install all-trusting trust manager.
 * 
 * @author Pauli Anttila
 * @since 1.7.0
 */
public class IhcConnectionPool {

	private static final Logger logger = LoggerFactory
			.getLogger(IhcConnectionPool.class);

	private static IhcConnectionPool instance = null;

	/**
	 * Controller TLS certificate is self signed, which means that certificate
	 * need to be manually added to java key store as a trusted certificate.
	 * This is special SSL context which will be configured to trust all
	 * certificates and manual work is not required.
	 */
	private SSLContext sslContext = null;

	/** Holds and share cookie information (session id) from authentication procedure */
	private CookieStore cookieStore = null;

	private HttpClientBuilder httpClientBuilder = null;
	private HttpClientContext localContext = null;

	protected IhcConnectionPool() {
		init();
	}

	public static IhcConnectionPool getInstance() {
		if (instance == null) {
			synchronized (IhcConnectionPool.class) {
				if (instance == null) {
					instance = new IhcConnectionPool();
				}
			}
		}
		return instance;
	}

	private void init() {

		// Create a local instance of cookie store
		cookieStore = new BasicCookieStore();

		// Create local HTTP context
		localContext = HttpClientContext.create();

		// Bind custom cookie store to the local context
		localContext.setCookieStore(cookieStore);

		httpClientBuilder = HttpClientBuilder.create();

		// Setup a Trust Strategy that allows all certificates.

		logger.debug("Initialize SSL context");

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
				logger.trace("Trusting server cert: " + certs[0].getIssuerDN());
			}
		} };

		// Install the all-trusting trust manager

		try {
			// Controller supports only SSLv3 and TLSv1
			sslContext = SSLContext.getInstance("TLSv1");
			sslContext.init(null, trustAllCerts,
					new java.security.SecureRandom());

		} catch (NoSuchAlgorithmException e) {
			logger.warn("Exception", e);
		} catch (KeyManagementException e) {
			logger.warn("Exception", e);
		}

		httpClientBuilder.setSslcontext(sslContext);

		// Controller accepts only HTTPS connections and because normally IP
		// address are used on home network rather than DNS names, create custom
		// host name verifier.
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {

			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				logger.trace("HostnameVerifier: arg0 = " + arg0);
				logger.trace("HostnameVerifier: arg1 = " + arg1);
				return true;
			}
		};

		// Create an SSL Socket Factory, to use our weakened "trust strategy"
		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
				sslContext, new String[] { "TLSv1" }, null, hostnameVerifier);

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("https", sslSocketFactory).build();

		// Create connection-manager using our Registry. Allows multi-threaded
		// use
		PoolingHttpClientConnectionManager connMngr = new PoolingHttpClientConnectionManager(
				socketFactoryRegistry);

		// Increase max connection counts
		connMngr.setMaxTotal(20);
		connMngr.setDefaultMaxPerRoute(6);

		httpClientBuilder.setConnectionManager(connMngr);
	}

	public HttpClient getHttpClient() {
		return httpClientBuilder.build();
	}

	public HttpClientContext getHttpContext() {
		return localContext;
	}
}
