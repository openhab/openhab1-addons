/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.intertechno;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * This class represents the configuration of a receiving intertechno device. An
 * address and the commands for on and off need to be configured here.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class IntertechnoBindingConfig implements BindingConfig {

	private String address;
	private Item item;
	private String commandOff;
	private String commandOn;
	private long lastReceived;

	public IntertechnoBindingConfig(Item item, String address, String commandOn, String commandOff) {
		this.address = address;
		this.commandOn = commandOn;
		this.commandOff = commandOff;
		this.item = item;
	}

	public String getAddress() {
		return address;
	}

	public String getCommandValueON() {
		return commandOn;
	}

	public String getCommandValueOFF() {
		return commandOff;
	}

	public Item getItem() {
		return item;
	}

	public long getLastReceived() {
		return lastReceived;
	}

	public void setLastReceived(long lastReceived) {
		this.lastReceived = lastReceived;
	}

}
