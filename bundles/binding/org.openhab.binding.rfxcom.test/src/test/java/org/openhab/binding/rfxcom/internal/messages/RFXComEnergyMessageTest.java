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
import static org.openhab.binding.rfxcom.internal.messages.RFXComEnergyMessage.SubType.ELEC2;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;

/**
 * Test for RFXCom-binding
 *
 * @author Martin van Wingerden
 * @since 1.9.0
 */
public class RFXComEnergyMessageTest {
    @Test
    public void testSomeMessages() throws RFXComException {
        String hexMessage = "115A01071A7300000003F600000000350B89";
        byte[] message = DatatypeConverter.parseHexBinary(hexMessage);
        RFXComEnergyMessage msg = (RFXComEnergyMessage) RFXComMessageFactory.getMessageInterface(message);
        assertEquals("SubType", ELEC2, msg.subType);
        assertEquals("Seq Number", 7, msg.seqNbr);
        assertEquals("Sensor Id", "6771", msg.generateDeviceId());
        assertEquals("Count", 0, msg.count);
        assertEquals("Instant usage", 1014d / 230, msg.instantAmps, 0.01);
        assertEquals("Total usage", 60.7d / 230, msg.totalAmpHours, 0.01);
        assertEquals("Signal Level", (byte) 8, msg.signalLevel);
        assertEquals("Battery Level", (byte) 9, msg.batteryLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMessage, DatatypeConverter.printHexBinary(decoded));
    }
}
