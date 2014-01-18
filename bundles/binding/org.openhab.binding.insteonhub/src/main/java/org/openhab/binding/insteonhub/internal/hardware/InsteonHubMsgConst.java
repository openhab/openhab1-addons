/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal.hardware;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Constants for INSTEON messages
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubMsgConst {

	public static final byte STX = 0x02;
	public static final byte ACK = 0x06;
	public static final byte NAK = 0x15;
	
	public static final byte CMD1_ON = 0x11;
	public static final byte CMD1_ON_FAST = 0x12;
	public static final byte CMD1_OFF = 0x13;
	public static final byte CMD1_OFF_FAST = 0x14;
	public static final byte CMD1_BRT = 0x15;
	public static final byte CMD1_DIM = 0x16;
	public static final byte CMD1_START_DIM_BRT = 0x17;
	public static final byte CMD1_STOP_DIM_BRT = 0x18;

	public static final byte CMD1_STATUS_REQUEST = 0x19;
	public static final byte CMD1_LEVEL_RESPONSE = 0x02;

	public static final byte CMD2_NO_VALUE = 0x00;
	public static final byte CMD2_DIM = 0x00;
	public static final byte CMD2_BRT = 0x01;
	
	public static final int STD_FLAG_BIT_BC_OR_NAK = 7;
	public static final int STD_FLAG_BIT_GROUP = 6;
	public static final int STD_FLAG_BIT_ACK = 5;
	public static final int STD_FLAG_BIT_EXT = 4;
	

	public static final byte REC_CODE_INSTEON_STD_MSG = 0x50;
	public static final byte REC_CODE_INSTEON_EXT_MSG = 0x51;
	public static final byte REC_CODE_X10 = 0x52;
	public static final byte REC_CODE_ALL_LINK_COMPLETE = 0x53;
	public static final byte REC_CODE_BTN_EVT_REPORT = 0x54;
	public static final byte REC_CODE_USER_RESET_DETECT = 0x55;
	public static final byte REC_CODE_ALL_LINK_CLEANUP_FAILURE_REPORT = 0x56;
	public static final byte REC_CODE_ALL_LINK_RECORD_RESPONSE = 0x57;
	public static final byte REC_CODE_ALL_LINK_CLEANUP_STATUS_REPORT = 0x58;
	public static final byte REC_CODE_DATABASE_RECORD_FOUND = 0x59;
	public static final byte SND_CODE_GET_IM_INFO = 0x60;
	public static final byte SND_CODE_ALL_LINK_COMMAND = 0x61;
	public static final byte SND_CODE_SEND_INSTEON_STD_OR_EXT_MSG = 0x62;
	public static final byte SND_CODE_SEND_X10 = 0x63;
	public static final byte SND_CODE_START_ALL_LINKING = 0x64;
	public static final byte SND_CODE_CANCEL_ALL_LINKING = 0x65;
	public static final byte SND_CODE_SET_HOST_DEVICE_CATEGORY = 0x66;
	public static final byte SND_CODE_RESET_IM = 0x67;
	public static final byte SND_CODE_SET_INSTEON_ACK_MSG_BYTE = 0x68;
	public static final byte SND_CODE_GET_FIRST_ALL_LINK_RECORD = 0x69;
	public static final byte SND_CODE_GET_NEXT_ALL_LINK_RECORD = 0x6A;
	public static final byte SND_CODE_SET_IM_CONFIG = 0x6B;
	public static final byte SND_CODE_GET_ALL_LINK_RECORD_FOR_SENDER = 0x6C;
	public static final byte SND_CODE_LED_ON = 0x6D;
	public static final byte SND_CODE_LED_OFF = 0x6E;
	public static final byte SND_CODE_MANAGE_ALL_LINK_RECORD = 0x6F;
	public static final byte SND_CODE_SET_INSTEON_NAK_MSG_BYTE = 0x70;
	public static final byte SND_CODE_SET_INSTEON_ACK_MSG_TWO_BYTES = 0x71;
	public static final byte SND_CODE_RF_SLEEP = 0x72;
	public static final byte SND_CODE_GET_IM_CONFIG = 0x73;
	public static final byte SND_CODE_CANCEL_CLEANUP = 0x74;
	public static final byte SND_CODE_READ_8_BYTES_FROM_DB = 0x75;
	public static final byte SND_CODE_WRITE_8_BYTES_TO_DB = 0x76;
	public static final byte SND_CODE_BEEP = 0x77;
	public static final byte SND_CODE_SET_STATUS = 0x78;
	public static final byte SND_CODE_SET_DB_LINK_DATA_FOR_NEXT_LINK = 0x79;
	public static final byte SND_CODE_SET_APP_RETRIES_FOR_NEW_LINK = 0x7A;
	public static final byte SND_CODE_SET_RF_FREQUENCY_OFFSET = 0x7B;
	public static final byte SND_CODE_SET_ACK_FOR_TEMPLINC_CMD = 0x7C;
	
	public static final Map<Byte, Integer> REC_MSG_SIZES;
	
	static {
		Map<Byte, Integer> recMsgSizes = new HashMap<Byte, Integer>();
		recMsgSizes.put(REC_CODE_INSTEON_STD_MSG, 11);
		recMsgSizes.put(REC_CODE_INSTEON_EXT_MSG, 25);
		recMsgSizes.put(REC_CODE_X10, 4);
		recMsgSizes.put(REC_CODE_ALL_LINK_COMPLETE, 10);
		recMsgSizes.put(REC_CODE_BTN_EVT_REPORT, 3);
		recMsgSizes.put(REC_CODE_USER_RESET_DETECT, 2);
		recMsgSizes.put(REC_CODE_ALL_LINK_CLEANUP_FAILURE_REPORT, 7);
		recMsgSizes.put(REC_CODE_ALL_LINK_RECORD_RESPONSE, 10);
		recMsgSizes.put(REC_CODE_ALL_LINK_CLEANUP_STATUS_REPORT, 3);
		recMsgSizes.put(REC_CODE_DATABASE_RECORD_FOUND, 12);
		recMsgSizes.put(SND_CODE_GET_IM_INFO, 9);
		recMsgSizes.put(SND_CODE_ALL_LINK_COMMAND, 6);
		recMsgSizes.put(SND_CODE_SEND_INSTEON_STD_OR_EXT_MSG, 9);
		recMsgSizes.put(SND_CODE_SEND_X10, 5);
		recMsgSizes.put(SND_CODE_START_ALL_LINKING, 5);
		recMsgSizes.put(SND_CODE_CANCEL_ALL_LINKING, 3);
		recMsgSizes.put(SND_CODE_SET_HOST_DEVICE_CATEGORY, 6);
		recMsgSizes.put(SND_CODE_RESET_IM, 3);
		recMsgSizes.put(SND_CODE_SET_INSTEON_ACK_MSG_BYTE, 4);
		recMsgSizes.put(SND_CODE_GET_FIRST_ALL_LINK_RECORD, 3);
		recMsgSizes.put(SND_CODE_GET_NEXT_ALL_LINK_RECORD, 3);
		recMsgSizes.put(SND_CODE_SET_IM_CONFIG, 4);
		recMsgSizes.put(SND_CODE_GET_ALL_LINK_RECORD_FOR_SENDER, 3);
		recMsgSizes.put(SND_CODE_LED_ON, 3);
		recMsgSizes.put(SND_CODE_LED_OFF, 3);
		recMsgSizes.put(SND_CODE_MANAGE_ALL_LINK_RECORD, 12);
		recMsgSizes.put(SND_CODE_SET_INSTEON_NAK_MSG_BYTE, 4);
		recMsgSizes.put(SND_CODE_SET_INSTEON_ACK_MSG_TWO_BYTES, 5);
		recMsgSizes.put(SND_CODE_RF_SLEEP, 5);
		recMsgSizes.put(SND_CODE_GET_IM_CONFIG, 6);
		recMsgSizes.put(SND_CODE_CANCEL_CLEANUP, 3);
		recMsgSizes.put(SND_CODE_READ_8_BYTES_FROM_DB, 17);
		recMsgSizes.put(SND_CODE_WRITE_8_BYTES_TO_DB, 13);
		recMsgSizes.put(SND_CODE_BEEP, 3);
		recMsgSizes.put(SND_CODE_SET_STATUS, 4);
		recMsgSizes.put(SND_CODE_SET_DB_LINK_DATA_FOR_NEXT_LINK, 6);
		recMsgSizes.put(SND_CODE_SET_APP_RETRIES_FOR_NEW_LINK, 4);
		recMsgSizes.put(SND_CODE_SET_RF_FREQUENCY_OFFSET, 4);
		recMsgSizes.put(SND_CODE_SET_ACK_FOR_TEMPLINC_CMD, 4);
		REC_MSG_SIZES = Collections.unmodifiableMap(recMsgSizes);
	}
	

}
