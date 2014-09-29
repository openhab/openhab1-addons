/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
