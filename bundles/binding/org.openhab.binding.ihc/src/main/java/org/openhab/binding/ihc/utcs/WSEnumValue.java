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
 * Java class for WSEnumValue complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="WSEnumValue">
 *   &lt;complexContent>
 *     &lt;extension base="{utcs.values}WSResourceValue">
 *       &lt;sequence>
 *         &lt;element name="definitionTypeID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="enumValueID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="enumName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

public class WSEnumValue extends WSResourceValue {

	protected int definitionTypeID;
	protected int enumValueID;
	protected String enumName;

	/**
	 * Gets the value of the definitionTypeID property.
	 * 
	 */
	public int getDefinitionTypeID() {
		return definitionTypeID;
	}

	/**
	 * Sets the value of the definitionTypeID property.
	 * 
	 */
	public void setDefinitionTypeID(int value) {
		this.definitionTypeID = value;
	}

	/**
	 * Gets the value of the enumValueID property.
	 * 
	 */
	public int getEnumValueID() {
		return enumValueID;
	}

	/**
	 * Sets the value of the enumValueID property.
	 * 
	 */
	public void setEnumValueID(int value) {
		this.enumValueID = value;
	}

	/**
	 * Gets the value of the enumName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEnumName() {
		return enumName;
	}

	/**
	 * Sets the value of the enumName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEnumName(String value) {
		this.enumName = value;
	}

}
