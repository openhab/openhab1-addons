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
import org.openhab.binding.dscalarm.internal.DSCAlarmEvent;
import org.openhab.binding.dscalarm.internal.model.DSCAlarmDeviceProperties.StateType;
import org.openhab.binding.dscalarm.internal.model.DSCAlarmDeviceProperties.TriggerType;
import org.openhab.binding.dscalarm.internal.protocol.APIMessage;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Partition represents a controllable area within a DSC Alarm system
 * 
 * @author Russell Stephens
 * @since 1.6.0
 */
public class Partition extends DSCAlarmDevice{
	private static final Logger logger = LoggerFactory.getLogger(Partition.class);

	public DSCAlarmDeviceProperties partitionProperties = new DSCAlarmDeviceProperties();

	/**
	 * Constructor
	 * 
	 * @param partitionId
	 */
	public Partition(int partitionId) {
		if(partitionId >= 1 && partitionId <= 8)
			partitionProperties.setPartitionId(partitionId);
		else
			partitionProperties.setPartitionId(1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refreshItem(Item item, DSCAlarmBindingConfig config, EventPublisher publisher) {
		logger.debug("refreshItem(): Partition Item Name: {}", item.getName());

		int state;
		String strStatus = "";
		boolean trigger;
		OnOffType onOffType;

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
					case PARTITION_ARMED:
						trigger = partitionProperties.getTrigger(TriggerType.ARMED);
						onOffType = trigger ? OnOffType.ON : OnOffType.OFF;
						publisher.postUpdate(item.getName(), onOffType);
						break;
					case PARTITION_ENTRY_DELAY:
						trigger = partitionProperties.getTrigger(TriggerType.ENTRY_DELAY);
						onOffType = trigger ? OnOffType.ON : OnOffType.OFF;
						publisher.postUpdate(item.getName(), onOffType);
						break;
					case PARTITION_EXIT_DELAY:
						trigger = partitionProperties.getTrigger(TriggerType.EXIT_DELAY);
						onOffType = trigger ? OnOffType.ON : OnOffType.OFF;
						publisher.postUpdate(item.getName(), onOffType);
						break;
					case PARTITION_IN_ALARM:
						trigger = partitionProperties.getTrigger(TriggerType.ALARMED);
						onOffType = trigger ? OnOffType.ON : OnOffType.OFF;
						publisher.postUpdate(item.getName(), onOffType);
						break;
					case PARTITION_OPENING_CLOSING_MODE:
						state = partitionProperties.getState(StateType.OPENING_CLOSING_STATE);
						strStatus = partitionProperties.getStateDescription(StateType.OPENING_CLOSING_STATE);
						if(item instanceof NumberItem) {
							publisher.postUpdate(item.getName(), new DecimalType(state));
						}
						if(item instanceof StringItem) {
							publisher.postUpdate(item.getName(), new StringType(strStatus));
						}
						break;
					default:
						logger.debug("refreshItem(): Partition item not updated.");
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
									break;
								case 651:
								case 672:
								case 673:
									state = 0;
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
						case PARTITION_OPENING_CLOSING_MODE:
							switch(apiCode) {
								case 700:
									state=1;
									break;
								case 701:
									state=2;
									break;
								case 702:
									state=3;
									break;
								case 750:
									state=4;
									break;
								case 751:
									state=5;
									break;
								default:
									state=0;
									strStatus = "";
									break;
							}							
							partitionProperties.setState(StateType.OPENING_CLOSING_STATE, state, strStatus);
							strStatus = partitionProperties.getStateDescription(StateType.OPENING_CLOSING_STATE);
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

	/**
	 * {@inheritDoc}
	 */
	public void updateProperties(Item item, DSCAlarmBindingConfig config, int state, String description) {
		logger.debug("updateProperties(): Partition Item Name: {}", item.getName());

		boolean trigger = state != 0 ? true : false;
		
		if(config != null) {
			if(config.getDSCAlarmItemType() != null) {
				switch(config.getDSCAlarmItemType()) {
					case PARTITION_STATUS:
						partitionProperties.setState(StateType.GENERAL_STATE, state, description);
						break;
					case PARTITION_ARM_MODE:
						partitionProperties.setState(StateType.ARM_STATE, state, description);
						break;
					case PARTITION_ARMED:
						partitionProperties.setTrigger(TriggerType.ARMED, trigger);
						break;
					case PARTITION_ENTRY_DELAY:
						partitionProperties.setTrigger(TriggerType.ENTRY_DELAY, trigger);
						break;
					case PARTITION_EXIT_DELAY:
						partitionProperties.setTrigger(TriggerType.EXIT_DELAY, trigger);
						break;
					case PARTITION_IN_ALARM:
						partitionProperties.setTrigger(TriggerType.ALARMED, trigger);
						break;
					case PARTITION_OPENING_CLOSING_MODE:
						partitionProperties.setState(StateType.OPENING_CLOSING_STATE, state, description);
						break;
					default: 
						logger.debug("updateProperties(): Partition property not updated.");
						break;
				}
			}
		}
	}
}
