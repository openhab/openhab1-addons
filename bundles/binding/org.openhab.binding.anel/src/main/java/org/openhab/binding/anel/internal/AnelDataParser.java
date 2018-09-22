/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.anel.internal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/**
 * Class for parsing data packets from Anel NET-PwrCtrl device.
 *
 * @since 1.6.0
 * @author paphko
 */
public class AnelDataParser {

    private final static Pattern NUMBER_PATTERN = Pattern.compile("\\d+(?:\\.\\d+)?");
    private final static BigDecimal NUMBER_THRESHOLD = BigDecimal.valueOf(1, 1); // 0.1

    /**
     * Parse data package from Anel NET-PwrCtrl device and update
     * {@link AnelState}. For all changes, {@link AnelCommandType}s are created
     * and returned. The expected format is as follows, separated with colons:
     * <ul>
     * <li>0. 'NET-PwrCtrl'
     * <li>1. &lt;name&gt; (may contain trailing spaces)
     * <li>2. &lt;ip&gt;
     * <li>3. &lt;netmask&gt;
     * <li>4. &lt;gateway&gt;
     * <li>5. &lt;mac addess&gt;
     * <li>6-13. &lt;name of switch n&gt;,&lt;state 0 or 1&gt;
     * <li>14. &lt;locked switches&gt;
     * <li>15. &lt;http port&gt;
     * <li>16-23. &lt;name of IO n&gt;,&lt;direction in=1 or out=0&gt;,&lt;state
     * 0 or 1&gt;
     * <li>24. &lt;temperature&gt;
     * <li>25. &lt;firmware version&gt; (may contain trailing line break)
     * </ul>
     * Source: <a
     * href="http://www.anel-elektronik.de/forum_new/viewtopic.php?f=16&t=207"
     * >Anel forum (German)</a>
     *
     * <p>
     * It turned out that the HOME variant has a different format which contains
     * only the first 16 segments. If that is the case, the remaining fields of
     * {@link AnelState} are simply ignored (and remain unset).
     * </p>
     * Source: <a href="https://github.com/openhab/openhab/issues/2068">Issue
     * 2068</a>
     *
     * <p>
     * With Firmware version 6.1, there are additional segments, so we must not check for exact length but we should
     * rather check for at least the expected segments.
     * </p>
     * Source: <a href="https://github.com/openhab/openhab/issues/3068">Issue
     * 3068</a>
     *
     * @param data
     *            The data received from {@link AnelUDPConnector}.
     * @param state
     *            The internal (cached) state of the device.
     * @return A map of commands to the new openHAB {@link State}s.
     * @throws Exception
     *             If the data is invalid or corrupt.
     */
    public static Map<AnelCommandType, State> parseData(byte[] data, AnelState state) throws Exception {
        final String string = new String(data);
        final String[] arr = string.split(":");

        /*-
         * HOME version apparently has length 16 (I don't own such a device), not sure which Firmware version;
         * Firmware version < 6 has 26 segments;
         * Firmware version 6.0 has 28 segments (type and power measurement are new);
         * Firmware version 6.1 has 30 segments (sensor and xor encryption flag are new).
         */
        if (arr.length < 26 && arr.length != 16) {
            throw new IllegalArgumentException(
                    "Data with 16 or at least 26 values expected but " + arr.length + " received: " + string);
        }
        if (!arr[0].equals("NET-PwrCtrl")) {
            throw new IllegalArgumentException("Data must start with 'NET-PwrCtrl' but it didn't: " + arr[0]);
        }
        if (!state.host.equals(arr[2]) && !state.host.equalsIgnoreCase(arr[1].trim())) {
            return Collections.emptyMap(); // this came from another device
        }

        final Map<AnelCommandType, State> result = new LinkedHashMap<AnelCommandType, State>();

        // maybe the device's name changed?!
        checkForNameChange(arr, state, result);

        if (arr.length >= 15) {
            checkForStateChanges(arr, state, result);
        }

        // IO occupies fields 16-23
        if (arr.length >= 24) {
            checkForIOChanges(arr, state, result);
        }

        // IO and temperature is only available if array has at least length 25
        if (arr.length >= 25) {
            checkForTemperatureChange(arr, state, result);
        }

        /*
         * subsequent fields may be firmware, power measurement (is that a variable count of fields?), sensor data,
         * and password encryption information.
         * to read the sensor values, we have to search for the pattern 's:<temperature>:<humidity>:<brightness>'.
         * corresponding github issue and Anel protocol documentation:
         * https://github.com/openhab/openhab1-addons/issues/5338
         * https://anel-elektronik.de/forum_neu/viewtopic.php?f=16&t=207
         */
        final int sensorDataIndex = findNameAndNumberPatternIndex(arr, 26, "s", 3);
        if (sensorDataIndex > 0) {
            checkForSensorChanges(arr, state, result, sensorDataIndex);
        }

        // as soon as someone wants read power measurement values, this is how it should be...
        // final int powerDataIndex = findNameAndNumberPatternIndex(arr, 26, "p", 7);
        // if (powerDataIndex > 0) { ... }

        return result;
    }

    private static void checkForNameChange(String[] arr, AnelState state, Map<AnelCommandType, State> result) {
        final String name = arr[1];
        if (!name.equals(state.name)) {
            result.put(AnelCommandType.NAME, new StringType(name));
            state.name = name;
        }
    }

    private static void checkForTemperatureChange(String[] arr, AnelState state, Map<AnelCommandType, State> result) {
        // example temperature string: '26.4°C'
        // '°' is caused by some different encoding, so cut last 2 chars
        final String temperature = arr[24].substring(0, arr[24].length() - 2);
        if (hasNumericValueChanged(state.temperature, temperature)) {
            result.put(AnelCommandType.TEMPERATURE, new DecimalType(temperature));
            state.temperature = temperature;
        }
    }

