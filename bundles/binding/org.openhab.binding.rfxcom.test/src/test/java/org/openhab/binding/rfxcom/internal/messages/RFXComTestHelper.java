/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import static org.junit.Assert.fail;

import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType;

/**
 * Helper class for testing the RFXCom-binding
 *
 * @author Martin van Wingerden
 * @since 1.9.0
 */
public class RFXComTestHelper {
    static void basicBoundaryCheck(PacketType packetType) throws RFXComException {
        RFXComMessageInterface intf = RFXComMessageFactory.getMessageInterface(packetType);

        // This is a place where its easy to make mistakes in coding, and can result in errors, normally
        // array bounds errors
        byte[] message = intf.decodeMessage();
        if (message[0] != (message.length - 1)) {
            fail(intf.getClass().getName() + " wrong packet length");
        }

        if (packetType.toByte() != message[1]) {
            fail(intf.getClass().getName() + " wrong packet type");
        }
    }
}
