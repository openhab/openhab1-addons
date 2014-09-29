/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.piface.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * Represents an openHAB binding to a PiFace extension board. There are 8 input
 * and 8 output pins. They are referenced using a pin type (IN or OUT) and
 * a pin number (0 - 7).
 * 
 * The binding also supports the WATCHDOG binding which will poll the PiFace
 * every few seconds to make sure it is still alive/responding.
 * 
 * @author Ben Jones
 * @since 1.3.0
 */
public class PifaceBindingConfig implements BindingConfig {

	public enum BindingType {
		IN,
		OUT,
		WATCHDOG;
		
		public static BindingType parse(String type) {
			if (type.equals("IN"))
				return BindingType.IN;
			if (type.equals("OUT"))
				return BindingType.OUT;
			if (type.equals("WATCHDOG"))
				return BindingType.WATCHDOG;
			
			throw new RuntimeException("Invalid binding type: " + type + " (only support IN/OUT/WATCHDOG)");
		}
	}
	
	private final String pifaceId;
	private final BindingType bindingType;
	private final int pinNumber;

	private final Class<? extends Item> itemType;
	
	public PifaceBindingConfig(String pifaceId, BindingType bindingType, int pinNumber, Class<? extends Item> itemType) {
		this.pifaceId = pifaceId;
		this.bindingType = bindingType;
		this.pinNumber = pinNumber;
		this.itemType = itemType;
	}
	
	public String getPifaceId() {
		return pifaceId;
	}
	
	public BindingType getBindingType() {
		return bindingType;
	}
	
	public int getPinNumber() {
		return pinNumber;
	}
	
	public Class<? extends Item> getItemType() {
		return itemType;
	}
}
