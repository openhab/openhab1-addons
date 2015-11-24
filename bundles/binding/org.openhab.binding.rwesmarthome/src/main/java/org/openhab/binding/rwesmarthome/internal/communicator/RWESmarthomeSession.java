/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal.communicator;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.openhab.binding.rwesmarthome.internal.communicator.client.RWEClient;
import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.ConfigurationChangedException;
import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.LoginFailedException;
import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.LogoutNotificationException;
import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.RWESmarthomeSessionExpiredException;
import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.SHTechnicalException;
import org.openhab.binding.rwesmarthome.internal.communicator.util.HttpComponentsHelper;
import org.openhab.binding.rwesmarthome.internal.communicator.util.XMLUtil;
import org.openhab.binding.rwesmarthome.internal.communicator.xmlresponse.GetAllLogicalDeviceStatesXMLResponse;
import org.openhab.binding.rwesmarthome.internal.communicator.xmlresponse.GetEntitiesXMLResponse;
import org.openhab.binding.rwesmarthome.internal.communicator.xmlresponse.NotificationsXMLResponse;
import org.openhab.binding.rwesmarthome.internal.model.Location;
import org.openhab.binding.rwesmarthome.internal.model.LogicalDevice;
import org.openhab.binding.rwesmarthome.internal.model.RoomTemperatureActuator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;


/**
 * The RWESmarthomeSession holds the session of the connection
 * with the RWE Smarthome Central.
 * 
 * @author ollie-dev
 *
 */
public class RWESmarthomeSession {
	
	private static final Logger logger = LoggerFactory.getLogger(RWESmarthomeSession.class);
	
	/** The firmware version of RWE Smarthome */
	public static final String FIRMWARE_VERSION = "1.70";
	
	/** The hostname. */
	private String hostname = "";
	
	/** The password encrypted. */
	private String passwordEncrypted = "";
	
	/** The client id. */
	private String clientId = "";
	
	/** The session id. */
	private String sessionId = "";

	/** The request id. */
	private String requestId = "";
	
	/** The http helper. */
	HttpComponentsHelper httpHelper = new HttpComponentsHelper();
	
	/** The current configuration version. */
	private String currentConfigurationVersion = "";
	
	/** The locations. */
	private ConcurrentHashMap<String, Location> locations = null;
	
	/** The logicalDevices */
	private ConcurrentHashMap<String, LogicalDevice> logicalDevices = null;
	
	private RWEClient client;
	
	/**
	 * Constructor with the client implementation.
	 * 
	 * @param client
	 */
	public RWESmarthomeSession(RWEClient client) {
		super();
		this.client = client;
	}

