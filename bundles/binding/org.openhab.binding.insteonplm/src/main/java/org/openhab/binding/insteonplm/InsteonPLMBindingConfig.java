/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm;

import java.util.HashMap;

import org.openhab.binding.insteonplm.internal.device.InsteonAddress;
import org.openhab.core.binding.BindingConfig;

/**
 * Holds binding configuration
 * 
 * @author Bernd Pfrommer
 * @author Daniel Pfrommer
 * @since 1.5.0
 */
public class InsteonPLMBindingConfig implements BindingConfig {
	/**
	 * Constructor
	 * @param adr is the Insteon address (format xx.xx.xx) as a string
	 * @param params arguments given in the binding file, as key-value pairs
	 */
	public InsteonPLMBindingConfig(InsteonAddress adr, String feature,
			String productKey, HashMap<String, String> params) {
		this.address	= adr;
		this.feature	= feature;
		this.productKey	= productKey;
		this.params		= params;
	}

	private final InsteonAddress		address;
	private final String				feature;
	private final String				productKey;
	private final HashMap<String, String>	params;

	/**
	 * Returns insteon address of device bound to item
	 * @return address of device
	 */
	public InsteonAddress getAddress() { return address; }
	
	/**
	 * Returns the feature of the device bound to item
	 * @return feature of device
	 */
	public String getFeature() { return feature; }
	
	/**
	 * Returns the product key of the device. The product key
	 * is necessary to configure the device properly.
	 * @return product key
	 */
	public String getProductKey() { return productKey; }

	/**
	 * Returns the arguments entered in the binding string.
	 * @return a map of arguments
	 */
	public HashMap<String,String> getParameters() {
		return params;
	}
}
