/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeInitStage;

import junit.framework.TestCase;

/**
 * @author Dave Badia
 * @since 1.9.0
 */
public class ZWaveSecurityPayloadFrameTest extends TestCase {

    @Test
    public void testSingle() throws Throwable {
        // Setup
        ZWaveNode node = Mockito.mock(ZWaveNode.class);
        Mockito.when(node.getNodeInitializationStage()).thenReturn(ZWaveNodeInitStage.DETAILS); // Force use of scheme0
                                                                                                // key
        Mockito.when(node.getNodeId()).thenReturn(0x02);

        byte SECURITY_COMMANDS_SUPPORTED_GET = 0x02;
        SerialMessage messageToEncapsulate = new SerialMessage(0x00, SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.High);
        byte[] payload = { (byte) 0x00, 2, (byte) CommandClass.SECURITY.getKey(), SECURITY_COMMANDS_SUPPORTED_GET, };
        messageToEncapsulate.setMessagePayload(payload);

        // Execute
        List<ZWaveSecurityPayloadFrame> resultList = ZWaveSecurityPayloadFrame.convertToSecurityPayload(node,
                messageToEncapsulate);
        // Verify
        assertEquals(1, resultList.size());
        ZWaveSecurityPayloadFrame result = resultList.get(0);
        assertEquals(1, result.getTotalParts());
        assertEquals(1, result.getPart());
        assertEquals(0, result.getSequenceByte());
        assertEquals(2, result.getLength());
        assertEquals(SerialMessage.bb2hex(new byte[] { (byte) CommandClass.SECURITY.getKey(), 0x02 }),
                SerialMessage.bb2hex(result.getMessageBytes()));

        String logMessage = result.getLogMessage();
        assertTrue(logMessage.contains("NODE 2"));
        assertTrue(logMessage.contains("part 1 of 1"));
        assertTrue(logMessage.contains("SECURITY"));
        assertTrue(logMessage.contains("SendData"));
        assertTrue(logMessage.contains("00"));
        assertTrue(logMessage.contains("02"));
        assertTrue(logMessage.contains("98"));
    }

    @Test
    public void testMultiple() throws Throwable {
        // Setup
        ZWaveNode node = Mockito.mock(ZWaveNode.class);
        Mockito.when(node.getNodeInitializationStage()).thenReturn(ZWaveNodeInitStage.DETAILS); // Force use of scheme0
                                                                                                // key
        Mockito.when(node.getNodeId()).thenReturn(0x03);

        SerialMessage messageToEncapsulate = new SerialMessage(0x00, SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.High);
        // Create a payload with size > 28 so we can test it
        byte[] payload = { (byte) 0x00, 2, (byte) CommandClass.USER_CODE.getKey(), 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D,
                0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x20, 0x21, 0x22, 0x23, 0x24,
                0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, };
        messageToEncapsulate.setMessagePayload(payload);

        // Execute
        List<ZWaveSecurityPayloadFrame> resultList = ZWaveSecurityPayloadFrame.convertToSecurityPayload(node,
                messageToEncapsulate);
        // Verify
        assertEquals(2, resultList.size());
        // frame1
        ZWaveSecurityPayloadFrame result = resultList.get(0);
        assertEquals(2, result.getTotalParts());
        assertEquals(1, result.getPart());
        assertEquals(16, result.getSequenceByte());
        assertEquals(28, result.getLength());
        byte[] expectedBytes = new byte[] { 0x63, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12,
                0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28 };
        assertEquals(SerialMessage.bb2hex(expectedBytes), SerialMessage.bb2hex(result.getMessageBytes()));

        String logMessage = result.getLogMessage();
        assertTrue(logMessage.contains("NODE 3"));
        assertTrue(logMessage.contains("part 1 of 2"));
        assertTrue(logMessage.contains("USER_CODE"));
        assertTrue(logMessage.contains("SendData"));
        assertTrue(logMessage.contains("63")); // first
        assertTrue(logMessage.contains("09"));
        assertTrue(logMessage.contains("22")); // last
        assertFalse(logMessage.contains("29")); // Part 2, should NOT appear
        assertFalse(logMessage.contains("2C")); // Part 2, should NOT appear

        // frame2
        result = resultList.get(1);
        assertEquals(2, result.getTotalParts());
        assertEquals(2, result.getPart());
        assertEquals(48, result.getSequenceByte());
        assertEquals(5, result.getLength());
        Assert.assertArrayEquals(new byte[] { 0x29, 0x2A, 0x2B, 0x2C, 0x2D }, result.getMessageBytes());

        logMessage = result.getLogMessage();
        assertTrue(logMessage.contains("NODE 3"));
        assertTrue(logMessage.contains("part 2 of 2"));
        assertTrue(logMessage.contains("USER_CODE"));
        assertTrue(logMessage.contains("SendData"));
        assertTrue(logMessage.contains("2A")); // just test for some
        assertTrue(logMessage.contains("29"));
        assertTrue(logMessage.contains("2D"));
        assertFalse(logMessage.contains("22")); // Part 1, should NOT appear
        assertFalse(logMessage.contains("63")); // Part 1, should NOT appear
    }
}
