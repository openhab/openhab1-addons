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
public class RFXComThermostat1MessageTest {

    @Test
    public void testSomeMessages() throws RFXComException {
        String hexMessage = "0940001B6B1816150270";
        byte[] message = DatatypeConverter.parseHexBinary(hexMessage);
        RFXComThermostat1Message msg = (RFXComThermostat1Message) RFXComMessageFactory.getMessageInterface(message);
        assertEquals("SubType", RFXComThermostat1Message.SubType.DIGIMAX_TLX7506, msg.subType);
        assertEquals("Seq Number", 27, (short) (msg.seqNbr & 0xFF));
        assertEquals("Sensor Id", "27416", msg.generateDeviceId());
        assertEquals("Temperature", 22, msg.temperature);
        assertEquals("Set point", 21, msg.set);
        assertEquals("Mode", RFXComThermostat1Message.Mode.HEATING, msg.mode);
        assertEquals("Status", RFXComThermostat1Message.Status.NO_DEMAND, msg.status);
        assertEquals("Signal Level", (byte) 7, msg.signalLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMessage, DatatypeConverter.printHexBinary(decoded));
    }
}