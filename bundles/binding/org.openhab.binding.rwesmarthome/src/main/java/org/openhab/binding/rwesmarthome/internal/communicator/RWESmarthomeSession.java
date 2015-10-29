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
 * @author ollie-dev
 *
 */
public class RWESmarthomeSession {
	
	private static final Logger logger = LoggerFactory.getLogger(RWESmarthomeSession.class);
	
	/** The firmware version of RWE Smarthome */
	public static final String FIRMWARE_VERSION = "1.70";
	
	/** The hostname. */
	private String hostname = "";
	
	/** The username. */
	private String username = "";

	/** The password. */
	private String password = "";
	
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
	 * @return the locations
	 */
	public ConcurrentHashMap<String, Location> getLocations() {
		return locations;
	}

	/**
	 * @return the logicalDevices
	 */
	public ConcurrentHashMap<String, LogicalDevice> getLogicalDevices() {
		return logicalDevices;
	}

	/**
	 * Destroy.
	 */
	public void destroy() {
		String logoutrequest = "";
		logoutrequest = "<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"LogoutRequest\" Version=\"" + FIRMWARE_VERSION + "\" RequestId=\""
				+ requestId + "\" SessionId=\"" + getSessionId() + "\" />";
		try {
			// String sResponse =
			executeRequest(logoutrequest, "/cmd");
		} catch (RWESmarthomeSessionExpiredException e) {
			// Ignore expired session for logout
		}
		sessionId = "";

	}
	
	private String executeRequest(String request, String sCmd)
			throws RWESmarthomeSessionExpiredException {
		return executeRequest(request, sCmd, false);
	}
	
