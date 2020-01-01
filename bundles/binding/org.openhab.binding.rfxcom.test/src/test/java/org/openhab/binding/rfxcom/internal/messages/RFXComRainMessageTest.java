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
import static org.openhab.binding.rfxcom.internal.messages.RFXComRainMessage.SubType.PCR800;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;

/**
 * Test for RFXCom-binding
 *
 * @author Martin van Wingerden
 * @since 1.9.0
 */
public class RFXComRainMessageTest {

    @Test
    public void testSomeMessages() throws RFXComException {
        String hexMessage = "0B550217B6000000004D3C69";
        byte[] message = DatatypeConverter.parseHexBinary(hexMessage);
        RFXComRainMessage msg = (RFXComRainMessage) RFXComMessageFactory.getMessageInterface(message);
        assertEquals("SubType", PCR800, msg.subType);
        assertEquals("Seq Number", 23, msg.seqNbr);
        assertEquals("Sensor Id", "46592", msg.generateDeviceId());
        assertEquals("Rain rate", 0.0, msg.rainRate, 0.001);
        assertEquals("Total rain", 1977.2, msg.rainTotal, 0.001);
        assertEquals("Signal Level", 6, msg.signalLevel);
        assertEquals("Battery Level", 9, msg.batteryLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMessage, DatatypeConverter.printHexBinary(decoded));
    }
}