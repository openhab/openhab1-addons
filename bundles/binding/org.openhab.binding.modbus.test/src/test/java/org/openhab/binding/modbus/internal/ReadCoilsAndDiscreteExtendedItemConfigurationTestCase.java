/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import static org.mockito.Mockito.*;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.binding.modbus.internal.Transformation.TransformationHelperWrapper;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationService;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;

import net.wimpi.modbus.procimg.SimpleDigitalIn;
import net.wimpi.modbus.procimg.SimpleDigitalOut;

/**
 * Tests for items with extended syntax. Run only against TCP server.
 *
 */
@RunWith(Parameterized.class)
public class ReadCoilsAndDiscreteExtendedItemConfigurationTestCase extends TestCaseSupport {

    private ModbusGenericBindingProvider provider;
    private String type;

    @Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[][] { { ModbusBindingProvider.TYPE_DISCRETE }, { ModbusBindingProvider.TYPE_COIL } });
    }

    public ReadCoilsAndDiscreteExtendedItemConfigurationTestCase(String type) {
        if (!type.equals(ModbusBindingProvider.TYPE_DISCRETE) && !type.equals(ModbusBindingProvider.TYPE_COIL)) {
            throw new IllegalStateException("Test does not support this type");
        }
        this.type = type;
    }

    private void addBinary(boolean value) {
        if (type.equals(ModbusBindingProvider.TYPE_DISCRETE)) {
            spi.addDigitalIn(new SimpleDigitalIn(value));
        } else {
            spi.addDigitalOut(new SimpleDigitalOut(value));
        }
    }

    private void setBinary(int idx, boolean value) {
        if (type.equals(ModbusBindingProvider.TYPE_DISCRETE)) {
            spi.setDigitalIn(idx, new SimpleDigitalIn(value));
        } else {
            spi.setDigitalOut(idx, new SimpleDigitalOut(value));
        }
    }

    @Before
    public void initSlaveAndServer() throws Exception {
        addBinary(false);
        addBinary(true);
        binding = new ModbusBinding();
        Dictionary<String, Object> config = newLongPollBindingConfig();
        addSlave(config, SLAVE_NAME, type, null, 0, 2);
        binding.updated(config);

        provider = new ModbusGenericBindingProvider();

        binding.setEventPublisher(eventPublisher);
        binding.addBindingProvider(provider);
    }

    @Test
    public void testSwitchItemSingleReadWithTriggerNoMatch()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format("<[%s:0:trigger=ON]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testSwitchItemSingleReadWithTriggerMatch()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format("<[%s:0:trigger=OFF]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", OnOffType.OFF);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testSwitchItemSingleReadWithTriggerMatch2()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format("<[%s:1:trigger=ON]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", OnOffType.ON);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testReadOnlyItemCommandNoEffect()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format("<[%s:1:trigger=ON]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", OnOffType.ON);
        verifyNoMoreInteractions(eventPublisher);
        binding.receiveCommand("Item1", OnOffType.OFF);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testNumberItemSingleReadRegisterComplextTransformation()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new NumberItem("Item1"),
                String.format("<[%s:1:trigger=*,transformation=MULTIPLY(5)]", SLAVE_NAME));

        ModbusBindingConfig config = provider.getConfig("Item1");
        // Inject transformation
        for (ItemIOConnection itemIOConnection : config.getReadConnections()) {
            itemIOConnection.getTransformation().setTransformationHelper(new TransformationHelperWrapper() {

                @Override
                public TransformationService getTransformationService(BundleContext context,
                        String transformationServiceName) {
                    if ("MULTIPLY".equals(transformationServiceName)) {
                        return new TransformationService() {

                            @Override
                            public String transform(String multiplier, String arg) throws TransformationException {
                                return String.valueOf(Integer.valueOf(multiplier) * Integer.valueOf(arg));
                            }
                        };
                    } else {
                        throw new AssertionError("unexpected transformation");
                    }
                }

            });
        }

        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        // boolean was converted to number 5 (True -> 1 -> 1*5 = 5)
        verify(eventPublisher).postUpdate("Item1", new DecimalType(5));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testNumberItemSingleReadRegisterComplextTransformation2()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new StringItem("Item1"),
                String.format("<[%s:1:trigger=*,transformation=STRINGMULTIPLY(5)]", SLAVE_NAME));

        ModbusBindingConfig config = provider.getConfig("Item1");
        // Inject transformation
        for (ItemIOConnection itemIOConnection : config.getReadConnections()) {
            itemIOConnection.getTransformation().setTransformationHelper(new TransformationHelperWrapper() {

                @Override
                public TransformationService getTransformationService(BundleContext context,
                        String transformationServiceName) {
                    if ("STRINGMULTIPLY".equals(transformationServiceName)) {
                        return new TransformationService() {

                            @Override
                            public String transform(String multiplier, String arg) throws TransformationException {
                                return "foob" + String.valueOf(Integer.valueOf(multiplier) * Integer.valueOf(arg));
                            }
                        };
                    } else {
                        throw new AssertionError("unexpected transformation");
                    }
                }

            });
        }

        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", new StringType("foob" + 5));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testNumberItemReadTwoDiscreteComplexTransformation3()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {

        provider.processBindingConfiguration("test.items", new StringItem("Item1"),
                String.format(
                        "<[%1$s:0:type=STATE,trigger=CHANGED,transformation=STRINGMULTIPLY(5)],<[%1$s:1:type=STATE,trigger=CHANGED,transformation=PLUS(7)]",
                        SLAVE_NAME));

        ModbusBindingConfig config = provider.getConfig("Item1");
        // Inject transformation
        for (ItemIOConnection itemIOConnection : config.getReadConnections()) {
            itemIOConnection.getTransformation().setTransformationHelper(new TransformationHelperWrapper() {

                @Override
                public TransformationService getTransformationService(BundleContext context,
                        String transformationServiceName) {
                    if ("STRINGMULTIPLY".equals(transformationServiceName)) {
                        return new TransformationService() {

                            @Override
                            public String transform(String multiplier, String arg) throws TransformationException {
                                return "foobar_" + String.valueOf(Integer.valueOf(multiplier) * Integer.valueOf(arg));
                            }
                        };
                    } else if ("PLUS".equals(transformationServiceName)) {
                        return new TransformationService() {

                            @Override
                            public String transform(String offset, String arg) throws TransformationException {
                                return "foobar_" + String.valueOf(Integer.valueOf(offset) + Integer.valueOf(arg));
                            }
                        };
                    }

                    else {
                        throw new AssertionError("unexpected transformation");
                    }
                }

            });
        }

        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        // two events on the first time(the changed-on-poll check is done for each connection separately)
        verify(eventPublisher).postUpdate("Item1", new StringType("foobar_0")); // false * 5 = 0
        verify(eventPublisher).postUpdate("Item1", new StringType("foobar_8")); // true + 7 = 8
        verifyNoMoreInteractions(eventPublisher);

        reset(eventPublisher);
        binding.execute();

        waitForConnectionsReceived(2);
        waitForRequests(2);

        // no events on the second time, nothing has changed
        verifyNoMoreInteractions(eventPublisher);

        // modify server data
        setBinary(0, true);

        reset(eventPublisher);
        binding.execute();

        waitForConnectionsReceived(3);
        waitForRequests(3);

        // only the PLUS connection was activated
        verify(eventPublisher).postUpdate("Item1", new StringType("foobar_5")); // true * 5 = 5
        verifyNoMoreInteractions(eventPublisher);
    }

}
