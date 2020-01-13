/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.lightwaverf.internal.message;

import static org.junit.Assert.*;

import org.junit.Test;

public class LightwaveRfGeneralMessageIdTest {

    @Test
    public void testGetMessageIdString() {
        LightwaveRfMessageId messageId001 = new LightwaveRfGeneralMessageId(1);
        assertEquals("001", messageId001.getMessageIdString());

        LightwaveRfMessageId messageId012 = new LightwaveRfGeneralMessageId(12);
        assertEquals("012", messageId012.getMessageIdString());

        LightwaveRfMessageId messageId = new LightwaveRfGeneralMessageId(123);
        assertEquals("123", messageId.getMessageIdString());
    }

    @Test
    public void testEqualsObject() {
        LightwaveRfMessageId messageId = new LightwaveRfGeneralMessageId(123);
        LightwaveRfMessageId otherMessageId = new LightwaveRfGeneralMessageId(123);
        assertTrue(otherMessageId.equals(messageId));
        assertTrue(messageId.hashCode() == otherMessageId.hashCode());
    }

}
