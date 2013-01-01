/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.ihc.utcs;

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
