/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import java.util.ArrayList;

import org.openhab.binding.insteonplm.internal.utils.Utils;

/**
 * Describes an Insteon device category, following the Insteon
 * documentation in "Insteon Device Categories and Product Keys".
 * The device categories are loaded from an XML file by the
 * {@link DeviceCategoryLoader} 
 * 
 * @author Daniel Pfrommer
 * @since 1.5.0
 */

public class DeviceCategory {
	private static ArrayList<DeviceCategory> s_categories =
			new ArrayList<DeviceCategory>();
	
	private String 	m_name;		// Insteon name of device category
	private String 	m_desc;		// description
	private int	m_devCat;	// Insteon device category number

	/**
	 * Default constructor, will create invalid category.
	 */
	public DeviceCategory() {
		this("INVAL_CAT_NAME", "INVAL_DESC_NAME", 0xFF);
	}
	
	/**
	 * Constructor. Expects the official Insteon naming and numbering as input
	 * parameters.
	 * @param name Insteon Device Category Name, from the Insteon Docs
	 * @param desc Insteon Device Description, from the Insteon Docs
	 * @param devcat Insteon Dev Cat, from the Insteon Docs
	 */
	public DeviceCategory(String name, String desc, int devcat) {
		m_name		= name;
		m_desc		= desc;
		m_devCat	= devcat;
	}
	
	public int		getDevCat()	{ return m_devCat; }
	public String	getDesc()	{ return m_desc; }
	public String	getName()	{ return m_name; }

	public void setName(String name)			{ m_name = name; }
	public void setDesc(String description)	{ m_desc = description; }
	public void setDevCat(int devCat)			{ m_devCat = devCat; }
	
	
	public String toString() {
		return "DevCat: " + Utils.getHexByte(getDevCat()) + " Name: " + getName();
	}

	/**
	 * Returns DeviceCategory object in the static array list,
	 * or creates and returns invalid object if it cannot find one.
	 * 
	 * @param devCat
	 * @return reference to the device category object
	 */
	public static DeviceCategory getCategory(int devCat) {
		for (DeviceCategory cat : s_categories) {
			if (cat.getDevCat() == devCat) {
				return cat;
			}
		}
		//We could not find one, create a default
		DeviceCategory category = new DeviceCategory();
		category.setDevCat(devCat);
		return category;
	}
	
	/**
	 * Override equality test to keep DeviceCategories
	 * in a container.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		DeviceCategory d = (DeviceCategory) o;
		if (d.getDevCat() != getDevCat()) return false;
		return true;
	}
	/**
	 * Adds device category to the 
	 * @param category the object to be added to the category list
	 */
	public static void addCategory(DeviceCategory category) {
		s_categories.add(category);
	}
}