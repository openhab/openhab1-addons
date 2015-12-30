/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal;

import java.util.Hashtable;

import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.State;
import org.openhab.io.cv.CVApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to retrieve the item name and the class of the state
 * the client expects
 * 
 * the naming structure is: [type:]itemName
 * examples: 
 *  - number:Lights returns the state if the Lights item (which is a group in demo.items) as DecimalType (returns the number of switched on lights
 *  - Lights returns the state of the groups baseItem (ON or OFF)
 * 
 * @author Tobias Bräutigam
 * @since 1.7.0
 *
 */
public class ReturnType {
	private static final Logger logger = LoggerFactory
			.getLogger(ReturnType.class);
	private Item item;
	private Class<? extends State> stateClass;
	private String clientItemName;
	
	/**
     * maps CometVisu address transform to State class
     */
    public static Hashtable<String,Class<? extends State>> itemTypeMapper = new Hashtable<String,Class<? extends State>>();

    static {
    	itemTypeMapper.put("number", DecimalType.class);
    	itemTypeMapper.put("switch", OnOffType.class);
    	itemTypeMapper.put("contact", OpenClosedType.class);
    	itemTypeMapper.put("dimmer", DecimalType.class);
    	itemTypeMapper.put("rollershutter", UpDownType.class);
    	itemTypeMapper.put("string", StringType.class);
    	itemTypeMapper.put("datetime", DateTimeType.class);
    	itemTypeMapper.put("time", DateTimeType.class);
    	itemTypeMapper.put("color", HSBType.class);
    }
    
	
	public ReturnType(String cvItemName) throws ItemNotFoundException {
		this.clientItemName = cvItemName;
		this.stateClass = null;
		this.refreshItem();
	}
	
	/**
	 * Retrieve the item from the ItemUIRegistry again
	 * 
	 * @throws ItemNotFoundException
	 */
	public void refreshItem() throws ItemNotFoundException {
		String[] parts = this.clientItemName.split(":");
		String ohItemName = this.clientItemName;
		if (parts.length==2) {
			String classPrefix=parts[0].toLowerCase();
			if (itemTypeMapper.containsKey(classPrefix)) { 
				stateClass = itemTypeMapper.get(classPrefix);
			} else {
				logger.debug("no type found for '{}'",classPrefix);
			}
			ohItemName = parts[1];
		}
		item = CVApplication.getItemUIRegistry().getItem(ohItemName);
	}

	@Override
	public String toString() {
		return item.getName()+"=>"+stateClass;
	}

	/**
	 * returns the item
	 * 
	 * @return Item item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * the name of the client item as is was requested by the client
	 * 
	 * @return String clientItemName
	 * 			- e.g number:Lights
	 */
	public String getClientItemName() {
		return this.clientItemName;
	}
	
	/**
	 * an state class, which can be used for Item.getStateAs(stateClass)
	 * @return Class<? extends State>
	 */
	public Class<? extends State> getStateClass() {
		return stateClass;
	}
	
	
}
