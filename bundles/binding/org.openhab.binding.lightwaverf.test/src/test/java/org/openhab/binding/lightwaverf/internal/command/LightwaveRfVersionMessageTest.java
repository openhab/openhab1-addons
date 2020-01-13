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
package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LightwaveRfVersionMessageTest {

    @Test
    public void testValidMessage() throws Exception {
        String message = "1,?V=\"U2.91Q\"\r\n";

        LightwaveRfVersionMessage versionMessage = new LightwaveRfVersionMessage(message);
        assertEquals("001,?V=\"U2.91Q\"\n", versionMessage.getLightwaveRfCommandString());
    }

}
