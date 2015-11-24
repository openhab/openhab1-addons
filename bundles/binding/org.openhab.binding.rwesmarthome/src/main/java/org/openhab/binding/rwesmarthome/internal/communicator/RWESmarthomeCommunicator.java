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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.rwesmarthome.RWESmarthomeBindingProvider;
import org.openhab.binding.rwesmarthome.internal.RWESmarthomeContext;
import org.openhab.binding.rwesmarthome.internal.RWESmarthomeGenericBindingProvider.RWESmarthomeBindingConfig;
import org.openhab.binding.rwesmarthome.internal.communicator.client.RWEClient;
import org.openhab.binding.rwesmarthome.internal.communicator.client.RWEHTTPClient;
import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.ConfigurationChangedException;
import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.LoginFailedException;
import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.LogoutNotificationException;
import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.RWESmarthomeSessionExpiredException;
import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.SHTechnicalException;
import org.openhab.binding.rwesmarthome.internal.communicator.xmlresponse.GetAllLogicalDeviceStatesXMLResponse;
import org.openhab.binding.rwesmarthome.internal.communicator.xmlresponse.GetEntitiesXMLResponse;
import org.openhab.binding.rwesmarthome.internal.communicator.xmlresponse.NotificationsXMLResponse;
import org.openhab.binding.rwesmarthome.internal.model.LogicalDevice;
import org.openhab.binding.rwesmarthome.internal.model.RoomTemperatureActuator;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The communicator to communicate with the RWE Smarthome Central.
 * 
 * @author ollie-dev
 *
 */
public class RWESmarthomeCommunicator {

	private static final Logger logger = LoggerFactory.getLogger(RWESmarthomeCommunicator.class);
	private RWESmarthomeSession rweSmarthomeSession;
	private RWESmarthomeContext context = RWESmarthomeContext.getInstance();
	private long lastEventTime = System.currentTimeMillis();
	private boolean runPoller = false;
	
	/**
	 * Stops the communication with the RWE Smarthome Central.
	 */
	public void stop() {
		runPoller = false;
		
		if(rweSmarthomeSession != null) {
			logger.info("Shutting down RWE Smarthome communicator");
			try {
				rweSmarthomeSession.destroy();
			} finally {
				rweSmarthomeSession = null;
			}
		}
	}

	/**
	 * Returns the timestamp from the last RweSmarthome server event.
	 */
	public long getLastEventTime() {
		return lastEventTime;
	}

	/**
	 * Starts the communication session with the RWE Smarthome Central.
	 */
	public void start() {
		logger.info("Starting RWE Smarthome communicator");
		try {
			RWEClient client = new RWEHTTPClient();
			
			rweSmarthomeSession = new RWESmarthomeSession(client);
			context.setRweSmarthomeSession(rweSmarthomeSession);

			rweSmarthomeSession.logon(context.getConfig().getUsername(), context.getConfig().getPassword(), context.getConfig().getHost());
			logger.info("Login successful.");
			
			refreshConfiguration();
			logger.info("Configuration refreshed: " + rweSmarthomeSession.getCurrentConfigurationVersion());
			
			logFoundDevices();

			runPoller = true;
			
			subscribeForDeviceStateChanges();
			logger.info("Subscribed for devicestate changes.");	
		
			subscribeForConfigurationChanges();
			logger.info("Subscribed for configuration changes.");

			lastEventTime = System.currentTimeMillis();
			
			this.context.setBindingChanged(true);
			
		} catch (Exception e) {
			logger.error("Could not start RWE Smarthome communicator: " + e.getMessage(), e);
			stop();
		}
	}

