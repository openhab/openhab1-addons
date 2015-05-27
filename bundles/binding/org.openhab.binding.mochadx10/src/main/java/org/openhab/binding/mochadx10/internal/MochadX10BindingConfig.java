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
 * <li>Optional: The transmission method for the command: pl for powerline, rf for radio. If no
 * transmission method is specified, powerline will be used.</li>
 * <li>Optional: The dimming method to be used: xdim for modules that support the xdim command, 
 * dim for modules that only support the dim command. If no dimming method is specified, 
 * xdim will be used.</li>
 * </ul>
 *
 * Examples: 
 * <ul>
 * <li>a12 to address the module with house code 'a' and unit code '12' via power line, using xdim command</li>
 * <li>m3:rf to address the module with house code 'm' and unit code '3' via radio</li>
 * <li>h8:dim to address the module with house code 'h' and unit code '8', using dim command</li>
 * <li>k5:rf:dim to address the module with house code 'k' and unit code '5', via radio, using dim command</li>
 * </ul>
 * 
 * @author Jack Sleuters
 * @since  1.7.0
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
	
	/**
	 * The method to be used when dimming
	 * Value 'dim':  use dim command
	 * Value 'xdim': use xdim command
	 */
	private String dimMethod;

	/**
	 * The type of the item
	 */
	private Class<? extends Item> itemType;
	
	/**
	 * The name of the item
	 */
	private String itemName;
	
	/**
	 * Constructor of the MochadX10BindingConfig.
	 * 
	 * @param itemName			The name of the item
	 * @param itemType			The type of the item			
	 * @param transmitMethod	The optional transmission method for the X10 command.
	 * @param dimMethod			The optional dim method 
	 * @param address 			The address of the X10 module
	 */
	public MochadX10BindingConfig(String itemName, Class<? extends Item> itemType, String transmitMethod, String dimMethod, String address) {
		this.itemName = itemName;
		this.itemType = itemType;
		this.address = address.toLowerCase();
		this.transmitMethod = transmitMethod.toLowerCase();
		this.dimMethod = dimMethod;
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
	 * @return the name of the item
	 */
	public String getItemName() {
		return itemName;
	}
	
	/**
	 * @return the dim method to be used when dimming
	 */
	public String getDimMethod() {
		return dimMethod;
	}
	
	/**
	 * @return the transmission method specified in the binding
	 */
	public String getTransmitMethod() {
		return transmitMethod;
	}
}
