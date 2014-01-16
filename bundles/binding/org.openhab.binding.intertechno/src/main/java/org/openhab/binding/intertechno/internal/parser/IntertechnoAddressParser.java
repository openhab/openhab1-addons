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
 * All parsers for Intertechno configs need to implement this interface.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public interface IntertechnoAddressParser {

	/**
	 * Parse the address from the given address parts. After this method was
	 * called the on and off commands can be read with getCommandValue(ON|OFF).
	 * 
	 * @param addressParts
	 *            String array created from the binding config.
	 * @return The encoded address.
	 * @throws BindingConfigParseException
	 */
	public String parseAddress(String... addressParts)
			throws BindingConfigParseException;

	public String getCommandValueON();

	public String getCOmmandValueOFF();

}
