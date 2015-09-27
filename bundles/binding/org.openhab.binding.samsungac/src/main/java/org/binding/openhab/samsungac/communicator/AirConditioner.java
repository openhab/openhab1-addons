/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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

/**
 * Class to that connects to the air conditioner, using IP-address and 
 * MAC-address.
 * This class talks to the air conditioner and makes sure the connections
 * is up and running. Otherwise it will reconnect. 
 * Also if no token is given to the constructor, a token will be requested from
 * the air conditioner at the first login 
 * 
 * @author Stein Tore Tøsse
 * @since 1.6.0
 *
 */
public class AirConditioner {

	private static Logger logger = LoggerFactory
			.getLogger(AirConditioner.class);

	private String IP;
	private String MAC;
	private String TOKEN_STRING;
	private final Integer PORT = 2878;
	private Map<CommandEnum, String> statusMap = new HashMap<CommandEnum, String>();
	private SSLSocket socket;

	/**
	 * This is the method to call first, it will try to connect to the given IP-, and MAC-
	 * address. If no token is specified, it will try to ask the air conditioner to give
	 * it a token.
	 * 
	 * When a token has been received from the air conditioner, we will try to login with this token.
	 * If a connection is established, the method will return itself. 
	 * 
	 * @return An instance of itself, which holds the state of the air conditioner
	 * @throws Exception If something goes wrong while trying to connect
	 */
	public AirConditioner login() throws Exception {
		try {
			connect();
			getToken();
			loginWithToken();
		} catch (Exception e) {
			logger.info("Disconneting...", e);
			disconnect();
			throw e;
		}
		return this;
	}

	/**
	 * Method should be called when all communication has finished. 
	 * For example when OpenHAB is being shut down.
	 * 
	 * Will only disconnect if we are already connected.
	 */
	public void disconnect() {
		try {
			if (socket != null)
				socket.close();
			socket = null;
			logger.info("Disconnected from AC: " + IP);
		} catch (IOException e) {
			logger.warn(
					"Could not disconnect from Air Conditioner with IP: "
							+ IP, e);
		} finally {
			socket = null;
		}
	}

