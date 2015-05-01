/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm.internal.model;

import org.openhab.binding.dscalarm.DSCAlarmBindingConfig;
import org.openhab.binding.dscalarm.internal.model.DSCAlarmDeviceProperties.StateType;
import org.openhab.binding.dscalarm.internal.model.DSCAlarmDeviceProperties.TriggerType;
import org.openhab.binding.dscalarm.internal.protocol.APIMessage;
import org.openhab.binding.dscalarm.internal.DSCAlarmEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
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

	public DSCAlarmDeviceProperties panelProperties = new DSCAlarmDeviceProperties();

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
		logger.debug("refreshItem(): Panel Item Name: {}", item.getName());

		int state;
		String str = "";
		boolean trigger;
		boolean boolState;
		OnOffType onOffType;

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
					case PANEL_TIME:
						str = panelProperties.getSystemTime();
						publisher.postUpdate(item.getName(), new DateTimeType(str));
						break;
					case PANEL_TIME_STAMP:
						boolState = panelProperties.getSystemTimeStamp();
						onOffType = boolState ? OnOffType.ON : OnOffType.OFF;
						publisher.postUpdate(item.getName(), onOffType);
						break;
					case PANEL_TIME_BROADCAST:
						boolState = panelProperties.getSystemTimeBroadcast();
						onOffType = boolState ? OnOffType.ON : OnOffType.OFF;
						publisher.postUpdate(item.getName(), onOffType);
						break;
					case PANEL_COMMAND:
						state = panelProperties.getSystemCommand();
						publisher.postUpdate(item.getName(), new DecimalType(state));
						break;
					case PANEL_FIRE_KEY_ALARM:
						trigger = panelProperties.getTrigger(TriggerType.FIRE_KEY_ALARM);
						onOffType = trigger ? OnOffType.ON : OnOffType.OFF;
						publisher.postUpdate(item.getName(), onOffType);
						break;
					case PANEL_PANIC_KEY_ALARM:
						trigger = panelProperties.getTrigger(TriggerType.PANIC_KEY_ALARM);
						onOffType = trigger ? OnOffType.ON : OnOffType.OFF;
						publisher.postUpdate(item.getName(), onOffType);
						break;
					case PANEL_AUX_KEY_ALARM:
						trigger = panelProperties.getTrigger(TriggerType.AUX_KEY_ALARM);
						onOffType = trigger ? OnOffType.ON : OnOffType.OFF;
						publisher.postUpdate(item.getName(), onOffType);
						break;
					case PANEL_AUX_INPUT_ALARM:
						trigger = panelProperties.getTrigger(TriggerType.AUX_KEY_ALARM);
						onOffType = trigger ? OnOffType.ON : OnOffType.OFF;
						publisher.postUpdate(item.getName(), onOffType);
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
								panelProperties.setSystemErrorDescription(apiMessage.getError());
								str = String.format("%03d", panelProperties.getSystemErrorCode()) + ": " + panelProperties.getSystemErrorDescription();
								publisher.postUpdate(item.getName(), new StringType(str));
							}
							break;
						case PANEL_TIME:
							if(apiMessage != null) {
								panelProperties.setSystemTime(apiMessage.getAPIData());
								str = panelProperties.getSystemTime();
								publisher.postUpdate(item.getName(), new DateTimeType(str));
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
		
		boolean trigger = state != 0 ? true : false;
		boolean boolState = state != 0 ? true : false;

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
					case PANEL_FIRE_KEY_ALARM:
						panelProperties.setTrigger(TriggerType.FIRE_KEY_ALARM, trigger);
						break;
					case PANEL_PANIC_KEY_ALARM:
						panelProperties.setTrigger(TriggerType.PANIC_KEY_ALARM, trigger);
						break;
					case PANEL_AUX_KEY_ALARM:
						panelProperties.setTrigger(TriggerType.AUX_KEY_ALARM, trigger);
						break;
					case PANEL_AUX_INPUT_ALARM:
						panelProperties.setTrigger(TriggerType.AUX_INPUT_ALARM, trigger);
						break;
					case PANEL_TIME:
						panelProperties.setSystemTime(description);
						break;
					case PANEL_TIME_STAMP:
						panelProperties.setSystemTimeStamp(boolState);
						break;
					case PANEL_TIME_BROADCAST:
						panelProperties.setSystemTimeBroadcast(boolState);
						break;
					default: 
						logger.debug("updateProperties(): Panel property not updated.");
						break;
				}
			}
		}
	}
}