	/**
	 * Writes the list of the found devices to the OpenHAB log.
	 */
	private void logFoundDevices() {
		// Hashmap as helper to get a sorted exampleConfig
		List<String> exampleConfig = new ArrayList<String>();
		
		for (LogicalDevice ld : rweSmarthomeSession.getLogicalDevices().values()) {
			String validParams = "";
			
			if(LogicalDevice.Type_RoomTemperatureActuator.equals(ld.getType())) {
				validParams = "settemperature, operationmodeauto";
				exampleConfig.add("Number\trweSettemp" + ld.getId().substring(30) + "\t\"Solltemp " + ld.getLocation().getName() + " [%.1f °C]\"\t<temperature>\t(rwe) {" + context.getBindingType() + "=\"id=" + ld.getId() + ",param=settemperature\"}\n");
				exampleConfig.add("Switch\trweSettempOpMode" + ld.getId().substring(30) + "\t\"Solltemp Automode " + ld.getLocation().getName() + "\"\t<temperature>\t(rwe) {" + context.getBindingType() + "=\"id=" + ld.getId() + ",param=operationmodeauto\"}\n");

			} else if(LogicalDevice.Type_RoomTemperatureSensor.equals(ld.getType())) {
				validParams = "temperature";
				exampleConfig.add("Number\trweTemp" + ld.getId().substring(30) + "\t\t\"Temp " + ld.getLocation().getName() + " [%.1f °C]\"\t<temperature>\t(rwe) {" + context.getBindingType() + "=\"id=" + ld.getId() + ",param=temperature\"}\n");
			
			} else if(LogicalDevice.Type_SwitchActuator.equals(ld.getType())) {
				validParams = "switch";
				exampleConfig.add("Switch\trweSwitch" + ld.getId().substring(30) + "\t\t\"Schalter " + ld.getLocation().getName() + "\"\t<switch>\t(rwe) {" + context.getBindingType() + "=\"id=" + ld.getId() + ",param=switch\"}\n");
			
			} else if(LogicalDevice.Type_WindowDoorSensor.equals(ld.getType())) {
				validParams = "contact";
				exampleConfig.add("Contact\trweContact" + ld.getId().substring(30) + "\t\"Fenster/Tür " + ld.getLocation().getName() + " [MAP(de.map):%s]\"\t<contact>\t(rwe) {" + context.getBindingType() + "=\"id=" + ld.getId() + ",param=contact\"}\n");
			
			} else if(LogicalDevice.Type_LuminanceSensor.equals(ld.getType())) {
				validParams = "luminance";
				exampleConfig.add("Number\trweLuminance" + ld.getId().substring(30) + "\t\"Helligkeit " + ld.getLocation().getName() + " [%d %%]\"\t<slider>\t(rwe) {" + context.getBindingType() + "=\"id=" + ld.getId() + ",param=luminance\"}\n");
			
			} else if(LogicalDevice.Type_RoomHumiditySensor.equals(ld.getType())) {
				validParams = "humidity";
				exampleConfig.add("Number\trweHumidity" + ld.getId().substring(30) + "\t\"Feuchtigkeit " + ld.getLocation().getName() + " [%.1f %%]\"\t<temperature>\t(rwe) {" + context.getBindingType() + "=\"id=" + ld.getId() + ",param=humidity\"}\n");
			
			} else if(LogicalDevice.Type_RollerShutterActuator.equals(ld.getType())) {
				validParams = "rollershutter, rollershutterinverted";
				exampleConfig.add("Rollershutter\trweRollershutter" + ld.getId().substring(30) + "\t\"Rollo " + ld.getLocation().getName() + " [%d %%]\"\t<rollershutter>\t(rwe) {" + context.getBindingType() + "=\"id=" + ld.getId() + ",param=rollershutter\"}\n");
			
			} else if(LogicalDevice.Type_SmokeDetectorSensor.equals(ld.getType())) {
				validParams = "smokedetector";
				exampleConfig.add("Switch\trweSmokeDetector" + ld.getId().substring(30) + "\t\"Rauchmelder " + ld.getLocation().getName() + "\"\t<fire>\t(rwe) {" + context.getBindingType() + "=\"id=" + ld.getId() + ",param=smokedetector\"}\n");
			
			} else if(LogicalDevice.Type_AlarmActuator.equals(ld.getType())) {
				validParams = "alarm";
				exampleConfig.add("Switch\trweAlarm" + ld.getId().substring(30) + "\t\t\"Alarm " + ld.getLocation().getName() + "\"\t<siren>\t(rwe) {" + context.getBindingType() + "=\"id=" + ld.getId() + ",param=alarm\"}\n");
			
			} else if(LogicalDevice.Type_DimmerActuator.equals(ld.getType())) {
				validParams = "dimmer, dimmerinverted";
				exampleConfig.add("Dimmer\trweDimmer" + ld.getId().substring(30) + "\t\"Dimmer " + ld.getLocation().getName() + " [%d %%]\"\t<slider>\t(rwe) {" + context.getBindingType() + "=\"id=" + ld.getId() + ",param=dimmer\"}\n");
			
			} else if(LogicalDevice.Type_GenericActuator_Value.equals(ld.getType())) {
				validParams = "variable";
				exampleConfig.add("Switch\trweVariable" + ld.getId().substring(30) + "\t\"Variable " + ld.getName() + "\"\t<switch>\t(rwe) {" + context.getBindingType() + "=\"id=" + ld.getId() + ",param=variable\"}\n");

			} else if(LogicalDevice.Type_ThermostatActuator.equals(ld.getType())) {
				continue; // ignore
			} else {
				validParams = "UNKNOWN";
			}
			
			if("UNKNOWN".equals(validParams)) {
				logger.debug("Found unsupported {} {} ({}/{}).", ld.getType(), ld.getId(), ld.getLocation().getName(), ld.getName());
			} else {
				logger.info("Found {} {} ({}/{}). Valid params: {}", ld.getType(), ld.getId(), ld.getLocation().getName(), ld.getName(), validParams);
			}
		}
		
		Collections.sort(exampleConfig);
		exampleConfig.add(0, "Group rwe\n");
		logger.info("Example configuration for RWE Smarthome items:\n" + StringUtils.join(exampleConfig, ""));
	}
	
