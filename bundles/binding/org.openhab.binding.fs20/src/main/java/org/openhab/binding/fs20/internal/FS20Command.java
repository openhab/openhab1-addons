/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.fs20.internal;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents possible commands send or received via RF.
 * @author Till Klocke
 * @since 1.4.0
 */
public enum FS20Command {

	OFF("00"), DIM_1("01"), DIM_2("02"), DIM_3("03"), DIM_4("04"), DIM_5("05"), DIM_6(
			"06"), DIM_7("07"), DIM_8("08"), DIM_9("09"), DIM_10("0A"), DIM_11(
			"0B"), DIM_12("0C"), DIM_13("0D"), DIM_14("0E"), DIM_15("0F"), ON(
			"10"), DIM_UP("13"), DIM_DOWN("14"), DIM_UP_DOWN("15"), RESET("1B"), UNKOWN(
			null), ON_OLD_DIM_VALUE("11"), TOGGLE("12");
	// TODO add the other values with extension bit set
	// http://fhz4linux.info/tiki-index.php?page=FS20+Protocol
	private final static Map<String, FS20Command> hexValueToCommand = new LinkedHashMap<String, FS20Command>();
	static {
		for (FS20Command c : FS20Command.values()) {
			hexValueToCommand.put(c.getHexValue(), c);
		}
	}

	private String hexValue;

	private FS20Command(String hexValue) {
		this.hexValue = hexValue;
	}

	public String getHexValue() {
		return hexValue;
	}

	public static FS20Command getFromHexValue(String hexValue) {
		FS20Command command = hexValueToCommand.get(hexValue);
		return command != null ? command : UNKOWN;
	}
}
