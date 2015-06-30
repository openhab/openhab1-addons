/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.types;

/**
 * Available Integra types.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public enum IntegraType {
	UNKNOWN(-1, "Unknown"), 
	I24(0, "Integra 24"), 
	I32(1, "Integra 32"), 
	I64(2, "Integra 64"), 
	I128(3, "Integra 128"), 
	I128_SIM300(4, "Integra 128-WRL SIM300"), 
	I128_LEON(132, "Integra 128-WRL LEON"), 
	I64_PLUS(66, "Integra 64 Plus"), 
	I128_PLUS(67, "Integra 128 Plus"), 
	I256_PLUS(72, "Integra 256 Plus");

	private int code;
	private String name;

	IntegraType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * @return name of Integra type
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns Integra type for given code.
	 * 
	 * @param code
	 *            code to get type for
	 * @return Integra type object
	 */
	public static IntegraType valueOf(int code) {
		for (IntegraType val : IntegraType.values()) {
			if (val.code == code)
				return val;
		}
		return UNKNOWN;
	}
}
