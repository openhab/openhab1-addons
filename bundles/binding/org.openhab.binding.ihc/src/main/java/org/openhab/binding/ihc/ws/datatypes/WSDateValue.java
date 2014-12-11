/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.ws.datatypes;

/**
 * <p>
 * Java class for WSDateValue complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="WSDateValue">
 *   &lt;complexContent>
 *     &lt;extension base="{utcs.values}WSResourceValue">
 *       &lt;sequence>
 *         &lt;element name="month" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="day" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public class WSDateValue extends WSResourceValue {

	protected byte month;
	protected short year;
	protected byte day;

	/**
	 * Gets the value of the month property.
	 * 
	 */
	public byte getMonth() {
		return month;
	}

	/**
	 * Sets the value of the month property.
	 * 
	 */
	public void setMonth(byte value) {
		this.month = value;
	}

	/**
	 * Gets the value of the year property.
	 * 
	 */
	public short getYear() {
		return year;
	}

	/**
	 * Sets the value of the year property.
	 * 
	 */
	public void setYear(short value) {
		this.year = value;
	}

	/**
	 * Gets the value of the day property.
	 * 
	 */
	public byte getDay() {
		return day;
	}

	/**
	 * Sets the value of the day property.
	 * 
	 */
	public void setDay(byte value) {
		this.day = value;
	}

}
