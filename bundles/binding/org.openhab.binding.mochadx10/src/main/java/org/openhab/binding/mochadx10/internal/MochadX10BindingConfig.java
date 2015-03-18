/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mochadx10.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This represents the configuration of an openHAB item that is binded to an
 * X10 module. It contains the following information:
 * 
 * <ul>
 * <li>The address of the module. Syntax: <house code><unit code>. Example: a12 or m3
 * <li>The transmission method for the command: pl for powerline, rf for radio. If no
 * transmission method is specified, powerline will be used.</li>
 * </ul>
 *
 * Examples: 
 * <ul>
 * <li>a12 to address the module with house code 'a' and unit code '12' via power line</li>
 * <li>m3 rf to address the module with house code 'm' and unit code '3' via radio</li>
 * </ul>
 * 
 * @author Jack Sleuters
 */
public class MochadX10BindingConfig implements BindingConfig {

	static final Logger logger = LoggerFactory.getLogger(MochadX10BindingConfig.class);

	/**
	 * Address of the module with syntax <house code><unit code>
	 */
	private String address;
	
	/**
	 * Transmission method
	 * Value 'pl': transmit command via powerline 
	 * Value 'rf': transmit command via radio
	 */
	private String transmitMethod;
	
	private Class<? extends Item> itemType;
	
	/**
	 * Constructor of the MochadX10BindingConfig.
	 * 
	 * @param item			
	 * @param transmitMethod
	 * 			The optional transmission method for the X10 command.
	 * @param address
	 * 			The address of the X10 module
	 */
	public MochadX10BindingConfig(Class<? extends Item> itemType, String transmitMethod, String address) {
		this.itemType = itemType;
		this.address = address.toLowerCase();
		this.transmitMethod = transmitMethod.toLowerCase();
	}

	/**
	 * @return the X10 address specified in the binding
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * @return the item class
	 */
	protected Class<? extends Item> getItemType() {
		return itemType;
	}

	/**
	 * @return the transmission method specified in the binding
	 */
	public String getTransmitMethod() {
		return transmitMethod;
	}
}
