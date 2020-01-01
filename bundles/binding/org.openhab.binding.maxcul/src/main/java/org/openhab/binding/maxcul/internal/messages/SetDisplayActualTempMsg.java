/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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

    public SetDisplayActualTempMsg(byte msgCount, byte msgFlag, byte groupId, String srcAddr, String dstAddr,
            boolean displayActualTemp) {
        super(msgCount, msgFlag, MaxCulMsgType.SET_DISPLAY_ACTUAL_TEMP, groupId, srcAddr, dstAddr);
        byte[] payload = new byte[SET_DISPLAY_ACTUAL_TEMP_PAYLOAD_LEN];
        payload[0] = displayActualTemp ? (byte) 0x04 : (byte) 0x00;
        super.appendPayload(payload);
    }

}
