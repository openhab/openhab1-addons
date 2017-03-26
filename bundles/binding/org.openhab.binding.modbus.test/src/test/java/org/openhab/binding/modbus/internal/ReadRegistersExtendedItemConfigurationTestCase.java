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

import net.wimpi.modbus.procimg.SimpleInputRegister;
import net.wimpi.modbus.procimg.SimpleRegister;

/**
 * Tests for items with extended syntax. Run only against TCP server.
 *
 */
@RunWith(Parameterized.class)
public class ReadRegistersExtendedItemConfigurationTestCase extends TestCaseSupport {

    private ModbusGenericBindingProvider provider;
    private String type;

    @Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[][] { { ModbusBindingProvider.TYPE_HOLDING }, { ModbusBindingProvider.TYPE_INPUT } });
    }

    public ReadRegistersExtendedItemConfigurationTestCase(String type) {
        if (!type.equals(ModbusBindingProvider.TYPE_HOLDING) && !type.equals(ModbusBindingProvider.TYPE_INPUT)) {
            throw new IllegalStateException("Test does not support this type");
        }
        this.type = type;
    }

    private void addRegister(int value) {
        if (type.equals(ModbusBindingProvider.TYPE_HOLDING)) {
            spi.addRegister(new SimpleRegister(value));
        } else {
            spi.addInputRegister(new SimpleInputRegister(value));
        }
    }

    private void setRegister(int register, int newValue) {
        if (type.equals(ModbusBindingProvider.TYPE_HOLDING)) {
            spi.getRegister(register).setValue(newValue);
        } else {
            spi.setInputRegister(register, new SimpleInputRegister(newValue));
        }
    }

    @Before
    public void initSlaveAndServer() throws Exception {
        addRegister(3);
        addRegister(5);
        addRegister(0);
        addRegister(9);
        addRegister(16456);
        addRegister(62915);

        binding = new ModbusBinding();

        Dictionary<String, Object> config = newLongPollBindingConfig();

        addSlave(config, SLAVE_NAME, type, ModbusBindingProvider.VALUE_TYPE_INT16, 0, 6);
        binding.updated(config);
        // Configure items

        provider = new ModbusGenericBindingProvider();

        binding.setEventPublisher(eventPublisher);
        binding.addBindingProvider(provider);
    }

    @Test
    public void testNumberItemSingleReadRegisterDefaults()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new NumberItem("Item1"),
                String.format("<[%s:1:trigger=*]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", new DecimalType(5));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testNumberItemSingleReadRegisterDefaults2()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new NumberItem("Item1"),
                String.format("<[%s:1:trigger=*]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", new DecimalType(5));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testNumberItemSingleReadRegisterWithTriggerMatch()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new NumberItem("Item1"),
                String.format("<[%s:1:trigger=5]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", new DecimalType(5));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testNumberItemSingleReadRegisterWithTriggerNoMatch()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        // trigger 999 does not match the state 9
        provider.processBindingConfiguration("test.items", new NumberItem("Item1"),
                String.format("<[%s:1:trigger=999]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testNumberItemSingleReadRegisterWithValueTypeOverriden()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new NumberItem("Item1"),
                String.format("<[%s:0:trigger=*,valueType=int32]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        // 196613 in 32bit 2's complement -> 0x00030005
        verify(eventPublisher).postUpdate("Item1", new DecimalType(196613));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testNumberItemSingleReadRegisterWithValueTypeOverriden2()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new NumberItem("Item1"),
                String.format("<[%s:2:trigger=*,valueType=float32]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        // constructed from 16456 and 62915
        verify(eventPublisher).postUpdate("Item1", new DecimalType(3.1400001049041748046875));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testSwitchItemSingleReadRegisterDefaults()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format("<[%s:1:trigger=*]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", OnOffType.ON);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testSwitchItemSingleReadRegisterWithTriggerMatch()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format("<[%s:1:type=STATE,trigger=ON]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", OnOffType.ON);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testSwitchItemSingleReadRegisterWithTriggerMatch2()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format("<[%s:2:trigger=OFF]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", OnOffType.OFF);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testSwitchItemSingleReadRegisterWithTriggerNoMatch()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format("<[%s:1:trigger=OFF]", SLAVE_NAME));
        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testNumberItemSingleReadRegisterComplexTransformation()
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
        verify(eventPublisher).postUpdate("Item1", new DecimalType(25));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testNumberItemSingleReadRegisterComplexTransformation2()
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
                                return "foobar_" + String.valueOf(Integer.valueOf(multiplier) * Integer.valueOf(arg));
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
        verify(eventPublisher).postUpdate("Item1", new StringType("foobar_25"));
        verifyNoMoreInteractions(eventPublisher);

        // Execute again, since updates unchanged a new event should be sent
        reset(eventPublisher);
        binding.execute();
        waitForConnectionsReceived(2);
        waitForRequests(2);
        verify(eventPublisher).postUpdate("Item1", new StringType("foobar_25"));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testNumberItemSingleReadRegisterComplexTransformation2DefaultTrigger()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new StringItem("Item1"),
                String.format("<[%s:1:transformation=STRINGMULTIPLY(5)]", SLAVE_NAME));

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
                    } else {
                        throw new AssertionError("unexpected transformation");
                    }
                }

            });
        }

        binding.execute();

        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", new StringType("foobar_25"));
        verifyNoMoreInteractions(eventPublisher);

        // Execute again, since does not update unchanged (slave default) *NO* new event should be sent
        reset(eventPublisher);
        binding.execute();
        waitForConnectionsReceived(2);
        waitForRequests(2);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testNumberItemReadTwoRegistersComplexTransformation3()
            throws UnknownHostException, ConfigurationException, BindingConfigParseException {

        provider.processBindingConfiguration("test.items", new StringItem("Item1"),
                String.format(
                        "<[%1$s:1:trigger=CHANGED,transformation=STRINGMULTIPLY(5)],<[%1$s:2:trigger=CHANGED,transformation=PLUS(7)]",
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
        verify(eventPublisher).postUpdate("Item1", new StringType("foobar_25"));
        verify(eventPublisher).postUpdate("Item1", new StringType("foobar_7"));
        verifyNoMoreInteractions(eventPublisher);

        reset(eventPublisher);
        binding.execute();

        waitForConnectionsReceived(2);
        waitForRequests(2);

        // no events on the second time, nothing has changed
        verifyNoMoreInteractions(eventPublisher);

        // modify server data
        setRegister(2, 5);

        reset(eventPublisher);
        binding.execute();

        waitForConnectionsReceived(3);
        waitForRequests(3);

        // only the PLUS connection was activated
        verify(eventPublisher).postUpdate("Item1", new StringType("foobar_12"));
        verifyNoMoreInteractions(eventPublisher);
    }

}
