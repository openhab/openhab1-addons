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
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.DecimalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Partition represents a controllable area within a DSC Alarm system.
 * @author Russell Stephens
 * @since 1.6.0
 */
public class Partition extends DSCAlarmDevice{
	private static final Logger logger = LoggerFactory.getLogger(Partition.class);

	DSCAlarmDeviceProperties partitionProperties = new DSCAlarmDeviceProperties();

	public Partition(int partitionId) {
		if(partitionId >= 1 && partitionId <= 8)
			partitionProperties.setPartitionId(partitionId);
		else
			partitionProperties.setPartitionId(1);
	}
	
	@Override
	public void refreshItem(Item item, DSCAlarmBindingConfig config, EventPublisher publisher) {
		int state;
		String strStatus = "";
		logger.debug("refreshItem(): Partition Item Name: {}", item.getName());

		if(config != null) {
			if(config.getDSCAlarmItemType() != null) {
				switch(config.getDSCAlarmItemType()) {
					case PARTITION_STATUS:
						state = partitionProperties.getState(StateType.GENERAL_STATE);
						strStatus = partitionProperties.getStateDescription(StateType.GENERAL_STATE);
						publisher.postUpdate(item.getName(), new StringType(strStatus));
						break;
					case PARTITION_ARM_MODE:
						state = partitionProperties.getState(StateType.ARM_STATE);
						strStatus = partitionProperties.getStateDescription(StateType.ARM_STATE);
						publisher.postUpdate(item.getName(), new DecimalType(state));
						break;
					default:
						logger.debug("refreshItem(): Partition item not updated.");
						break;
				}
			}
		}
	}

	@Override
	public void handleEvent(Item item, DSCAlarmBindingConfig config, EventPublisher publisher, DSCAlarmEvent event) {
		int state = 0;
		int apiCode = -1;
		APIMessage apiMessage = null;
		String strStatus = "";
		
		if(event != null) {
			apiMessage = event.getAPIMessage();
			apiCode = Integer.parseInt(apiMessage.getAPICode());
			strStatus = apiMessage.getAPIName();
			logger.debug("handleEvent(): Partition Item Name: {}", item.getName());
			
			if(config != null) {
				if(config.getDSCAlarmItemType() != null) {
					switch(config.getDSCAlarmItemType()) {
						case PARTITION_STATUS:
							switch(apiCode) {
								case 650:
								case 653:
									state = 1;
									partitionProperties.setState(StateType.GENERAL_STATE, state, strStatus);
									break;
								case 651:
								case 672:
								case 673:
									state = 0;
									partitionProperties.setState(StateType.GENERAL_STATE, 0, strStatus);
									break;
								case 654:
									partitionProperties.setState(StateType.ALARM_STATE, 1, strStatus);
									break;
								default:
									break;
							}
							partitionProperties.setState(StateType.GENERAL_STATE, state, strStatus);
							strStatus = partitionProperties.getStateDescription(StateType.GENERAL_STATE);
							publisher.postUpdate(item.getName(), new StringType(strStatus));
							break;
						case PARTITION_ARM_MODE:
							if(apiCode == 652)
								state = Integer.parseInt(apiMessage.getMode()) + 1;
							partitionProperties.setState(StateType.ARM_STATE, state, strStatus);
							strStatus = partitionProperties.getStateDescription(StateType.ARM_STATE);
							if(item instanceof NumberItem) {
								publisher.postUpdate(item.getName(), new DecimalType(state));
							}
							if(item instanceof StringItem) {
								publisher.postUpdate(item.getName(), new StringType(strStatus));
							}
							break;
						default:
							logger.debug("handleEvent(): Partition item not updated.");
							break;
					}
				}
			}
		}
	}

	public void updateProperties(Item item, DSCAlarmBindingConfig config, int state, String description) {
		logger.debug("updateProperties(): Partition Item Name: {}", item.getName());

		if(config != null) {
			if(config.getDSCAlarmItemType() != null) {
				switch(config.getDSCAlarmItemType()) {
					case PARTITION_STATUS:
						partitionProperties.setState(StateType.GENERAL_STATE, state, description);
						break;
					case PARTITION_ARM_MODE:
						partitionProperties.setState(StateType.ARM_STATE, state, description);
						break;
					default: 
						logger.debug("updateProperties(): Partition property not updated.");
						break;
				}
			}
		}
	}
}
