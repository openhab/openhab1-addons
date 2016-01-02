/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal.messages;

/**
 * Message to set either the Wall Thermostat display shows the actual temperature
 * 
 * @author Johannes Goehr (johgoe)
 * @since 1.8.0
 */
public class SetDisplayActualTempMsg extends BaseMsg {

	private static final int SET_DISPLAY_ACTUAL_TEMP_PAYLOAD_LEN = 1;

	public SetDisplayActualTempMsg(byte msgCount, byte msgFlag, byte groupId, String srcAddr,
			String dstAddr, boolean displayActualTemp) {
		super(msgCount, msgFlag, MaxCulMsgType.SET_DISPLAY_ACTUAL_TEMP, groupId, srcAddr, dstAddr);
		byte[] payload = new byte[SET_DISPLAY_ACTUAL_TEMP_PAYLOAD_LEN];
		payload[0] =  displayActualTemp?( byte )0x04:( byte )0x00;
		super.appendPayload(payload);
	}

}
