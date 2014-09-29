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
import java.util.HashMap;

import org.openhab.binding.insteonplm.internal.utils.Utils;

/**
 * Describes an Insteon device sub category, following the Insteon
 * documentation in "Insteon Device Categories and Product Keys".
 * A subcategory references all known product keys of that subcategory.
 * The device sub categories are loaded from an XML file by the
 * {@link DeviceCategoryLoader} 
 * 
 * @author Daniel Pfrommer
 * @since 1.5.0
 */

public class DeviceSubCategory {
	public static ArrayList<DeviceSubCategory> SUBCATEGORIES = new ArrayList<DeviceSubCategory>();
	private String 			m_name;
	private String			m_desc;
	private HashMap<String, HashMap<String,String>> 	m_productKeys =
				new HashMap<String, HashMap<String,String>>();
	private int				m_subCat;
	private DeviceCategory 	m_category;

	public DeviceSubCategory(int subCat) {
		this(new DeviceCategory(), subCat);
	}
	public DeviceSubCategory(DeviceCategory category, int subCat) {
		this("INVALID_SUBCAT_NAME", "INVALID_SUBCAT_DESC", subCat, category);
	}
	public DeviceSubCategory(String name, String desc, int subCat, DeviceCategory category) {
		m_name = name;
		m_desc = desc;
		m_subCat = subCat;
		m_category = category;
	}
	
	public int 				getSubCat() {return m_subCat;}
	public DeviceCategory 	getCategory() {return m_category;}
	public String 			getName() {return m_name;}
	public String 			getDesc() {return m_desc;}
	public HashMap<String, HashMap<String,String>> getProductKeys() {
			return m_productKeys; }
	
	public void 		setCategory(DeviceCategory category) {m_category = category;}
	public void			setName(String name) { m_name = name; }
	public void			setDesc(String desc) { m_desc = desc; }
	public void			addProductKey(String key) { 
		if (!m_productKeys.containsKey(key))
			m_productKeys.put(key,  new HashMap<String,String>());
	}
	public void 		setSubCat(int subCategory) {m_subCat = subCategory;}
	
	public void			addFeature(String productKey, String name, String f) {
								m_productKeys.get(productKey).put(name, f); }

	public String toString() {
		return (m_name + "|desc:" + m_desc + "|prod keys:" + m_productKeys.size() +
				"|cat:" + m_category + "(" + Utils.getHexByte(m_subCat) + ")");
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		DeviceSubCategory d = (DeviceSubCategory) o;
		if (d.getSubCat() != getSubCat()) return false;
		return true;
	}

	public static void addSubCategory(DeviceSubCategory category) {
		SUBCATEGORIES.add(category);
	}
	
	public static DeviceSubCategory getSubCategory(int devCat, int subCat) {
		DeviceCategory category = DeviceCategory.getCategory(devCat);
		return getSubCategory(category, subCat);
	}
	
	public static DeviceSubCategory getSubCategory(DeviceCategory category, int subCat) {
		for (DeviceSubCategory cat : SUBCATEGORIES) {
			if (cat.getSubCat() == subCat && cat.getCategory().equals(category)) {
				return cat;
			}
		}
		return new DeviceSubCategory(category, subCat);
	}
}