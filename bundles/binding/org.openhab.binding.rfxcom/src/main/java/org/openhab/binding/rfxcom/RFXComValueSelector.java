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
package org.openhab.binding.rfxcom;

import java.io.InvalidClassException;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;

/**
 * Represents all valid value selectors which could be processed by this
 * binding.
 *
 * @author Pauli Anttila, Evert van Es, Neil Renaud
 * @since 1.2.0
 */
public enum RFXComValueSelector {

    RAW_DATA("RawData", StringItem.class),
    DATA("Data", StringItem.class),
    SHUTTER("Shutter", RollershutterItem.class),
    COMMAND("Command", SwitchItem.class),
    MOOD("Mood", NumberItem.class),
    SIGNAL_LEVEL("SignalLevel", NumberItem.class),
    DIMMING_LEVEL("DimmingLevel", DimmerItem.class),
    TEMPERATURE("Temperature", NumberItem.class),
    HUMIDITY("Humidity", NumberItem.class),
    HUMIDITY_STATUS("HumidityStatus", StringItem.class),
    CHIME_SOUND("ChimeSound", NumberItem.class),
    BATTERY_LEVEL("BatteryLevel", NumberItem.class),
    PRESSURE("Pressure", NumberItem.class),
    FORECAST("Forecast", StringItem.class),
    RAIN_RATE("RainRate", NumberItem.class),
    RAIN_TOTAL("RainTotal", NumberItem.class),
    WIND_DIRECTION("WindDirection", NumberItem.class),
    WIND_SPEED("WindSpeed", NumberItem.class),
    WIND_AVSPEED("WindAvSpeed", NumberItem.class),
    UV("UV", NumberItem.class),
    GUST("Gust", NumberItem.class),
    CHILL_FACTOR("ChillFactor", NumberItem.class),
    INSTANT_POWER("InstantPower", NumberItem.class),
    TOTAL_USAGE("TotalUsage", NumberItem.class),
    INSTANT_AMPS("InstantAmps", NumberItem.class),
    TOTAL_AMP_HOURS("TotalAmpHours", NumberItem.class),
    INSTANT_ENERGY("InstantEnergy", NumberItem.class),
    CHANNEL1_AMPS("Channel1Amps", NumberItem.class),
    CHANNEL2_AMPS("Channel2Amps", NumberItem.class),
    CHANNEL3_AMPS("Channel3Amps", NumberItem.class),
    POWER_FACTOR("PowerFactor", NumberItem.class),
    STATUS("Status", StringItem.class),
    WEIGHT("Weight", NumberItem.class),
    MOTION("Motion", SwitchItem.class),
    CONTACT("Contact", ContactItem.class),
    CONTACT_1("Contact1", ContactItem.class),
    CONTACT_2("Contact2", ContactItem.class),
    CONTACT_3("Contact3", ContactItem.class),
    VOLTAGE("Voltage", NumberItem.class),
    SET_POINT("SetPoint", NumberItem.class),
    DATE_TIME("DateTime", DateTimeItem.class),
    FREQUENCY("Frequency", NumberItem.class);

    private final String text;
    private Class<? extends Item> itemClass;

    private RFXComValueSelector(final String text, Class<? extends Item> itemClass) {
        this.text = text;
        this.itemClass = itemClass;
    }

    @Override
    public String toString() {
        return text;
    }

    public Class<? extends Item> getItemClass() {
        return itemClass;
    }

    /**
     * Procedure to validate selector string.
     * 
     * @param valueSelector
     *            selector string e.g. RawData, Command, Temperature
     * @return true if item is valid.
     * @throws IllegalArgumentException
     *             Not valid value selector.
     * @throws InvalidClassException
     *             Not valid class for value selector.
     */
    public static boolean validateBinding(String valueSelector, Class<? extends Item> itemClass)
            throws IllegalArgumentException, InvalidClassException {

        for (RFXComValueSelector c : RFXComValueSelector.values()) {
            if (c.text.equals(valueSelector)) {

                if (c.getItemClass().equals(itemClass)) {
                    return true;
                } else {
                    throw new InvalidClassException("Not valid class for value selector");
                }
            }
        }

        throw new IllegalArgumentException("Not valid value selector");

    }

    /**
     * Procedure to convert selector string to value selector class.
     * 
     * @param valueSelectorText
     *            selector string e.g. RawData, Command, Temperature
     * @return corresponding selector value.
     * @throws InvalidClassException
     *             Not valid class for value selector.
     */
    public static RFXComValueSelector getValueSelector(String valueSelectorText) throws IllegalArgumentException {

        for (RFXComValueSelector c : RFXComValueSelector.values()) {
            if (c.text.equals(valueSelectorText)) {
                return c;
            }
        }

        throw new IllegalArgumentException("Not valid value selector");
    }

}
