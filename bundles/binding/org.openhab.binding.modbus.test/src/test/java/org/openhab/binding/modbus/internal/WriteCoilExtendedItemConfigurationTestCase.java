/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Dictionary;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.binding.modbus.internal.Transformation.TransformationHelperWrapper;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationService;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.framework.BundleContext;

import net.wimpi.modbus.procimg.SimpleDigitalOut;

/**
 * Tests for items with extended syntax. Run only against TCP server.
 *
 */
public class WriteCoilExtendedItemConfigurationTestCase extends TestCaseSupport {

    private ModbusGenericBindingProvider provider;

    @Before
    public void initSlaveAndServer() throws Exception {
        spi.addDigitalOut(new SimpleDigitalOut(true));
        spi.addDigitalOut(new SimpleDigitalOut(false));
        //
        binding = new ModbusBinding();
        Dictionary<String, Object> config = newLongPollBindingConfig();
        addSlave(config, SLAVE_NAME, ModbusBindingProvider.TYPE_COIL, null, 0, 2);
        binding.updated(config);
        // Configure items

        provider = new ModbusGenericBindingProvider();

        binding.setEventPublisher(eventPublisher);
        binding.addBindingProvider(provider);
    }

    @Test
    public void testWriteCoilItemManyConnections() throws BindingConfigParseException {
        // Inspired by https://github.com/openhab/openhab/issues/4745
        // item reads from coil index 0, and writes to coil index 1. Both ON and OFF are translated to "true" on the
        // wire
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format(
                        "<[%1$s:0:trigger=*],>[%1$s:1:trigger=ON,transformation=1],>[%1$s:1:trigger=OFF,transformation=1]",
                        SLAVE_NAME));
        binding.execute();
        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", OnOffType.ON);
        verifyNoMoreInteractions(eventPublisher);
        assertThat(spi.getDigitalOut(0).isSet(), is(equalTo(true)));
        assertThat(spi.getDigitalOut(1).isSet(), is(equalTo(false)));

        // send OFF to the item and ensure that coil is set as specified by the transformation
        binding.receiveCommand("Item1", OnOffType.OFF);
        assertThat(spi.getDigitalOut(0).isSet(), is(equalTo(true)));
        assertThat(spi.getDigitalOut(1).isSet(), is(equalTo(true)));
        verifyNoMoreInteractions(eventPublisher);

        // reset coil
        spi.getDigitalOut(1).set(false);
        assertThat(spi.getDigitalOut(1).isSet(), is(equalTo(false)));

        // send ON to the item
        binding.receiveCommand("Item1", OnOffType.ON);
        assertThat(spi.getDigitalOut(0).isSet(), is(equalTo(true)));
        assertThat(spi.getDigitalOut(1).isSet(), is(equalTo(true)));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testWriteCoilItemNonNumericConstantTransformationManyConnections() throws BindingConfigParseException {
        // Inspired by https://github.com/openhab/openhab/issues/4745
        // item reads from coil index 0, and writes to coil index 1. Both ON and OFF are translated to "true" on the
        // wire
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format(
                        "<[%1$s:0:trigger=*],>[%1$s:1:trigger=ON,transformation=ON],>[%1$s:1:trigger=OFF,transformation=ON]",
                        SLAVE_NAME));
        binding.execute();
        waitForConnectionsReceived(1);
        waitForRequests(1);
        verify(eventPublisher).postUpdate("Item1", OnOffType.ON);
        verifyNoMoreInteractions(eventPublisher);
        assertThat(spi.getDigitalOut(0).isSet(), is(equalTo(true)));
        assertThat(spi.getDigitalOut(1).isSet(), is(equalTo(false)));

        // send OFF to the item and ensure that coil is set as specified by the transformation
        binding.receiveCommand("Item1", OnOffType.OFF);
        assertThat(spi.getDigitalOut(0).isSet(), is(equalTo(true)));
        assertThat(spi.getDigitalOut(1).isSet(), is(equalTo(true)));
        verifyNoMoreInteractions(eventPublisher);

    }

    @Test
    public void testCoilWriteFiltered() throws BindingConfigParseException {
        provider.processBindingConfiguration("test.items", new SwitchItem("Item1"),
                String.format(">[%1$s:1:trigger=ON]", SLAVE_NAME));
        binding.execute();
        waitForConnectionsReceived(1);
        waitForRequests(1);
        verifyNoMoreInteractions(eventPublisher);

        assertThat(spi.getDigitalOut(0).isSet(), is(equalTo(true)));
        assertThat(spi.getDigitalOut(1).isSet(), is(equalTo(false)));

        // send OFF to the item -- it is not processed
        binding.receiveCommand("Item1", OnOffType.OFF);
        assertThat(spi.getDigitalOut(0).isSet(), is(equalTo(true)));
        assertThat(spi.getDigitalOut(1).isSet(), is(equalTo(false)));
        verifyNoMoreInteractions(eventPublisher);

        // ON command is processed
        binding.receiveCommand("Item1", OnOffType.ON);
        assertThat(spi.getDigitalOut(0).isSet(), is(equalTo(true)));
        assertThat(spi.getDigitalOut(1).isSet(), is(equalTo(true)));
        verifyNoMoreInteractions(eventPublisher);

    }

    @Test
    public void testRegisterStringItemWriteNumberItemComplexTransformation() throws BindingConfigParseException {

        provider.processBindingConfiguration("test.items", new StringItem("Item1"),
                String.format(">[%1$s:1:trigger=*,transformation=PARSEBOOL()]", SLAVE_NAME));

        ModbusBindingConfig config = provider.getConfig("Item1");

        // Inject transformation
        for (ItemIOConnection itemIOConnection : config.getWriteConnections()) {
            itemIOConnection.getTransformation().setTransformationHelper(new TransformationHelperWrapper() {

                @Override
                public TransformationService getTransformationService(BundleContext context,
                        String transformationServiceName) {
                    if ("PARSEBOOL".equals(transformationServiceName)) {
                        return new TransformationService() {

                            @Override
                            public String transform(String ignored, String arg) throws TransformationException {
                                if (arg.equals("true") || arg.equals("T")) {
                                    return "1";
                                } else if (arg.equals("false") || arg.equals("F")) {
                                    return "0";
                                } else {
                                    throw new AssertionError("unexpected arg in test");
                                }
                            }
                        };
                    } else {
                        throw new AssertionError("unexpected transformation");
                    }
                }

            });
        }

        binding.execute();
        verifyNoMoreInteractions(eventPublisher); // write-only item, no event sent
        binding.receiveCommand("Item1", new StringType("T"));
        assertThat(spi.getDigitalOut(0).isSet(), is(equalTo(true)));
        assertThat(spi.getDigitalOut(1).isSet(), is(equalTo(true)));

        binding.receiveCommand("Item1", new StringType("F"));
        assertThat(spi.getDigitalOut(0).isSet(), is(equalTo(true)));
        assertThat(spi.getDigitalOut(1).isSet(), is(equalTo(false)));
    }

}
