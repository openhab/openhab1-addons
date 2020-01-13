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
package org.openhab.binding.rfxcom.internal.messages;

import static org.junit.Assert.assertEquals;
import static org.openhab.binding.rfxcom.internal.messages.RFXComCurrentMessage.SubType.ELEC1;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;

/**
 * Test for RFXCom-binding
 *
 * @author Martin van Wingerden
 * @since 1.9.0
 */
public class RFXComCurrentMessageTest {
    @Test
    public void testSomeMessages() throws RFXComException {
        String hexMessage = "0D59010F860004001D0000000049";
        byte[] message = DatatypeConverter.parseHexBinary(hexMessage);
        RFXComCurrentMessage msg = (RFXComCurrentMessage) RFXComMessageFactory.getMessageInterface(message);
        assertEquals("SubType", ELEC1, msg.subType);
        assertEquals("Seq Number", 15, msg.seqNbr);
        assertEquals("Sensor Id", "34304", msg.generateDeviceId());
        assertEquals("Count", 4, msg.count);
        assertEquals("Channel 1", 2.9, msg.channel1Amps, 0.01);
        assertEquals("Channel 2", 0, msg.channel2Amps, 0.01);
        assertEquals("Channel 3", 0, msg.channel3Amps, 0.01);
        assertEquals("Signal Level", (byte) 4, msg.signalLevel);
        assertEquals("Battery Level", (byte) 9, msg.batteryLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMessage, DatatypeConverter.printHexBinary(decoded));
    }
}
