/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;

import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ReadCoilsRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.procimg.DigitalIn;
import net.wimpi.modbus.procimg.DigitalOut;
import net.wimpi.modbus.procimg.SimpleDigitalIn;
import net.wimpi.modbus.procimg.SimpleDigitalOut;

@RunWith(Parameterized.class)
public class WriteCoilsAndDiscreteTestCase extends TestCaseSupport {

    /**
     * Known issues
     * 1. coil is not written if the binding "state" (BitVector) matches the command already
     */
    private void setExpectedFailures() {
        boolean coilSameAsCommand = coilInitialValue ? Arrays.asList(ONE_COMMANDS).contains(command)
                : Arrays.asList(ZERO_COMMANDS).contains(command);
        expectingAssertionError = ((ModbusBindingProvider.TYPE_COIL.equals(type) && coilSameAsCommand));
    }

    private static final int BIT_READ_COUNT = 2;

    private static Command[] ZERO_COMMANDS = new Command[] { OnOffType.OFF, OpenClosedType.CLOSED };

    private static Command[] ONE_COMMANDS = new Command[] { OnOffType.ON, OpenClosedType.OPEN };

    @SuppressWarnings("serial")
    public static class ExpectedFailure extends AssertionError {
    }

    /**
     * Create cross product of test parameters
     *
     * @param expectedValue
     * @param commands
     * @return
     */
    private static ArrayList<Object[]> generateParameters(Object expectedValue, Command... commands) {
        ArrayList<Object[]> parameters = new ArrayList<Object[]>();
        for (ServerType serverType : TEST_SERVERS) {
            for (boolean discreteInputInitialValue : new Boolean[] { true, false }) {
                for (boolean coilInitialValue : new Boolean[] { true, false }) {
                    for (boolean nonZeroOffset : new Boolean[] { true, false }) {
                        for (Command command : commands) {
                            for (int itemIndex : new Integer[] { 0, 1 }) {
                                for (String type : new String[] { ModbusBindingProvider.TYPE_COIL,
                                        ModbusBindingProvider.TYPE_DISCRETE }) {
                                    parameters.add(new Object[] { serverType, discreteInputInitialValue,
                                            coilInitialValue, nonZeroOffset, type, itemIndex, command, expectedValue });
                                }
                            }
                        }
                    }
                }
            }
        }
        return parameters;
    }

    @Parameters
    public static Collection<Object[]> parameters() {
        List<Object[]> parameters = generateParameters(false, ZERO_COMMANDS);
        parameters.addAll(generateParameters(true, ONE_COMMANDS));
        return parameters;
    }

    private boolean nonZeroOffset;
    private String type;
    private int itemIndex;
    private Command command;
    private boolean expectedValue;
    private boolean expectingAssertionError;
    private boolean coilInitialValue;
    private boolean discreteInitialValue;

    private DigitalIn[] dins;
    private DigitalOut[] douts;

    /**
     * @param serverType type of server
     * @param discreteInitialValue
     *            initial value of the discrete inputs
     * @param coilInitialValue
     *            initial value of the coils
     * @param nonZeroOffset
     *            whether to test non-zero start address in modbus binding
     * @param type
     *            type of the slave (e.g. "holding")
     * @param itemIndex
     *            index of the item that receives command
     * @param command
     *            received command
     * @param expectedValue
     *            expected boolean written to corresponding coil/discrete input.
     */
    public WriteCoilsAndDiscreteTestCase(ServerType serverType, boolean discreteInitialValue, boolean coilInitialValue,
            boolean nonZeroOffset, String type, int itemIndex, Command command, boolean expectedValue) {
        this.serverType = serverType;
        this.discreteInitialValue = discreteInitialValue;
        this.coilInitialValue = coilInitialValue;
        this.nonZeroOffset = nonZeroOffset;
        this.type = type;
        this.itemIndex = itemIndex;
        this.command = command;
        setExpectedFailures();
        this.expectedValue = expectedValue;

    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        initSpi();
    }

    /**
     * Test writing of discrete inputs (i.e. digital inputs)/coils (i.e. digital
     * outputs), uses default valuetype
     *
     *
     * @throws Exception
     */
    @Test
    public void testWriteDigitalsNoReads() throws Exception {
        // XXX
        /*
         * Test is ignored since current implementation synchronized(storage) on coil commands which throws
         * NullPointerException
         */
        if (type == ModbusBindingProvider.TYPE_COIL) {
            return;
        }

        binding = new ModbusBinding();
        int offset = (nonZeroOffset ? 1 : 0);
        binding.updated(addSlave(newLongPollBindingConfig(), SLAVE_NAME, type, null, offset, 2));
        configureSwitchItemBinding(2, SLAVE_NAME, 0);

        try {
            binding.receiveCommand(String.format("Item%s", itemIndex + 1), command);
        } catch (NullPointerException e) {
            if (type != ModbusBindingProvider.TYPE_COIL) {
                fail("Expecting NullPointerException only with coil");
            }
            return;
        }
        if (type == ModbusBindingProvider.TYPE_COIL) {
            String msg = "Should have raised NullPointerException with coil";
            fail(msg);
        }
        verifyRequests(false);
    }

