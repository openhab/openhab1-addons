/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;

import javax.xml.bind.DatatypeConverter;

import static org.junit.Assert.assertEquals;
import static org.openhab.binding.rfxcom.RFXComValueSelector.COMMAND;
import static org.openhab.binding.rfxcom.RFXComValueSelector.CONTACT;
import static org.openhab.binding.rfxcom.RFXComValueSelector.SIGNAL_LEVEL;
import static org.openhab.binding.rfxcom.internal.messages.RFXComLighting4Message.Commands.OFF_4;
import static org.openhab.binding.rfxcom.internal.messages.RFXComLighting4Message.Commands.ON_7;
import static org.openhab.binding.rfxcom.internal.messages.RFXComLighting4Message.Commands.ON_9;
import static org.openhab.binding.rfxcom.internal.messages.RFXComLighting4Message.SubType.PT2262;

/**
 * Test for RFXCom-binding
 *
 * @author Martin van Wingerden
 * @since 1.9.0
 */
public class RFXComLighting4MessageTest {

    private RFXComLighting4Message testMessage(String hexMsg, int seqNbr, String deviceId,
                                               int signalLevel, RFXComLighting4Message.Commands command, boolean on, int pulse) throws RFXComException {
        final RFXComLighting4Message msg = (RFXComLighting4Message) RFXComMessageFactory
                .getMessageInterface(DatatypeConverter.parseHexBinary(hexMsg));
        assertEquals("SubType", PT2262, msg.subType);
        assertEquals("Seq Number", seqNbr, msg.seqNbr);
        assertEquals("Sensor Id", deviceId, msg.generateDeviceId());
        assertEquals("Signal Level", signalLevel, msg.signalLevel);
        assertEquals("Command", command, msg.command);
        assertEquals("State", on, msg.command.isOn());
        assertEquals("Pulse", pulse, msg.pulse);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMsg, DatatypeConverter.printHexBinary(decoded));

        return msg;
    }

    @Test
    public void testPirSensorMessages() throws RFXComException {
        RFXComLighting4Message msg = testMessage("0913000145DD99018870", 1, "286169", 7, ON_9, true, 392);

        assertEquals("convert to Contact", OpenClosedType.OPEN, msg.convertToState(CONTACT));
        assertEquals("convert to Command", OnOffType.ON, msg.convertToState(COMMAND));
        assertEquals("convert to Number", new DecimalType(7), msg.convertToState(SIGNAL_LEVEL));
    }

    @Test
    public void testContactSensorMessages() throws RFXComException {
        testMessage("0913002B455157016560", 43, "283925", 6, ON_7, true, 357);
        testMessage("0913002C455154016260", 44, "283925", 6, OFF_4, false, 354);
    }

    @Test
    public void basicBoundaryCheck() throws RFXComException {
        RFXComTestHelper.basicBoundaryCheck(PacketType.LIGHTING4);
    }

    // TODO please add more tests for real messages
}
