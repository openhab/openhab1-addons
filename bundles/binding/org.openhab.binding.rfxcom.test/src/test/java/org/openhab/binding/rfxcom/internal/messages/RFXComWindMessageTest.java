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
import static org.openhab.binding.rfxcom.internal.messages.RFXComWindMessage.SubType.WTGR800;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;

/**
 * Test for RFXCom-binding
 *
 * @author Martin van Wingerden
 * @since 1.9.0
 */
public class RFXComWindMessageTest {
    @Test
    public void testSomeMessages() throws RFXComException {
        String hexMessage = "105601122F000087000000140000000079";
        byte[] message = DatatypeConverter.parseHexBinary(hexMessage);
        RFXComWindMessage msg = (RFXComWindMessage) RFXComMessageFactory.getMessageInterface(message);
        assertEquals("SubType", WTGR800, msg.subType);
        assertEquals("Seq Number", 18, msg.seqNbr);
        assertEquals("Sensor Id", "12032", msg.generateDeviceId());
        assertEquals("Direction", 135.0, msg.windDirection, 0.001);
        assertEquals("Average speed", 0.0, msg.windAvSpeed, 0.001);
        assertEquals("Wind Gust", 2.0, msg.windSpeed, 0.001);
        assertEquals("Signal Level", 7, msg.signalLevel);
        assertEquals("Battery Level", 9, msg.batteryLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMessage, DatatypeConverter.printHexBinary(decoded));
    }
}
