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
import java.util.Arrays;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

public class WriteRegistersTestParameters extends TestCaseSupport {

    private static Command[] ZERO_COMMANDS = new Command[] { OnOffType.OFF, OpenClosedType.CLOSED };

    private static Command[] ONE_COMMANDS = new Command[] { OnOffType.ON, OpenClosedType.OPEN };

    @Parameters
    public static List<Object[]> parameters() {
        List<Object[]> parameters = new ArrayList<Object[]>();
        generateWriteInt16SameValueAsBefore(parameters);
        generateNumberItemInt16(parameters);
        generateSwitchItemInt16(parameters);

        // Currently not testing 32bit valuetypes due to bit buggy (subject to argue) implementation (32bit float is
        // written as rounded 16bit integer, same goes for 32bit int)
        // generateNumberItemFloat32(parameters);
        // generateNumberItemInt32(parameters);
        // // See
        // https://community.openhab.org/t/writing-values-in-modbus-binding-not-considering-valuetype-and-binding-state/5247
        //
        //
        // Also not tested:
        // - Switch to slave with valuetype=bit (correct functionality tbd)
        // (requires hardware) Serial RTU/ASCII/whatever

        // (more exotic cases, perhaps not of interest)
        // Switch item to *int32 (correct functionality tbd)
        // Switch item to *int8 (correct functionality tbd)
        // Switch item to float (0x3F800000 or 0x0 expected)

        // INCREASE/DECREASE/UP/DOWN tests?
        return parameters;
    }

    private static void generateSwitchItemInt16(List<Object[]> parameters) {
        parameters.addAll(WriteRegistersTestParameters.generateParameters(
                new short[] { 0xAAA, 0xAAA, 0xAAA, 0xAAA, 0xAAA }, new DecimalType(.0), new short[] { 0x0001 }, false, // if
                                                                                                                       // true
                                                                                                                       // expecting
                                                                                                                       // failure
                new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                new String[] { ModbusBindingProvider.VALUE_TYPE_UINT16, ModbusBindingProvider.VALUE_TYPE_INT16, },
                ONE_COMMANDS));
        parameters.addAll(WriteRegistersTestParameters.generateParameters(
                new short[] { 0xAAA, 0xAAA, 0xAAA, 0xAAA, 0xAAA }, new DecimalType(.0), new short[] { 0x0 }, false, // if
                                                                                                                    // true
                                                                                                                    // expecting
                                                                                                                    // failure
                new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                new String[] { ModbusBindingProvider.VALUE_TYPE_UINT16, ModbusBindingProvider.VALUE_TYPE_INT16, },
                ZERO_COMMANDS));
    }

