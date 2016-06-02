/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import static org.mockito.Mockito.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.core.library.types.OnOffType;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.service.cm.ConfigurationException;

import net.wimpi.modbus.procimg.SimpleDigitalIn;

/**
 * Testing how configuration update is handled
 *
 */
@RunWith(Parameterized.class)
public class ConfigUpdatedTestCase extends TestCaseSupport {

    @Parameters
    public static List<Object[]> data() {
        List<Object[]> parameters = new ArrayList<Object[]>();
        for (ServerType server : TEST_SERVERS) {
            parameters.add(new Object[] { server });
        }
        return parameters;
    }

    @SuppressWarnings("serial")
    public static class ExpectedFailure extends AssertionError {
    }

    public ConfigUpdatedTestCase(ServerType serverType) {
        super();
        this.serverType = serverType;
    }

    @Test
    public void testConfigUpdated() throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        // Modbus server ("modbus slave") has two digital inputs
        spi.addDigitalIn(new SimpleDigitalIn(true));
        spi.addDigitalIn(new SimpleDigitalIn(false));

        binding = new ModbusBinding();

        // simulate configuration changes
        for (int i = 0; i < 2; i++) {
            binding.updated(
                    addSlave(newLongPollBindingConfig(), SLAVE_NAME, ModbusBindingProvider.TYPE_DISCRETE, null, 0, 2));
        }
        configureSwitchItemBinding(2, SLAVE_NAME, 0);
        binding.execute();

        // Give the system some time to make the expected connections & requests
        waitForRequests(1);
        if (!serverType.equals(ServerType.UDP)) {
            waitForConnectionsReceived(1);
        }

        verify(eventPublisher, never()).postCommand(null, null);
        verify(eventPublisher, never()).sendCommand(null, null);
        try {
            verify(eventPublisher).postUpdate("Item1", OnOffType.ON);
            verify(eventPublisher).postUpdate("Item2", OnOffType.OFF);
        } catch (AssertionError e) {
            throw new ExpectedFailure();
        }
        verifyNoMoreInteractions(eventPublisher);
    }

}