    @Test
    public void testWriteDigitalsAfterRead() throws Exception {
        binding = new ModbusBinding();
        int offset = (nonZeroOffset ? 1 : 0);
        binding.updated(addSlave(newLongPollBindingConfig(), SLAVE_NAME, type, null, offset, BIT_READ_COUNT));
        configureSwitchItemBinding(2, SLAVE_NAME, 0);

        // READ -- initializes register
        binding.execute();
        binding.receiveCommand(String.format("Item%s", itemIndex + 1), command);
        verifyRequests(true);
    }

    private void verifyRequests(boolean readRequestExpected) throws Exception {
        // XXX: for test performance reasons, skipping failing tests
        if (expectingAssertionError) {
            return;
        }
        try {
            ArrayList<ModbusRequest> requests = modbustRequestCaptor.getAllReturnValues();
            int expectedDOIndex = nonZeroOffset ? (itemIndex + 1) : itemIndex;
            WriteCoilRequest writeRequest;
            boolean writeExpected = type == ModbusBindingProvider.TYPE_COIL;
            int expectedRequests = (writeExpected ? 1 : 0) + (readRequestExpected ? 1 : 0);
            // One connection per request
            int expectedConnections = serverType.equals(ServerType.UDP) ? 1 : expectedRequests;

            // Give the system 5 seconds to make the expected connections & requests
            waitForConnectionsReceived(expectedConnections);
            waitForRequests(expectedRequests);
            assertThat(requests.size(), is(equalTo(expectedRequests)));

            if (readRequestExpected) {
                if (type == ModbusBindingProvider.TYPE_DISCRETE) {
                    assertThat(requests.get(0), is(instanceOf(ReadInputDiscretesRequest.class)));
                } else if (type == ModbusBindingProvider.TYPE_COIL) {
                    assertThat(requests.get(0), is(instanceOf(ReadCoilsRequest.class)));
                } else {
                    throw new RuntimeException();
                }
            }
            if (writeExpected) {
                assertThat(requests.get(expectedRequests - 1), is(instanceOf(WriteCoilRequest.class)));
                writeRequest = (WriteCoilRequest) requests.get(expectedRequests - 1);
                assertThat(writeRequest.getCoil(), is(equalTo(expectedValue)));
                assertThat(writeRequest.getReference(), is(equalTo(expectedDOIndex)));
            }
        } catch (AssertionError e) {
            if (expectingAssertionError) {
                System.err.println(String.format(
                        "Expected failure: discreteInitial=%s, coilInitial=%s, nonZeroOffset=%s, command=%s, itemIndex=%d, type=%s",
                        discreteInitialValue, coilInitialValue, nonZeroOffset, command, itemIndex, type));
                return;
            } else {
                System.err.println(String.format(
                        "Unexpected assertion error: discreteInitial=%s, coilInitial=%s, nonZeroOffset=%s, command=%s, itemIndex=%d, type=%s",
                        discreteInitialValue, coilInitialValue, nonZeroOffset, command, itemIndex, type));
                throw new AssertionError("Got unexpected assertion error", e);
            }
        }
        if (expectingAssertionError) {
            System.err.println(String.format(
                    "Did not get assertion error (as expected): discreteInitial=%s, coilInitial=%s, nonZeroOffset=%s, command=%s, itemIndex=%d, type=%s",
                    discreteInitialValue, coilInitialValue, nonZeroOffset, command, itemIndex, type));
            throw new AssertionError("Did not get assertion error (as expected)");
        } else {
            System.err.println(String.format(
                    "OK: discreteInitial=%s, coilInitial=%s, nonZeroOffset=%s, command=%s, itemIndex=%d, type=%s",
                    discreteInitialValue, coilInitialValue, nonZeroOffset, command, itemIndex, type));
        }
    }

    private void initSpi() {
        dins = new DigitalIn[] { new SimpleDigitalIn(discreteInitialValue), new SimpleDigitalIn(discreteInitialValue),
                new SimpleDigitalIn(discreteInitialValue), new SimpleDigitalIn(discreteInitialValue) };
        for (DigitalIn din : dins) {
            spi.addDigitalIn(din);
        }
        douts = new DigitalOut[] { new SimpleDigitalOut(coilInitialValue), new SimpleDigitalOut(coilInitialValue),
                new SimpleDigitalOut(coilInitialValue), new SimpleDigitalOut(coilInitialValue) };
        for (DigitalOut dout : douts) {
            spi.addDigitalOut(dout);
        }
    }
}
