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
import java.util.Dictionary;

import org.junit.Test;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.service.cm.ConfigurationException;

import net.wimpi.modbus.procimg.SimpleDigitalIn;
import net.wimpi.modbus.procimg.SimpleDigitalOut;

/**
 * Tests for erroring queries. Run only against TCP server.
 *
 */
public class ErroringQueriesTestCase extends TestCaseSupport {

    @SuppressWarnings("serial")
    public static class ExpectedFailure extends AssertionError {
    }

    /**
     * Test case for situation where we try to poll too much data.
     *
     * In this test server has
     * - single discrete input
     * - single coil inputs
     *
     * Binding is configured to read
     * - two coils
     *
     * Items are follows
     * - first discrete input (Item1) -> no output since query
     * should fail
     */
    @Test
    public void testReadingTooMuch() throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        spi.addDigitalIn(new SimpleDigitalIn(true));
        spi.addDigitalOut(new SimpleDigitalOut(true));
        spi.addDigitalOut(new SimpleDigitalOut(false));

        binding = new ModbusBinding();
        Dictionary<String, Object> config = newLongPollBindingConfig();
        addSlave(config, SLAVE_NAME, ModbusBindingProvider.TYPE_DISCRETE, null, 0, 2);
        binding.updated(config);

        // Configure items
        final ModbusGenericBindingProvider provider = new ModbusGenericBindingProvider();
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format("%s:%d", SLAVE_NAME, 0));
        binding.setEventPublisher(eventPublisher);
        binding.addBindingProvider(provider);

        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);

        verifyNoMoreInteractions(eventPublisher);
    }

    /**
     * Test case for situation where we try to poll too much data.
     *
     * In this test server has
     * - single coil
     * - two discrete inputs
     *
     * Binding is configured to read
     * - two coils
     * - two discrete inputs
     *
     * Items are follows
     * - first coil (Item1) -> no output since coil query should fail
     * - both coils (Item2 and Item3) -> should have valid output
     */
    @Test(expected = ExpectedFailure.class)
    public void testReadingTooMuchTwoSlaves()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        spi.addDigitalOut(new SimpleDigitalOut(true));
        spi.addDigitalIn(new SimpleDigitalIn(true));
        spi.addDigitalIn(new SimpleDigitalIn(false));

        binding = new ModbusBinding();
        Dictionary<String, Object> config = newLongPollBindingConfig();
        addSlave(config, SLAVE_NAME, ModbusBindingProvider.TYPE_COIL, null, 0, 2);
        addSlave(config, SLAVE2_NAME, ModbusBindingProvider.TYPE_DISCRETE, null, 0, 2);
        binding.updated(config);

        // Configure items
        final ModbusGenericBindingProvider provider = new ModbusGenericBindingProvider();
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format("%s:%d", SLAVE_NAME, 0));
        provider.processBindingConfiguration("test.items", new SwitchItem("Item2"),
                String.format("%s:%d", SLAVE2_NAME, 1));
        provider.processBindingConfiguration("test.items", new SwitchItem("Item3"),
                String.format("%s:%d", SLAVE2_NAME, 2));
        binding.setEventPublisher(eventPublisher);
        binding.addBindingProvider(provider);

        binding.execute();

        // Give the system some time to make the expected connections & requests
        waitForConnectionsReceived(2);
        waitForRequests(2);

        verify(eventPublisher, never()).postCommand(null, null);
        verify(eventPublisher, never()).sendCommand(null, null);
        try {
            verify(eventPublisher).postUpdate("Item2", OnOffType.ON);
            verify(eventPublisher).postUpdate("Item3", OnOffType.OFF);
            verifyNoMoreInteractions(eventPublisher);
        } catch (AssertionError e) {
            throw new ExpectedFailure();
        }
    }

}
