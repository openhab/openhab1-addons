/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal.model;

/**
 * Address Range model
 * 
 * @author Paolo Denti
 * @since 1.8.0
 * 
 */
public class SappAddressRange {

	private int loRange;
	private int hiRange;

	/**
	 * Constructor
	 */
	public SappAddressRange(int loRange, int hiRange) {
		this.loRange = loRange;
		this.hiRange = hiRange;
	}

	/**
	 * loRange getter
	 */
	public int getLoRange() {
		return loRange;
	}

	/**
	 * hiRange getter
	 */
	public int getHiRange() {
		return hiRange;
	}
}
