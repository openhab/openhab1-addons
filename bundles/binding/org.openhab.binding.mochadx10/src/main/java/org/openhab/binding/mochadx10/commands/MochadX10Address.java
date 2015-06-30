/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mochadx10.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is a data class that stores X10 addresses.
 * 
 * @author Jack Sleuters
 * @since 1.7.0
 * 
 */
public class MochadX10Address {

	/**
	 * The regular expression that specifies an X10 address
	 */
	public static final Pattern X10_ADDRESS_PATTERN = Pattern.compile("(?<houseCode>[a-pA-P])(?<unitCode>([1-9]|1[0-6]))");
	
	/**
	 * The house code part of the X10 address [a..p].
	 */
	private String houseCode;
	
	/**
	 * The unit code part of the X10 address [1..16].
	 */
	private String unitCode;

	/**
	 * Constructor which initializes the houseCode and unitCode fields specifying them with separate strings.
	 * 
	 * @param houseCode		The string representing the house code part of the address
	 * @param unitCode		The string representing the unit code part of the address
	 */
	public MochadX10Address(String houseCode, String unitCode) {
		this.houseCode = houseCode;
		this.unitCode  = unitCode;
	}
	
	/**
	 * Constructor which initializes the houseCode and unitCode fields by retrieving them from one string 'address'
	 * 
	 * @param address  a string representing an X10 address
	 */
	public MochadX10Address(String address) {
		setAddress(address);
	}

	/**
	 * Default constructor
	 */
	public MochadX10Address() {
		this.houseCode = "a";
		this.unitCode  = "1";
	}

	/**
	 * Parses the string 'address' to retrieve the house code and unit code part from it.
	 * 
	 * @param address 	a string representing an X10 address
	 */
	public void setAddress(String address) {
		Matcher matcher = X10_ADDRESS_PATTERN.matcher(address);
		if (matcher.matches()) {
			try  {
				this.houseCode = matcher.group("houseCode");
				this.unitCode  = matcher.group("unitCode");
			} catch (IllegalArgumentException e) {
				this.houseCode = "a";
				this.unitCode  = "1";
			}
		}
	}
	
	/**
	 * Retrieve the value of the houseCode field
	 * 
	 * @return	a string representing the house code part of the X10 address
	 */
	public String getHouseCode() {
		return houseCode;
	}
	
	/**
	 * Sets the value of the houseCode field
	 * 
	 * @param houseCode 	a string representing the house code part of the X10 address
	 */
	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode.toLowerCase();
	}

	/**
	 * Retrieve the value of the unitCode field
	 * 
	 * @return	a string representing the unit code part of the X10 address
	 */
	public String getUnitCode() {
		return unitCode;
	}

	/**
	 * Sets the value of the unitCode field
	 * 
	 * @param houseCode 	a string representing the unit code part of the X10 address
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((houseCode == null) ? 0 : houseCode.hashCode());
		result = prime * result
				+ ((unitCode == null) ? 0 : unitCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MochadX10Address other = (MochadX10Address) obj;
		if (houseCode == null) {
			if (other.houseCode != null)
				return false;
		} else if (!houseCode.equals(other.houseCode))
			return false;
		if (unitCode == null) {
			if (other.unitCode != null)
				return false;
		} else if (!unitCode.equals(other.unitCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return houseCode + unitCode;
	}
}
