/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import static org.junit.Assert.*;

import javax.xml.bind.DatatypeConverter;


import org.junit.Test;
import org.junit.runner.RunWith;

import org.openhab.binding.rfxcom.internal.RFXComException;

public class RFXComTemperatureHumidityMessageTest {


    private void testMessage(String hexMsg, 
            RFXComTemperatureHumidityMessage.SubType subType, int seqNbr, int sensorId, 
            double temperature, byte humidity, 
            RFXComTemperatureHumidityMessage.HumidityStatus humidityStatus, byte signalLevel, byte batteryLevel) throws RFXComException { 
        final RFXComTemperatureHumidityMessage msg = (RFXComTemperatureHumidityMessage)RFXComMessageFactory.getMessageInterface(DatatypeConverter.parseHexBinary(hexMsg));
        assertEquals("SubType", subType, msg.subType);
        assertEquals("Seq Number", seqNbr, (short) (msg.seqNbr & 0xFF));
        assertEquals("Sensor Id", sensorId, msg.sensorId);
        assertEquals("Temperature", temperature, msg.temperature, 0.01);
        assertEquals("Humidity", humidity, msg.humidity);
        assertEquals("Humidity Status", humidityStatus, msg.humidityStatus);
        assertEquals("Signal Level", signalLevel, msg.signalLevel);
        assertEquals("Battery Level", batteryLevel, msg.batteryLevel);
    }

    @Test
    public void testSomeMessages() throws RFXComException {
        testMessage("0A5201800F0201294C0349", RFXComTemperatureHumidityMessage.SubType.THGN122_123_132_THGR122_228_238_268, 128, 3842, 29.7, (byte)76, RFXComTemperatureHumidityMessage.HumidityStatus.WET, (byte)4, (byte)9);
        //TODO add more real messages 
    }
    
}