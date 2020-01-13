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
import static org.openhab.binding.rfxcom.internal.messages.RFXComTemperatureHumidityMessage.HumidityStatus.*;
import static org.openhab.binding.rfxcom.internal.messages.RFXComTemperatureHumidityMessage.SubType.*;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.binding.rfxcom.internal.messages.RFXComTemperatureHumidityMessage.HumidityStatus;

/**
 * Test for RFXCom-binding
 *
 * @author Ivan F. Martinez
 * @author Martin van Wingerden
 * @since 1.9.0
 */
public class RFXComTemperatureHumidityMessageTest {

    private void testMessage(String hexMsg, RFXComTemperatureHumidityMessage.SubType subType, int seqNbr, int sensorId,
            double temperature, int humidity, HumidityStatus humidityStatus, int signalLevel, int batteryLevel)
            throws RFXComException {
        byte[] binaryMessage = DatatypeConverter.parseHexBinary(hexMsg);
        final RFXComTemperatureHumidityMessage msg = (RFXComTemperatureHumidityMessage) RFXComMessageFactory
                .getMessageInterface(binaryMessage);
        assertEquals("SubType", subType, msg.subType);
        assertEquals("Seq Number", seqNbr, (short) (msg.seqNbr & 0xFF));
        assertEquals("Sensor Id", sensorId, msg.sensorId);
        assertEquals("Temperature", temperature, msg.temperature, 0.01);
        assertEquals("Humidity", humidity, msg.humidity);
        assertEquals("Humidity Status", humidityStatus, msg.humidityStatus);
        assertEquals("Signal Level", signalLevel, msg.signalLevel);
        assertEquals("Battery Level", batteryLevel, msg.batteryLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMsg, DatatypeConverter.printHexBinary(decoded));
    }

    @Test
    public void testSomeMessages() throws RFXComException {
        testMessage("0A5201800F0201294C0349", THGN122_123_132_THGR122_228_238_268, 128, 3842, 29.7, 76, WET, 4, 9);
        testMessage("0A520211700200A72D0089", THGN800_THGR810, 17, 28674, 16.7, 45, NORMAL, 8, 9);
        testMessage("0A5205D42F000082590379", WTGR800, 212, 12032, 13, 89, WET, 7, 9);
    }

}