    private static void checkForIOChanges(String[] arr, AnelState state, Map<AnelCommandType, State> result) {
        for (int nr = 0; nr < 8; nr++) {
            final String[] ioState = arr[16 + nr].split(",");
            if (ioState.length == 3) {
                // expected format
                addCommand(state.ioName, nr, ioState[0], "IO" + (nr + 1) + "NAME", result);
                addCommand(state.ioIsInput, nr, "1".equals(ioState[1]), "IO" + (nr + 1) + "ISINPUT", result);
                addCommand(state.ioState, nr, "1".equals(ioState[2]), "IO" + (nr + 1), result);
            } else {
                // unexpected format, set states to null
                addCommand(state.ioName, nr, null, "IO" + (nr + 1) + "NAME", result);
                addCommand(state.ioIsInput, nr, null, "IO" + (nr + 1) + "ISINPUT", result);
                addCommand(state.ioState, nr, null, "IO" + (nr + 1), result);
            }
        }
    }

    private static void checkForStateChanges(String[] arr, AnelState state, Map<AnelCommandType, State> result) {
        final int locked = Integer.parseInt(arr[14]);
        for (int nr = 0; nr < 8; nr++) {
            final String[] swState = arr[6 + nr].split(",");
            if (swState.length == 2) {
                // expected format
                addCommand(state.switchName, nr, swState[0], "F" + (nr + 1) + "NAME", result);
                addCommand(state.switchState, nr, "1".equals(swState[1]), "F" + (nr + 1), result);
            } else {
                // unexpected format, set states to null
                addCommand(state.switchName, nr, null, "F" + (nr + 1) + "NAME", result);
                addCommand(state.switchState, nr, null, "F" + (nr + 1), result);
            }
            addCommand(state.switchLocked, nr, (locked & (1 << nr)) > 0, "F" + (nr + 1) + "LOCKED", result);
        }
    }

    private static void checkForSensorChanges(String[] arr, AnelState state, Map<AnelCommandType, State> result,
            int sensorDataIndex) {
        final String sensorTemperature = arr[sensorDataIndex + 1];
        if (!sensorTemperature.equals(state.sensorTemperature)) {
            result.put(AnelCommandType.SENSOR_TEMPERATURE, new DecimalType(sensorTemperature));
            state.sensorTemperature = sensorTemperature;
        }
        final String sensorHumidity = arr[sensorDataIndex + 2];
        if (!sensorHumidity.equals(state.sensorHumidity)) {
            result.put(AnelCommandType.SENSOR_HUMIDITY, new DecimalType(sensorHumidity));
            state.sensorHumidity = sensorHumidity;
        }
        final String sensorBrightness = arr[sensorDataIndex + 3];
        if (!sensorBrightness.equals(state.sensorBrightness)) {
            result.put(AnelCommandType.SENSOR_BRIGHTNESS, new DecimalType(sensorBrightness));
            state.sensorBrightness = sensorBrightness;
        }
    }

    private static int findNameAndNumberPatternIndex(String[] arr, int startIndex, String name, int numberCount) {
        if (arr != null && arr.length >= startIndex + numberCount) {
            indexLoop: for (int i = startIndex; i + numberCount < arr.length; i++) {
                if (name.equals(arr[i])) {
                    for (int j = 1; j <= numberCount; j++) {
                        if (!NUMBER_PATTERN.matcher(arr[i + j]).matches()) {
                            continue indexLoop;
                        }
                    }
                    return i; // name and number of number values matches
                }
            }
        }
        return -1;
    }

    private static boolean hasNumericValueChanged(final String oldValue, final String newValue) {
        if (oldValue == null || oldValue.isEmpty()) {
            return true; // no calculation needed if cached state is empty
        }
        if (oldValue.equals(newValue)) {
            return false; // if it equals, nothing changed
        }

        // report only changes of more than 0.1 degrees
        final Matcher newValueMatcher = NUMBER_PATTERN.matcher(newValue);
        final Matcher oldValueMatcher = NUMBER_PATTERN.matcher(oldValue);
        if (newValueMatcher.matches() && oldValueMatcher.matches()) {
            final BigDecimal newValueDecimal = new BigDecimal(newValue);
            final BigDecimal oldValueDecimal = new BigDecimal(oldValue);
            final BigDecimal difference = newValueDecimal.subtract(oldValueDecimal).abs();
            return difference.compareTo(NUMBER_THRESHOLD) > 0;
        }

        // pattern does not match, always report a change
        return true;
    }

    private static <T> void addCommand(T[] cache, int index, T newValue, String commandType,
            Map<AnelCommandType, State> result) {
        if (newValue != null) {
            if (!newValue.equals(cache[index])) {
                final AnelCommandType cmd = AnelCommandType.getCommandType(commandType);
                final State state;
                if (newValue instanceof String) {
                    state = new StringType((String) newValue);
                } else if (newValue instanceof Boolean) {
                    state = (Boolean) newValue ? OnOffType.ON : OnOffType.OFF;
                } else {
                    throw new UnsupportedOperationException(
                            "TODO: implement value to state conversion for: " + newValue.getClass().getCanonicalName());
                }
                result.put(cmd, state);
                cache[index] = newValue;
            }
        } else if (cache[index] != null) {
            result.put(AnelCommandType.getCommandType(commandType), UnDefType.UNDEF);
            cache[index] = null;
        }
    }
}
