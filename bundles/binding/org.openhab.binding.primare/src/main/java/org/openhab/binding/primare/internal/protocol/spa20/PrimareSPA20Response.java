/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal.protocol.spa20;

import java.util.Arrays;

import org.openhab.binding.primare.internal.protocol.PrimareUtils;
import org.openhab.binding.primare.internal.protocol.PrimareResponse;
import org.openhab.binding.primare.internal.protocol.spa20.PrimareSPA20Command;
import org.openhab.binding.primare.internal.protocol.spa20.PrimareSPA20MessageFactory;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class for Primare SP31.7/SP31/SPA20/SPA21 responses
 * 
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public class PrimareSPA20Response extends PrimareResponse {
	
	private static final Logger logger = 
		LoggerFactory.getLogger(PrimareSPA20Response.class);
	

	/**
	 * Constructor
	 * 
	 */
	public PrimareSPA20Response(byte[] message) {
		this.message = message;
	}

	
	/**
	 * @{inheritDoc}
	 */
	public boolean isRelevantFor(String deviceCmdString) {
		
		boolean relevant = false;

		switch (PrimareSPA20Command.valueOf(deviceCmdString)) {
			
			// Variable 1 = Power/Standby 0..1, 0=Standby (default), 1=Operate
		case POWER_QUERY:
		case POWER_TOGGLE:
		case POWER_OFF:
		case POWER_ON:
			relevant = (message[1] == 0x01);
			break;
			
			// Variable 2 = Main Input number 1..15
		case MAIN_INPUT_QUERY:
		case MAIN_INPUT_UP:
		case MAIN_INPUT_DOWN:
		case MAIN_INPUT_SET:
			relevant = (message[1] == 0x02);
			break;

			// Variable 3 = Volume level 0..99, default:0
		case VOLUME_QUERY:
		case VOLUME_UP:
		case VOLUME_DOWN:
		case VOLUME_SET:
			relevant = (message[1] == 0x03);
			break;

			// Variable 4 = Balance -20..+20, default:0
		case BALANCE_UP:
		case BALANCE_QUERY:
		case BALANCE_DOWN:
		case BALANCE_SET:
			relevant = (message[1] == 0x04);
			break;

			// Variable 5 = Center -20..+20, default:0
		case CENTER_UP:
		case CENTER_QUERY:
		case CENTER_DOWN:
		case CENTER_SET:
			relevant = (message[1] == 0x05);
			break;

			// Variable 6 = Surround -20..+20, default:0
		case SURROUND_UP:
		case SURROUND_QUERY:
		case SURROUND_DOWN:
		case SURROUND_SET:
			relevant = (message[1] == 0x06);
			break;

			// Variable 7 = Back -20..+20, default:0
		case BACK_QUERY:
		case BACK_UP:
		case BACK_DOWN:
		case BACK_SET:
			relevant = (message[1] == 0x07);
			break;

			// Variable 8 = SUB -20..+20, default:0
		case SUB_UP:
		case SUB_QUERY:
		case SUB_DOWN:
		case SUB_SET:
			relevant = (message[1] == 0x08);
			break;


			// Variable 9 = Mute 0..1, 0=inactive (default), 1=active
		case MUTE_QUERY:
		case MUTE_TOGGLE:
		case MUTE_OFF:
		case MUTE_ON:
			relevant = (message[1] == 0x09);
			break;

			// Variable 10 = Dim 0..1, 0=display on, 1=display off (default)
		case DIM_QUERY:
		case DIM_TOGGLE:
		case DIM_OFF:
		case DIM_ON:
			relevant = (message[1] == 0x0A);
			break;

			// Variable 11 = Record Input number 1..15
		case RECORD_INPUT_QUERY:
		case RECORD_INPUT_UP:
		case RECORD_INPUT_DOWN:
		case RECORD_INPUT_SET:
			relevant = (message[1] == 0x0B);
			break;

			// Variable 12 = Surround mode 0..6
		case SURROUND_MODE_QUERY:
		case SURROUND_MODE_UP:
		case SURROUND_MODE_SET:
			relevant = (message[1] == 0x0C);
			break;

			// Variable 13 = Verbose 0..1, 1=verbose on, 2=verbose off (default)
		case VERBOSE_QUERY:
		case VERBOSE_TOGGLE:
		case VERBOSE_OFF:
		case VERBOSE_ON:
			relevant = (message[1] == 0x0D);
			break;

			// Variable 14 = Menu 0..2,  0 = Exit menu (default), 1=Menu back, 2=Enter menu
		case MENU_QUERY:
		case MENU_TOGGLE: // Menu enter / menu back
		case MENU_SET:
			relevant = (message[1] == 0x0E);
			break;

			// Variable 16 = Extra surround mode 0..1
			
		case EXTRA_SURROUND_MODE_QUERY:
		case EXTRA_SURROUND_MODE_TOGGLE:
		case EXTRA_SURROUND_MODE_ON:
		case EXTRA_SURROUND_MODE_OFF:
			relevant = (message[1] == 0x10);
			break;

			// Variable 17 = Front panel lock 0..1, 0 = lock off (default), 1 = lock on
		case FRONT_PANEL_LOCK_QUERY:
		case FRONT_PANEL_LOCK_TOGGLE:
		case FRONT_PANEL_LOCK_OFF:
		case FRONT_PANEL_LOCK_ON:
			relevant = (message[1] == 0x11);
			break;

			// Variable 18 = IR Input select 0..1,  0 = front ir input (default), other = back ir input
		case IR_INPUT_QUERY:
		case IR_INPUT_TOGGLE:
		case IR_INPUT_FRONT:
		case IR_INPUT_BACK:
			relevant = (message[1] == 0x12);
			break;

			// Variable 19 = Recall settings 0..2,  0=factory settings, 1=memory factory settings 2=memory installer settings
		case RECALL_MEMORY:
		case RECALL_MEMORY_DIRECT_USER_SETTINGS:
		case RECALL_MEMORY_DIRECT_FACTORY_SETTINGS:
		case RECALL_MEMORY_DIRECT_INSTALLER_SETTINGS:
			relevant = (message[1] == 0x13);
			break;

			// Variable 20 = Current input name
		case CURRENT_INPUT_NAME_QUERY:
			relevant = (message[1] == 0x14);
			break;

			// Variable 21 = name of productline
		case PRODUCTLINE_QUERY:
			relevant = (message[1] == 0x15);
			break;

			// Variable 22 = name of model
		case MODEL_QUERY:
			relevant = (message[1] == 0x16);
			break;

			// Variable 23 = software version
		case SW_VERSION_QUERY:
			relevant = (message[1] == 0x17);
			break;

			// Variable 25 = Late night mode 0..1, 0=off (default), 1=on
		case LATE_NIGHT_MODE_QUERY:
		case LATE_NIGHT_MODE_TOGGLE:
		case LATE_NIGHT_MODE_OFF:
		case LATE_NIGHT_MODE_ON:
			relevant = (message[1] == 0x18);
			break;
		}
		
		return relevant;
	}
	

	/**
	 * Convert received response message containing a Primare device variable value
	 * to suitable OpenHAB state for the given itemType
	 * 
	 * @param itemType
	 * 
	 * @return openHAB state
	 */
	@Override
	public State openHabState(Class<? extends Item> itemType) {
		State state = UnDefType.UNDEF;
		
		try {
			int index;
			String s;
		
			if (itemType == SwitchItem.class) {
				index = (int) message[2];
				// Documentation and behavior mismatch with MUTE (var 9)
				// observed with SPA20, software version 1.50 Nov 2 2003
				if (message[1] == 9) {
					logger.trace("MUTE (variable 9) converted opposite to documentation");
					state = index == 0 ? OnOffType.ON : OnOffType.OFF;
				} else {
					state = index == 0 ? OnOffType.OFF : OnOffType.ON;
				}			

			} else if (itemType == NumberItem.class) {
				index = (int) message[2];
				state = new DecimalType(index);
			
			} else if (itemType == DimmerItem.class) {
				index = (int) message[2];
				state = new PercentType(index);
				
			} else if (itemType == RollershutterItem.class) {
				index = (int) message[2];
				state = new PercentType(index);
				
			} else if (itemType == StringItem.class) {
				s = new String(Arrays.copyOfRange(message, 2, message.length-2));
				state = new StringType(s);
			}
		} catch (Exception e) {
			logger.debug("Cannot convert value '{}' to data type {}", message[1], itemType);
		}
		
		return state;
	}




}
