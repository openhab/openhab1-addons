/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.benqprojector.internal;

/**
 * Define mapping between response string and DecimalType for projector sources
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public enum BenqProjectorSourceMapping {
	COMPUTER("RGB", 0), COMPUTER2("RGB2", 1), COMPONENT("ypbr", 2), DVIA(
			"dviA", 3), DVID("dvid", 4), HDMI("hdmi", 5), HDMI2("hdmi2", 6), COMPOSITE(
			"vid", 7), SVIDEO("svid", 8), NETWORK("network", 9), USB_DISPLAY(
			"usbdisplay", 10), USB_READER("usbreader", 11);

	private final String commandString;
	private final int index;

	BenqProjectorSourceMapping(String str, int value) {
		this.commandString = str;
		this.index = value;
	}

	/**
	 * Convert string representation of source to integer
	 * 
	 * @param s
	 *            String representation (e.g. hdmi2)
	 * @return Numerical mapping of source
	 */
	public static int getMappingFromString(String s) {
		for (BenqProjectorSourceMapping mapping : BenqProjectorSourceMapping
				.values()) {
			if (mapping.commandString.equalsIgnoreCase(s))
				return mapping.index;
		}
		return -1;
	}

	/**
	 * Convert integer mapping to string
	 * 
	 * @param idx
	 * @return String representation of source
	 */
	public static String getStringFromMapping(int idx) {
		if (idx >= 0 && idx < BenqProjectorSourceMapping.values().length)
			return BenqProjectorSourceMapping.values()[idx].commandString;
		else
			return "";
	}

}
