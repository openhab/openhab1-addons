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
 * This parser is for raw Intertechno configurations. Use this if we don't have
 * a more convenient parser. The address parts need to specify the encoded
 * address and the on and off command as Strings which can be send via the CUL.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class RawParser extends AbstractIntertechnoParser {

	private String commandOn;
	private String commandOff;

	@Override
	public String parseAddress(String... addressParts)
			throws BindingConfigParseException {
		String address = addressParts[0];
		commandOn = addressParts[1];
		commandOff = addressParts[2];
		return address;
	}

	@Override
	public String getCommandValueON() {
		return commandOn;
	}

	@Override
	public String getCOmmandValueOFF() {
		return commandOff;
	}

}
