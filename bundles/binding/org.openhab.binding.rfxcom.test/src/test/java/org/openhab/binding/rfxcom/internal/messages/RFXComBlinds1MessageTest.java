package org.openhab.binding.rfxcom.internal.messages;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.binding.rfxcom.internal.messages.RFXComBlinds1Message.Commands;
import org.openhab.binding.rfxcom.internal.messages.RFXComBlinds1Message.SubType;

import javax.xml.bind.DatatypeConverter;

import static org.junit.Assert.assertEquals;

public class RFXComBlinds1MessageTest {
    private void testMessage(String hexMsg, SubType subType, int seqNbr, String deviceId, int signalLevel,
            RFXComBlinds1Message.Commands command) throws RFXComException {
        final RFXComBlinds1Message msg = (RFXComBlinds1Message) RFXComMessageFactory
                .getMessageInterface(DatatypeConverter.parseHexBinary(hexMsg));
        assertEquals("SubType", subType, msg.subType);
        assertEquals("Seq Number", seqNbr, (short) (msg.seqNbr & 0xFF));
        assertEquals("Sensor Id", deviceId, msg.generateDeviceId());
        assertEquals("Command", command, msg.command);
        assertEquals("Signal Level", signalLevel, msg.signalLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMsg, DatatypeConverter.printHexBinary(decoded));
    }

    @Test
    public void testSomeMessages() throws RFXComException {
        testMessage("0919040600A21B010280", SubType.YR1326, 6, "41499.1", 8, Commands.STOP);

        testMessage("091905021A6280010000", SubType.MEDIAMOUNT, 2, "1729152.1", 0, Commands.OPEN);
    }
}
