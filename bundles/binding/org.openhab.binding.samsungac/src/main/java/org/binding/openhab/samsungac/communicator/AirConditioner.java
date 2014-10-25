/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.binding.openhab.samsungac.communicator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.openhab.binding.samsungac.internal.CommandEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirConditioner {

	private static Logger logger = LoggerFactory
			.getLogger(AirConditioner.class);

	private String IP;
	private String MAC;
	private String TOKEN_STRING;
	private final Integer PORT = 2878;
	private Map<CommandEnum, String> statusMap = new HashMap<CommandEnum, String>();
	private SSLSocket socket;

	public AirConditioner login() {
		try {
			connect();
			getToken();
			loginWithToken();
		} catch (Exception e) {
			logger.warn("Connection to Air Conditioner failed", e);
			disconnect();
		}
		return this;
	}

	public void disconnect() {
		if (isConnected()) {
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				logger.warn(
						"Could not disconnect from Air Conditioner with IP: "
								+ IP, e);
			}
		}
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}

	private Map<CommandEnum, String> loginWithToken() throws Exception {
		if (TOKEN_STRING != null) {
			writeLine("<Request Type=\"AuthToken\"><User Token=\""
					+ TOKEN_STRING + "\" /></Request>");
			handleResponse();
		} else
			throw new Exception(
					"Must connect and retrieve a token before login in");
		return getStatus();
	}

	private void getToken() throws Exception {
		while (TOKEN_STRING == null) {
			handleResponse();
			Thread.sleep(2000);
		}
		logger.debug("Token has been acquired: " + TOKEN_STRING);
	}

	private void handleResponse() throws Exception {
		handleResponse(null);
	}

	private void handleResponse(String commandId) throws Exception {
		String line;
		while ((line = readLine(socket)) != null) {
			logger.debug("Got response:'" + line + "'");

			if (line == null || ResponseParser.isFirstLine(line)) {
				return;
			}

			if (ResponseParser.isNotLoggedInResponse(line)) {
				if (TOKEN_STRING != null)
					return;
				writeLine("<Request Type=\"GetToken\" />");
				return;
			}

			if (ResponseParser.isFailedAuthenticationResponse(line)) {
				throw new Exception("failed to connect: '" + line + "'");
			}

			if (commandId != null
					&& ResponseParser
							.isCorrectCommandResponse(line, commandId)) {
				logger.debug("Correct command response: '" + line + "'");
				return;
			}

			if (ResponseParser.isResponseWithToken(line)) {
				TOKEN_STRING = ResponseParser.parseTokenFromResponse(line);
				logger.info("Received TOKEN from AC: '" + TOKEN_STRING + "'");
				return;
			}
			if (ResponseParser.isReadyForTokenResponse(line)) {
				logger.warn("Switch off and on the air conditioner within 30 seconds");
				Thread.sleep(20);
				return;
			}

			if (ResponseParser.isSuccessfulLoginResponse(line)) {
				logger.debug("SuccessfulLoginResponse: '" + line + "'");
				return;
			}

			if (ResponseParser.isDeviceState(line)) {
				statusMap = ResponseParser.parseStatusResponse(line);
				return;
			}

			if (ResponseParser.isDeviceControl(line)) {
				logger.debug("DeviceControl: '" + line + "'");
				return;
			}

			if (ResponseParser.isUpdateStatus(line)) {
				Pattern pattern = Pattern
						.compile("Attr ID=\"(.*)\" Value=\"(.*)\"");
				Matcher matcher = pattern.matcher(line);
				if (matcher.groupCount() == 2) {
					try {
						CommandEnum cmd = CommandEnum.valueOf(matcher.group(0));
					if (cmd != null)
						statusMap.put(cmd, matcher.group(1));
					} catch (IllegalStateException e) {
					}
				}
				return;
			}
		}
	}

	private void writeLine(String line) throws Exception {
		logger.debug("Sending request:'" + line + "'");
		connect();
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			writer.write(line);
			writer.newLine();
			writer.flush();
		} catch (Exception e) {
			logger.warn("Could not write line. Disconnecting...");
			disconnect();
		}
	}

	String readLine(SSLSocket socket) throws Exception {
		connect();
		BufferedReader r = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		try {
			return r.readLine();
		} catch (SocketTimeoutException e) {
		} catch (SSLException e){
			logger.debug("Got SSL Exception. Disconnecting...");
			disconnect();
		}
		return null;
	}

	private void connect() throws Exception {
		if (isConnected())
			return;
		else
			disconnect();
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}
			} };

			ctx.init(null, trustAllCerts, null);
			socket = (SSLSocket) ctx.getSocketFactory().createSocket(IP, PORT);
			socket.setSoTimeout(10000);
			socket.startHandshake();
		} catch (Exception e) {
			throw new Exception("Cannot connect to " + IP + ":" + PORT, e);
		}
		handleResponse();
	}

	public String sendCommand(CommandEnum command, String value) throws Exception {
		logger.debug("Sending command: '" + command.toString() + "' with value: '" + value + "'");
		String id = "cmd" + Math.round(Math.random() * 10000);
		writeLine("<Request Type=\"DeviceControl\"><Control CommandID=\"" + id
				+ "\" DUID=\"" + MAC + "\"><Attr ID=\"" + command
				+ "\" Value=\"" + value + "\" /></Control></Request>");
		handleResponse(id);
		return id;
	}

	public Map<CommandEnum, String> getStatus() {
		try {
			writeLine("<Request Type=\"DeviceState\" DUID=\"" + MAC
					+ "\"></Request>");
			handleResponse();
		} catch (Exception e) {
			logger.warn("Could not update status for air conditioner with IP: "
					+ IP, e);
		}
		return statusMap;
	}
	
	public String getIpAddress() {
		return IP;
	}
	
	public void setIpAddress(String ipAddress) {
		IP = ipAddress;
	}
	
	public void setMacAddress(String macAddress) {
		MAC = macAddress;
	}
	
	public void setToken(String token) {
		TOKEN_STRING = token;
	}
}
