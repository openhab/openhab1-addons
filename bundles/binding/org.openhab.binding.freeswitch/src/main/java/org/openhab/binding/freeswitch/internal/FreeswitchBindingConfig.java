/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.freeswitch.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * Freeswitch binding configuration. This holds the item configuration
 * needed to bind to a Freeswitch server. 
 * @author Dan Cunningham
 * @since 1.4.0
 */
public class FreeswitchBindingConfig implements BindingConfig{
	private FreeswitchBindingType type;
	private String argument;
	private final Class<? extends Item> itemType;
	private String itemName;
	
	public FreeswitchBindingConfig(String itemName, Class<? extends Item> itemType, FreeswitchBindingType type, String argument) {
		super();
		this.itemName = itemName;
		this.itemType = itemType;
		this.type = type;
		this.argument = argument;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Class<? extends Item> getItemType() {
		return itemType;
	}
	public FreeswitchBindingType getType() {
		return type;
	}
	public void setType(FreeswitchBindingType type) {
		this.type = type;
	}
	public String getArgument() {
		return argument;
	}
	public void setArgument(String argument) {
		this.argument = argument;
	}
	public boolean filtered(){
		return StringUtils.isNotBlank(argument);
	}
	
}
