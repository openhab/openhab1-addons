/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panstamp;

import org.openhab.core.binding.BindingConfig;

/**
 * This is the item binding config structure. It connects an item name with the panStamp specific setup details.
 * 
 * @author Gideon le Grange
 * @since 1.8.0
 */
public class PanStampBindingConfig<T> implements BindingConfig {

	/**
	 * Create a new instance.
	 * 
	 * @param itemName
	 *            Item name
	 * @param address
	 *            panStamp device address.
	 * @param manufacturerId
	 *            panStamp manufacturer ID as configured.
	 * @param productId
	 *            panStamp product ID as configured.
	 * @param register
	 *            panStamp register defining item.
	 * @param endpoint
	 *            panStamp endpoint defining item.
	 * @param unit
	 *            Unit for endpoint ("" means default)
	 */
	public PanStampBindingConfig(String itemName, int address, int manufacturerId, int productId, int register,
			String endpoint, String unit) {
		this.itemName = itemName;
		this.manufacturerId = manufacturerId;
		this.productId = productId;
		this.address = address;
		this.register = register;
		this.endpoint = endpoint;
		this.unit = unit;
	}

	/**
	 * Get the item name associated with this item configuration
	 * 
	 * @return The item name
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * Get the manufacturer ID configured for this item configuration
	 * 
	 * @return
	 */
	public int getManufacturerId() {
		return manufacturerId;
	}

	/**
	 * Get the product ID configured for this item configuration
	 * 
	 * @return
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * Get the device address configured for this item configuration
	 * 
	 * @return
	 */
	public int getAddress() {
		return address;
	}

	/**
	 * Get the register ID for this item configuration
	 * 
	 * @return
	 */
	public int getRegister() {
		return register;
	}

	/**
	 * Get the end point name for this item configuration
	 * 
	 * @return
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * Get the unit for this item configuration
	 * 
	 * @return
	 */
	public String getUnit() {
		return unit;
	}

	@Override
	public String toString() {
		return String
				.format("%s => (%d,%d,%s):%d/%d", itemName, address, register, endpoint, manufacturerId, productId);
	}

	private final String itemName;
	private final int manufacturerId;
	private final int productId;
	private final int address;
	private final int register;
	private final String endpoint;
	private final String unit;

}
