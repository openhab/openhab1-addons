/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.utcs;

/**
 * <p>
 * Java class for WSTimeValue complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="WSTimeValue">
 *   &lt;complexContent>
 *     &lt;extension base="{utcs.values}WSResourceValue">
 *       &lt;sequence>
 *         &lt;element name="hours" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="minutes" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="seconds" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

public class WSTimeValue extends WSResourceValue {

	protected int hours;
	protected int minutes;
	protected int seconds;

	/**
	 * Gets the value of the hours property.
	 * 
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * Sets the value of the hours property.
	 * 
	 */
	public void setHours(int value) {
		this.hours = value;
	}

	/**
	 * Gets the value of the minutes property.
	 * 
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * Sets the value of the minutes property.
	 * 
	 */
	public void setMinutes(int value) {
		this.minutes = value;
	}

	/**
	 * Gets the value of the seconds property.
	 * 
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * Sets the value of the seconds property.
	 * 
	 */
	public void setSeconds(int value) {
		this.seconds = value;
	}

}
