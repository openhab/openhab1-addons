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
import static org.openhab.binding.rfxcom.internal.messages.RFXComPowerMessage.SubType.ELEC5;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;

/**
 * Test for RFXCom-binding
 *
 * @author Martin van Wingerden
 * @since 1.9.0
 */
public class RFXComPowerMessageTest {

    private void testMessage(String hexMsg, RFXComPowerMessage.SubType subType, int seqNbr, String deviceId,
            int voltage, double instantAmps, double instantPower, double instantEnergy, double powerFactor,
            int frequency, int signalLevel) throws RFXComException {
        final RFXComPowerMessage msg = (RFXComPowerMessage) RFXComMessageFactory
                .getMessageInterface(DatatypeConverter.parseHexBinary(hexMsg));
        assertEquals("SubType", subType, msg.subType);
        assertEquals("Seq Number", seqNbr, (short) (msg.seqNbr & 0xFF));
        assertEquals("Sensor Id", deviceId, msg.generateDeviceId());
        assertEquals("Voltage", voltage, msg.voltage);
        assertEquals("Current", instantAmps, msg.instantAmps, 0.01);
        assertEquals("Instant power", instantPower, msg.instantPower, 0.01);
        assertEquals("Total usage", instantEnergy, msg.instantEnergy, 0.01);
        assertEquals("Power factor", powerFactor, msg.powerFactor, 0.01);
        assertEquals("Frequency", frequency, msg.frequency);
        assertEquals("Signal Level", signalLevel, msg.signalLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMsg, DatatypeConverter.printHexBinary(decoded));
    }

    @Test
    public void testSomeMessages() throws RFXComException {
        testMessage("0F5C0103002DE4000000000003003280", ELEC5, 3, "45", 228, 0, 0, 0.03, 0, 50, 8);
        testMessage("0F5C0104002DE40002002F0003643280", ELEC5, 4, "45", 228, 0.02, 4.7, 0.03, 1, 50, 8);
        testMessage("0F5C0105002DE3001401BD0003643280", ELEC5, 5, "45", 227, 0.2, 44.5, 0.03, 1, 50, 8);
        testMessage("0F5C0106002DE30005005700034D3280", ELEC5, 6, "45", 227, 0.05, 8.7, 0.03, 0.77, 50, 8);
    }

}