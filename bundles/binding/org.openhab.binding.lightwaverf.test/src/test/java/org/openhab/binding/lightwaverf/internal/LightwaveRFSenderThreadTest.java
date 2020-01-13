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
package org.openhab.binding.lightwaverf.internal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfCommandOk;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfSetHeatingTemperatureCommand;

public class LightwaveRFSenderThreadTest {

    @Mock
    DatagramSocket mockSocket;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() throws Exception {

        LightwaveRfSetHeatingTemperatureCommand tempCommand = new LightwaveRfSetHeatingTemperatureCommand(
                "768,!R3DhF*tP18.0");
        final LightwaveRfCommandOk okCommand = new LightwaveRfCommandOk("768,OK");

        final LightwaveRFSenderThread senderThread = new LightwaveRFSenderThread(mockSocket, "192.168.1.1", 8000,
                120000);

        executor.schedule(new Runnable() {
            @Override
            public void run() {
                senderThread.okMessageReceived(okCommand);
            }
        }, 1000, TimeUnit.MILLISECONDS);

        senderThread.sendLightwaveCommand(tempCommand);
        senderThread.run();

        verify(mockSocket, times(1)).send(any(DatagramPacket.class));
    }
}
