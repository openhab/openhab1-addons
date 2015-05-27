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
import org.openhab.binding.primare.internal.protocol.PrimareMessage;
import org.openhab.binding.primare.internal.protocol.spa20.PrimareSPA20Command;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Class for Primare SP31.7/SP31/SPA20/SPA21 messages
 * 
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public class PrimareSPA20Message extends PrimareMessage {

	private static final Logger logger = 
		LoggerFactory.getLogger(PrimareSPA20Message.class);


	/**
	 * Constructor, use device command (String) and OpenHAB command
	 * (org.openhab.core.types.Command) instances to build a PrimareSPA20Message
	 * 
	 * @throws Exception
	 */
	public PrimareSPA20Message(Command command, String deviceCmdString) {
		this(command, PrimareSPA20Command.valueOf(deviceCmdString));

	}

	
	/**
	 * Constructor, use device command (PrimareSPA20Command) and OpenHAB command
	 * (org.openhab.core.types.Command) instances to build a PrimareSPA20Message
	 * 
	 * @throws Exception
	 */
	public PrimareSPA20Message(Command command, PrimareSPA20Command deviceCmd) {

		byte[] message = null;

		switch (deviceCmd) {
			
			// Variable 1 = Power/Standby 0..1, 0=Standby (default), 1=Operate
		case POWER_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(1);
			break;
		case POWER_TOGGLE:
			message = PrimareSPA20Message.toggleDeviceVariable(1);
			break;
		case POWER_OFF:
			message = PrimareSPA20Message.setDeviceVariable(1, 0);
			break;
		case POWER_ON:
			message = PrimareSPA20Message.setDeviceVariable(1, 1);
			break;
			
			// Variable 2 = Main Input number 1..15
		case MAIN_INPUT_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(2);
			break;
		case MAIN_INPUT_UP:
			message = PrimareSPA20Message.incDeviceVariable(2);
			break;
		case MAIN_INPUT_DOWN:
			message = PrimareSPA20Message.decDeviceVariable(2);
			break;
		case MAIN_INPUT_SET:
			message = PrimareSPA20Message.setDeviceVariable(2, ((DecimalType) command).intValue());
			break;

			// Variable 3 = Volume level 0..99, default:0
		case VOLUME_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(3);
			break;
		case VOLUME_UP:
			message = PrimareSPA20Message.incDeviceVariable(3);
			break;
		case VOLUME_DOWN:
			message = PrimareSPA20Message.decDeviceVariable(3);
			break;
		case VOLUME_SET:
			message = PrimareSPA20Message.setDeviceVariable(3, ((DecimalType) command).intValue());
			break;


			// Variable 4 = Balance -20..+20, default:0
		case BALANCE_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(4);
			break;
		case BALANCE_UP:
			message = PrimareSPA20Message.stepDeviceVariable(4,1);
			break;
		case BALANCE_DOWN:
			message = PrimareSPA20Message.stepDeviceVariable(4,-1);
			break;
		case BALANCE_SET:
			message = PrimareSPA20Message.setDeviceVariable(4, ((DecimalType) command).intValue());
			break;

			// Variable 5 = Center -20..+20, default:0
		case CENTER_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(5);
			break;
		case CENTER_UP:
			message = PrimareSPA20Message.stepDeviceVariable(5,1);
			break;
		case CENTER_DOWN:
			message = PrimareSPA20Message.stepDeviceVariable(5,-1);
			break;
		case CENTER_SET:
			message = PrimareSPA20Message.setDeviceVariable(5, ((DecimalType) command).intValue());
			break;

			// Variable 6 = Surround -20..+20, default:0
		case SURROUND_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(6);
			break;
		case SURROUND_UP:
			message = PrimareSPA20Message.stepDeviceVariable(6,1);
			break;
		case SURROUND_DOWN:
			message = PrimareSPA20Message.stepDeviceVariable(6,-1);
			break;
		case SURROUND_SET:
			message = PrimareSPA20Message.setDeviceVariable(6, ((DecimalType) command).intValue());
			break;

			// Variable 7 = Back -20..+20, default:0
		case BACK_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(7);
			break;
		case BACK_UP:
			message = PrimareSPA20Message.stepDeviceVariable(7,1);
			break;
		case BACK_DOWN:
			message = PrimareSPA20Message.stepDeviceVariable(7,-1);
			break;
		case BACK_SET:
			message = PrimareSPA20Message.setDeviceVariable(7, ((DecimalType) command).intValue());
			break;

			// Variable 8 = SUB -20..+20, default:0
		case SUB_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(8);
			break;
		case SUB_UP:
			message = PrimareSPA20Message.stepDeviceVariable(8,1);
			break;
		case SUB_DOWN:
			message = PrimareSPA20Message.stepDeviceVariable(8,-1);
			break;
		case SUB_SET:
			message = PrimareSPA20Message.setDeviceVariable(8, ((DecimalType) command).intValue());
			break;


			// Variable 9 = Mute 0..1, 0=inactive (default), 1=active
		case MUTE_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(9);
			break;
		case MUTE_TOGGLE:
			message = PrimareSPA20Message.toggleDeviceVariable(9);
			break;
		case MUTE_OFF:
			message = PrimareSPA20Message.setDeviceVariable(9, 0);
			break;
		case MUTE_ON:
			message = PrimareSPA20Message.setDeviceVariable(9, 1);
			break;

			// Variable 10 = Dim 0..1, 0=display on, 1=display off (default)
		case DIM_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(10);
			break;
		case DIM_TOGGLE:
			message = PrimareSPA20Message.toggleDeviceVariable(10);
			break;
		case DIM_OFF:
			// 1 = Display on (Dim off)
			message = PrimareSPA20Message.setDeviceVariable(10, 1);
			break;
		case DIM_ON:
			// 0 = Display off (Dim on)
			message = PrimareSPA20Message.setDeviceVariable(10, 0);
			break;

			// Variable 11 = Record Input number 1..15
		case RECORD_INPUT_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(11);
			break;
		case RECORD_INPUT_UP:
			message = PrimareSPA20Message.incDeviceVariable(11);
			break;
		case RECORD_INPUT_DOWN:
			message = PrimareSPA20Message.decDeviceVariable(11);
			break;
		case RECORD_INPUT_SET:
			message = PrimareSPA20Message.setDeviceVariable(11, ((DecimalType) command).intValue());
			break;

			// Variable 12 = Surround mode 0..6
		case SURROUND_MODE_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(12);
			break;
		case SURROUND_MODE_UP:
			message = PrimareSPA20Message.incDeviceVariable(12);
			break;
		case SURROUND_MODE_SET:
			message = PrimareSPA20Message.setDeviceVariable(12, ((DecimalType) command).intValue());
			break;

			// Variable 13 = Verbose 0..1, 1=verbose on, 2=verbose off (default)
		case VERBOSE_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(13);
			break;
		case VERBOSE_TOGGLE:
			message = PrimareSPA20Message.toggleDeviceVariable(13);
			break;
		case VERBOSE_OFF:
			message = PrimareSPA20Message.setDeviceVariable(13, 0);
			break;
		case VERBOSE_ON:
			message = PrimareSPA20Message.setDeviceVariable(13, 1);
			break;

			// Variable 14 = Menu 0..2,  0 = Exit menu (default), 1=Menu back, 2=Enter menu
		case MENU_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(14);
			break;
		case MENU_TOGGLE: // Menu enter / menu back
			message = PrimareSPA20Message.toggleDeviceVariable(14);
			break;
		case MENU_SET:
			message = PrimareSPA20Message.setDeviceVariable(14, ((DecimalType) command).intValue());
			break;

			// Variable 16 = Extra surround mode 0..1
		case EXTRA_SURROUND_MODE_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(16);
			break;
		case EXTRA_SURROUND_MODE_TOGGLE:
			message = PrimareSPA20Message.toggleDeviceVariable(16);
			break;
		case EXTRA_SURROUND_MODE_ON:
			message = PrimareSPA20Message.setDeviceVariable(16, 1);
			break;
		case EXTRA_SURROUND_MODE_OFF:
			message = PrimareSPA20Message.setDeviceVariable(16, 0);
			break;

			// Variable 17 = Front panel lock 0..1, 0 = lock off (default), 1 = lock on
		case FRONT_PANEL_LOCK_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(17);
			break;
		case FRONT_PANEL_LOCK_TOGGLE:
			message = PrimareSPA20Message.toggleDeviceVariable(17);
			break;
		case FRONT_PANEL_LOCK_OFF:
			message = PrimareSPA20Message.setDeviceVariable(17, 0);
			break;
		case FRONT_PANEL_LOCK_ON:
			message = PrimareSPA20Message.setDeviceVariable(17, 1);
			break;

			// Variable 18 = IR Input select 0..1,  0 = front ir input (default), other = back ir input
		case IR_INPUT_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(18);
			break;
		case IR_INPUT_TOGGLE:
			message = PrimareSPA20Message.toggleDeviceVariable(18);
			break;
		case IR_INPUT_FRONT:
			message = PrimareSPA20Message.setDeviceVariable(18, 0);
			break;
		case IR_INPUT_BACK:
			message = PrimareSPA20Message.setDeviceVariable(18, 1);
			break;

			// Variable 19 = Recall settings 0..2,  0=factory settings, 1=memory factory settings 2=memory installer settings
		case RECALL_MEMORY:
			message = PrimareSPA20Message.toggleDeviceVariable(19);
			break;
		case RECALL_MEMORY_DIRECT_USER_SETTINGS:
			message = PrimareSPA20Message.setDeviceVariable(19, 0);
			break;
		case RECALL_MEMORY_DIRECT_FACTORY_SETTINGS:
			message = PrimareSPA20Message.setDeviceVariable(19, 1);
			break;
		case RECALL_MEMORY_DIRECT_INSTALLER_SETTINGS:
			message = PrimareSPA20Message.setDeviceVariable(19, 2);
			break;

			// Variable 20 = Current input 1..15
		case CURRENT_INPUT_NAME_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(20);
			break;

			// Variable 21 = name of productline
		case PRODUCTLINE_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(21);
			break;

			// Variable 22 = name of model
		case MODEL_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(22);
			break;

			// Variable 23 = software version
		case SW_VERSION_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(23);
			break;

			// Variable 25 = Late night mode 0..1, 0=off (default), 1=on
		case LATE_NIGHT_MODE_QUERY:
			message = PrimareSPA20Message.queryDeviceVariable(25);
			break;
		case LATE_NIGHT_MODE_TOGGLE:
			message = PrimareSPA20Message.toggleDeviceVariable(25);
			break;
		case LATE_NIGHT_MODE_OFF:
			message = PrimareSPA20Message.setDeviceVariable(25, 0);
			break;
		case LATE_NIGHT_MODE_ON:
			message = PrimareSPA20Message.setDeviceVariable(25, 1);
			break;
			
			// Composite message: full status query
		case ALL_QUERY:
			messageParts = new byte[][]{ PrimareSPA20Message.queryDeviceVariable(1),
						     PrimareSPA20Message.queryDeviceVariable(2),
						     PrimareSPA20Message.queryDeviceVariable(3),
						     PrimareSPA20Message.queryDeviceVariable(4),
						     PrimareSPA20Message.queryDeviceVariable(5),
						     PrimareSPA20Message.queryDeviceVariable(6),
						     PrimareSPA20Message.queryDeviceVariable(7),
						     PrimareSPA20Message.queryDeviceVariable(8),
						     PrimareSPA20Message.queryDeviceVariable(9),
						     PrimareSPA20Message.queryDeviceVariable(10),
						     PrimareSPA20Message.queryDeviceVariable(11),
						     PrimareSPA20Message.queryDeviceVariable(12),
						     PrimareSPA20Message.queryDeviceVariable(13),
						     PrimareSPA20Message.queryDeviceVariable(14),
						     PrimareSPA20Message.queryDeviceVariable(16),
						     PrimareSPA20Message.queryDeviceVariable(17),
						     PrimareSPA20Message.queryDeviceVariable(18),
						     PrimareSPA20Message.queryDeviceVariable(20),
						     PrimareSPA20Message.queryDeviceVariable(21),
						     PrimareSPA20Message.queryDeviceVariable(22),
						     PrimareSPA20Message.queryDeviceVariable(23),
						     PrimareSPA20Message.queryDeviceVariable(25) };
			break;
		}

		if (message != null)
			messageParts = new byte[][]{ message };

		logger.trace("New PrimareSPA20Message is (hex) [{}]",
			     PrimareUtils.byteArraysToHex(messageParts));
	}
	
	
	/*
	  Utility functions
	*/
	public static byte[] setDeviceVariable(int var_number, String paramValue) {
		return setDeviceVariable(var_number, (byte) Integer.parseInt(paramValue));
	}
	
	public static byte[] setDeviceVariable(int var_number, int var_value) {
		return setDeviceVariable(var_number, (byte) var_value);
	}

	public static byte[] setDeviceVariable(int var_number, byte var_value) {
		String req = "02 57 vn vv 10 03";
			
		byte[] message = PrimareUtils.hexStringToByteArray(req.replaceAll(" ", "").toLowerCase());
		message[2] = (byte) (var_number | 0x80);   // OR with 0x80 required when setting variable
		message[3] = var_value;

		logger.debug(String.format("PrimareSPA20Message.setDeviceVariable %d (0x%02x) = %d (0x%02x), msg: %s",
					   var_number, var_number, var_value, var_value, PrimareUtils.byteArrayToHex(message)));

		return message;
	}

	public static byte[] toggleDeviceVariable(int var_number) {
		String req = "02 57 vn 00 10 03";
			
		byte[] message = PrimareUtils.hexStringToByteArray(req.replaceAll(" ", "").toLowerCase());
		message[2] = (byte) var_number;

		logger.trace(String.format("PrimareSPA20Message.toggleDeviceVariable %d (0x%02x), msg: %s",
					   var_number, var_number, PrimareUtils.byteArrayToHex(message)));

		return message;
	}
		
	public static byte[] queryDeviceVariable(int var_number) {
		String req = "02 52 vn 10 03";
			
		byte[] message = PrimareUtils.hexStringToByteArray(req.replaceAll(" ", "").toLowerCase());
		message[2] = (byte) var_number;

		logger.trace(String.format("PrimareSPA20Message.queryDeviceVariable %d (0x%02x), msg: %s",
					   var_number, var_number, PrimareUtils.byteArrayToHex(message)));

		return message;
	}

	public static byte[] incDeviceVariable(int var_number) {
		return stepDeviceVariable(var_number, 1);
	}
		
	public static byte[] decDeviceVariable(int var_number) {
		return stepDeviceVariable(var_number, -1);
	}
		
	public static byte[] stepDeviceVariable(int var_number, int step) {
		String req = "02 57 vn st 10 03";
			
		byte[] message = PrimareUtils.hexStringToByteArray(req.replaceAll(" ", "").toLowerCase());
		message[2] = (byte) var_number;
		message[3] = (byte) step;
			
		logger.trace(String.format("PrimareSPA20Message.stepDeviceVariable %d (0x%02x), step:%d (0x%02x), msg: %s",
					   var_number, var_number, step, step, PrimareUtils.byteArrayToHex(message)));
			
		return message;
	}
		
}
