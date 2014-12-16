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
import org.openhab.binding.dscalarm.internal.model.DSCAlarmDeviceProperties.LEDStateType;
import org.openhab.binding.dscalarm.internal.protocol.APIMessage;
import org.openhab.binding.dscalarm.internal.DSCAlarmEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.DecimalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Keypad is the central administrative unit of the DSC Alarm System
 * 
 * @author Russell Stephens
 * @since 1.6.0
 */
public class Keypad extends DSCAlarmDevice{
	private static final Logger logger = LoggerFactory.getLogger(Keypad.class);

	DSCAlarmDeviceProperties keypadProperties = new DSCAlarmDeviceProperties();

	/**
	 * Constructor
	 * 
	 * @param keypadId
	 */
	public Keypad(int keypadId) {
		keypadProperties.setKeypadId(keypadId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refreshItem(Item item, DSCAlarmBindingConfig config, EventPublisher publisher) {
		int state;
		String str = "";
		logger.debug("refreshItem(): Keypad Item Name: {}", item.getName());

		if(config != null) {
			if(config.getDSCAlarmItemType() != null) {
				switch(config.getDSCAlarmItemType()) {
					case KEYPAD_READY_LED:
						state = keypadProperties.getLEDState(LEDStateType.READY_LED_STATE);
						str = keypadProperties.getLEDStateDescription(LEDStateType.READY_LED_STATE);
						if(item instanceof NumberItem) {
							publisher.postUpdate(item.getName(), new DecimalType(state));
						}
						if(item instanceof StringItem) {
							publisher.postUpdate(item.getName(), new StringType(str));
						}						
						break;
					case KEYPAD_ARMED_LED:
						state = keypadProperties.getLEDState(LEDStateType.ARMED_LED_STATE);
						str = keypadProperties.getLEDStateDescription(LEDStateType.ARMED_LED_STATE);
						if(item instanceof NumberItem) {
							publisher.postUpdate(item.getName(), new DecimalType(state));
						}
						if(item instanceof StringItem) {
							publisher.postUpdate(item.getName(), new StringType(str));
						}						
						break;
					case KEYPAD_MEMORY_LED:
						state = keypadProperties.getLEDState(LEDStateType.MEMORY_LED_STATE);
						str = keypadProperties.getLEDStateDescription(LEDStateType.MEMORY_LED_STATE);
						if(item instanceof NumberItem) {
							publisher.postUpdate(item.getName(), new DecimalType(state));
						}
						if(item instanceof StringItem) {
							publisher.postUpdate(item.getName(), new StringType(str));
						}						
						break;
					case KEYPAD_BYPASS_LED:
						state = keypadProperties.getLEDState(LEDStateType.BYPASS_LED_STATE);
						str = keypadProperties.getLEDStateDescription(LEDStateType.BYPASS_LED_STATE);
						if(item instanceof NumberItem) {
							publisher.postUpdate(item.getName(), new DecimalType(state));
						}
						if(item instanceof StringItem) {
							publisher.postUpdate(item.getName(), new StringType(str));
						}						
						break;
					case KEYPAD_TROUBLE_LED:
						state = keypadProperties.getLEDState(LEDStateType.TROUBLE_LED_STATE);
						str = keypadProperties.getLEDStateDescription(LEDStateType.TROUBLE_LED_STATE);
						if(item instanceof NumberItem) {
							publisher.postUpdate(item.getName(), new DecimalType(state));
						}
						if(item instanceof StringItem) {
							publisher.postUpdate(item.getName(), new StringType(str));
						}						
						break;
					case KEYPAD_PROGRAM_LED:
						state = keypadProperties.getLEDState(LEDStateType.PROGRAM_LED_STATE);
						str = keypadProperties.getLEDStateDescription(LEDStateType.PROGRAM_LED_STATE);
						if(item instanceof NumberItem) {
							publisher.postUpdate(item.getName(), new DecimalType(state));
						}
						if(item instanceof StringItem) {
							publisher.postUpdate(item.getName(), new StringType(str));
						}						
						break;
					case KEYPAD_FIRE_LED:
						state = keypadProperties.getLEDState(LEDStateType.FIRE_LED_STATE);
						str = keypadProperties.getLEDStateDescription(LEDStateType.FIRE_LED_STATE);
						if(item instanceof NumberItem) {
							publisher.postUpdate(item.getName(), new DecimalType(state));
						}
						if(item instanceof StringItem) {
							publisher.postUpdate(item.getName(), new StringType(str));
						}						
						break;
					case KEYPAD_BACKLIGHT_LED:
						state = keypadProperties.getLEDState(LEDStateType.BACKLIGHT_LED_STATE);
						str = keypadProperties.getLEDStateDescription(LEDStateType.BACKLIGHT_LED_STATE);
						if(item instanceof NumberItem) {
							publisher.postUpdate(item.getName(), new DecimalType(state));
						}
						if(item instanceof StringItem) {
							publisher.postUpdate(item.getName(), new StringType(str));
						}						
						break;
					case KEYPAD_AC_LED:
						state = keypadProperties.getLEDState(LEDStateType.AC_LED_STATE);
						str = keypadProperties.getLEDStateDescription(LEDStateType.AC_LED_STATE);
						if(item instanceof NumberItem) {
							publisher.postUpdate(item.getName(), new DecimalType(state));
						}
						if(item instanceof StringItem) {
							publisher.postUpdate(item.getName(), new StringType(str));
						}						
						break;
					default:
						logger.debug("refreshItem(): Keypad item not updated.");
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
			state = Integer.parseInt(apiMessage.getAPIData().substring(1));
			String str = "";
			logger.debug("handleEvent(): Keypad Item Name: {}", item.getName());
	
			if(config != null) {
				if(config.getDSCAlarmItemType() != null) {
					switch(config.getDSCAlarmItemType()) {
						case KEYPAD_READY_LED:
							keypadProperties.setLEDState(LEDStateType.READY_LED_STATE, state);
							str = keypadProperties.getLEDStateDescription(LEDStateType.READY_LED_STATE);
							if(item instanceof NumberItem) {
								publisher.postUpdate(item.getName(), new DecimalType(state));
							}
							if(item instanceof StringItem) {
								publisher.postUpdate(item.getName(), new StringType(str));
							}						
							break;
						case KEYPAD_ARMED_LED:
							keypadProperties.setLEDState(LEDStateType.ARMED_LED_STATE, state);
							str = keypadProperties.getLEDStateDescription(LEDStateType.ARMED_LED_STATE);
							if(item instanceof NumberItem) {
								publisher.postUpdate(item.getName(), new DecimalType(state));
							}
							if(item instanceof StringItem) {
								publisher.postUpdate(item.getName(), new StringType(str));
							}						
							break;
						case KEYPAD_MEMORY_LED:
							keypadProperties.setLEDState(LEDStateType.MEMORY_LED_STATE, state);
							str = keypadProperties.getLEDStateDescription(LEDStateType.MEMORY_LED_STATE);
							if(item instanceof NumberItem) {
								publisher.postUpdate(item.getName(), new DecimalType(state));
							}
							if(item instanceof StringItem) {
								publisher.postUpdate(item.getName(), new StringType(str));
							}						
							break;
						case KEYPAD_BYPASS_LED:
							keypadProperties.setLEDState(LEDStateType.BYPASS_LED_STATE, state);
							str = keypadProperties.getLEDStateDescription(LEDStateType.BYPASS_LED_STATE);
							if(item instanceof NumberItem) {
								publisher.postUpdate(item.getName(), new DecimalType(state));
							}
							if(item instanceof StringItem) {
								publisher.postUpdate(item.getName(), new StringType(str));
							}						
							break;
						case KEYPAD_TROUBLE_LED:
							keypadProperties.setLEDState(LEDStateType.TROUBLE_LED_STATE, state);
							str = keypadProperties.getLEDStateDescription(LEDStateType.TROUBLE_LED_STATE);
							if(item instanceof NumberItem) {
								publisher.postUpdate(item.getName(), new DecimalType(state));
							}
							if(item instanceof StringItem) {
								publisher.postUpdate(item.getName(), new StringType(str));
							}						
							break;
						case KEYPAD_PROGRAM_LED:
							keypadProperties.setLEDState(LEDStateType.PROGRAM_LED_STATE, state);
							str = keypadProperties.getLEDStateDescription(LEDStateType.PROGRAM_LED_STATE);
							if(item instanceof NumberItem) {
								publisher.postUpdate(item.getName(), new DecimalType(state));
							}
							if(item instanceof StringItem) {
								publisher.postUpdate(item.getName(), new StringType(str));
							}						
							break;
						case KEYPAD_FIRE_LED:
							keypadProperties.setLEDState(LEDStateType.FIRE_LED_STATE, state);
							str = keypadProperties.getLEDStateDescription(LEDStateType.FIRE_LED_STATE);
							if(item instanceof NumberItem) {
								publisher.postUpdate(item.getName(), new DecimalType(state));
							}
							if(item instanceof StringItem) {
								publisher.postUpdate(item.getName(), new StringType(str));
							}						
							break;
						case KEYPAD_BACKLIGHT_LED:
							keypadProperties.setLEDState(LEDStateType.BACKLIGHT_LED_STATE, state);
							str = keypadProperties.getLEDStateDescription(LEDStateType.BACKLIGHT_LED_STATE);
							if(item instanceof NumberItem) {
								publisher.postUpdate(item.getName(), new DecimalType(state));
							}
							if(item instanceof StringItem) {
								publisher.postUpdate(item.getName(), new StringType(str));
							}						
							break;
						case KEYPAD_AC_LED:
							keypadProperties.setLEDState(LEDStateType.AC_LED_STATE, state);
							str = keypadProperties.getLEDStateDescription(LEDStateType.AC_LED_STATE);
							if(item instanceof NumberItem) {
								publisher.postUpdate(item.getName(), new DecimalType(state));
							}
							if(item instanceof StringItem) {
								publisher.postUpdate(item.getName(), new StringType(str));
							}						
							break;
						default:
							logger.debug("handleEvent(): Keypad item not updated.");
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
					case KEYPAD_READY_LED:
						keypadProperties.setLEDState(LEDStateType.READY_LED_STATE, state);
						break;
					case KEYPAD_ARMED_LED:
						keypadProperties.setLEDState(LEDStateType.ARMED_LED_STATE, state);
						break;
					case KEYPAD_MEMORY_LED:
						keypadProperties.setLEDState(LEDStateType.MEMORY_LED_STATE, state);
						break;
					case KEYPAD_BYPASS_LED:
						keypadProperties.setLEDState(LEDStateType.BYPASS_LED_STATE, state);
						break;
					case KEYPAD_TROUBLE_LED:
						keypadProperties.setLEDState(LEDStateType.TROUBLE_LED_STATE, state);
						break;
					case KEYPAD_PROGRAM_LED:
						keypadProperties.setLEDState(LEDStateType.PROGRAM_LED_STATE, state);
						break;
					case KEYPAD_FIRE_LED:
						keypadProperties.setLEDState(LEDStateType.FIRE_LED_STATE, state);
						break;
					case KEYPAD_BACKLIGHT_LED:
						keypadProperties.setLEDState(LEDStateType.BACKLIGHT_LED_STATE, state);
						break;
					case KEYPAD_AC_LED:
						keypadProperties.setLEDState(LEDStateType.AC_LED_STATE, state);
						break;
					default: 
						logger.debug("updateProperties(): Keypad property not updated.");
						break;
				}
			}
		}
	}
}
