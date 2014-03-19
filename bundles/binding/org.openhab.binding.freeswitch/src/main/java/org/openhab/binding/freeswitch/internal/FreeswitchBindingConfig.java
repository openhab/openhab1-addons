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
	
	/**
	 * Bind an item to a Freeswitch server
	 * For active calls (active), leave the argument empty to match any inbound
	 * call.  To filter calls by one or more Freeswitch headers, add the headers
	 * in key:value,key:value... as the argument.  For voice mail, the argument
	 * should be the users mailbox (user@domain).  For api commands the argument
	 * is ignored.
	 * 
	 * @param itemName
	 * @param itemType
	 * @param type
	 * @param argument optional
	 * 
	 *  Sample configurations:
	 *  
	 * Match all inbound calls
	 * {freeswitch="active"}
	 * 
	 * Filter calls, match all inbound call going to 5555551212
	 * {freeswitch="active:Call-Direction:inbound,Caller-Destination-Number:5555551212"}
	 * 
	 * Bind to a users (1000@pbx.mydomain.com) voice mail box
	 * {freeswitch="message_waiting:1000@pbx.mydomain.com"}
	 * 
	 * This is an API item
	 * {freeswitch="api"}
	 * 
	 */
	public FreeswitchBindingConfig(String itemName, Class<? extends Item> itemType, FreeswitchBindingType type, String argument) {
		super();
		this.itemName = itemName;
		this.itemType = itemType;
		this.type = type;
		this.argument = argument;
	}
	/**
	 * Get the name of the item
	 * @return
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * Sets the name of the item
	 * @param itemName
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * Gets the openHAB item type for a item
	 * @return
	 */
	public Class<? extends Item> getItemType() {
		return itemType;
	}
	
	/**
	 * Gets the Freeswitch binding type (active, mwi, etc..) for the item.
	 * @return
	 */
	public FreeswitchBindingType getType() {
		return type;
	}
	
	/**
	 * Sets the Freeswitch binding type for the item
	 * @param type
	 */
	public void setType(FreeswitchBindingType type) {
		this.type = type;
	}
	
	/**
	 * Gets the optional argument. For calls this is a filter String (key:value,
	 * key:value....) or empty for all inbound calls, for voice mail this will
	 * be the users vmail box and is ignored for api types.
	 * @return
	 */
	public String getArgument() {
		return argument;
	}
	
	/**
	 * Sets the optional argument
	 * @param argument
	 */
	public void setArgument(String argument) {
		this.argument = argument;
	}
	
	/**
	 * Is this a filtered binding type for active calls.
	 * @return
	 */
	public boolean filtered(){
		return StringUtils.isNotBlank(argument);
	}
	
}
