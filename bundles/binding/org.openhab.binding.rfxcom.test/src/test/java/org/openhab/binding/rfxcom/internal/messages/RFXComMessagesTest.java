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

import org.junit.Test;
import org.junit.runner.RunWith;

import org.openhab.binding.rfxcom.internal.RFXComException;
import static org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType;

public class RFXComMessagesTest {
    
        @Test
        public void checkNotImplementedPackets() throws Exception {
            String errors = "";

            // Currently there are a lot of not implemented messages,group together and throw a single error message listing all
            for (PacketType p : PacketType.values()) {
                if (p != PacketType.UNKNOWN) {
                    try {
                        RFXComMessageInterface intf = RFXComMessageFactory.getMessageInterface(p);
                    } catch (Exception e) {
                        errors += "\n\t" + e.getMessage();
                    }
                }
            }
            if (errors.length() > 0) {
                throw new Exception("Packet messages not implemented : " + errors);
            }
        }


        @Test
        public void checkDecodeMessage() throws Exception {
            String errors = "";
            
            for (PacketType p : PacketType.values()) {
                if (p != PacketType.UNKNOWN) {
                    try {
                        RFXComMessageInterface intf = RFXComMessageFactory.getMessageInterface(p);
                        try {
                            // This is a place where its easy to make mistakes in coding, and can result in errors, normally array bounds errors
                            byte[] message = intf.decodeMessage();
                            if (message[0] != (message.length-1)) {
                                errors += "\n\t" + intf.getClass().getName() + " wrong packet length";
                            }
                            
                        } catch (Throwable t) {
                            errors += "\n\t" + intf.getClass().getName() + " " + t.getClass().getName() + " " + t.getMessage();
                        }
                    } catch (Exception e) {
                        // already checked in checkNotImplementedPackets()
                    }
                }
            }
            if (errors.length() > 0) {
                throw new Exception("Errors in decodeMessage :" + errors);
            }
        }
}

