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
import static org.openhab.binding.rfxcom.internal.messages.RFXComTemperatureHumidityBarometricMessage.ForecastStatus.RAIN;
import static org.openhab.binding.rfxcom.internal.messages.RFXComTemperatureHumidityBarometricMessage.HumidityStatus.DRY;
import static org.openhab.binding.rfxcom.internal.messages.RFXComTemperatureHumidityBarometricMessage.SubType.BTHR918N_BTHR968;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;

/**
 * Test for RFXCom-binding
 *
 * @author Martin van Wingerden
 * @since 1.9.0
 */
public class RFXComTemperatureHumidityBarometricMessageTest {

    @Test
    public void testSomeMessages() throws RFXComException {
        String hexMessage = "0D54020EE90000C9270203E70439";
        byte[] message = DatatypeConverter.parseHexBinary(hexMessage);
        RFXComTemperatureHumidityBarometricMessage msg = (RFXComTemperatureHumidityBarometricMessage) RFXComMessageFactory
                .getMessageInterface(message);
        assertEquals("SubType", BTHR918N_BTHR968, msg.subType);
        assertEquals("Seq Number", 14, msg.seqNbr);
        assertEquals("Sensor Id", "59648", msg.generateDeviceId());
        assertEquals("Temperature", 20.1, msg.temperature, 0.01);
        assertEquals("Humidity", 39, msg.humidity);
        assertEquals("Humidity status", DRY, msg.humidityStatus);
        assertEquals("Barometer", 999.0, msg.pressure, 0.001);
        assertEquals("Forecast", RAIN, msg.forecastStatus);
        assertEquals("Signal Level", 3, msg.signalLevel);
        assertEquals("Battery Level", 9, msg.batteryLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMessage, DatatypeConverter.printHexBinary(decoded));
    }
}