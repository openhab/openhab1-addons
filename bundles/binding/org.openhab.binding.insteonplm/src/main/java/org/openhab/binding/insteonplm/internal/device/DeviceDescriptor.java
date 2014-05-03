/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import org.openhab.binding.insteonplm.internal.utils.Utils;

/**
 * Class that holds all the data describing an Insteon device:
 * (category, sub category, version) 
 * 
 * @author Daniel Pfrommer
 * @since 1.5.0
 */

public class DeviceDescriptor {
	public static DeviceDescriptor DEFAULT =
		new DeviceDescriptor(new DeviceSubCategory(0xff));

	private DeviceSubCategory m_deviceCat;
	private int				m_version;
	
	public DeviceDescriptor(DeviceSubCategory deviceCat) {
		m_deviceCat = deviceCat;
	}
	
	public DeviceDescriptor(DeviceCategory category) {
		m_deviceCat = new DeviceSubCategory(category, 0xff);
	}
	
	public DeviceCategory		getDevCat() 	{return m_deviceCat.getCategory(); }
	public DeviceSubCategory	getSubCat() 	{return m_deviceCat;				}
	public int					getVersion()	{return m_version;					}
	
	public DeviceDescriptor		setVersion(int vers) { m_version = vers; return this;	}
	
	public String toString() {
		return getDevCat().getName() + "(" + Utils.getHexByte(getDevCat().getDevCat()) + ")"
				+ ":" + getSubCat().getName() + "(" + Utils.getHexByte(getSubCat().getSubCat()) +
				")(vers:" + getVersion() + ")"; 
	}
	
	public String toShortString() {
		return "Cat:" + Utils.getHexByte(getDevCat().getDevCat()) + ",SubCat:" + Utils.getHexByte(getSubCat().getSubCat());
	}
	
	/**
	 * implement equals for equality check.
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		DeviceDescriptor d = (DeviceDescriptor) o;
		if (d.getVersion() != getVersion()) return false;
		if (!getDevCat().equals(d.getDevCat())) return false;
		if (!getSubCat().equals(d.getSubCat())) return false;
		return true;
	}
	
	/**
	 * Factory method for creating a device descriptor
	 */
	public static DeviceDescriptor s_getDeviceDescriptor(int devCat, int subCat, int version) {
		return new DeviceDescriptor(DeviceSubCategory.getSubCategory(devCat, subCat)).setVersion(version);
	}

}