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
package org.openhab.binding.plugwise.protocol;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sense report request
 *
 * @author Wouter Born
 * @since 1.9.0
 */
public class SenseReportRequestMessage extends Message {

    private static final Pattern REQUEST_PATTERN = Pattern.compile("(\\w{16})(\\w{4})(\\w{4})");

    private float humidity;
    private float temperature;
    private Calendar dateTimeReceived = Calendar.getInstance();

    public SenseReportRequestMessage(int sequenceNumber, String payLoad) {
        super(sequenceNumber, payLoad);
        type = MessageType.SENSE_REPORT_REQUEST;
    }

    @Override
    protected String payLoadToHexString() {
        return payLoad;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public Calendar getDateTimeReceived() {
        return dateTimeReceived;
    }

    @Override
    protected void parsePayLoad() {
        Matcher matcher = REQUEST_PATTERN.matcher(payLoad);
        if (matcher.matches()) {
            MAC = matcher.group(1);
            humidity = hexToHumidity(matcher.group(2));
            temperature = hexToTemperature(matcher.group(3));
        } else {
            logger.debug("Plugwise protocol SenseReportRequestMessage error: {} does not match", payLoad);
        }
    }

    private float hexToHumidity(String humidityHex) {
        if ("FFFF".equals(humidityHex)) {
            return Float.MIN_VALUE;
        }
        return 125.0f * (Integer.parseInt(humidityHex, 16) / 65536.0f) - 6.0f;
    }

    private float hexToTemperature(String temperatureHex) {
        if ("FFFF".equals(temperatureHex)) {
            return Float.MIN_VALUE;
        }
        return 175.72f * (Integer.parseInt(temperatureHex, 16) / 65536.0f) - 46.85f;
    }

}
