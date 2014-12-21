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
 * Base class for the rise and set ranges.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public abstract class RiseSet implements Planet {
	private Range rise = new Range();
	private Range set = new Range();

	/**
	 * Returns the rise range.
	 */
	public Range getRise() {
		return rise;
	}

	/**
	 * Sets the rise range.
	 */
	public void setRise(Range rise) {
		this.rise = rise;
	}

	/**
	 * Returns the set range.
	 */
	public Range getSet() {
		return set;
	}

	/**
	 * Sets the set range.
	 */
	public void setSet(Range set) {
		this.set = set;
	}

}
