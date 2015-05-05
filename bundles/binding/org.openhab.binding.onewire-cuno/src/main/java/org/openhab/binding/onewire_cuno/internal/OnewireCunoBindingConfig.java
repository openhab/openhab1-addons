/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire_cuno.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * Binding config for Onewire devices.
 * 
 * @author Robert Delbrück
 * @since 1.7.0
 * 
 */
public class OnewireCunoBindingConfig implements BindingConfig {

	/**
	 * The complete address including Housecode of the device
	 */
	private String address;
	private Item item;

	public OnewireCunoBindingConfig(String address, Item item) {
		this.address = address;
		this.item = item;
	}

	public String getAddress() {
		return address;
	}

	public Item getItem() {
		return item;
	}

}
