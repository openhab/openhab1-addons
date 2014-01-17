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
 * Java class for WSIntegerValue complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="WSIntegerValue">
 *   &lt;complexContent>
 *     &lt;extension base="{utcs.values}WSResourceValue">
 *       &lt;sequence>
 *         &lt;element name="integer" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="maximumValue" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="minimumValue" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

public class WSIntegerValue extends WSResourceValue {

	protected int integer;
	protected int maximumValue;
	protected int minimumValue;

	/**
	 * Gets the value of the integer property.
	 * 
	 */
	public int getInteger() {
		return integer;
	}

	/**
	 * Sets the value of the integer property.
	 * 
	 */
	public void setInteger(int value) {
		this.integer = value;
	}

	/**
	 * Gets the value of the maximumValue property.
	 * 
	 */
	public int getMaximumValue() {
		return maximumValue;
	}

	/**
	 * Sets the value of the maximumValue property.
	 * 
	 */
	public void setMaximumValue(int value) {
		this.maximumValue = value;
	}

	/**
	 * Gets the value of the minimumValue property.
	 * 
	 */
	public int getMinimumValue() {
		return minimumValue;
	}

	/**
	 * Sets the value of the minimumValue property.
	 * 
	 */
	public void setMinimumValue(int value) {
		this.minimumValue = value;
	}

}
