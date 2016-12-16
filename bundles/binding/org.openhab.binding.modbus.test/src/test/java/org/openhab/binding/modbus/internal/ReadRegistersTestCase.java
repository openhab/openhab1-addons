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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.service.cm.ConfigurationException;

import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleInputRegister;
import net.wimpi.modbus.procimg.SimpleRegister;

/**
 * Parameterized test case that tests reading of both input and holding
 * registers
 */
@RunWith(Parameterized.class)
public class ReadRegistersTestCase extends TestCaseSupport {

    @Parameters
    public static Collection<Object[]> parameters() {
        List<Object[]> allParameters = new ArrayList<Object[]>();
        List<Object[]> baseParameters = Arrays.asList(new Object[][] {
                { false, ModbusBindingProvider.TYPE_INPUT, SimpleInputRegister.class, "addInputRegister",
                        InputRegister.class },
                { true, ModbusBindingProvider.TYPE_INPUT, SimpleInputRegister.class, "addInputRegister",
                        InputRegister.class },
                { false, ModbusBindingProvider.TYPE_HOLDING, SimpleRegister.class, "addRegister", Register.class },
                { true, ModbusBindingProvider.TYPE_HOLDING, SimpleRegister.class, "addRegister", Register.class } });
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
    private Constructor<Register> constructRegisterInt;
    private Constructor<Register> constructRegister2Byte;
    private String type;
    private Method addRegisterMethod;
    private String spiAddRegisterMethodName;
    private Class<?> addRegisterArgClass;

    private byte[] int32AsRegisters(int value) throws IOException {
        /**
         * Return value converted to bytes
         *
         * Bytes are returned in most significant bit (MSB) order
         */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(value); // writes all 4 bytes as MSB order
        byte[] byteArray = baos.toByteArray();
        return byteArray;
    }

    private byte[] float32AsRegisters(float value) throws IOException {
        /**
         * Return value converted to bytes
         *
         * Bytes are returned in most significant bit (MSB) order
         */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeFloat(value); // writes all 4 bytes as MSB order
        byte[] byteArray = baos.toByteArray();
        return byteArray;
    }

    /**
     * @param serverType
     * @param nonZeroOffset
     *            whether to test non-zero start address in modbus binding
     * @param type
     *            type of the slave (e.g. "holding")
     * @param registerClass
     *            register class to instantiate when configuring SPI of the
     *            server
     * @param spiAddRegisterMethodName
     *            method to call when adding register to SPI
     * @param addRegisterArgClass
     *            argument type of the method corresponding to
     *            spiAddRegisterMethodName
     */
    public ReadRegistersTestCase(ServerType serverType, boolean nonZeroOffset, String type,
            Class<Register> registerClass, String spiAddRegisterMethodName, Class<?> addRegisterArgClass)
                    throws NoSuchMethodException, SecurityException {
        this.serverType = serverType;
        this.nonZeroOffset = nonZeroOffset;
        this.type = type;
        this.spiAddRegisterMethodName = spiAddRegisterMethodName;
        this.addRegisterArgClass = addRegisterArgClass;
        constructRegisterInt = registerClass.getDeclaredConstructor(new Class[] { int.class });
        constructRegister2Byte = registerClass.getDeclaredConstructor(new Class[] { byte.class, byte.class });
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        addRegisterMethod = spi.getClass().getMethod(spiAddRegisterMethodName, new Class[] { addRegisterArgClass });
    }

    /**
     * Test reading of input/holding registers, uses valuetype=int8
     */
    @Test
    public void testReadRegistersInt8()
            throws InterruptedException, UnknownHostException, BindingConfigParseException, ConfigurationException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // Modbus server ("modbus slave") has input registers
        // first register has following bytes (hi byte, lo byte)
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance((byte) 1, (byte) 2));
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance((byte) 3, (byte) -4));
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance((byte) 5, (byte) 6));
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(99));

        binding = new ModbusBinding();
        binding.updated(addSlave(newLongPollBindingConfig(), SLAVE_NAME, type, ModbusBindingProvider.VALUE_TYPE_INT8,
                nonZeroOffset ? 1 : 0, 2));
        configureNumberItemBinding(4, SLAVE_NAME, 0);
        binding.execute();

        // Give the system some time to make the expected connections & requests
        waitForConnectionsReceived(1);
        waitForRequests(1);

        verify(eventPublisher, never()).postCommand(null, null);
        verify(eventPublisher, never()).sendCommand(null, null);

        if (nonZeroOffset) {
            // 2nd register, lo byte
            verify(eventPublisher).postUpdate("Item1", new DecimalType(-4));
            // 2nd register, hi byte
            verify(eventPublisher).postUpdate("Item2", new DecimalType(3));
            // 3rd register, lo byte
            verify(eventPublisher).postUpdate("Item3", new DecimalType(6));
            // 3rd register, hi byte
            verify(eventPublisher).postUpdate("Item4", new DecimalType(5));
        } else {
            // 1st register, lo byte
            verify(eventPublisher).postUpdate("Item1", new DecimalType(2));
            // 1st register, hi byte
            verify(eventPublisher).postUpdate("Item2", new DecimalType(1));
            // 2nd register, lo byte
            verify(eventPublisher).postUpdate("Item3", new DecimalType(-4));
            // 2nd register, hi byte
            verify(eventPublisher).postUpdate("Item4", new DecimalType(3));
        }
        verifyNoMoreInteractions(eventPublisher);
    }

    /**
     * Test reading of input/holding registers, uses valuetype=uint8
     */
    @Test
    public void testReadRegistersUint8()
            throws InterruptedException, UnknownHostException, BindingConfigParseException, ConfigurationException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // Modbus server ("modbus slave") has input registers
        // first register has following bytes (hi byte, lo byte)
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance((byte) 1, (byte) 2));
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance((byte) 3, (byte) -4));
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance((byte) 5, (byte) 6));
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(99));

        binding = new ModbusBinding();
        binding.updated(addSlave(newLongPollBindingConfig(), SLAVE_NAME, type, ModbusBindingProvider.VALUE_TYPE_UINT8,
                nonZeroOffset ? 1 : 0, 2));
        Assert.assertEquals(REFRESH_INTERVAL, binding.getRefreshInterval());
        configureNumberItemBinding(4, SLAVE_NAME, 0);
        binding.execute();

        // Give the system some time to make the expected connections & requests
        waitForConnectionsReceived(1);
        waitForRequests(1);

        verify(eventPublisher, never()).postCommand(null, null);
        verify(eventPublisher, never()).sendCommand(null, null);

        if (nonZeroOffset) {
            // 2nd register, lo byte
            verify(eventPublisher).postUpdate("Item1", new DecimalType(256 - 4));
            // 2nd register, hi byte
            verify(eventPublisher).postUpdate("Item2", new DecimalType(3));
            // 3rd register, lo byte
            verify(eventPublisher).postUpdate("Item3", new DecimalType(6));
            // 3rd register, hi byte
            verify(eventPublisher).postUpdate("Item4", new DecimalType(5));
        } else {
            // 1st register, lo byte
            verify(eventPublisher).postUpdate("Item1", new DecimalType(2));
            // 1st register, hi byte
            verify(eventPublisher).postUpdate("Item2", new DecimalType(1));
            // 2nd register, lo byte
            verify(eventPublisher).postUpdate("Item3", new DecimalType(256 - 4));
            // 2nd register, hi byte
            verify(eventPublisher).postUpdate("Item4", new DecimalType(3));
        }
        verifyNoMoreInteractions(eventPublisher);
    }

    /**
     * Test reading of input/holding registers, uses default valuetype
     */
    @Test
    public void testReadRegistersUint16()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            UnknownHostException, ConfigurationException, BindingConfigParseException {
        // Modbus server ("modbus slave") has input registers
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(2));
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(-4));
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(99));

        binding = new ModbusBinding();
        binding.updated(addSlave(newLongPollBindingConfig(), SLAVE_NAME, type, null, nonZeroOffset ? 1 : 0, 2));
        configureNumberItemBinding(2, SLAVE_NAME, 0);
        binding.execute();

        // Give the system some time to make the expected connections & requests
        waitForConnectionsReceived(1);
        waitForRequests(1);

        verify(eventPublisher, never()).postCommand(null, null);
        verify(eventPublisher, never()).sendCommand(null, null);

        if (nonZeroOffset) {
            verify(eventPublisher).postUpdate("Item1", new DecimalType(65532));
            verify(eventPublisher).postUpdate("Item2", new DecimalType(99));
        } else {
            verify(eventPublisher).postUpdate("Item1", new DecimalType(2));
            verify(eventPublisher).postUpdate("Item2", new DecimalType(65532));
        }
        verifyNoMoreInteractions(eventPublisher);
    }

    /**
     * Test reading of input/holding registers, uses valuetype=int16
     */
    @Test
    public void testReadRegistersInt16()
            throws InterruptedException, UnknownHostException, BindingConfigParseException, ConfigurationException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // Modbus server ("modbus slave") has input registers
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(2));
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(-4));
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(99));

        binding = new ModbusBinding();
        binding.updated(addSlave(newLongPollBindingConfig(), SLAVE_NAME, type, ModbusBindingProvider.VALUE_TYPE_INT16,
                nonZeroOffset ? 1 : 0, 2));
        configureNumberItemBinding(2, SLAVE_NAME, 0);
        binding.execute();

        // Give the system some time to make the expected connections & requests
        waitForConnectionsReceived(1);
        waitForRequests(1);

        verify(eventPublisher, never()).postCommand(null, null);
        verify(eventPublisher, never()).sendCommand(null, null);

        if (nonZeroOffset) {
            verify(eventPublisher).postUpdate("Item1", new DecimalType(-4));
            verify(eventPublisher).postUpdate("Item2", new DecimalType(99));
        } else {
            verify(eventPublisher).postUpdate("Item1", new DecimalType(2));
            verify(eventPublisher).postUpdate("Item2", new DecimalType(-4));
        }
        verifyNoMoreInteractions(eventPublisher);
    }

    /**
     * Test reading of input/holding registers, uses valuetype=bit
     *
     * Items refer to individual bits (lowest significant bit = index 0) of the
     * 16bits registers.
     *
     * In this test, we have items referring to the all 32bits (two registers)
     */
    @Test
    public void testReadRegistersBit()
            throws InterruptedException, UnknownHostException, BindingConfigParseException, ConfigurationException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // Modbus server ("modbus slave") has input registers
        // 0x0002 = 00000000 00000010
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(2));
        // 0xFFFC = 11111111 11111100
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(-4));
        // 0x0063 = 00000000 01100011
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(99));

        binding = new ModbusBinding();
        binding.updated(addSlave(newLongPollBindingConfig(), SLAVE_NAME, type, ModbusBindingProvider.VALUE_TYPE_BIT,
                nonZeroOffset ? 1 : 0, 2));
        configureSwitchItemBinding(32, SLAVE_NAME, 0);
        binding.execute();

        // Give the system some time to make the expected connections & requests
        waitForConnectionsReceived(1);
        waitForRequests(1);

        verify(eventPublisher, never()).postCommand(null, null);
        verify(eventPublisher, never()).sendCommand(null, null);

        // Bits should correspond to bits of the register, in LSB order.
        if (nonZeroOffset) {
            verifyBitItems(new StringBuffer("1111111111111100").reverse().toString());
            verifyBitItems(new StringBuffer("0000000001100011").reverse().toString(), 16);
        } else {
            // 1st register bits
            verifyBitItems(new StringBuffer("0000000000000010").reverse().toString());
            // 2nd register bits
            verifyBitItems(new StringBuffer("1111111111111100").reverse().toString(), 16);
        }
        verifyNoMoreInteractions(eventPublisher);
    }

    /**
     * Test reading of input/holding registers, uses valuetype=uint32
     *
     * @throws IOException
     */
    @Test
    public void testReadRegistersUint32()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            ConfigurationException, BindingConfigParseException, IOException {
        // Modbus server ("modbus slave") has input registers
        byte[] registerData = int32AsRegisters(123456789); // 0x075BCD15
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[0], registerData[1]));
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[2], registerData[3]));
        registerData = int32AsRegisters(-123456789); // 0xF8A432EB
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[0], registerData[1]));
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[2], registerData[3]));
        registerData = int32AsRegisters(123456788); // 0x075BCD14
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[0], registerData[1]));
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[2], registerData[3]));

        binding = new ModbusBinding();
        // read 4 registers = 2 uint32 numbers
        binding.updated(addSlave(newLongPollBindingConfig(), SLAVE_NAME, type, ModbusBindingProvider.VALUE_TYPE_UINT32,
                nonZeroOffset ? 1 : 0, 4));
        configureNumberItemBinding(2, SLAVE_NAME, 0);
        binding.execute();

        // Give the system some time to make the expected connections & requests
        waitForConnectionsReceived(1);
        waitForRequests(1);

        verify(eventPublisher, never()).postCommand(null, null);
        verify(eventPublisher, never()).sendCommand(null, null);

        if (nonZeroOffset) {
            // 3440769188 = 0xCD15 F8A4 = (1st register lo byte, 2nd register hi
            // byte)
            verify(eventPublisher).postUpdate("Item1", new DecimalType(3440769188L));
            // 854263643 = 0x32EB075B = (2nd register lo byte, 3rd register hi
            // byte)
            verify(eventPublisher).postUpdate("Item2", new DecimalType(854263643));
        } else {
            verify(eventPublisher).postUpdate("Item1", new DecimalType(123456789));
            verify(eventPublisher).postUpdate("Item2", new DecimalType(4294967296L - 123456789L));
        }
        verifyNoMoreInteractions(eventPublisher);
    }

    /**
     * Test reading of input/holding registers, uses valuetype=int32
     *
     * @throws IOException
     */
    @Test
    public void testReadRegistersInt32()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            ConfigurationException, BindingConfigParseException, IOException {
        // Modbus server ("modbus slave") has input registers
        byte[] registerData = int32AsRegisters(123456789); // 0x075BCD15
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[0], registerData[1]));
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[2], registerData[3]));
        registerData = int32AsRegisters(-123456789); // 0xF8A432EB
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[0], registerData[1]));
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[2], registerData[3]));
        registerData = int32AsRegisters(123456788); // 0x075BCD14
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[0], registerData[1]));
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[2], registerData[3]));

        binding = new ModbusBinding();
        // read 4 registers = 2 uint32 numbers
        binding.updated(addSlave(newLongPollBindingConfig(), SLAVE_NAME, type, ModbusBindingProvider.VALUE_TYPE_INT32,
                nonZeroOffset ? 1 : 0, 4));
        configureNumberItemBinding(2, SLAVE_NAME, 0);
        binding.execute();

        // Give the system some time to make the expected connections & requests
        waitForConnectionsReceived(1);
        waitForRequests(1);

        verify(eventPublisher, never()).postCommand(null, null);
        verify(eventPublisher, never()).sendCommand(null, null);

        if (nonZeroOffset) {
            // -854198108 = 0xCD15F8A4 = (1st register lo byte, 2nd register hi
            // byte)
            verify(eventPublisher).postUpdate("Item1", new DecimalType(-854198108));
            // 854263643 = 0x32EB 075B = (2nd register lo byte, 3rd register hi
            // byte)
            verify(eventPublisher).postUpdate("Item2", new DecimalType(854263643));
        } else {
            verify(eventPublisher).postUpdate("Item1", new DecimalType(123456789));
            verify(eventPublisher).postUpdate("Item2", new DecimalType(-123456789));
        }
        verifyNoMoreInteractions(eventPublisher);
    }

    /**
     * Test reading of input/holding registers, uses valuetype=float32
     *
     * @throws IOException
     */
    @Test
    public void testReadRegistersFloat32()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            ConfigurationException, BindingConfigParseException, IOException {
        // Modbus server ("modbus slave") has input registers
        byte[] registerData = float32AsRegisters(123456789.95623f);
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[0], registerData[1]));
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[2], registerData[3]));
        registerData = float32AsRegisters(-123456789.1241243f);
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[0], registerData[1]));
        addRegisterMethod.invoke(spi, constructRegister2Byte.newInstance(registerData[2], registerData[3]));

        binding = new ModbusBinding();
        // read 4 registers = 2 uint32 numbers
        binding.updated(
                addSlave(newLongPollBindingConfig(), SLAVE_NAME, type, ModbusBindingProvider.VALUE_TYPE_FLOAT32, 0, 4));
        configureNumberItemBinding(2, SLAVE_NAME, 0);
        binding.execute();

        // Give the system some time to make the expected connections & requests
        waitForConnectionsReceived(1);
        waitForRequests(1);

        verify(eventPublisher, never()).postCommand(null, null);
        verify(eventPublisher, never()).sendCommand(null, null);

        verify(eventPublisher).postUpdate("Item1", new DecimalType(123456789.95623f));
        verify(eventPublisher).postUpdate("Item2", new DecimalType(-123456789.1241243f));
        verifyNoMoreInteractions(eventPublisher);
    }

    /**
     * Test reading same registers using different value types.
     */
    @Test
    public void testReadRegistersMultipleWays()
            throws InterruptedException, UnknownHostException, BindingConfigParseException, ConfigurationException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // 0x0002 = 00000000 00000010
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(2));
        // 0xFFFC = 11111111 11111100
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(-4));
        // 0x0063 = 00000000 01100011
        addRegisterMethod.invoke(spi, constructRegisterInt.newInstance(99));

        binding = new ModbusBinding();
        Dictionary<String, Object> cfg = newLongPollBindingConfig();
        for (String valueType : new String[] { ModbusBindingProvider.VALUE_TYPE_BIT,
                ModbusBindingProvider.VALUE_TYPE_UINT8, ModbusBindingProvider.VALUE_TYPE_INT16 }) {
            addSlave(cfg, SLAVE_NAME + valueType, type, valueType, nonZeroOffset ? 1 : 0, 2);
        }
        binding.updated(cfg);

        // Here we test only some of the read values (int16 read but not tested)
        configureSwitchItemBinding(32, SLAVE_NAME + ModbusBindingProvider.VALUE_TYPE_BIT, 0, "B", null);
        configureNumberItemBinding(4, SLAVE_NAME + ModbusBindingProvider.VALUE_TYPE_UINT8, 0, "UI8", null);

        binding.execute();

        // Give the system some time to make the expected connections & requests
        // We expect as many requests and connections as there are slaves conifigured for the binding.
        // Note: same registers are read many times, there is currently no optimization implemented for this use case
        waitForConnectionsReceived(3);
        waitForRequests(3);

        verify(eventPublisher, never()).postCommand(null, null);
        verify(eventPublisher, never()).sendCommand(null, null);

        // verify bit items
        if (nonZeroOffset) {
            verifyBitItems(new StringBuffer("1111111111111100").reverse().toString(), 0, "B");
            verifyBitItems(new StringBuffer("0000000001100011").reverse().toString(), 16, "B");
        } else {
            // 1st register bits
            verifyBitItems(new StringBuffer("0000000000000010").reverse().toString(), 0, "B");
            // 2nd register bits
            verifyBitItems(new StringBuffer("1111111111111100").reverse().toString(), 16, "B");
        }

        // verify int8 items
        if (nonZeroOffset) {
            // 2nd register, lo byte
            verify(eventPublisher).postUpdate("UI8Item1", new DecimalType(0xFC));
            // 2nd register, hi byte
            verify(eventPublisher).postUpdate("UI8Item2", new DecimalType(0xFF));
            // 3rd register, lo byte
            verify(eventPublisher).postUpdate("UI8Item3", new DecimalType(0x63));
            // 3rd register, hi byte
            verify(eventPublisher).postUpdate("UI8Item4", new DecimalType(0x00));
        } else {
            // 1st register, lo byte
            verify(eventPublisher).postUpdate("UI8Item1", new DecimalType(0x02));
            // 1st register, hi byte
            verify(eventPublisher).postUpdate("UI8Item2", new DecimalType(0x00));
            // 2nd register, lo byte
            verify(eventPublisher).postUpdate("UI8Item3", new DecimalType(0xFC));
            // 2nd register, hi byte
            verify(eventPublisher).postUpdate("UI8Item4", new DecimalType(0xFF));
        }
        verifyNoMoreInteractions(eventPublisher);
    }
}
