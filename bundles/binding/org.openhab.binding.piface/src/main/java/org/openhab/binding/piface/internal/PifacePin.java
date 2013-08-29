/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.piface.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * Represents a single pin on the PiFace extension board. There are 8 input
 * and 8 output pins. They are referenced using a pin type (IN or OUT) and
 * a pin number (0 - 7).
 * 
 * @author Ben Jones
 * @since 1.3.0
 */
public class PifacePin implements BindingConfig {

	public enum PinType {
		IN,
		OUT;
		
		public static PinType parse(String type) {
			if (type.equals("IN"))
				return PinType.IN;
			if (type.equals("OUT"))
				return PinType.OUT;
			
			throw new RuntimeException("Invalid pin type: " + type + " (only support IN/OUT)");
		}
	}
	
	private final String pifaceId;
	private final PinType pinType;
	private final int pinNumber;

	private final Class<? extends Item> itemType;
	
	public PifacePin(String pifaceId, PinType pinType, int pinNumber, Class<? extends Item> itemType) {
		this.pifaceId = pifaceId;
		this.pinType = pinType;
		this.pinNumber = pinNumber;
		this.itemType = itemType;
	}
	
	public String getPifaceId() {
		return pifaceId;
	}
	
	public PinType getPinType() {
		return pinType;
	}
	
	public int getPinNumber() {
		return pinNumber;
	}
	
	public Class<? extends Item> getItemType() {
		return itemType;
	}
}
