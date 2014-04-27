/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal.hardware.api.serial;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubAdjustmentType;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubMsgConst;

/**
 * Builds INSTEON Hub messages per spec.
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubSerialMessageBuilder {

	public static final int STD_MSG_SIZE = 8;
	private static final byte DEFAULT_FLAG = 0x0F;
	public static InsteonHubSerialMessageBuilder INSTANCE = new InsteonHubSerialMessageBuilder();
	
	public static InsteonHubSerialMessageBuilder getInstance() {
		return INSTANCE;
	}
	
	private InsteonHubSerialMessageBuilder() {
		
	}

	public byte[] buildRequestLevelMessage(byte[] msgBuffer, String device) {
		return buildStandardMessage(msgBuffer, device, DEFAULT_FLAG,
				InsteonHubMsgConst.CMD1_STATUS_REQUEST, (byte) 0x02);
	}

	public byte[] buildFastPowerMessage(byte[] msgBuffer, String device,
			boolean power) {
		byte cmd1 = power ? InsteonHubMsgConst.CMD1_ON_FAST
				: InsteonHubMsgConst.CMD1_OFF_FAST;
		return buildStandardMessage(msgBuffer, device, DEFAULT_FLAG, cmd1,
				InsteonHubMsgConst.CMD2_NO_VALUE);
	}

	public byte[] buildSetLevelMessage(byte[] msgBuffer, String device,
			int level) {
		byte cmd1 = level > 0 ? InsteonHubMsgConst.CMD1_ON
				: InsteonHubMsgConst.CMD1_OFF;
		return buildStandardMessage(msgBuffer, device, DEFAULT_FLAG, cmd1,
				(byte) level);
	}

	public byte[] buildStartDimBrtMessage(byte[] msgBuffer, String device,
			InsteonHubAdjustmentType type) {
		byte cmd2 = type == InsteonHubAdjustmentType.DIM ? InsteonHubMsgConst.CMD2_DIM
				: InsteonHubMsgConst.CMD2_BRT;
		return buildStandardMessage(msgBuffer, device, DEFAULT_FLAG,
				InsteonHubMsgConst.CMD1_START_DIM_BRT, cmd2);
	}

	public byte[] buildStopDimBrtMessage(byte[] msgBuffer, String device) {
		return buildStandardMessage(msgBuffer, device, DEFAULT_FLAG,
				InsteonHubMsgConst.CMD1_STOP_DIM_BRT,
				InsteonHubMsgConst.CMD2_NO_VALUE);
	}

	private byte[] buildStandardMessage(byte[] msgBuffer, String device,
			int flag, byte cmd1, byte cmd2) {
		// check to make sure reuse-buffer is large enough. if not, create a new
		// one to use.
		if (msgBuffer == null || msgBuffer.length < STD_MSG_SIZE) {
			msgBuffer = new byte[STD_MSG_SIZE];
		}

		// populate message
		msgBuffer[0] = InsteonHubMsgConst.STX;
		msgBuffer[1] = InsteonHubMsgConst.SND_CODE_SEND_INSTEON_STD_OR_EXT_MSG;
		try {
			System.arraycopy(Hex.decodeHex(device.toCharArray()), 0, msgBuffer,
					2, 3);
		} catch (DecoderException e) {
			throw new IllegalArgumentException("Bad device id: " + device, e);
		}
		msgBuffer[5] = (byte) flag;
		msgBuffer[6] = cmd1;
		msgBuffer[7] = cmd2;
		return msgBuffer;
	}
}
