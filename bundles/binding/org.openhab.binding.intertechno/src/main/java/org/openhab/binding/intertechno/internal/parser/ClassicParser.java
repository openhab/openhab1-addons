/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.intertechno.internal.parser;

import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This parser is able to parse classic Intertechno configs.
 * @author Till Klocke
 * @since 1.4.0
 */
public class ClassicParser extends AbstractIntertechnoParser {

	@Override
	public String parseAddress(String... addressParts)
			throws BindingConfigParseException {
		char group = addressParts[0].charAt(0);
		int subAddress = 0;
		try {
			subAddress = Integer.parseInt(addressParts[1]);
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(
					"Sub address is not a number. Configured subaddress: "
							+ addressParts[1]);
		}
		return getGroupAddress(group) + getSubAddress(subAddress) + "0F";
	}

	private String getGroupAddress(char address) {
		char aChar = 'A';
		int intValue = address - aChar;
		return getEncodedString(4, intValue, 'F', '0');
	}

	private String getSubAddress(int address) {
		return getEncodedString(4, address - 1, 'F', '0');
	}

	@Override
	public String getCommandValueON() {
		return "FF";
	}

	@Override
	public String getCOmmandValueOFF() {
		return "F0";
	}

}
