/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm.internal.model;

import org.openhab.binding.dscalarm.DSCAlarmBindingConfig;
import org.openhab.binding.dscalarm.internal.model.DSCAlarmDeviceProperties.StateType;
import org.openhab.binding.dscalarm.internal.protocol.APIMessage;
import org.openhab.binding.dscalarm.internal.DSCAlarmEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.DateTimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Panel represents the basic DSC Alarm System
 * 
 * @author Russell Stephens
 * @since 1.6.0
 */
public class Panel extends DSCAlarmDevice{
	private static final Logger logger = LoggerFactory.getLogger(Panel.class);

	DSCAlarmDeviceProperties panelProperties = new DSCAlarmDeviceProperties();

	/**
	 * Constructor
	 * 
	 * @param panelId
	 */
	public Panel(int panelId) {
		panelProperties.setPanelId(panelId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refreshItem(Item item, DSCAlarmBindingConfig config, EventPublisher publisher) {
		int state;
		String str = "";
		logger.debug("refreshItem(): Panel Item Name: {}", item.getName());

		if(config != null) {
			if(config.getDSCAlarmItemType() != null) {
				switch(config.getDSCAlarmItemType()) {
					case PANEL_CONNECTION:
						state = panelProperties.getSystemConnection();
						publisher.postUpdate(item.getName(), new DecimalType(state));
						break;
					case PANEL_MESSAGE:
						str = panelProperties.getSystemMessage();
						publisher.postUpdate(item.getName(), new StringType(str));
						break;
					case PANEL_SYSTEM_ERROR:
						str = String.format("%03d", panelProperties.getSystemErrorCode()) + ": " + panelProperties.getSystemErrorDescription();
						publisher.postUpdate(item.getName(), new StringType(str));
						break;
					case PANEL_TIME_DATE:
						str = panelProperties.getTimeDate();
						publisher.postUpdate(item.getName(), new StringType(str));
						break;
					case PANEL_COMMAND:
						state = panelProperties.getSystemCommand();
						publisher.postUpdate(item.getName(), new DecimalType(state));
						break;
					default:
						logger.debug("refreshItem(): Panel item not updated.");
						break;
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleEvent(Item item, DSCAlarmBindingConfig config, EventPublisher publisher, DSCAlarmEvent event) {
		APIMessage apiMessage = null;
		int state;

		if(event != null) {
			apiMessage = event.getAPIMessage();
			String str = "";
			logger.debug("handleEvent(): Panel Item Name: {}", item.getName());
	
			if(config != null) {
				if(config.getDSCAlarmItemType() != null) {
					switch(config.getDSCAlarmItemType()) {
						case PANEL_CONNECTION:
							state = panelProperties.getSystemConnection();
							publisher.postUpdate(item.getName(), new DecimalType(state));
							break;
						case PANEL_MESSAGE:
							if(apiMessage != null) {
								str = apiMessage.getAPIDescription();
								panelProperties.setSystemMessage(str);
							}
							publisher.postUpdate(item.getName(), new StringType(str));
							break;
			
						case PANEL_SYSTEM_ERROR:
							panelProperties.setSystemError(1);
							panelProperties.setSystemErrorCode(0);
							int systemErrorCode = 0;
							
							if(apiMessage != null) {
								systemErrorCode = Integer.parseInt(apiMessage.getAPIData());
								panelProperties.setSystemErrorCode(systemErrorCode);
							}
							
							switch(systemErrorCode) {
								case 1:
									panelProperties.setSystemErrorDescription("Receive Buffer Overrun");
									break;
								case 2:
									panelProperties.setSystemErrorDescription("Receive Buffer Overflow");
									break;
								case 3:
									panelProperties.setSystemErrorDescription("Transmit Buffer Overflow");
									break;
								case 10:
									panelProperties.setSystemErrorDescription("Keybus Transmit Buffer Overrun");
									break;
								case 11:
									panelProperties.setSystemErrorDescription("Keybus Transmit Time Timeout");
									break;
								case 12:
									panelProperties.setSystemErrorDescription("Keybus Transmit Mode Timeout");
									break;
								case 13:
									panelProperties.setSystemErrorDescription("Keybus Transmit Keystring Timeout");
									break;
								case 14:
									panelProperties.setSystemErrorDescription("Keybus Interface Not Functioning");
									break;
								case 15:
									panelProperties.setSystemErrorDescription("Keybus Busy - Attempting to Disarm or Arm with user code");
									break;
								case 16:
									panelProperties.setSystemErrorDescription("Keybus Busy – Lockout");
									break;
								case 17:
									panelProperties.setSystemErrorDescription("Keybus Busy – Installers Mode");
									break;
								case 18:
									panelProperties.setSystemErrorDescription("Keybus Busy - General Busy");
									break;
								case 20:
									panelProperties.setSystemErrorDescription("API Command Syntax Error");
									break;
								case 21:
									panelProperties.setSystemErrorDescription("API Command Partition Error - Requested Partition is out of bounds");
									break;
								case 22:
									panelProperties.setSystemErrorDescription("API Command Not Supported");
									break;
								case 23:
									panelProperties.setSystemErrorDescription("API System Not Armed - Sent in response to a disarm command");
									break;
								case 24:
									panelProperties.setSystemErrorDescription("API System Not Ready to Arm - System is either not-secure, in exit-delay, or already armed");
									break;
								case 25:
									panelProperties.setSystemErrorDescription("API Command Invalid Length");
									break;
								case 26:
									panelProperties.setSystemErrorDescription("API User Code not Required");
									break;
								case 27:
									panelProperties.setSystemErrorDescription("API Invalid Characters in Command - No alpha characters are allowed except for checksum");
									break;
								case 28:
									panelProperties.setSystemErrorDescription("API Virtual Keypad is Disabled");
									break;
								case 29:
									panelProperties.setSystemErrorDescription("API Not Valid Parameter");
									break;
								case 30:
									panelProperties.setSystemErrorDescription("API Keypad Does Not Come Out of Blank Mode");
									break;
								case 31:
									panelProperties.setSystemErrorDescription("API IT-100 is Already in Thermostat Menu");
									break;
								case 32:
									panelProperties.setSystemErrorDescription("API IT-100 is NOT in Thermostat Menu");
									break;
								case 33:
									panelProperties.setSystemErrorDescription("API No Response From Thermostat or Escort Module");
									break;
								case 0:
								default:
									panelProperties.setSystemErrorDescription("No Error");
									break;
							}
							str = String.format("%03d", panelProperties.getSystemErrorCode()) + ": " + panelProperties.getSystemErrorDescription();
							publisher.postUpdate(item.getName(), new StringType(str));
							break;
						case PANEL_TIME_DATE:
							if(apiMessage != null) {
								panelProperties.setTimeDate(apiMessage.getAPIData());
								publisher.postUpdate(item.getName(), new DateTimeType(str));
								str = apiMessage.getAPIData();
							}
							break;
						default:
							logger.debug("handleEvent(): Panel item not updated.");
							break;
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateProperties(Item item, DSCAlarmBindingConfig config, int state, String description) {
		logger.debug("updateProperties(): Panel Item Name: {}", item.getName());
		if(config != null) {
			if(config.getDSCAlarmItemType() != null) {
				switch(config.getDSCAlarmItemType()) {
					case PANEL_CONNECTION:
						panelProperties.setState(StateType.CONNECTION_STATE, state, description);
						break;
					case PANEL_MESSAGE:
						panelProperties.setSystemMessage(description);
						break;
					case PANEL_COMMAND:
						panelProperties.setSystemCommand(state);
						break;
					/*case PANEL_TIME_DATE:
						panelProperties.setState(StateType.TIME_DATE, state, description);
						logger.debug("updateProperties(): Panel property updated: {}", item.getName());
						break;*/
					default: 
						logger.debug("updateProperties(): Panel property not updated.");
						break;
				}
			}
		}
	}
}
