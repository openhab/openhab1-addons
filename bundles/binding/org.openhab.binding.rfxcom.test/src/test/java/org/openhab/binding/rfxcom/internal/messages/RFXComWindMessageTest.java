package org.openhab.binding.rfxcom.internal.messages;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;

import javax.xml.bind.DatatypeConverter;

import static org.junit.Assert.assertEquals;
import static org.openhab.binding.rfxcom.internal.messages.RFXComWindMessage.SubType.WS4500_ET_AL;
import static org.openhab.binding.rfxcom.internal.messages.RFXComWindMessage.SubType.WTGR800;

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
