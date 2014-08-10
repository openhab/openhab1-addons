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
import org.openhab.binding.dscalarm.internal.DSCAlarmEvent;
import org.openhab.binding.dscalarm.internal.model.DSCAlarmDeviceProperties.StateType;
import org.openhab.binding.dscalarm.internal.protocol.APIMessage;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OpenClosedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Zone represents a physical device such as a door, window, or motion sensor.
 * @author Russell Stephens
 * @since 1.6.0
 */
public class Zone extends DSCAlarmDevice{
	private static final Logger logger = LoggerFactory.getLogger(Zone.class);

	DSCAlarmDeviceProperties zoneProperties = new DSCAlarmDeviceProperties();
	
	public Zone(int partitionId, int zoneId) {
		if(partitionId >= 1 && partitionId <= 8)
			zoneProperties.setPartitionId(partitionId);
		
		zoneProperties.setZoneId(zoneId);
	}
	
	@Override
	public void refreshItem(Item item, DSCAlarmBindingConfig config, EventPublisher publisher) {
		int state;
		String strStatus = "";
		logger.debug("refreshItem(): Zone Item Name: {}", item.getName());

		if(config != null) {
			if(config.getDSCAlarmItemType() != null) {
				switch(config.getDSCAlarmItemType()) {
					case ZONE_ALARM_STATUS:
						state = zoneProperties.getState(StateType.ALARM_STATE);
						strStatus = zoneProperties.getStateDescription(StateType.ALARM_STATE);
						publisher.postUpdate(item.getName(), new StringType(strStatus));
						break;
					case ZONE_TAMPER_STATUS:
						state = zoneProperties.getState(StateType.TAMPER_STATE);
						strStatus = zoneProperties.getStateDescription(StateType.TAMPER_STATE);
						publisher.postUpdate(item.getName(), new StringType(strStatus));
						break;
					case ZONE_FAULT_STATUS:
						state = zoneProperties.getState(StateType.FAULT_STATE);
						strStatus = zoneProperties.getStateDescription(StateType.FAULT_STATE);
						publisher.postUpdate(item.getName(), new StringType(strStatus));
						break;
					case ZONE_GENERAL_STATUS:
						state = zoneProperties.getState(StateType.GENERAL_STATE);
						publisher.postUpdate(item.getName(), state == 1 ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
						break;
					case ZONE_BYPASS_MODE:
						state = zoneProperties.getState(StateType.ARM_STATE);
						publisher.postUpdate(item.getName(), new DecimalType(state));
						break;
					default: 
						logger.debug("refreshItem(): Zone item not updated.");
						break;
				}
			}
		}
		
	}
	
	@Override
	public void handleEvent(Item item, DSCAlarmBindingConfig config, EventPublisher publisher, DSCAlarmEvent event) {
		int tpiCode = -1;
		APIMessage tpiMessage = null;
		String strStatus = "Status Unknown!";
		
		if(event != null) {
			tpiMessage = event.getAPIMessage();
			tpiCode = Integer.parseInt(tpiMessage.getAPICode());
			strStatus = tpiMessage.getAPIName();
			logger.debug("handleEvent(): Zone Item Name: {}", item.getName());

			if(config != null) {
				if(config.getDSCAlarmItemType() != null) {
					switch(config.getDSCAlarmItemType()) {
						case ZONE_ALARM_STATUS:
							zoneProperties.setState(StateType.ALARM_STATE, (tpiCode == 601) ? 1:0, strStatus);
							publisher.postUpdate(item.getName(), new StringType(strStatus));
							break;
						case ZONE_TAMPER_STATUS:
							zoneProperties.setState(StateType.TAMPER_STATE, (tpiCode == 603) ? 1:0, strStatus);
							publisher.postUpdate(item.getName(), new StringType(strStatus));
							break;
						case ZONE_FAULT_STATUS:
							zoneProperties.setState(StateType.FAULT_STATE, (tpiCode == 605) ? 1:0, strStatus);
							publisher.postUpdate(item.getName(), new StringType(strStatus));
							break;
						case ZONE_GENERAL_STATUS:
							zoneProperties.setState(StateType.GENERAL_STATE, (tpiCode == 609) ? 1:0, strStatus);
							publisher.postUpdate(item.getName(), (zoneProperties.getState(StateType.GENERAL_STATE) == 1) ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
							break;
						default: 
							logger.debug("handleEvent(): Zone item not updated.");
							break;
					}
				}
			}
		}
	}
	
	public void updateProperties(Item item, DSCAlarmBindingConfig config, int state, String description) {
		logger.debug("updateProperties(): Zone Item Name: {}", item.getName());

		if(config != null) {
			if(config.getDSCAlarmItemType() != null) {
				switch(config.getDSCAlarmItemType()) {
					case ZONE_ALARM_STATUS:
						zoneProperties.setState(StateType.ALARM_STATE, state, description);
						break;
					case ZONE_TAMPER_STATUS:
						zoneProperties.setState(StateType.TAMPER_STATE, state, description);
						break;
					case ZONE_FAULT_STATUS:
						zoneProperties.setState(StateType.FAULT_STATE, state, description);
						break;
					case ZONE_GENERAL_STATUS:
						zoneProperties.setState(StateType.GENERAL_STATE, state, description);
						break;
					case ZONE_BYPASS_MODE:
						zoneProperties.setState(StateType.ARM_STATE, state, description);
						break;
					default: 
						logger.debug("updateProperties():: Zone property not updated.");
						break;
				}
			}
		}
	}
}
