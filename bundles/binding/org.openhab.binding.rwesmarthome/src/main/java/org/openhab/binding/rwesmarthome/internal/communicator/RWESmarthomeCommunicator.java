/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal.communicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
 * @author ollie-dev
 *
 */
public class RWESmarthomeCommunicator {

	private static final Logger logger = LoggerFactory.getLogger(RWESmarthomeCommunicator.class);
	private RWESmarthomeSession rweSmarthomeSession;
	private RWESmarthomeContext context = RWESmarthomeContext.getInstance();
	private long lastEventTime = System.currentTimeMillis();
	
	/**
	 * Stops the communication with the RWE Smarthome Central.
	 */
	public void stop() {
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
			//RWEClient client = new RWEStubClient();
			
			rweSmarthomeSession = new RWESmarthomeSession(client);
			context.setRweSmarthomeSession(rweSmarthomeSession);

			rweSmarthomeSession.logon(context.getConfig().getUsername(), context.getConfig().getPassword(), context.getConfig().getHost());
			logger.info("Login successful.");
			
			rweSmarthomeSession.refreshConfiguration();
			logger.info("Configuration refreshed: " + rweSmarthomeSession.getCurrentConfigurationVersion());
			
			outputFoundDevices();

			rweSmarthomeSession.subscribeForDeviceStateChanges();
			logger.info("Subscribed for devicestate changes.");	
		
			rweSmarthomeSession.subscribeForConfigurationChanges();
			logger.info("Subscribed for configuration changes.");

//			rweSmarthomeSession.refreshPhysicalDevicesState();
//			logger.info("PhysicalDevice states refreshed.");

			lastEventTime = System.currentTimeMillis();
			
		} catch (Exception e) {
			logger.error("Could not start RWE Smarthome communicator: " + e.getMessage(), e);
			stop();
		}
	}

	private void outputFoundDevices() {
		// output the found logical devices

		// Hashmap as helper to get a sorted exampleConfig
		List<String> exampleConfig = new ArrayList<String>();
		
		for (Iterator<LogicalDevice> iterator = rweSmarthomeSession.getLogicalDevices().values().iterator(); iterator.hasNext();) {
			LogicalDevice ld = iterator.next();
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
			
			if("UNKNOWN".equals(validParams)) 
				logger.debug("Found unsupported {} {} ({}/{}).", ld.getType(), ld.getId(), ld.getLocation().getName(), ld.getName());
			else
				logger.info("Found {} {} ({}/{}). Valid params: {}", ld.getType(), ld.getId(), ld.getLocation().getName(), ld.getName(), validParams);
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
			rweSmarthomeSession.refreshLogicalDeviceStates();
			logger.info("LogicalDevice states refreshed.");
		} catch (RWESmarthomeSessionExpiredException e) {
			logger.warn("Session expired!" + e.getMessage());

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
	
		// Exit, if there is no valid session id
		if (!context.getRweSmarthomeSession().isValid())
			return;
		
		
		try {
			rweSmarthomeSession.getNotifications();
			lastEventTime = System.currentTimeMillis();
			
		} catch (LogoutNotificationException e) {
			// restart communicator to get back a valid session
			logger.info("Session expired!" + e.getMessage());
			stop();
			start();
			
		} catch (RWESmarthomeSessionExpiredException e) {
			logger.warn("Session expired!" + e.getMessage());

			try {
				rweSmarthomeSession.logon(context.getConfig().getUsername(), context.getConfig().getPassword(), context.getConfig().getHost());
			} catch (LoginFailedException e1) {
				logger.error("Error loging in: " + e1.getMessage());
			} catch (SHTechnicalException e1) {
				logger.error("Error loging in: " + e1.getMessage());
			}
			logger.info("Login successful.");
			
		} catch (ConfigurationChangedException e) {
			// restart communicator to rebuild everything
			logger.warn("Need to restart RWE Smarthome communicator: " + e.getMessage());
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
			RWESmarthomeBindingConfig config = provider.getBindingFor(itemName);
			try {
				// SWITCH
				if("switch".equals(config.getDeviceParam())) {
					boolean on = command.equals(OnOffType.ON);
					logger.debug("Switching '{}' to '{}'", itemName, command);
					context.getRweSmarthomeSession().switchActuatorChangeState(config.getDeviceId(), on);
					
				// SETTEMPERATURE
				} else if("settemperature".equals(config.getDeviceParam())) {
					logger.debug("Setting temperature for '{}' to '{}'.", itemName, command);
					context.getRweSmarthomeSession().roomTemperatureActuatorChangeState(config.getDeviceId(), command.toString());
				
				// OPERATIONMODEAUTO
				} else if("operationmodeauto".equals(config.getDeviceParam())) {
					boolean opModeAuto = command.equals(OnOffType.ON);
					logger.debug("Setting operationmode for '{}' to '{}'.", itemName, opModeAuto ? RoomTemperatureActuator.OPERATION_MODE_AUTO : RoomTemperatureActuator.OPERATION_MODE_MANUAL);
					context.getRweSmarthomeSession().roomTemperatureActuatorChangeOperationMode(config.getDeviceId(), opModeAuto);
				
				// DIMMER
				} else if("dimmer".equals(config.getDeviceParam()) || "dimmerinverted".equals(config.getDeviceParam())) {
					Integer val = getPercentageValueFromCommand(command, "dimmerinverted".equals(config.getDeviceParam()));
					if(val == null)
						continue;
					
					logger.debug("Setting dimmer '{}' to '{}'.", itemName, val);
					context.getRweSmarthomeSession().switchDimmerState(config.getDeviceId(), val.toString());
				
				// ROLLERSHUTTER
				} else if("rollershutter".equals(config.getDeviceParam()) || "rollershutterinverted".equals(config.getDeviceParam())) {
					Integer val = getPercentageValueFromCommand(command, "rollershutterinverted".equals(config.getDeviceParam()));
					if(val == null)
						continue;

					logger.debug("Setting rollershutter '{}' to '{}'.", itemName, val);
					context.getRweSmarthomeSession().switchRollerShutter(config.getDeviceId(), val.toString());
				
				// ALARM
				} else if("alarm".equals(config.getDeviceParam())) {
					boolean on = command.equals(OnOffType.ON);
					logger.debug("Switching alarm '{}' to '{}'", itemName, command);
					context.getRweSmarthomeSession().alarmActuatorChangeState(config.getDeviceId(), on);
				
				// VARIABLE
				} else if("variable".equals(config.getDeviceParam())) {
					boolean on = command.equals(OnOffType.ON);
					logger.debug("Switching variable '{}' to '{}'", itemName, command);
					context.getRweSmarthomeSession().zustandsVariableChangeState(config.getDeviceId(), on);
			
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
		if(command.equals(OnOffType.ON) || command.equals(UpDownType.DOWN))
			val = 100;
		else if(command.equals(OnOffType.OFF) || command.equals(UpDownType.UP))
			val = 0;
		else if(command instanceof IncreaseDecreaseType || command instanceof StopMoveType) {
			logger.warn("Command '{}' not implemented for dimmer/rollershutter. Must be handled within a rule, see binding documentation.", command.toString());
			return null;
		} else 
			val = new Integer(command.toString());
		
		if(inverted || command instanceof UpDownType) // always invert up/down commands
			val = 100-val;
		return val;
	}
}
