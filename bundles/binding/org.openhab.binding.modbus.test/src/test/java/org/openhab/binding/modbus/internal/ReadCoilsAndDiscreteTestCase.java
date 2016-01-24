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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.core.library.types.OnOffType;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.service.cm.ConfigurationException;

import net.wimpi.modbus.procimg.DigitalIn;
import net.wimpi.modbus.procimg.DigitalOut;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleDigitalIn;
import net.wimpi.modbus.procimg.SimpleDigitalOut;

/**
 * Parameterized test case that tests reading of both coils (i.e. boolean 0/1
 * output signals) and discrete inputs (i.e. boolean 0/1 inputs signals)
 * registers
 */
@RunWith(Parameterized.class)
public class ReadCoilsAndDiscreteTestCase extends TestCaseSupport {

    @Parameters
    public static Collection<Object[]> parameters() {
        List<Object[]> allParameters = new ArrayList<Object[]>();
        List<Object[]> baseParameters = Arrays.asList(new Object[][] {
                { false, ModbusBindingProvider.TYPE_COIL, SimpleDigitalOut.class, "addDigitalOut", DigitalOut.class },
                { true, ModbusBindingProvider.TYPE_COIL, SimpleDigitalOut.class, "addDigitalOut", DigitalOut.class },
                { false, ModbusBindingProvider.TYPE_DISCRETE, SimpleDigitalIn.class, "addDigitalIn", DigitalIn.class },
                { true, ModbusBindingProvider.TYPE_DISCRETE, SimpleDigitalIn.class, "addDigitalIn",
                        DigitalIn.class } });
        for (ServerType serverType : TEST_SERVERS) {
            for (Object[] params : baseParameters) {
                ArrayList<Object> paramsWithServer = new ArrayList<Object>();
                paramsWithServer.add(serverType);
                paramsWithServer.addAll(Arrays.asList(params));
                allParameters.add(paramsWithServer.toArray());
            }
        }

        return allParameters;
    }

    private boolean nonZeroOffset;
    private Constructor<Register> constructBoolStore;
    private String type;
    private Method addRegisterMethod;
    private String spiAddRegisterMethodName;
    private Class<?> addRegisterArgClass;

    /**
     * @param serverType
     * @param nonZeroOffset
     *            whether to test non-zero start address in modbus binding
     * @param type
     *            type of the slave (e.g. "holding")
     * @param storeClass
     *            "register" class to instantiate when configuring SPI of the
     *            server
     * @param spiAddRegisterMethodName
     *            method to call when adding register to SPI
     * @param addRegisterArgClass
     *            argument type of the method corresponding to
     *            spiAddRegisterMethodName
     */
    public ReadCoilsAndDiscreteTestCase(ServerType serverType, boolean nonZeroOffset, String type,
            Class<Register> storeClass, String spiAddRegisterMethodName, Class<?> addRegisterArgClass)
                    throws NoSuchMethodException, SecurityException {
        this.serverType = serverType;
        this.nonZeroOffset = nonZeroOffset;
        this.type = type;
        this.spiAddRegisterMethodName = spiAddRegisterMethodName;
        this.addRegisterArgClass = addRegisterArgClass;
        constructBoolStore = storeClass.getDeclaredConstructor(new Class[] { boolean.class });
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        addRegisterMethod = spi.getClass().getMethod(spiAddRegisterMethodName, new Class[] { addRegisterArgClass });
    }

    /**
     * Test reading of discrete inputs/outputs, uses default valuetype
     */
    @Test
    public void testReadDigitals()
            throws InterruptedException, UnknownHostException, BindingConfigParseException, ConfigurationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        // Modbus server ("modbus slave") has two digital inputs/outputs
        addRegisterMethod.invoke(spi, constructBoolStore.newInstance(false));
        addRegisterMethod.invoke(spi, constructBoolStore.newInstance(true));
        addRegisterMethod.invoke(spi, constructBoolStore.newInstance(false));

        binding = new ModbusBinding();
        binding.updated(addSlave(newLongPollBindingConfig(), SLAVE_NAME, type, null, nonZeroOffset ? 1 : 0, 2));
        configureSwitchItemBinding(2, SLAVE_NAME, 0);
        binding.execute();

        // Give the system some time to make the expected connections & requests
        waitForConnectionsReceived(1);
        waitForRequests(1);

        verify(eventPublisher, never()).postCommand(null, null);
        verify(eventPublisher, never()).sendCommand(null, null);
        if (nonZeroOffset) {
            verify(eventPublisher).postUpdate("Item1", OnOffType.ON);
            verify(eventPublisher).postUpdate("Item2", OnOffType.OFF);
        } else {
            verify(eventPublisher).postUpdate("Item1", OnOffType.OFF);
            verify(eventPublisher).postUpdate("Item2", OnOffType.ON);
        }
        verifyNoMoreInteractions(eventPublisher);
    }

}
