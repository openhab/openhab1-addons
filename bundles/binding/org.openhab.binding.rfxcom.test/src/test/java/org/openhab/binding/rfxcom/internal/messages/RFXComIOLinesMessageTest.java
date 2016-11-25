package org.openhab.binding.rfxcom.internal.messages;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType;

public class RFXComIOLinesMessageTest {

    @Test(expected = RFXComException.class)
    public void checkNotImplemented() throws Exception {
        RFXComMessageFactory.getMessageInterface(PacketType.IO_LINES);
    }
}