    /*-
     * Tests
     * - writing 12345
     * - writing -12345
     * - writing 12345.5
     * - writing -12345.5
     *
     * All tests are run with these combinations
     * - holding register or input register
     * - int16 or uint16
     * - any other combination introduced by generateParameters
     *
     * @param parameters
     */
    private static void generateNumberItemInt16(List<Object[]> parameters) {
        /* int16 */
        Command[] decimal_12345 = new Command[] { new DecimalType(12345) };
        Command[] decimal_minus_12345 = new Command[] { new DecimalType(-12345) };
        // -12345 int16 = 53191 uint16
        Command[] decimal_53191 = new Command[] { new DecimalType(53191) };

        short[] reg_int16_12345 = new short[] { 0x3039 }; // 0x3039;
        short[] reg_uint16_12345 = reg_int16_12345;
        short[] reg_int16_minus_12345 = new short[] { (short) 0xCFC7 }; // 0xCFC7;
        short[] reg_uint16_minus_12345 = reg_int16_minus_12345; // 0xCFC7

        Command[] decimal_12345_pt_5 = new Command[] { new DecimalType(12345.5) };
        Command[] decimal_minus_12345_pt_5 = new Command[] { new DecimalType(-12345.5) };

        // positive
        for (Command command : Arrays.asList(decimal_12345[0], decimal_12345_pt_5[0])) {
            parameters.addAll(WriteRegistersTestParameters.generateParameters(
                    new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF }, new DecimalType(.0), reg_int16_12345, false, // if
                                                                                                                    // true
                                                                                                                    // expecting
                                                                                                                    // failure
                    new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                    new String[] { ModbusBindingProvider.VALUE_TYPE_INT16, }, new Command[] { command }));
        }
        // negative
        for (Command command : Arrays.asList(decimal_minus_12345[0], decimal_minus_12345_pt_5[0])) {
            parameters.addAll(
                    WriteRegistersTestParameters.generateParameters(new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF },
                            new DecimalType(.0), reg_int16_minus_12345, false, // if true expecting failure
                            new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                            new String[] { ModbusBindingProvider.VALUE_TYPE_INT16, }, new Command[] { command }));
        }
        // overflow
        parameters.addAll(WriteRegistersTestParameters.generateParameters(
                new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF }, new DecimalType(.0), reg_int16_minus_12345, false, // if
                                                                                                                      // true
                                                                                                                      // expecting
                                                                                                                      // failure
                new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                new String[] { ModbusBindingProvider.VALUE_TYPE_INT16, }, decimal_53191));

        /* uint16 */
        // positive
        for (Command command : Arrays.asList(decimal_12345[0], decimal_12345_pt_5[0])) {
            parameters.addAll(WriteRegistersTestParameters.generateParameters(
                    new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF }, new DecimalType(.0), reg_uint16_12345, false, // if
                                                                                                                     // true
                                                                                                                     // expecting
                                                                                                                     // failure
                    new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                    new String[] { ModbusBindingProvider.VALUE_TYPE_UINT16, }, new Command[] { command }));
        }
        // negative
        for (Command command : Arrays.asList(decimal_minus_12345[0], decimal_minus_12345_pt_5[0])) {
            parameters.addAll(
                    WriteRegistersTestParameters.generateParameters(new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF },
                            new DecimalType(.0), reg_uint16_minus_12345, false, // if true expecting failure
                            new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                            new String[] { ModbusBindingProvider.VALUE_TYPE_UINT16, }, new Command[] { command }));
        }
        // overflow
        parameters.addAll(WriteRegistersTestParameters.generateParameters(
                new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF }, new DecimalType(.0), reg_uint16_minus_12345, false, // if
                                                                                                                       // true
                                                                                                                       // expecting
                                                                                                                       // failure
                new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                new String[] { ModbusBindingProvider.VALUE_TYPE_UINT16, }, decimal_53191));
    }

    /*-
     * - writing 12345678
     * - writing -12345678
     * - writing 12345678.5
     * - writing -12345678.5
     *
     * All tests are run with these combinations
     * - holding register or input register
     * - int32 or uint32
     * - any other combination introduced by generateParameters
     *
     * @param parameters
     */
    private static void generateNumberItemInt32(List<Object[]> parameters) {

        /* int32 */
        Command[] decimal_12345678 = new Command[] { new DecimalType(12345678) };
        Command[] decimal_minus_12345678 = new Command[] { new DecimalType(-12345678) };
        // -12345678 int32 = 4282621618 uint32
        Command[] decimal_4282621618 = new Command[] { new DecimalType(4282621618L) };

        short[] reg_int32_12345678 = new short[] { 0x00BC, 0x614E }; // 0x00BC614E;
        short[] reg_uint32_12345678 = reg_int32_12345678;
        short[] reg_int32_minus_12345678 = new short[] { 0x00BC, 0x614E }; // 0xFF439EB2;
        short[] reg_uint32_minus_12345678 = reg_int32_minus_12345678; // 0xFF439EB2

        Command[] decimal_12345678_pt_5 = new Command[] { new DecimalType(12345678.5) };
        Command[] decimal_minus_12345678_pt_5 = new Command[] { new DecimalType(-12345678.5) };

        for (Command command : Arrays.asList(decimal_12345678[0], decimal_12345678_pt_5[0])) {
            parameters.addAll(WriteRegistersTestParameters.generateParameters(
                    new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF }, new DecimalType(.0), reg_int32_12345678, false, // true
                                                                                                                       // ->
                                                                                                                       // exptected
                                                                                                                       // failure
                    new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                    new String[] { ModbusBindingProvider.VALUE_TYPE_INT32, }, new Command[] { command }));
        }
        for (Command command : Arrays.asList(decimal_minus_12345678[0], decimal_minus_12345678_pt_5[0])) {
            parameters.addAll(
                    WriteRegistersTestParameters.generateParameters(new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF },
                            new DecimalType(.0), reg_int32_minus_12345678, false, // true -> exptected failure
                            new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                            new String[] { ModbusBindingProvider.VALUE_TYPE_INT32, }, new Command[] { command }));
        }
        parameters.addAll(WriteRegistersTestParameters.generateParameters(
                new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF }, new DecimalType(.0), reg_int32_minus_12345678, false, // true
                                                                                                                         // ->
                                                                                                                         // exptected
                                                                                                                         // failure
                new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                new String[] { ModbusBindingProvider.VALUE_TYPE_INT32, }, decimal_4282621618));

        /* uint32 */
        for (Command command : Arrays.asList(decimal_12345678[0], decimal_12345678_pt_5[0])) {
            parameters.addAll(WriteRegistersTestParameters.generateParameters(
                    new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF }, new DecimalType(.0), reg_uint32_12345678, false, // true
                                                                                                                        // ->
                                                                                                                        // exptected
                                                                                                                        // failure
                    new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                    new String[] { ModbusBindingProvider.VALUE_TYPE_UINT32, }, new Command[] { command }));
        }
        for (Command command : Arrays.asList(decimal_minus_12345678[0], decimal_minus_12345678_pt_5[0])) {
            parameters.addAll(
                    WriteRegistersTestParameters.generateParameters(new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF },
                            new DecimalType(.0), reg_uint32_minus_12345678, false, // true -> exptected failure
                            new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                            new String[] { ModbusBindingProvider.VALUE_TYPE_UINT32, }, new Command[] { command }));
        }
        parameters.addAll(
                WriteRegistersTestParameters.generateParameters(new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF },
                        new DecimalType(.0), reg_uint32_minus_12345678, false, // true -> exptected failure
                        new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                        new String[] { ModbusBindingProvider.VALUE_TYPE_UINT32, }, decimal_4282621618));
    }

    /*-
     * - writing 12345678.5
     * - writing -12345678.5
     *
     * All tests are run with these combinations
     * - holding register or input
     * - any other combination introduced by generateParameters
     *
     * @param parameters
     */
    private static void generateNumberItemFloat32(List<Object[]> parameters) {
        /* float32 */
        Command[] decimal_12345678_pt_5 = new Command[] { new DecimalType(12345678.5) };
        Command[] decimal_minus_12345678_pt_5 = new Command[] { new DecimalType(-12345678.5) };

        short[] reg_float32_12345678_pt_5 = new short[] { 0x4B3C, 0x614E }; // 0x4B3C614E;
        short[] reg_float32_minus_12345678_pt_5 = new short[] { (short) 0xCB3C, 0x614F }; // 0xCB3C614F;

        parameters.addAll(
                WriteRegistersTestParameters.generateParameters(new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF },
                        new DecimalType(.0), reg_float32_12345678_pt_5, false, // true -> exptected failure
                        new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                        new String[] { ModbusBindingProvider.VALUE_TYPE_FLOAT32, }, decimal_12345678_pt_5));
        parameters.addAll(
                WriteRegistersTestParameters.generateParameters(new short[] { 0xFFF, 0xFFF, 0xFFF, 0xFFF, 0xFFF },
                        new DecimalType(.0), reg_float32_minus_12345678_pt_5, false, // true -> exptected failure
                        new String[] { ModbusBindingProvider.TYPE_INPUT, ModbusBindingProvider.TYPE_HOLDING },
                        new String[] { ModbusBindingProvider.VALUE_TYPE_FLOAT32, }, decimal_minus_12345678_pt_5));
    }

    private static void generateWriteInt16SameValueAsBefore(List<Object[]> parameters) {
        // Verify that we will write the data even though value remains the
        // same!
        // (only tested for 16bit values for simplicity)
        parameters.addAll(WriteRegistersTestParameters.generateParameters(new short[] { 5, 5, 5, 5, 5 },
                new DecimalType(5.0), new short[] { 5 }, false, // true -> expected failure
                new String[] { ModbusBindingProvider.TYPE_HOLDING, ModbusBindingProvider.TYPE_INPUT },
                new String[] {
                        // ModbusBindingProvider.VALUE_TYPE_FLOAT32,
                        // ModbusBindingProvider.VALUE_TYPE_INT32,
                        // ModbusBindingProvider.VALUE_TYPE_UINT32,
                        ModbusBindingProvider.VALUE_TYPE_INT16, ModbusBindingProvider.VALUE_TYPE_UINT16,
                // ModbusBindingProvider.VALUE_TYPE_INT8,
                // ModbusBindingProvider.VALUE_TYPE_UINT8,
                // ModbusBindingProvider.VALUE_TYPE_BIT,
        }, new Command[] { new DecimalType(5.0) }));
    }

    /**
     * Create cross product of test parameters
     *
     * @param initialRegisters
     *            Registers are initialized to these values (starting from
     *            smallest index to largest)
     * @param itemInitialState
     *            Items are initialized this value
     * @param expectedValue
     *            Expected register values that are written. Register positions
     *            are automatically determined
     * @param expectingAssertionError
     *            whether assertion error is expected (i.e. known failure)
     * @param types
     *            for generating multiple tests: all types (e.g. "holding") that
     *            are tested
     * @param valueTypes
     *            for generating multiple tests: all valuetypes (e.g. "int16")
     *            that are tested
     * @param commands
     *            for generating multiple tests: all commands that are tested
     * @return
     */
    public static List<Object[]> generateParameters(short[] initialRegisters, State itemInitialState,
            short[] expectedValue, boolean expectingAssertionError, String[] types, String[] valueTypes,
            Command... commands) {
        List<Object[]> parameters = new ArrayList<Object[]>();
        for (ServerType serverType : TestCaseSupport.TEST_SERVERS) {
            for (boolean nonZeroOffset : new Boolean[] { true, false }) {
                for (Command command : commands) {
                    for (int itemIndex : new Integer[] { 0, 1 }) {
                        for (String type : types) {
                            for (String valueType : valueTypes) {
                                parameters.add(new Object[] { serverType, initialRegisters, itemInitialState,
                                        nonZeroOffset, type, valueType, itemIndex, command, expectedValue,
                                        expectingAssertionError });
                            }
                        }
                    }
                }
            }
        }
        return parameters;
    }

}
