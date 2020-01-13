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

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;

/**
 * Test for RFXCom-binding
 *
 * @author Martin van Wingerden
 * @since 1.9.0
 */
public class RFXComSecurity1MessageTest {

    @Test
    public void testSomeMessages() throws RFXComException {
        String hexMessage = "0820004DD3DC540089";
        byte[] message = DatatypeConverter.parseHexBinary(hexMessage);
        RFXComSecurity1Message msg = (RFXComSecurity1Message) RFXComMessageFactory.getMessageInterface(message);
        assertEquals("SubType", RFXComSecurity1Message.SubType.X10_SECURITY, msg.subType);
        assertEquals("Seq Number", 77, (short) (msg.seqNbr & 0xFF));
        assertEquals("Sensor Id", "13884500", msg.generateDeviceId());
        assertEquals("Battery level", 8, msg.batteryLevel);
        assertEquals("Contact", RFXComSecurity1Message.Contact.NORMAL, msg.contact);
        assertEquals("Motion", RFXComSecurity1Message.Motion.UNKNOWN, msg.motion);
        assertEquals("Status", RFXComSecurity1Message.Status.NORMAL, msg.status);
        assertEquals("Signal Level", 9, msg.signalLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMessage, DatatypeConverter.printHexBinary(decoded));
    }
}