/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openenergymonitor.internal;

import java.io.InvalidClassException;

/**
 * Represents all valid function types which could be processed by this binding.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public enum OpenEnergyMonitorFunctionType {

	KWH("kwh"), 
	KWHD("kwh/d"), 
	CUMULATIVE("cumulative"),
	;

	private final String text;

	private OpenEnergyMonitorFunctionType(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	/**
	 * Procedure to convert function type string to function type class.
	 * 
	 * @param functionTypeText
	 *            command string e.g. KWH
	 * @return corresponding command type.
	 * @throws InvalidClassException
	 *             Not valid class for command type.
	 */
	public static OpenEnergyMonitorFunctionType getFunctionType(
			String functionTypeText) throws IllegalArgumentException {

		for (OpenEnergyMonitorFunctionType c : OpenEnergyMonitorFunctionType
				.values()) {
			if (c.text.equals(functionTypeText)) {
				return c;
			}
		}

		throw new IllegalArgumentException("Not valid function type");
	}

}