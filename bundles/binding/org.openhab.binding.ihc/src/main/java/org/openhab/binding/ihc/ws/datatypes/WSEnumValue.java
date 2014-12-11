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
