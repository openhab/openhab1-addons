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
import static org.openhab.binding.rfxcom.internal.messages.RFXComTemperatureMessage.SubType.*;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;

/**
 * Test for RFXCom-binding
 *
 * @author Martin van Wingerden
 * @since 1.9.0
 */
public class RFXComTemperatureMessageTest {
    private void testMessage(String hexMsg, RFXComTemperatureMessage.SubType subType, int seqNbr, String deviceId,
            double temperature, int signalLevel, int bateryLevel) throws RFXComException {
        final RFXComTemperatureMessage msg = (RFXComTemperatureMessage) RFXComMessageFactory
                .getMessageInterface(DatatypeConverter.parseHexBinary(hexMsg));
        assertEquals("SubType", subType, msg.subType);
        assertEquals("Seq Number", seqNbr, (short) (msg.seqNbr & 0xFF));
        assertEquals("Sensor Id", deviceId, msg.generateDeviceId());
        assertEquals("Temperature", temperature, msg.temperature, 0.001);
        assertEquals("Signal Level", signalLevel, msg.signalLevel);
        assertEquals("Battery", bateryLevel, msg.batteryLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMsg, DatatypeConverter.printHexBinary(decoded));
    }

    @Test
    public void testSomeMessages() throws RFXComException {
        testMessage("08500110000180BC69", THR128_138_THC138, 16, "1", -18.8d, 6, 9);
        testMessage("0850021DFB0100D770", THC238_268_THN122_132_THWR288_THRN122_AW129_131, 29, "64257", 21.5d, 7, 0);
        testMessage("08500502770000D389", LACROSSE_TX2_TX3_TX4_TX17, 2, "30464", 21.1d, 8, 9);
        testMessage("0850091A00C3800689", RUBICSON, 26, "195", -0.6d, 8, 9);
        testMessage("0850097200C300E089", RUBICSON, 114, "195", 22.4d, 8, 9);
    }
}