	/**
	 * Returns true, if the communicator is already running and false, if not.
	 * 
	 * @return boolean
	 */
	public boolean isRunning() {
		return rweSmarthomeSession == null;
	}
	
	/**
	 * load the state of the logical devices from RWE Smarthome SHC.
	 * 
	 * This is only necessary, when the RWE binding is started and the states unknown or
	 * if the binding configuration has changed. Standard device state changes are
	 * propagated using poll().
	 */
	public void loadDeviceStates() {
		try {
			refreshLogicalDeviceStates();
			logger.info("LogicalDevice states refreshed.");
		} catch (RWESmarthomeSessionExpiredException e) {
			logger.info("Session expired!" + e.getMessage());

			try {
				rweSmarthomeSession.logon(context.getConfig().getUsername(), context.getConfig().getPassword(), context.getConfig().getHost());
				logger.info("Login successful.");
				
				// trigger refresh again as we had to login again...
				context.setBindingChanged(true);
			} catch (Exception e1) {
				logger.error("Could not recover session with RWE Smarthome: " + e1.getMessage(), e1);
				stop();
			}
		}
	}

	/**
	 * Polls for notifications and handles
	 */
	public void poll() {
		if(!runPoller) {
			return;
		}
	
		// Exit, if there is no valid session id
		if (!context.getRweSmarthomeSession().isValid()) {
			logger.debug("Invalid session, getNotifications aborted.");
			return;
		}
		
		try {
			getNotifications();
			lastEventTime = System.currentTimeMillis();
			
		} catch (LogoutNotificationException e) {
			// restart communicator to get back a valid session
			logger.info("Session expired!" + e.getMessage());
			stop();
			start();
			
		} catch (RWESmarthomeSessionExpiredException e) {
			logger.info("Session expired!" + e.getMessage());

			try {
				rweSmarthomeSession.logon(context.getConfig().getUsername(), context.getConfig().getPassword(), context.getConfig().getHost());
			} catch (LoginFailedException e1) {
				logger.error("Error logging in with user'" + context.getConfig().getUsername()+ "' to host '" + context.getConfig().getHost() + "': " + e1.getMessage());
			} catch (SHTechnicalException e1) {
				logger.error("Error logging in with user'" + context.getConfig().getUsername()+ "' to host '" + context.getConfig().getHost() + "': " + e1.getMessage());
			}
			logger.info("Login successful.");
			
		} catch (ConfigurationChangedException e) {
			// restart communicator to rebuild everything
			logger.info("Need to restart RWE Smarthome communicator: " + e.getMessage());
			stop();
			start();
		}
		
	}

