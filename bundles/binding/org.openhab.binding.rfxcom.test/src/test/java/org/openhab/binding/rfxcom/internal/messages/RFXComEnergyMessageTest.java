package org.openhab.binding.rfxcom.internal.messages;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;

import javax.xml.bind.DatatypeConverter;

import static org.junit.Assert.assertEquals;
import static org.openhab.binding.rfxcom.internal.messages.RFXComCurrentMessage.SubType.ELEC1;
import static org.openhab.binding.rfxcom.internal.messages.RFXComEnergyMessage.SubType.ELEC2;

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
