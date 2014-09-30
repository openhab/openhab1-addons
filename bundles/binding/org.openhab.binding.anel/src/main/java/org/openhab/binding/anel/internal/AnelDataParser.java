/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.anel.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
	 * Source: http://www.anel-elektronik.de/forum_new/viewtopic.php?f=16&t=207
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

		if (arr.length != 26)
			throw new IllegalArgumentException("Data with 26 values expected but " + arr.length + " received: "
					+ string);
		if (!arr[0].equals("NET-PwrCtrl"))
			throw new IllegalArgumentException("Data must start with 'NET-PwrCtrl' but it didn't: " + arr[0]);
		if (!state.host.equals(arr[2]) && !state.host.equalsIgnoreCase(arr[1].trim()))
			return Collections.emptyMap(); // this came from another device

		final Map<AnelCommandType, State> result = new HashMap<AnelCommandType, State>();

		// check for switch changes, update cached state, and prepare command if
		// needed
		final int locked = Integer.parseInt(arr[14]);
		for (int i = 0; i < 8; i++) {
			final String[] swState = arr[6 + i].split(",");
			if (swState.length == 2) {
				addCommand(state.switchName, i, swState[0], "F" + (i + 1) + "NAME", result);
				addCommand(state.switchState, i, "1".equals(swState[1]), "F" + (i + 1), result);
			} else {
				addCommand(state.switchName, i, null, "F" + (i + 1) + "NAME", result);
				addCommand(state.switchState, i, null, "F" + (i + 1), result);
			}
			addCommand(state.switchLocked, i, (locked & (1 << i)) > 0, "F" + (i + 1) + "LOCKED", result);
		}

		// check for IO changes, update cached state, and prepare command if
		// needed
		for (int i = 0; i < 8; i++) {
			final String[] ioState = arr[16 + i].split(",");
			if (ioState.length == 3) {
				addCommand(state.ioName, i, ioState[0], "IO" + (i + 1) + "NAME", result);
				addCommand(state.ioIsInput, i, "1".equals(ioState[1]), "IO" + (i + 1) + "ISINPUT", result);
				addCommand(state.ioState, i, "1".equals(ioState[2]), "IO" + (i + 1), result);
			} else {
				addCommand(state.ioName, i, null, "IO" + (i + 1) + "NAME", result);
				addCommand(state.ioIsInput, i, null, "IO" + (i + 1) + "ISINPUT", result);
				addCommand(state.ioState, i, null, "IO" + (i + 1), result);
			}
		}

		// example temperature string: '26.4°C' (btw, the '°' seems to have a
		// different encoding)
		final String temperature = arr[24].substring(0, arr[24].length() - 2);
		if (!temperature.equals(state.temperature)) {
			result.put(AnelCommandType.TEMPERATURE, new DecimalType(temperature));
			state.temperature = temperature;
		}
		final String name = arr[1];
		if (!name.equals(state.name)) {
			result.put(AnelCommandType.NAME, new StringType(name));
			state.name = name;
		}

		if (!result.isEmpty())
			state.lastUpdate = System.currentTimeMillis();
		return result;
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
					throw new UnsupportedOperationException("TODO: implement value to state conversion for: "
							+ newValue.getClass().getCanonicalName());
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
