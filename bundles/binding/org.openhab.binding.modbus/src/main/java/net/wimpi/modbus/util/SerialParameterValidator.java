/***
Copyright (c) 2010-${year}, openHAB.org and others.

All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html
***/

package net.wimpi.modbus.util;

import java.util.Arrays;

import gnu.io.SerialPort;
import net.wimpi.modbus.Modbus;

/**
 * Data validation helper encapsulation for the
 * modbus.util.SerialParameters class
 *
 * @author Nick Mayerhofer
 */
public class SerialParameterValidator {
    public static final Integer[] COMMON_BAUD_RATES = { 75, 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600,
            115200 };

    /**
     * validating the given baudRate
     *
     * @param baudRate value to be checked
     * @return whether if the value is valid or not
     */
    public static boolean isBaudRateValid(int baudRate) {
        return Arrays.asList(COMMON_BAUD_RATES).contains(baudRate);
    }

    public static boolean isDataBitsValid(int databits) {
        return (databits >= SerialPort.DATABITS_5) && (databits <= SerialPort.DATABITS_8);
    }

    public static final Double[] VALID_STOP_BITS = { 1.0, 1.5, 2.0 };

    public static boolean isStopbitsValid(double stopbits) {
        return Arrays.asList(VALID_STOP_BITS).contains(stopbits);
    }

    public static final Integer[] VALID_PARITYS = { SerialPort.PARITY_NONE, SerialPort.PARITY_EVEN,
            SerialPort.PARITY_ODD };

    public static boolean isParityValid(int parity) {
        return Arrays.asList(VALID_PARITYS).contains(parity);
    }

    public static boolean isEncodingValid(String enc) {
        return Arrays.asList(Modbus.validSerialEncodings).contains(enc);
    }

    public static boolean isReceiveTimeoutValid(int receiveTimeout) {
        return (receiveTimeout > 0);
    }

    public static final String[] VALID_FLOWCONTROL_STRINGS = { "none", "xon/xoff out", "xon/xoff in", "rts/cts in",
            "rts/cts out" };

    public static boolean isFlowControlValid(String flowcontrol) {
        return Arrays.asList(VALID_FLOWCONTROL_STRINGS).contains(flowcontrol);
    }

    public static final Integer[] VALID_FLOWCONTROL_INT = { 0, (1 << 0), (1 << 1), (1 << 2), (1 << 3) };

    public static boolean isFlowControlValid(int flowcontrol) {
        return Arrays.asList(VALID_FLOWCONTROL_INT).contains(flowcontrol);
    }
}
