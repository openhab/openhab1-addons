/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.protocol;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.plugwise.internal.PlugwiseDevice.DeviceType;

/**
 * Circle Information response message
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class InformationResponseMessage extends Message {

    private static final Pattern RESPONSE_PATTERN = Pattern
            .compile("(\\w{16})(\\w{2})(\\w{2})(\\w{4})(\\w{8})(\\w{2})(\\w{2})(\\w{12})(\\w{8})(\\w{2})");

    private int year;
    private int month;
    private int minutes;
    private int logAddress;
    private boolean powerState;
    private int hertz;
    private String hardwareVersion;
    private int firmwareVersion;
    private DeviceType deviceType;

    private Calendar dateTimeReceived = Calendar.getInstance();

    public InformationResponseMessage(int sequenceNumber, String payLoad) {
        super(sequenceNumber, payLoad);
        type = MessageType.DEVICE_INFORMATION_RESPONSE;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getLogAddress() {
        return logAddress;
    }

    public boolean getPowerState() {
        return powerState;
    }

    public int getHertz() {
        return (hertz == 133) ? 50 : 60;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public int getFirmwareVersion() {
        return firmwareVersion;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    @Override
    protected String payLoadToHexString() {
        return payLoad;
    }

    public Calendar getDateTimeReceived() {
        return dateTimeReceived;
    }

    @Override
    protected void parsePayLoad() {
        Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
        if (matcher.matches()) {
            MAC = matcher.group(1);
            year = Integer.parseInt(matcher.group(2), 16) + 2000;
            month = Integer.parseInt(matcher.group(3), 16);
            minutes = Integer.parseInt(matcher.group(4), 16);
            logAddress = (Integer.parseInt(matcher.group(5), 16) - 278528) / 32;
            powerState = (matcher.group(6).equals("01"));
            hertz = Integer.parseInt(matcher.group(7), 16);
            hardwareVersion = StringUtils.left(matcher.group(8), 4) + "-" + StringUtils.mid(matcher.group(8), 4, 4)
                    + "-" + StringUtils.right(matcher.group(8), 4);
            firmwareVersion = Integer.parseInt(matcher.group(9), 16);
            deviceType = intToDeviceType(Integer.parseInt(matcher.group(10), 16));
        } else {
            logger.debug("Plugwise protocol RoleCallResponseMessage error: {} does not match", payLoad);
        }
    }

    private DeviceType intToDeviceType(int i) {
        switch (i) {
            case 0:
                return DeviceType.Stick;
            case 1:
                return DeviceType.CirclePlus;
            case 2:
                return DeviceType.Circle;
            case 3:
                return DeviceType.Switch;
            case 5:
                return DeviceType.Sense;
            case 6:
                return DeviceType.Scan;
            case 9:
                return DeviceType.Stealth;
            default:
                return null;
        }
    }

}
