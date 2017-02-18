/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.iec6205621meter.internal;

/**
 * Class defining the communication configuration parameter for metering device
 *
 * @author Peter Kreutzer
 * @author Günter Speckhofer
 * @since 1.5.0
 */
public class MeterConfig {

    // configuration defaults for optional properties
    static final int DEFAULT_BAUD_RATE_CHANGE_DELAY = 0;
    static final boolean DEFAULT_ECHO_HANDLING = true;
    static final String DEFAULT_SERIAL_PORT = "COM1";

    private final String serialPort;
    private final byte[] initMessage;
    private final int baudRateChangeDelay;
    private final boolean echoHandling;

    public MeterConfig(String serialPort, byte[] initMessage, int baudRateChangeDelay, boolean echoHandling) {
        this.serialPort = serialPort;
        this.initMessage = initMessage;
        this.baudRateChangeDelay = baudRateChangeDelay;
        this.echoHandling = echoHandling;
    }

    public String getSerialPort() {
        return this.serialPort;
    }

    public byte[] getInitMessage() {
        return this.initMessage;
    }

    public int getBaudRateChangeDelay() {
        return this.baudRateChangeDelay;
    }

    public boolean getEchoHandling() {
        return this.echoHandling;
    }

    @Override
    public String toString() {
        return "IEC 62056-21Meter DeviceConfig [serialPort=" + serialPort + ", initMessage=" + initMessage
                + ", baudRateChangeDelay=" + baudRateChangeDelay + ", echoHandling=" + echoHandling + "]";
    }
}