	/**
	 * Returns true, if there is a valid session id.
	 * @return boolean
	 */
	public boolean isValid() {
		if (sessionId == null || "".equals(sessionId)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Gets the session id.
	 * 
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
	
	
	/**
	 * Returns the firmware version
	 * 
	 * @return the firmware version
	 */
	public static String getFirmwareVersion() {
		return FIRMWARE_VERSION;
	}
	
	/**
	 * Return the current request id
	 * 
	 * @return the request id
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * Returns a list of locations.
	 * 
	 * @return the locations
	 */
	public ConcurrentHashMap<String, Location> getLocations() {
		return locations;
	}

	/**
	 * Returns the list of logical devices.
	 * 
	 * @return the logicalDevices
	 */
	public ConcurrentHashMap<String, LogicalDevice> getLogicalDevices() {
		return logicalDevices;
	}

	/**
	 * Sets the current configuration version.
	 * 
	 * @param currentConfigurationVersion
	 */
	public void setCurrentConfigurationVersion(String currentConfigurationVersion) {
		this.currentConfigurationVersion = currentConfigurationVersion;
	}

	/**
	 * Sets the locations.
	 * 
	 * @param locations
	 */
	public void setLocations(ConcurrentHashMap<String, Location> locations) {
		this.locations = locations;
	}

	/**
	 * Sets the logical devices.
	 * 
	 * @param logicalDevices
	 */
	public void setLogicalDevices(
			ConcurrentHashMap<String, LogicalDevice> logicalDevices) {
		this.logicalDevices = logicalDevices;
	}

	/**
	 * Destroys the session.
	 */
	public void destroy() {
		final String LOGOUT_REQUEST = String.format(
			"<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"LogoutRequest\" Version=\"%s\" RequestId=\"%s\" SessionId=\"%s\" />",
			FIRMWARE_VERSION, 
			requestId, 
			getSessionId()
		);
		try {
			logger.debug("Destroying session...");
			executeRequest(LOGOUT_REQUEST, "/cmd");
			logger.debug("Session destroyed.");
		} catch (RWESmarthomeSessionExpiredException e) {
			// Ignore expired session for logout
		}
		sessionId = "";

	}
	
	/**
	 * Executes a request with the given command.
	 * 
	 * @param request
	 * @param sCmd
	 * @return
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public String executeRequest(String request, String sCmd)
			throws RWESmarthomeSessionExpiredException {
		return executeRequest(request, sCmd, false);
	}
	
	/**
	 * Executes a request with the given command.
	 * Set login to true to execute a login request.
	 * 
	 * @param request
	 *            the login request
	 * @param command
	 *            the s cmd
	 * @return the string
	 * @throws SmartHomeSessionExpiredException
	 *             the smart home session expired exception
	 */
	private String executeRequest(String request, String command, boolean login)
			throws RWESmarthomeSessionExpiredException {
		
		// If there is no sessionId and no login wanted, session is expired
		if(!login && "".equals(sessionId)) {
			throw new RWESmarthomeSessionExpiredException();
		}
			
		String sReturn = "";

		try {
			// execute the request
			sReturn = client.execute(hostname, clientId, request, command);
			
			// return may contain an IllegalSessionId -> session expired.
			if (sReturn.contains("IllegalSessionId")) {
				logger.info("Session expired!");
				sessionId = "";
				throw new RWESmarthomeSessionExpiredException(sReturn);
			}
			logger.trace("XMLResponse:" + sReturn);

		} catch (ClientProtocolException ex) {
			logger.error(ex.getClass().getSimpleName(), ex);
		} catch (IOException ex) {
			logger.error(ex.getClass().getSimpleName(), ex);
		} 
		return sReturn;

	}

	/**
	 * Logon and initialize a session.
	 * 
	 * @param username
	 *            the user name
	 * @param password
	 *            the pass word
	 * @param hostname
	 *            the host name
	 * @throws SHTechnicalException
	 *             the sH technical exception
	 * @throws LoginFailedException
	 *             the login failed exception
	 */
	public void logon(String username, String password, String hostname) throws SHTechnicalException, LoginFailedException {
		this.hostname = hostname;

		clientId = UUID.randomUUID().toString();
		requestId = generateRequestId();
		passwordEncrypted = generateHashFromPassword(password);
		final String LOGIN_REQUEST = String.format(
			"<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"LoginRequest\" Version=\"%s\" RequestId=\"%s\" UserName=\"%s\" Password=\"%s\" />", 
			FIRMWARE_VERSION, requestId, username, passwordEncrypted
		);
		String sResponse = "";

		try {

			sResponse = executeRequest(LOGIN_REQUEST, "/cmd", true);
			sessionId = XMLUtil.XPathValueFromString(sResponse, "/BaseResponse/@SessionId");
			if (sessionId == null || "".equals(sessionId)) {
				throw new LoginFailedException(String.format("LoginFailed: Authentication with user '%s' was not possible. Session ID is empty.", username));
			}
			currentConfigurationVersion = XMLUtil.XPathValueFromString(sResponse, "/BaseResponse/@CurrentConfigurationVersion");
		} catch (ParserConfigurationException ex) {
			throw new SHTechnicalException("ParserConfigurationException:" + ex.getMessage(), ex);
		} catch (SAXException ex) {
			throw new SHTechnicalException("SAXException:" + ex.getMessage(), ex);
		} catch (XPathExpressionException ex) {
			throw new SHTechnicalException("XPathExpressionException:" + ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new SHTechnicalException(String.format("IOException. Communication with host '%s' was not possible or interrupted: %s", hostname, ex.getMessage()), ex);
		} catch (RWESmarthomeSessionExpiredException e) {
			logger.error("SessionExpiredException while login?!? Should never exist...");
			throw new SHTechnicalException("SessionExpiredException while login?!? Should never exist...");
		}
	}


	/**
	 * Generate hash from password.
	 * 
	 * @param plainPassword
	 *            the plain password
	 * @return the string
	 */
	private String generateHashFromPassword(String plainPassword) {
		String sReturn = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(plainPassword.getBytes());

			byte byteData[] = md.digest();
			sReturn = new String(Base64.encodeBase64(byteData));
		} catch (NoSuchAlgorithmException ex) {
			// ignore
		}
		return sReturn;
	}

	/**
	 * Generate request id.
	 * 
	 * @return the string
	 */
	private String generateRequestId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Returns the current configuration version.
	 * @return
	 */
	public String getCurrentConfigurationVersion() {
		return currentConfigurationVersion;
	}
}
