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
 * Java class for WSFloatingPointValue complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="WSFloatingPointValue">
 *   &lt;complexContent>
 *     &lt;extension base="{utcs.values}WSResourceValue">
 *       &lt;sequence>
 *         &lt;element name="maximumValue" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="minimumValue" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="floatingPointValue" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

public class WSFloatingPointValue extends WSResourceValue {

	protected double maximumValue;
	protected double minimumValue;
	protected double floatingPointValue;

	/**
	 * Gets the value of the maximumValue property.
	 * 
	 */
	public double getMaximumValue() {
		return maximumValue;
	}

	/**
	 * Sets the value of the maximumValue property.
	 * 
	 */
	public void setMaximumValue(double value) {
		this.maximumValue = value;
	}

	/**
	 * Gets the value of the minimumValue property.
	 * 
	 */
	public double getMinimumValue() {
		return minimumValue;
	}

	/**
	 * Sets the value of the minimumValue property.
	 * 
	 */
	public void setMinimumValue(double value) {
		this.minimumValue = value;
	}

	/**
	 * Gets the value of the floatingPointValue property.
	 * 
	 */
	public double getFloatingPointValue() {
		return floatingPointValue;
	}

	/**
	 * Sets the value of the floatingPointValue property.
	 * 
	 */
	public void setFloatingPointValue(double value) {
		this.floatingPointValue = value;
	}

}