	/**
	 * Sends a command to an item
	 * 
	 * @param itemName
	 * @param command
	 */
	public void sendCommand(String itemName, Command command) {
		for (RWESmarthomeBindingProvider provider : context.getProviders()) {
			RWESmarthomeBindingConfig config = provider.getBindingConfigFor(itemName);
			try {
				// SWITCH
				if("switch".equals(config.getDeviceParam())) {
					boolean on = command.equals(OnOffType.ON);
					logger.debug("Switching '{}' to '{}'", itemName, command);
					setSwitchActuatorState(config.getDeviceId(), on);
					
				// SETTEMPERATURE
				} else if("settemperature".equals(config.getDeviceParam())) {
					logger.debug("Setting temperature for '{}' to '{}'.", itemName, command);
					setTemperatureActuatorState(config.getDeviceId(), command.toString());
				
				// OPERATIONMODEAUTO
				} else if("operationmodeauto".equals(config.getDeviceParam())) {
					boolean opModeAuto = command.equals(OnOffType.ON);
					logger.debug("Setting operationmode for '{}' to '{}'.", itemName, opModeAuto ? RoomTemperatureActuator.OPERATION_MODE_AUTO : RoomTemperatureActuator.OPERATION_MODE_MANUAL);
					setRoomTemperatureActuatorOperationModeToAuto(config.getDeviceId(), opModeAuto);
				
				// DIMMER
				} else if("dimmer".equals(config.getDeviceParam()) || "dimmerinverted".equals(config.getDeviceParam())) {
					Integer val = getPercentageValueFromCommand(command, "dimmerinverted".equals(config.getDeviceParam()));
					if(val == null) {
						continue;
					}
					
					logger.debug("Setting dimmer '{}' to '{}'.", itemName, val);
					setDimmerState(config.getDeviceId(), val.toString());
				
				// ROLLERSHUTTER
				} else if("rollershutter".equals(config.getDeviceParam()) || "rollershutterinverted".equals(config.getDeviceParam())) {
					Integer val = getPercentageValueFromCommand(command, "rollershutterinverted".equals(config.getDeviceParam()));
					if(val == null) {
						continue;
					}
					
					logger.debug("Setting rollershutter '{}' to '{}'.", itemName, val);
					setRollershutterState(config.getDeviceId(), val.toString());
				
				// ALARM
				} else if("alarm".equals(config.getDeviceParam())) {
					boolean on = command.equals(OnOffType.ON);
					logger.debug("Switching alarm '{}' to '{}'", itemName, command);
					setAlarmActuatorState(config.getDeviceId(), on);
				
				// VARIABLE
				} else if("variable".equals(config.getDeviceParam())) {
					boolean on = command.equals(OnOffType.ON);
					logger.debug("Switching variable '{}' to '{}'", itemName, command);
					setVariableState(config.getDeviceId(), on);
			
				}								
			} catch (RWESmarthomeSessionExpiredException e) {
				logger.error("Error handling command", e);
			}
		}
	}

	/**
	 * Returns a percentage value as integer for a given command.
	 * 
	 * Command types increase/decrease are not supported and lead to the return of null.
	 * If inverted is true, the returned value will be inverted (e.g. 40% -> 60%).
	 * 
	 * @param command
	 * @param inverted
	 * @return integer or null
	 */
	private Integer getPercentageValueFromCommand(Command command, boolean inverted) {
		Integer val;
		if(command.equals(OnOffType.ON) || command.equals(UpDownType.DOWN)) {
			val = 100;
		} else if(command.equals(OnOffType.OFF) || command.equals(UpDownType.UP)) {
			val = 0;
		} else if(command instanceof IncreaseDecreaseType || command instanceof StopMoveType) {
			logger.info("Command '{}' not implemented for dimmer/rollershutter. Must be handled within a rule, see binding documentation.", command.toString());
			return null;
		} else { 
			val = new Integer(command.toString());
		}
		
		if(inverted || command instanceof UpDownType) { // always invert up/down commands
			val = 100-val;
		}
		return val;
	}
	
