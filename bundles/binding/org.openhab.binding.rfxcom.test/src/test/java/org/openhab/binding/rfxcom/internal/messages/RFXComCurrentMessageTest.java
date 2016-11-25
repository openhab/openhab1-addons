package org.openhab.binding.rfxcom.internal.messages;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;

import javax.xml.bind.DatatypeConverter;

import static org.junit.Assert.assertEquals;
import static org.openhab.binding.rfxcom.internal.messages.RFXComCurrentMessage.SubType.ELEC1;

public class RFXComCurrentMessageTest {
    @Test
    public void testSomeMessages() throws RFXComException {
        String hexMessage = "0D59010F860004001D0000000049";
        byte[] message = DatatypeConverter.parseHexBinary(hexMessage);
        RFXComCurrentMessage msg = (RFXComCurrentMessage) RFXComMessageFactory.getMessageInterface(message);
        assertEquals("SubType", ELEC1, msg.subType);
        assertEquals("Seq Number", 15, msg.seqNbr);
        assertEquals("Sensor Id", "34304", msg.generateDeviceId());
        assertEquals("Count", 4, msg.count);
        assertEquals("Channel 1", 2.9, msg.channel1Amps, 0.01);
        assertEquals("Channel 2", 0, msg.channel2Amps, 0.01);
        assertEquals("Channel 3", 0, msg.channel3Amps, 0.01);
        assertEquals("Signal Level", (byte) 4, msg.signalLevel);
        assertEquals("Battery Level", (byte) 9, msg.batteryLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMessage, DatatypeConverter.printHexBinary(decoded));
    }
}
