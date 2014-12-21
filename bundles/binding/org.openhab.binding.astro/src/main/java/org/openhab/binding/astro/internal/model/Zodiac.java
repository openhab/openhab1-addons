/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.model;

/**
 * Holds the sign of the zodiac.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Zodiac {
	private ZodiacSign sign;

	public Zodiac(ZodiacSign sign) {
		this.sign = sign;
	}

	/**
	 * Returns the sign of the zodiac.
	 */
	public ZodiacSign getSign() {
		return sign;
	}

}