	/**
	 * Subscribes for notifications.
	 * 
	 * Should be called once before getNotifications(). Times out after a couple of hours. So make sure to
	 * test against LogoutNotification and resubscribe.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForNotifications(String notificationType) throws RWESmarthomeSessionExpiredException {
		String sResponse = "";
		final String NOTIFICATION_REQUEST = String.format(
			"<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"NotificationRequest\" Version=\"%s\" RequestId=\"%s\" SessionId=\"%s\">" +
			"<Action>Subscribe</Action>" +
			"<NotificationType>%s</NotificationType>" +
			"</BaseRequest>",
			RWESmarthomeSession.getFirmwareVersion(),
			rweSmarthomeSession.getRequestId(),
			rweSmarthomeSession.getSessionId(),
			notificationType
		);
		
		logger.debug("REQ: " + NOTIFICATION_REQUEST);
		sResponse = rweSmarthomeSession.executeRequest(NOTIFICATION_REQUEST, "/cmd");
		logger.debug("SubscribeForNotification-Response: " + sResponse);
	}
	
	/**
	 * Subscribes for configuration change notifications.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForConfigurationChanges() throws RWESmarthomeSessionExpiredException {
		subscribeForNotifications("ConfigurationChanges");
	}
	
	/**
	 * Subscribes for calibration notifications.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForCalibration() throws RWESmarthomeSessionExpiredException {
		subscribeForNotifications("Calibration");
	}
	
	/**
	 * Subscribes for custom application notifications.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForCustomApplication() throws RWESmarthomeSessionExpiredException {
		subscribeForNotifications("CustomApplication");
	}
	
	/**
	 * Subscribes for device state changes.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForDeviceStateChanges() throws RWESmarthomeSessionExpiredException {
		subscribeForNotifications("DeviceStateChanges");
	}
	
	/**
	 * Subscribes for deployment changes.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForDeploymentChanges() throws RWESmarthomeSessionExpiredException {
		subscribeForNotifications("DeploymentChanges");
	}
	
	/**
	 * Subscribes for message updates.
	 * 
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void subscribeForMessageUpdates() throws RWESmarthomeSessionExpiredException {
		subscribeForNotifications("MessageUpdates");
	}
	
	/**
	 * Refresh configuration.
	 * 
	 * @return the RWE Smarthome configuration XML response
	 * @throws RWESmarthomeSessionExpiredException
	 *             the smart home session expired exception
	 */
	public String refreshConfiguration() throws RWESmarthomeSessionExpiredException {

		String sResponse = "";
		final String GET_CONFIGURATION_REQUEST = String.format(
			"<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"GetEntitiesRequest\" Version=\"%s\" RequestId=\"%s\" SessionId=\"%s\">\n" +
			"<EntityType>Configuration</EntityType>" +
			"</BaseRequest>", 
			RWESmarthomeSession.getFirmwareVersion(),
			rweSmarthomeSession.getRequestId(),
			rweSmarthomeSession.getSessionId()
		);
		sResponse = rweSmarthomeSession.executeRequest(GET_CONFIGURATION_REQUEST, "/cmd");

		try {
			GetEntitiesXMLResponse entitiesXMLRes = new GetEntitiesXMLResponse(IOUtils.toInputStream(sResponse, "UTF8"));
			rweSmarthomeSession.setLocations(entitiesXMLRes.getLocations());
			rweSmarthomeSession.setLogicalDevices(entitiesXMLRes.getLogicalDevices());
			rweSmarthomeSession.setCurrentConfigurationVersion(entitiesXMLRes.getConfigurationVersion());
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

		String sResponse = "";
		final String GET_ALL_LOGICAL_DEVICE_STATES_REQUEST = String.format(
			"<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"GetAllLogicalDeviceStatesRequest\" Version=\"%s\" RequestId=\"%s\" SessionId=\"%s\" BasedOnConfigVersion=\"%s\" />",
			RWESmarthomeSession.getFirmwareVersion(),
			rweSmarthomeSession.getRequestId(),
			rweSmarthomeSession.getSessionId(),
			rweSmarthomeSession.getCurrentConfigurationVersion()
		);
		sResponse = rweSmarthomeSession.executeRequest(GET_ALL_LOGICAL_DEVICE_STATES_REQUEST, "/cmd");

		try {
			GetAllLogicalDeviceStatesXMLResponse entitiesXMLRes = new GetAllLogicalDeviceStatesXMLResponse(IOUtils.toInputStream(sResponse, "UTF8"));
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
			
			logger.trace("Notifications: {}", sResponse);
			
			return sResponse;
	}
	
	
	/**
	 * Sends the getNotificationsRequest.
	 * 
	 * @return the XML response
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public String getNotificationsRequest() throws RWESmarthomeSessionExpiredException {
		return rweSmarthomeSession.executeRequest("upd", "/upd");
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
	public void setSwitchActuatorState(String deviceId, boolean on)
			throws RWESmarthomeSessionExpiredException {

		final String SWITCH_ON_REQUEST = String.format(
			"<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"%s\" RequestId=\"%s\" SessionId=\"%s\" BasedOnConfigVersion=\"%s\">" +
			"<ActuatorStates>" +
			"<LogicalDeviceState xsi:type=\"SwitchActuatorState\" LID=\"%s\" IsOn=\"%s\" />" +
			"</ActuatorStates>" +
			"</BaseRequest>",
			RWESmarthomeSession.getFirmwareVersion(),
			rweSmarthomeSession.getRequestId(),
			rweSmarthomeSession.getSessionId(),
			rweSmarthomeSession.getCurrentConfigurationVersion(),
			deviceId, 
			on
		);
		
		String response = rweSmarthomeSession.executeRequest(SWITCH_ON_REQUEST, "/cmd");
		if(!response.contains("Result=\"Ok\"")) {
			logger.warn("Response not ok: "+response);
		}
	}
	
	/**
	 * Changes the temperature of a RoomTemperatureActuator
	 * 
	 * @param deviceId
	 * @param temperature
	 * @throws RWESmarthomeSessionExpiredException 
	 */
	public void setTemperatureActuatorState(String deviceId, String temperature) throws RWESmarthomeSessionExpiredException {

		final String TEMPERATURE_CHANGE_REQUEST = String.format(
			"<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"%s\" RequestId=\"%s\" SessionId=\"%s\" BasedOnConfigVersion=\"%s\">" +
			"<ActuatorStates>" +
			"<LogicalDeviceState xsi:type=\"RoomTemperatureActuatorState\" LID=\"%s\" PtTmp=\"%s\" OpnMd=\"Auto\" WRAc=\"False\" />" +
			"</ActuatorStates></BaseRequest>",
			RWESmarthomeSession.getFirmwareVersion(),
			rweSmarthomeSession.getRequestId(),
			rweSmarthomeSession.getSessionId(),
			rweSmarthomeSession.getCurrentConfigurationVersion(),
			deviceId,
			temperature
		);

		String response = rweSmarthomeSession.executeRequest(TEMPERATURE_CHANGE_REQUEST, "/cmd");
		if(!response.contains("Result=\"Ok\"")) {
			logger.warn("Response not ok: "+response);
		}
	}

	/**
	 * Set the operation mode of a TemperaturActuator to "Auto" if true, "Manu" if false.
	 * 
	 * @param deviceId
	 * @param operationModeAuto
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void setRoomTemperatureActuatorOperationModeToAuto(String deviceId, Boolean operationModeAuto) throws RWESmarthomeSessionExpiredException {

		final String OPERATION_MODE_CHANGE_REQUEST = String.format(
			"<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"%s\" RequestId=\"%s\" SessionId=\"%s\" BasedOnConfigVersion=\"%s\">" +
			"<ActuatorStates>" +
			"<LogicalDeviceState xsi:type=\"RoomTemperatureActuatorState\" LID=\"%s\" OpnMd=\"%s\" />" +
			"</ActuatorStates>" +
			"</BaseRequest>", 
			RWESmarthomeSession.getFirmwareVersion(),
			rweSmarthomeSession.getRequestId(),
			rweSmarthomeSession.getSessionId(),
			rweSmarthomeSession.getCurrentConfigurationVersion(),
			deviceId, 
			(operationModeAuto ? RoomTemperatureActuator.OPERATION_MODE_AUTO : RoomTemperatureActuator.OPERATION_MODE_MANUAL)
		);

		String response = rweSmarthomeSession.executeRequest(OPERATION_MODE_CHANGE_REQUEST, "/cmd");
		if(!response.contains("Result=\"Ok\"")) {
			logger.warn("Response not ok: " + response);
		}
	}

	/**
	 * Changes the alarm state
	 * 
	 * @param deviceId
	 * @param on
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void setAlarmActuatorState(String deviceId, boolean on) throws RWESmarthomeSessionExpiredException {

		final String CHANGE_ALARM_ACTUATOR_CHANGE_REQUEST = String.format(
			"<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"%s\" RequestId=\"%s\" SessionId=\"%s\" BasedOnConfigVersion=\"%s\">" +
			"<ActuatorStates>" +
			"<LogicalDeviceState xsi:type=\"AlarmActuatorState\" LID=\"%s\">" +
			"<IsOn>%s</IsOn>" +
			"</LogicalDeviceState>" +
			"</ActuatorStates>" +
			"</BaseRequest>",
			RWESmarthomeSession.getFirmwareVersion(),
			rweSmarthomeSession.getRequestId(),
			rweSmarthomeSession.getSessionId(),
			rweSmarthomeSession.getCurrentConfigurationVersion(),
			deviceId, 
			on
		);

		String response = rweSmarthomeSession.executeRequest(CHANGE_ALARM_ACTUATOR_CHANGE_REQUEST, "/cmd");
		if(!response.contains("Result=\"Ok\"")) {
			logger.warn("Response not ok: " + response);
		}
	}

	/**
	 * Change the state of a variable.
	 * 
	 * @param deviceId
	 * @param on
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void setVariableState(String deviceId, boolean on) throws RWESmarthomeSessionExpiredException {
		final String CHANGE_VARIABLE_STATE_REQUEST = String.format(
			"<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"%s\" RequestId=\"%s\" SessionId=\"%s\" BasedOnConfigVersion=\"%s\">" +
			"<ActuatorStates>" +
			"<LogicalDeviceState xsi:type=\"GenericDeviceState\" LID=\"%s\">" +
			"<Ppts>" +
			"<Ppt xsi:type=\"BooleanProperty\" Name=\"Value\" Value=\"%s\" />" +
			"</Ppts>" +
			"</LogicalDeviceState>" +
			"</ActuatorStates>" +
			"</BaseRequest>",
			RWESmarthomeSession.getFirmwareVersion(),
			rweSmarthomeSession.getRequestId(),
			rweSmarthomeSession.getSessionId(),
			rweSmarthomeSession.getCurrentConfigurationVersion(),
			deviceId, 
			on
		);

		String response = rweSmarthomeSession.executeRequest(CHANGE_VARIABLE_STATE_REQUEST, "/cmd");
		if(!response.contains("Result=\"Ok\"")) {
			logger.warn("Response not ok: "+response);
		}
	}

	/**
	 * Sets a dimmer to a new value.
	 * 
	 * @param deviceId
	 * @param newValue
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void setDimmerState(String deviceId, String newValue)
			throws RWESmarthomeSessionExpiredException {
		final String SWITCH_DIMMER_STATE_REQUEST = String.format(
			"<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"%s\" RequestId=\"%s\" SessionId=\"%s\" BasedOnConfigVersion=\"%s\">" +
			"<ActuatorStates>" +
			"<LogicalDeviceState xsi:type=\"DimmerActuatorState\" LID=\"%s\" DmLvl=\"%s\" />" +
			"</ActuatorStates>" +
			"</BaseRequest>",
			RWESmarthomeSession.getFirmwareVersion(),
			rweSmarthomeSession.getRequestId(),
			rweSmarthomeSession.getSessionId(),
			rweSmarthomeSession.getCurrentConfigurationVersion(),
			deviceId, 
			newValue
		);

		String response = rweSmarthomeSession.executeRequest(SWITCH_DIMMER_STATE_REQUEST, "/cmd");
		if(!response.contains("Result=\"Ok\"")) {
			logger.warn("Response not ok: " + response);
		}
	}

	/**
	 * Sets the rollershutter to a new value.
	 * 
	 * @param deviceId
	 * @param newValue
	 * @throws RWESmarthomeSessionExpiredException
	 */
	public void setRollershutterState(String deviceId, String newValue)
			throws RWESmarthomeSessionExpiredException {
		final String CHANGE_ROLLERSHUTTER_STATE_REQUEST = String.format(
			"<BaseRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SetActuatorStatesRequest\" Version=\"%s\" RequestId=\"%s\" SessionId=\"%s\" BasedOnConfigVersion=\"%s\">" +
			"<ActuatorStates>" +
			"<LogicalDeviceState xsi:type=\"RollerShutterActuatorState\" LID=\"%s\">" +
			"<ShutterLevel>%s</ShutterLevel>" +
			"</LogicalDeviceState>" +
			"</ActuatorStates>" +
			"</BaseRequest>",
			RWESmarthomeSession.getFirmwareVersion(),
			rweSmarthomeSession.getRequestId(),
			rweSmarthomeSession.getSessionId(),
			rweSmarthomeSession.getCurrentConfigurationVersion(),
			deviceId, 
			newValue
		);

		String response = rweSmarthomeSession.executeRequest(CHANGE_ROLLERSHUTTER_STATE_REQUEST, "/cmd");
		if(!response.contains("Result=\"Ok\"")) {
			logger.warn("Response not ok: " + response);
		}
	}
}