	/**
	 * 
	 * @return true if connected to air conditioner, otherwise false
	 */
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
		logger.info("Token has been acquired: " + TOKEN_STRING);
	}

	/**
	 * Handle response when we are not waiting for a specific answer.
	 * 
	 * @throws Exception
	 */
	private void handleResponse() throws Exception {
		handleResponse(null);
	}

	/**
	 * Handling of the responses is done by reading a response from the air conditioner,
	 * until there's no more responses to read. This is because the air conditioner will
	 * send us messages each time some presses the remote or some state of the air conditioner
	 * changes.
	 * 
	 * @param commandId An id of the command we are waiting for a response on. Not mandatory
	 * @throws Exception Is thrown if we cannot parse the response from the air conditioner
	 */
	private void handleResponse(String commandId) throws Exception {
		String line;
		while ((line = readLine(socket)) != null) {
			logger.debug("Got response:'" + line + "'");

			if (line == null || ResponseParser.isFirstLine(line)) {
				continue;
			}

			if (ResponseParser.isNotLoggedInResponse(line)) {
				if (TOKEN_STRING != null)
					return;
				writeLine("<Request Type=\"GetToken\" />");
				continue;
			}

			if (ResponseParser.isFailedAuthenticationResponse(line)) {
				throw new Exception("failed to connect: '" + line + "'");
			}

			if (commandId != null
					&& ResponseParser
							.isCorrectCommandResponse(line, commandId)) {
				logger.debug("Correct command response: '" + line + "'");
				continue;
			}

			if (ResponseParser.isResponseWithToken(line)) {
				TOKEN_STRING = ResponseParser.parseTokenFromResponse(line);
				logger.warn("Received TOKEN from AC: '" + TOKEN_STRING + "'");
				return;
			}
			if (ResponseParser.isReadyForTokenResponse(line)) {
				logger.warn("NO TOKEN SET! Please switch off and on the air conditioner within 30 seconds");
				return;
			}

			if (ResponseParser.isSuccessfulLoginResponse(line)) {
				logger.debug("SuccessfulLoginResponse: '" + line + "'");
				return;
			}

			if (ResponseParser.isDeviceState(line)) {
				logger.debug("Response is device state '" + line + "'");
				statusMap.clear();
				statusMap = ResponseParser.parseStatusResponse(line);
				return;
			}

			if (ResponseParser.isDeviceControl(line)) {
				logger.debug("DeviceControl: '" + line + "'");
				continue;
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
				continue;
			}
		}
	}

	private void writeLine(String line) throws Exception {
		logger.debug("Sending request:'" + line + "'");
		if (!isConnected())
			login();
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			writer.write(line);
			writer.newLine();
			writer.flush();
		} catch (Exception e) {
			logger.info("Could not write line. Disconnecting..., exception was: " + e);
			disconnect();
			throw(e);
		}
	}

	String readLine(SSLSocket socket) throws Exception {
		if (!isConnected())
			login();
		BufferedReader r = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		try {
			return r.readLine();
		} catch (SocketTimeoutException e) {
			logger.debug("Got socket timeout exception ... ", e);
		} catch (SSLException e){
			logger.debug("Got SSL Exception. Disconnecting...");
			disconnect();
		}
		return null;
	}

	private void connect() throws Exception {
		if (isConnected())
			return;
		else {
			logger.info("Disconnected so we'll try again");
			disconnect();
		}
			
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
			logger.debug("Connected again...");
		} catch (Exception e) {
			throw new Exception("Cannot connect to " + IP + ":" + PORT, e);
		}
		handleResponse();
	}

	/**
	 * Method to send a command to the air conditioner. Will generate a "unique" id for each
	 * command we send, so that we can wait and check the return value of our sent command.
	 * 
	 * @param command The command to send to the air conditioner
	 * @param value Value to change to
	 * @return the generated command id
	 * @throws Exception If we cannot write to the air conditioner or if we cannot handle the response
	 */
	public String sendCommand(CommandEnum command, String value) throws Exception {
		logger.info("Sending command: '" + command.toString() + "' with value: '" + value + "'");
		String id = "cmd" + Math.round(Math.random() * 10000);
		writeLine("<Request Type=\"DeviceControl\"><Control CommandID=\"" + id
				+ "\" DUID=\"" + MAC + "\"><Attr ID=\"" + command
				+ "\" Value=\"" + value + "\" /></Control></Request>");
		handleResponse(id);
		return id;
	}

	/**
	 * Get the status for each of the commands in {@link CommandEnum}
	 * 
	 * @return A Map of the current air conditioner status
	 * @throws Exception If we cannot send a command or if there is a problem parsing the results
	 */
	public Map<CommandEnum, String> getStatus() throws Exception {
		try {
			writeLine("<Request Type=\"DeviceState\" DUID=\"" + MAC
					+ "\"></Request>");
			handleResponse();
		} catch (Exception e) {
			throw new Exception("Could not update status for air conditioner with IP: " + IP, e);
		}
		return statusMap;
	}
	
	/**
	 * 
	 * @return the configured IP-address of the air conditioner
	 */
	public String getIpAddress() {
		return IP;
	}
	
	/**
	 * 
	 * @param ipAddress The IP-address of the air conditioner
	 */
	public void setIpAddress(String ipAddress) {
		IP = ipAddress;
	}
	
	/**
	 * 
	 * @param macAddress The MAC-address of the air conditioner
	 */
	public void setMacAddress(String macAddress) {
		MAC = macAddress;
	}
	
	/**
	 * 
	 * @param token The token to use when connecting to the air conditioner
	 */
	public void setToken(String token) {
		TOKEN_STRING = token;
	}
	
	public String toString() {
		return "Samsung AC: [" + (IP != null ? IP : "") + ":" + (PORT != null ? PORT : "") + ", MAC: " + (MAC != null ? MAC : "") + ", TOKEN: " + (TOKEN_STRING != null ? TOKEN_STRING : "") + "]";
		
	}
}
