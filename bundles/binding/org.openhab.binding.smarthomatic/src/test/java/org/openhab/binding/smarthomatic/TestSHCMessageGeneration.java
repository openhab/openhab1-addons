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
package org.openhab.binding.smarthomatic;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.smarthomatic.internal.BaseStation;
import org.openhab.core.library.types.DecimalType;

public class TestSHCMessageGeneration {
    private BaseStation base;
    private String cmd;

    @Before
    public void setUp() throws Exception {
        base = new BaseStation("/dev/ttyUSB0", 19200, null);
    }

    /*
     * sKK{T}{X}{D}...Use AES key KK to send a packet with MessageType T,
     * followed by all necessary extension header fields and message data.
     * Fields are: ReceiverID (RRRR), MessageGroup (GG), MessageID (MM)
     * AckSenderID (SSSS), AckPacketCounter (PPPPPP), Error (EE). MessageData
     * (DD) can be 0..17 bytes with bits moved to the left. End data with ENTER.
     * SenderID, PacketCounter and CRC are automatically added.
     * sKK00RRRRGGMMDD...........Get sKK01RRRRGGMMDD...........Set
     * sKK02RRRRGGMMDD...........SetGet sKK08GGMMDD...............Status
     * sKK09SSSSPPPPPPEE.........Ack sKK0ASSSSPPPPPPEEGGMMDD...AckStatus
     */

    @Test
    public void testAllDeviceIds() {
        for (int deviceid = 0; deviceid < 4096; deviceid++) {
            cmd = base.prepareCommand(deviceid, 60, 1, 0, new DecimalType(0));
            assertEquals(deviceid, Integer.parseInt(cmd.substring(5, 9), 16));
        }
    }

    @Test
    public void testAllMessageGroupIds() {
        HashMap<Integer, Integer> sup_msggroupids = new HashMap<Integer, Integer>();
        // key = message group id; value = message id
        sup_msggroupids.put(1, 1);
        sup_msggroupids.put(1, 5);
        sup_msggroupids.put(1, 6);
        sup_msggroupids.put(60, 10);
        sup_msggroupids.put(60, 11);
        sup_msggroupids.put(60, 1);
        sup_msggroupids.put(60, 2);

        for (int msggroupid = 0; msggroupid < 256; msggroupid++) {
            int msgid = 0;
            boolean is_supported = false;
            if (sup_msggroupids.containsKey(msggroupid)) {
                msgid = sup_msggroupids.get(msggroupid);
                is_supported = true;
            }
            cmd = base.prepareCommand(0, msggroupid, msgid, 0, new DecimalType(0));
            if (is_supported) {
                int strlen = cmd.length();
                assertEquals("Length = " + strlen, true, strlen > 0);
                assertEquals(msggroupid, Integer.parseInt(cmd.substring(9, 11), 16));
            } else {
                assertEquals("", cmd);
            }
        }
    }

    @Test
    public void testAllMessageIds() {
        HashMap<Integer, Integer> sup_msgids = new HashMap<Integer, Integer>();
        sup_msgids.put(1, 60);
        sup_msgids.put(2, 60);
        sup_msgids.put(10, 60);
        sup_msgids.put(11, 60);

        for (int msgid = 0; msgid < 256; msgid++) {
            int msggroupid = 0;
            boolean is_supported = false;
            if (sup_msgids.containsKey(msgid)) {
                msggroupid = sup_msgids.get(msgid);
                is_supported = true;
            }
            cmd = base.prepareCommand(0, msggroupid, msgid, 0, new DecimalType(0));
            if (is_supported) {
                int strlen = cmd.length();
                assertEquals("Length = " + strlen, true, strlen > 0);
                assertEquals(msgid, Integer.parseInt(cmd.substring(11, 13), 16));
            } else {
                assertEquals("", cmd);
            }
        }
    }

    @Test
    public void testGPIODigitalPortAllOff() {
        HashMap<Integer, String> test_msg = new HashMap<Integer, String>();
        test_msg.put(0, "s0002000e010100"); // All off
        test_msg.put(255, "s0002000e0101ff"); // All on
        test_msg.put(170, "s0002000e0101aa"); // Even pins on
        test_msg.put(85, "s0002000e010155"); // Odd pins on

        for (int key : test_msg.keySet()) {
            cmd = base.prepareCommand(14, 1, 1, 0, new DecimalType(key));
            assertEquals(test_msg.get(key), cmd);
        }
    }

    @Test
    public void testGPIODigitalPin() {
        for (int pin = 0; pin < 8; pin++) {
            // Pin i = Off
            cmd = base.prepareCommand(14, 1, 5, 0, new DecimalType(pin * 2));
            assertEquals(pin * 2, Integer.parseInt(cmd.substring(13, 14), 16));
            assertEquals(14, cmd.length());
            // Pin i = On
            cmd = base.prepareCommand(14, 1, 5, 0, new DecimalType(pin * 2 + 1));
            assertEquals(pin * 2 + 1, Integer.parseInt(cmd.substring(13, 14), 16));
            assertEquals(14, cmd.length());
        }
    }

    @Test
    public void testGPIODigitalPinTimeoutTyp() {
        HashMap<Integer, String> test_msg = new HashMap<Integer, String>();
        test_msg.put(65544, "s0002000e0106100080");
        test_msg.put(196616, "s0002000e0106300080");
        test_msg.put(327688, "s0002000e0106500080");
        test_msg.put(458880, "s0002000e0106700800");
        test_msg.put(590080, "s0002000e0106901000");
        test_msg.put(721408, "s0002000e0106b02000");

        for (int key : test_msg.keySet()) {
            cmd = base.prepareCommand(14, 1, 6, 0, new DecimalType(key));
            assertEquals(test_msg.get(key), cmd);
        }
    }

    @Test
    public void testDimmerBrightnessTyp() {
        HashMap<Integer, String> test_msg = new HashMap<Integer, String>();
        test_msg.put(0, "s0002000d3c010");
        test_msg.put(25, "s0002000d3c0132");
        test_msg.put(50, "s0002000d3c0164");
        test_msg.put(75, "s0002000d3c0196");
        test_msg.put(100, "s0002000d3c01c8");

        for (int key : test_msg.keySet()) {
            cmd = base.prepareCommand(13, 60, 1, 0, new DecimalType(key));
            assertEquals(test_msg.get(key), cmd);
        }
    }

}