	/**
	 * Execute request.
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
		if(!login && "".equals(sessionId))
			throw new RWESmarthomeSessionExpiredException();
			
		String sReturn = "";

		try {
			// execute the request
			sReturn = client.execute(hostname, clientId, request, command);
			
			// return may contain an IllegalSessionId -> session expired.
			if (sReturn.contains("IllegalSessionId")) {
				logger.warn("Session expired!");
				sessionId = "";
				throw new RWESmarthomeSessionExpiredException(sReturn);
			}
			//logger.debug("XMLResponse:" + sReturn);

		} catch (ClientProtocolException ex) {
			logger.error(ex.getClass().getSimpleName(), ex);
		} catch (IOException ex) {
			logger.error(ex.getClass().getSimpleName(), ex);
		} 
		finally {
			//logger.debug("finally called");
			// httpPost.releaseConnection();
		}
		return sReturn;

	}

	/**
	 * Logon.
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
	public void logon(String username, String password, String hostname)
			throws SHTechnicalException, LoginFailedException {
		this.username = username;
		this.password = password;
		this.hostname = hostname;
		initialize();
	}

	/**
	 * Initialize.
	 * 
	 * @throws SHTechnicalException
	 *             the sH technical exception
	 * @throws LoginFailedException
	 *             the login failed exception
	 */
	public void initialize() throws SHTechnicalException, LoginFailedException {

		clientId = UUID.randomUUID().toString();
		requestId = generateRequestId();
		passwordEncrypted = generateHashFromPassword(password);
		String sResponse = "";
		String loginRequest = "<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"LoginRequest\" Version=\"" + FIRMWARE_VERSION + "\" RequestId=\""
				+ requestId
				+ "\" UserName=\""
				+ username
				+ "\" Password=\"" + passwordEncrypted + "\" />";
		try {

			sResponse = executeRequest(loginRequest, "/cmd", true);
			sessionId = XMLUtil.XPathValueFromString(sResponse, "/BaseResponse/@SessionId");
			if (sessionId == null || "".equals(sessionId))
				throw new LoginFailedException(
						"LoginFailed: Authentication with user:" + username
								+ " was not possible. Session ID is empty.");
			currentConfigurationVersion = XMLUtil.XPathValueFromString(sResponse, "/BaseResponse/@CurrentConfigurationVersion");
		} catch (ParserConfigurationException ex) {
			throw new SHTechnicalException("ParserConfigurationException:" + ex.getMessage(), ex);
		} catch (SAXException ex) {
			throw new SHTechnicalException("SAXException:" + ex.getMessage(), ex);
		} catch (XPathExpressionException ex) {
			throw new SHTechnicalException("XPathExpressionException:" + ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new SHTechnicalException(
					"IOException. Communication with host " + hostname
							+ " was not possiblte or interrupted. "
							+ ex.getMessage(), ex);
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
	 * Subscribes for notifications.
	 * 
	 * Should be called once before getNotifications(). Times out after a couple of hours. So make sure to
	 * test against LogoutNotification and resubscribe.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForNotification(String notificationType) throws RWESmarthomeSessionExpiredException {
		String notificationRequest;
		String sResponse = "";
		notificationRequest = "<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"NotificationRequest\" Version=\"" + FIRMWARE_VERSION + "\" RequestId=\""
				+ requestId + "\" SessionId=\"" + getSessionId() + "\">"
		+ "<Action>Subscribe</Action>"
		+ "<NotificationType>" + notificationType + "</NotificationType>"
		+ "</BaseRequest>";
		logger.debug("REQ: " + notificationRequest);
		sResponse = executeRequest(notificationRequest, "/cmd");
		logger.debug("SubscribeForNotification-Response: " + sResponse);
	}
	
	/**
	 * Subscribes for configuration change notifications.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForConfigurationChanges() throws RWESmarthomeSessionExpiredException {
		subscribeForNotification("ConfigurationChanges");
	}
	
	/**
	 * Subscribes for calibration notifications.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForCalibration() throws RWESmarthomeSessionExpiredException {
		subscribeForNotification("Calibration");
	}
	
	/**
	 * Subscribes for custom application notifications.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForCustomApplication() throws RWESmarthomeSessionExpiredException {
		subscribeForNotification("CustomApplication");
	}
	
	/**
	 * Subscribes for device state changes.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForDeviceStateChanges() throws RWESmarthomeSessionExpiredException {
		subscribeForNotification("DeviceStateChanges");
	}
	
	/**
	 * Subscribes for deployment changes.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForDeploymentChanges() throws RWESmarthomeSessionExpiredException {
		subscribeForNotification("DeploymentChanges");
	}
	
	/**
	 * Subscribes for message updates.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForMessageUpdates() throws RWESmarthomeSessionExpiredException {
		subscribeForNotification("MessageUpdates");
	}

	/**
	 * @return
	 */
	public String getCurrentConfigurationVersion() {
		return currentConfigurationVersion;
	}

	/**
	 * Refresh configuration.
	 * 
	 * @return the RWE Smarthome configuration XML response
	 * @throws RWESmarthomeSessionExpiredException
	 *             the smart home session expired exception
	 */
	public String refreshConfiguration() throws RWESmarthomeSessionExpiredException {

		String getConfigurationRequest;
		String sResponse = "";
		getConfigurationRequest = "<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"GetEntitiesRequest\" Version=\"" + FIRMWARE_VERSION + "\" RequestId=\""
				+ requestId
				+ "\" SessionId=\""
				+ sessionId
				+ "\">\n"
				+ "<EntityType>Configuration</EntityType></BaseRequest>";
		sResponse = executeRequest(getConfigurationRequest, "/cmd");

		try {
			GetEntitiesXMLResponse entitiesXMLRes = new GetEntitiesXMLResponse(IOUtils.toInputStream(sResponse, "UTF8"));
			this.locations = entitiesXMLRes.getLocations();
			this.logicalDevices = entitiesXMLRes.getLogicalDevices();
			this.currentConfigurationVersion = entitiesXMLRes.getConfigurationVersion();
		} catch (IOException e) {
			throw new RWESmarthomeSessionExpiredException(e);
		}
		
		return sResponse;
	}

	/**
	 * Refresh logical device state.
	 * 
	 * @return the string
	 * @throws SmartHomeSessionExpiredException
	 *             the smart home session expired exception
	 */
	public String refreshLogicalDeviceStates() throws RWESmarthomeSessionExpiredException {

		String getAllLogicalDeviceStatesRequest;
		String sResponse = "";
		getAllLogicalDeviceStatesRequest = "<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"GetAllLogicalDeviceStatesRequest\" Version=\"" + FIRMWARE_VERSION + "\" RequestId=\""
				+ requestId
				+ "\" SessionId=\""
				+ this.getSessionId()
				+ "\" BasedOnConfigVersion=\""
				+ currentConfigurationVersion
				+ "\" />";
		sResponse = executeRequest(getAllLogicalDeviceStatesRequest, "/cmd");

		try {
			GetAllLogicalDeviceStatesXMLResponse entitiesXMLRes = new GetAllLogicalDeviceStatesXMLResponse(IOUtils.toInputStream(sResponse, "UTF8"));

//			this.currentConfigurationVersion = entitiesXMLRes.getConfigurationVersion();
		} catch (IOException e) {
			throw new RWESmarthomeSessionExpiredException(e);
		}
		
		logger.debug("Refresh LD Response: " + sResponse);
		return sResponse;
	}

	/**
	 * Gets the notifications.
	 * @return 
	 * 
	 * @return the XML response
	 * @throws SmartHomeSessionExpiredException
	 *             the smart home session expired exception
	 * @throws LogoutNotificationException, SmartHomeSessionExpiredException
	 * @throws ConfigurationChangedException 
	 */	
	public String getNotifications() throws LogoutNotificationException, RWESmarthomeSessionExpiredException, ConfigurationChangedException {
			
			String sResponse = getNotificationsRequest();

			// check for logout first			
			if(sResponse.contains("LogoutNotification") || sResponse.contains("ConfigurationChangedNotification")) {
				// logout from notification updates received
				NotificationsXMLResponse notXmlRes = new NotificationsXMLResponse(IOUtils.toInputStream(sResponse));
			}
			
			// as the XML format is almost the same, we can reuse GetAllLogicalDeviceStatesXMLResponse
			// for the LogicalDevicesStatesChangedNotification
			GetAllLogicalDeviceStatesXMLResponse logDevXmlRes = new GetAllLogicalDeviceStatesXMLResponse(IOUtils.toInputStream(sResponse));
			
			logger.debug("Notifications: {}", sResponse);
			
			return sResponse;
	}
	
	
	/**
	 * Sends the getNotificationsRequest.
	 * 
	 * @return the XML response
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public String getNotificationsRequest() throws RWESmarthomeSessionExpiredException {
		return executeRequest("upd", "/upd");
	}

	/**
	 * Switch actuator change state.
	 * 
	 * @param deviceId
	 *            the device id
	 * @param on
	 *            the new switchstate
	 * @throws RWESmarthomeSessionExpiredException
	 *             the smart home session expired exception
	 */
	public void switchActuatorChangeState(String deviceId, boolean on)
			throws RWESmarthomeSessionExpiredException {

		String switchOnRequest;
		switchOnRequest = "<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"" + FIRMWARE_VERSION + "\" RequestId=\""
				+ requestId
				+ "\" SessionId=\""
				+ getSessionId()
				+ "\" BasedOnConfigVersion=\""
				+ currentConfigurationVersion
				+ "\"><ActuatorStates><LogicalDeviceState xsi:type=\"SwitchActuatorState\" LID=\""
				+ deviceId
				+ "\" IsOn=\""
				+ on
				+ "\" /></ActuatorStates></BaseRequest>";
		String response = executeRequest(switchOnRequest, "/cmd");
		if(!response.contains("Result=\"Ok\""))
			logger.warn("Response not ok: "+response);
	}

	/**
	 * Changes the temperature of a RoomTemperatureActuator
	 * 
	 * @param deviceId
	 * @param temperature
	 * @throws RWESmarthomeSessionExpiredException 
	 */
	public void roomTemperatureActuatorChangeState(String deviceId, String temperature) throws RWESmarthomeSessionExpiredException {
		String temperatureChangeRequest;
		temperatureChangeRequest = "<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"" + FIRMWARE_VERSION + "\" RequestId=\""
				+ requestId
				+ "\" SessionId=\""
				+ getSessionId()
				+ "\" BasedOnConfigVersion=\""
				+ currentConfigurationVersion
				+ "\"><ActuatorStates><LogicalDeviceState xsi:type=\"RoomTemperatureActuatorState\" LID=\""
				+ deviceId
				+ "\" PtTmp=\""
				+ temperature
				+ "\" OpnMd=\""
				+ "Auto"
				+ "\" WRAc=\"False\" /></ActuatorStates></BaseRequest>";

		String response = executeRequest(temperatureChangeRequest, "/cmd");
		if(!response.contains("Result=\"Ok\""))
			logger.warn("Response not ok: "+response);
	}

	/**
	 * Set the operation mode of a TemperaturActuator to "Auto" if true, "Manu" if false.
	 * 
	 * @param deviceId
	 * @param operationModeAuto
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void roomTemperatureActuatorChangeOperationMode(String deviceId, Boolean operationModeAuto) throws RWESmarthomeSessionExpiredException {
		String operationModeChangeRequest;

		operationModeChangeRequest = "<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"" + FIRMWARE_VERSION + "\" RequestId=\""
				+ requestId
				+ "\" SessionId=\""
				+ getSessionId()
				+ "\" BasedOnConfigVersion=\""
				+ currentConfigurationVersion
				+ "\"><ActuatorStates><LogicalDeviceState xsi:type=\"RoomTemperatureActuatorState\" LID=\""
				+ deviceId
				+ "\" OpnMd=\""
				+ (operationModeAuto ? RoomTemperatureActuator.OPERATION_MODE_AUTO : RoomTemperatureActuator.OPERATION_MODE_MANUAL)
				+ "\" /></ActuatorStates></BaseRequest>";

		String response = executeRequest(operationModeChangeRequest, "/cmd");
		if(!response.contains("Result=\"Ok\""))
			logger.warn("Response not ok: "+response);

	}

	/**
	 * Changes the alarm state
	 * 
	 * @param deviceId
	 * @param on
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void alarmActuatorChangeState(String deviceId, boolean on) throws RWESmarthomeSessionExpiredException {
		String switchOnRequest;
		// String sResponse = "";
		switchOnRequest = "<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"" + FIRMWARE_VERSION + "\" RequestId=\""
				+ requestId
				+ "\" SessionId=\""
				+ getSessionId()
				+ "\" BasedOnConfigVersion=\""
				+ currentConfigurationVersion
				+ "\"><ActuatorStates><LogicalDeviceState xsi:type=\"AlarmActuatorState\" LID=\""
				+ deviceId
				+ "\"><IsOn>"
				+ on
				+ "</IsOn></LogicalDeviceState></ActuatorStates></BaseRequest>";

		String response = executeRequest(switchOnRequest, "/cmd");
		if(!response.contains("Result=\"Ok\""))
			logger.warn("Response not ok: "+response);
	}

	public void zustandsVariableChangeState(String deviceId, boolean on) throws RWESmarthomeSessionExpiredException {
		String switchOnRequest = "<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"" + FIRMWARE_VERSION + "\" RequestId=\""
				+ requestId
				+ "\" SessionId=\""
				+ getSessionId()
				+ "\" BasedOnConfigVersion=\""
				+ currentConfigurationVersion
				+ "\"><ActuatorStates><LogicalDeviceState xsi:type=\"GenericDeviceState\" LID=\""
				+ deviceId
				+ "\"><Ppts><Ppt xsi:type=\"BooleanProperty\" Name=\"Value\" Value=\""
				+ on
				+ "\" /></Ppts></LogicalDeviceState></ActuatorStates></BaseRequest>";

		String response = executeRequest(switchOnRequest, "/cmd");
		if(!response.contains("Result=\"Ok\""))
			logger.warn("Response not ok: "+response);
	}

	/**
	 * Sets a dimmer to a new value.
	 * 
	 * @param deviceId
	 * @param newValue
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void switchDimmerState(String deviceId, String newValue)
			throws RWESmarthomeSessionExpiredException {
		String switchOnRequest = "<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"" + FIRMWARE_VERSION + "\" RequestId=\""
				+ requestId
				+ "\" SessionId=\""
				+ getSessionId()
				+ "\" BasedOnConfigVersion=\""
				+ currentConfigurationVersion
				+ "\"><ActuatorStates><LogicalDeviceState xsi:type=\"DimmerActuatorState\" LID=\""
				+ deviceId
				+ "\" DmLvl=\""
				+ newValue
				+ "\" /></ActuatorStates></BaseRequest>";

		String response = executeRequest(switchOnRequest, "/cmd");
		if(!response.contains("Result=\"Ok\""))
			logger.warn("Response not ok: "+response);
	}

	/**
	 * Sets the rollershutter to a new value.
	 * 
	 * @param deviceId
	 * @param newValue
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void switchRollerShutter(String deviceId, String newValue)
			throws RWESmarthomeSessionExpiredException {
		String switchOnRequest = "<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"" + FIRMWARE_VERSION + "\" RequestId=\""
				+ requestId
				+ "\" SessionId=\""
				+ getSessionId()
				+ "\" BasedOnConfigVersion=\""
				+ currentConfigurationVersion
				+ "\"><ActuatorStates><LogicalDeviceState xsi:type=\"RollerShutterActuatorState\" LID=\""
				+ deviceId
				+ "\"><ShutterLevel>"
				+ newValue
				+ "</ShutterLevel></LogicalDeviceState></ActuatorStates></BaseRequest>";

		String response = executeRequest(switchOnRequest, "/cmd");
		if(!response.contains("Result=\"Ok\""))
			logger.warn("Response not ok: "+response);
	}
}
