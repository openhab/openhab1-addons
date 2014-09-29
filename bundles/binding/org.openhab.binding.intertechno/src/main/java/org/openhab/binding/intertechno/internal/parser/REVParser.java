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
 * This class parses configurations for REV type Intertechno devices. This is
 * untested since I don't own such devices.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class REVParser extends AbstractIntertechnoParser {

	@Override
	public String parseAddress(String... addressParts)
			throws BindingConfigParseException {
		String group = addressParts[0];
		String sub = addressParts[1];
		return getGroupAddress(group) + getSubAddress(sub) + "0FF";
	}

	private String getGroupAddress(String group)
			throws BindingConfigParseException {
		char groupChar = group.charAt(0);
		switch (groupChar) {
		case 'A':
			return "1FFF";
		case 'B':
			return "F1FF";
		case 'C':
			return "FF1F";
		case 'D':
			return "FFF1";
		default:
			throw new BindingConfigParseException("Unkown group: " + group);
		}
	}

	private String getSubAddress(String sub) throws BindingConfigParseException {
		char subChar = sub.charAt(0);
		switch (subChar) {
		case '1':
			return "1FF";
		case '2':
			return "F1F";
		case 3:
			return "FF1";
		default:
			throw new BindingConfigParseException("Unkwown sub address: " + sub);
		}
	}

	@Override
	public String getCommandValueON() {
		return "FF";
	}

	@Override
	public String getCOmmandValueOFF() {
		return "00";
	}

}
