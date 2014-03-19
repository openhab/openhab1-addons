/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.intertechno.internal.parser;

/**
 * Simple factory to create Intertechno parsers based on the type of the binding
 * config.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class AddressParserFactory {

	public static IntertechnoAddressParser getParser(String type) {

		if ("classic".equals(type)) {
			return new ClassicParser();
		} else if ("fls".equals(type)) {
			return new FLSParser();
		} else if ("rev".equals(type)) {
			return new REVParser();
		} else if ("raw".equals(type)) {
			return new RawParser();
		}

		return null;

	}

}
