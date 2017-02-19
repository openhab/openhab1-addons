/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.pool2.KeyedObjectPool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.binding.modbus.internal.pooling.ModbusSlaveEndpoint;
import org.openhab.binding.modbus.internal.pooling.ModbusTCPSlaveEndpoint;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;

import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.ModbusSlaveConnection;
import net.wimpi.modbus.procimg.SimpleInputRegister;
import net.wimpi.modbus.procimg.SimpleRegister;

@RunWith(Parameterized.class)
public class SimultaneousReadWriteTestCase extends TestCaseSupport {

    private static final int READ_COUNT = 4;

    @Parameters
    public static List<Object[]> parameters() {
        List<Object[]> parameters = new ArrayList<Object[]>();
        for (ServerType serverType : TestCaseSupport.TEST_SERVERS) {
            parameters.add(new Object[] { serverType, new short[] { 5, 5, 5, 5, 5 }, ModbusBindingProvider.TYPE_HOLDING,
                    ModbusBindingProvider.VALUE_TYPE_INT16, new DecimalType(5.0) });
        }
        return parameters;
    }

    private String valueType;
    private String type;
    private Command command;
    private short[] registerInitialValues;
    private State itemInitialState;

    /*
     */
    public SimultaneousReadWriteTestCase(ServerType serverType, short[] registerInitialValues, String type,
            String valueType, Command command) {
        this.serverType = serverType;
        this.registerInitialValues = registerInitialValues;
        this.type = type;
        this.valueType = valueType;
        this.command = command;
        // Server is a bit slower to respond than norrmally, so we certainly get clashes with read/write
        this.artificialServerWait = 500;
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        initSpi();
    }

    private void configureItems(String slave) throws BindingConfigParseException {
        configureNumberItemBinding(2, slave, 0, slave, itemInitialState);
    }

    private static class UpdateThread extends Thread {
        private ModbusBinding binding;

        public UpdateThread(ModbusBinding binding) {
            this.binding = binding;
        }

        @Override
        public void run() {
            binding.execute();
        }
    }

    private static class WriteCommandThread extends Thread {
        private ModbusBinding binding;
        private String slave;
        private Command command;

        public WriteCommandThread(ModbusBinding binding, String slave, Command command) {
            this.binding = binding;
            this.slave = slave;
            this.command = command;
        }

        @Override
        public void run() {
            binding.receiveCommand(String.format("%sItem%s", slave, 1), command);
        }
    }

    /**
     * Testing how binding handles simultaneous read and writes coming in.
     *
     * Even though the server in this test is able to handle at most one client at a time the binding
     * queues requests.
     *
     * Note higher artificialServerWait in constructor
     *
     * @throws Exception
     */
    @Test
    public void testSimultaneousReadWrite() throws Exception {
        binding = new ModbusBinding();
        binding.updated(addSlave(addSlave(newLongPollBindingConfig(), SLAVE_NAME, type, null, 0, READ_COUNT),
                SLAVE2_NAME, type, null, 0, READ_COUNT));
        configureItems(SLAVE_NAME);
        configureItems(SLAVE2_NAME);

        /*
         * - both slaves read twice -> 4 read requests
         * - followed by write (slave1) -> 1 write request
         * - both slaves read once -> 2 read requests.
         * - Finally three writes (slave2) -> 3 write requets
         */
        int expectedRequests = 10;
        ExecutorService pool = Executors.newFixedThreadPool(expectedRequests);
        binding.execute();
        pool.execute(new UpdateThread(binding));
        pool.execute(new WriteCommandThread(binding, SLAVE_NAME, command));
        pool.execute(new UpdateThread(binding));
        pool.execute(new WriteCommandThread(binding, SLAVE2_NAME, command));
        pool.execute(new WriteCommandThread(binding, SLAVE2_NAME, command));
        pool.execute(new WriteCommandThread(binding, SLAVE2_NAME, command));

        pool.shutdown();
        pool.awaitTermination(artificialServerWait * 7 + 5000, TimeUnit.MILLISECONDS);
        waitForRequests(expectedRequests);

        ArrayList<ModbusRequest> values = modbustRequestCaptor.getAllReturnValues();
        System.err.println(values);
        int readCount = 0;
        int writeCount = 0;
        for (ModbusRequest request : values) {
            if (request instanceof ReadMultipleRegistersRequest) {
                readCount++;
            } else if (request instanceof WriteSingleRegisterRequest) {
                writeCount++;
            }
        }
        Assert.assertEquals(6, readCount);
        Assert.assertEquals(4, writeCount);
    }

    @Test
    public void testPoolBlocks() throws Exception {
        final KeyedObjectPool<ModbusSlaveEndpoint, ModbusSlaveConnection> pool = ModbusBinding
                .getReconstructedConnectionPoolForTesting();

        final ModbusTCPSlaveEndpoint endpoint = new ModbusTCPSlaveEndpoint(localAddress().getHostAddress(),
                this.tcpModbusPort);

        ModbusSlaveConnection borrowObject = pool.borrowObject(endpoint);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    ModbusSlaveConnection borrowObject2 = pool.borrowObject(endpoint);
                    pool.returnObject(endpoint, borrowObject2);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        };
        thread.start();
        thread.join(500);
        if (!thread.isAlive()) {
            throw new AssertionError("Thread should still be alive -- blocking since no objects");
        } else {
            thread.interrupt();
        }

        pool.returnObject(endpoint, borrowObject);
        // Now that object has been returned, borrowing should work again
        ModbusSlaveConnection borrowObject2 = pool.borrowObject(endpoint);
        pool.returnObject(endpoint, borrowObject2);

    }

    private void initSpi() {
        int registerCount = registerInitialValues.length;
        for (int i = 0; i < registerCount; i++) {
            if (ModbusBindingProvider.TYPE_HOLDING.equals(type)) {
                spi.addRegister(new SimpleRegister(registerInitialValues[i]));
            } else if (ModbusBindingProvider.TYPE_INPUT.equals(type)) {
                spi.addInputRegister(new SimpleInputRegister(registerInitialValues[i]));
            }
        }
    }
}
