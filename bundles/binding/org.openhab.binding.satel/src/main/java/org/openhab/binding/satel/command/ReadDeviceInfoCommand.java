/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.satel.command;

import java.io.UnsupportedEncodingException;

import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command class for command that reads information about specific device
 * (partition, zone, user, etc).
 *
 * @author Krzysztof Goworek
 * @since 1.9.0
 */
public class ReadDeviceInfoCommand extends SatelCommandBase {

    private static final Logger logger = LoggerFactory.getLogger(ReadDeviceInfoCommand.class);

    public static final byte COMMAND_CODE = (byte) 0xee;

    /**
     * Device type: partition, zone, expander, etc.
     *
     * @author Krzysztof Goworek
     *
     */
    public enum DeviceType {
        PARTITION(0),
        ZONE(1),
        USER(2),
        EXPANDER(3),
        LCD(3),
        OUTPUT(4),
        ZONE_WITH_PARTITION(5),
        TIMER(6),
        TELEPHONE(7),
        OBJECT(15),
        PARTITION_WITH_OBJECT(16);

        int code;

        DeviceType(int code) {
            this.code = code;
        }

        int getCode() {
            return code;
        }
    }

    /**
     * Creates new command class instance to read description for given
     * parameters.
     *
     * @param deviceType
     *            type of the device
     * @param deviceNumber
     *            device number
     */
    public ReadDeviceInfoCommand(DeviceType deviceType, int deviceNumber) {
        super(COMMAND_CODE, new byte[] { (byte) deviceType.getCode(), getDeviceNumber(deviceType, deviceNumber) });
    }

    private static byte getDeviceNumber(DeviceType deviceType, int deviceNumber) {
        switch (deviceType) {
            case EXPANDER:
                if (deviceNumber < 128) {
                    return (byte) (deviceNumber + 128);
                }
                break;
            case LCD:
                if (deviceNumber < 128) {
                    return (byte) (deviceNumber + 192);
                }
                break;
            case ZONE:
            case ZONE_WITH_PARTITION:
                return (byte) (deviceNumber == 256 ? 0 : deviceNumber);
            default:
                break;
        }
        return (byte) deviceNumber;
    }

    /**
     * Returns device model or function depending on device type:
     * <ul>
     * <li>partition - partition type</li>
     * <li>zone - zone reaction</li>
     * <li>user - 0</li>
     * <li>object - 0</li>
     * <li>expander - expander model, CA-64 PP, CA-64 E, etc</li>
     * <li>LCD - LCD model, INT-KLCD, INT-KLCDR, etc</li>
     * <li>output - output function</li>
     * </ul>
     *
     * @return kind of the device
     */
    public int getDeviceKind() {
        return response.getPayload()[2] & 0xff;
    }

    /**
     * Returns name of the device decoded using given encoding. Encoding
     * depends on firmware language and must be specified in the binding
     * configuration.
     *
     * @param encoding
     *            encoding for the text
     * @return device name
     * @throws UnsupportedEncodingException
     *             if provided encoding is not supported
     */
    public String getName(String encoding) throws UnsupportedEncodingException {
        return new String(response.getPayload(), 3, 16, encoding).trim();
    }

    @Override
    protected boolean isResponseValid(SatelMessage response) {
        // validate response
        if (response.getCommand() != COMMAND_CODE) {
            logger.error("Invalid response code: {}", response.getCommand());
            return false;
        }
        if (response.getPayload().length < 19 || response.getPayload().length > 20) {
            logger.error("Invalid payload length: {}", response.getPayload().length);
            return false;
        }
        return true;
    }

}
