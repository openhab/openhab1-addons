/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.harmonyhub.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;

/**
 * This is a helper class holding binding specific configuration details
 * 
 * @author Dan Cunningham
 * @since 1.7.0
 */
public class HarmonyHubBindingConfig implements BindingConfig {
	HarmonyHubBindingType bindingType;
	String qualifier;
	String type;
	String param1;
	String param2;
	Class<? extends Item> itemType;
	
	/**
	 * Constructor for the HarmonyHubBindingConfig
	 * @param bindingType (in, out or both)
	 * @param qualifier for this hub if one exists
	 * @param action that is to be performed
	 * @param param1 first optional parameter for the action
	 * @param param2 second optional parameter for the action
	 * @param itemType of the item
	 */
	public HarmonyHubBindingConfig(HarmonyHubBindingType bindingType,
		String qulifier, String type, String param1, String param2, 
		Class<? extends Item> itemType) {
	    super();
	    this.bindingType = bindingType;
	    this.qualifier = qulifier;
	    this.type = type;
	    this.param1 = param1;
	    this.param2 = param2;
	    this.itemType = itemType;
	}
	/**
	 * Returns the type of binding type for this item
	 * @return
	 */
	public HarmonyHubBindingType getBindingType() {
	    return bindingType;
	}
	/**
	 * The qualifier allows multiple hubs to be used
	 * @return
	 */
	public String getQualifier() {
	    return qualifier;
	}
	/**
	 * Returns the harmony item type, see {@link HarmonyHubBindingType}
	 * @return
	 */
	public String getType() {
	    return type;
	}
	/**
	 * Return the first parsed optional parameter
	 * @return
	 */
	public String getParam1() {
	    return param1;
	}
	/**
	 * Return the second parsed optional parameter
	 * @return
	 */
	public String getParam2() {
	    return param2;
	}

	public Class<? extends Item> getItemType() {
		return itemType;
	}
	/**
	 * Returns true if the qualifier matches our the config qualifier
	 * This will match equality as well as if both are null
	 * @param qualifier
	 * @return true if matching, false if not.
	 */
	public boolean matchesQualifier(String qualifier){
		if(this.qualifier == null){
			return qualifier == null;
		} else {
			return this.qualifier.equals(qualifier);
		}
	}
}
