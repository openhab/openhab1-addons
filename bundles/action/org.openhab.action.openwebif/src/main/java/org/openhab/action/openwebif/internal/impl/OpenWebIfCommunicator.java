/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.openwebif.internal.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.openhab.action.openwebif.internal.impl.config.OpenWebIfConfig;
import org.openhab.action.openwebif.internal.impl.model.MessageType;
import org.openhab.action.openwebif.internal.impl.model.PowerState;
import org.openhab.action.openwebif.internal.impl.model.SimpleResult;
import org.openhab.action.openwebif.internal.impl.ssl.AllowAllHostnameVerifier;
import org.openhab.action.openwebif.internal.impl.ssl.SimpleTrustManager;
import org.openhab.io.net.actions.Ping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class to communicate with a enigma2 based sat receiver.
 * 
 * @see http://e2devel.com/apidoc/webif/
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class OpenWebIfCommunicator {
	private static final Logger logger = LoggerFactory.getLogger(OpenWebIfCommunicator.class);

	private static final String POWERSTATE = "/web/powerstate";
	private static final String MESSAGE = "/web/message";
	private static final int CONNECTION_TIMEOUT = 5000;

	/**
	 * Returns true, if the sat receiver is turned off or is in deep standby.
	 */
	public boolean isOff(OpenWebIfConfig config) throws IOException {
		return !Ping.checkVitality(config.getHost(), config.getPort(), CONNECTION_TIMEOUT);
	}

	/**
	 * Returns true, if the sat reveiver is in standby.
	 */
	public boolean isStandby(OpenWebIfConfig config) throws IOException {
		String url = new UrlBuilder(config, POWERSTATE).build();
		PowerState result = executeRequest(config, url, PowerState.class);
		return result.isStandby();
	}

	/**
	 * Sends a message to the sat receiver specified in the config.
	 */
	public SimpleResult sendMessage(OpenWebIfConfig config, String text, MessageType type, int timeout)
			throws IOException {
		UrlBuilder ub = new UrlBuilder(config, MESSAGE).addParameter("text", text).addParameter("type", type.getId())
				.addParameter("timeout", String.valueOf(timeout));
		return executeRequest(config, ub.build(), SimpleResult.class);
	}

	/**
	 * Executes the http request and parses the returned stream.
	 */
	@SuppressWarnings("unchecked")
	private <T> T executeRequest(OpenWebIfConfig config, String url, Class<T> clazz) throws IOException {
		HttpURLConnection con = null;
		try {
			logger.trace("Request [{}]: {}", config.getName(), url);

			con = (HttpURLConnection) new URL(url).openConnection();
			con.setConnectTimeout(CONNECTION_TIMEOUT);
			con.setReadTimeout(10000);

			if (config.hasLogin()) {
				String userpass = config.getUser() + ":" + config.getPassword();
				String basicAuth = "Basic " + DatatypeConverter.printBase64Binary(userpass.getBytes());
				con.setRequestProperty("Authorization", basicAuth);
			}

			if (con instanceof HttpsURLConnection) {
				HttpsURLConnection sCon = (HttpsURLConnection) con;
				TrustManager[] trustManager = new TrustManager[] { new SimpleTrustManager() };
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(new KeyManager[0], trustManager, new SecureRandom());
				sCon.setSSLSocketFactory(context.getSocketFactory());
				sCon.setHostnameVerifier(new AllowAllHostnameVerifier());
			}
			StringWriter sw = new StringWriter();
			IOUtils.copy(con.getInputStream(), sw);
			con.disconnect();

			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String response = sw.toString();
				logger.trace("Response: [{}]: {}", config.getName(), response);

				Unmarshaller um = JAXBContext.newInstance(clazz).createUnmarshaller();
				return (T) um.unmarshal(new StringReader(response));
			} else {
				throw new IOException(con.getResponseMessage());
			}
		} catch (JAXBException ex) {
			throw new IOException(ex.getMessage(), ex);
		} catch (GeneralSecurityException ex) {
			throw new IOException(ex.getMessage(), ex);
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}
}
