/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.omnilink.internal;

import org.openhab.binding.omnilink.internal.model.OmnilinkDevice;
import org.openhab.core.binding.BindingConfig;

/**
 * 
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class OmniLinkBindingConfig implements BindingConfig {

	private OmniLinkItemType objectType;
	private int number;
	private OmnilinkDevice device;
			
	/**
	 * 
	 * @param objectType that is linked to this item (units, thermostats, ect...)
	 * @param number of the Omni object 
	 */
	public OmniLinkBindingConfig(OmniLinkItemType objectType, int number) {
		super();
		this.objectType = objectType;
		this.number = number;
	}

	/**
	 * 
	 * @return the Omni object type
	 */
	public OmniLinkItemType getObjectType() {
		return objectType;
	}
	/**
	 * 
	 * @param objectType linked to this item
	 */
	public void setObjectType(OmniLinkItemType objectType) {
		this.objectType = objectType;
	}
	
	/**
	 * 
	 * @return the omni object number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * 
	 * @param number of the omni object number
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * 
	 * @return the Omni device that this item is associated with
	 */
	public OmnilinkDevice getDevice() {
		return device;
	}

	/**
	 * 
	 * @param device that this item is assoiciated with 
	 */
	public void setDevice(OmnilinkDevice device) {
		this.device = device;
	}
	
}
