/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.logic.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.types.Command;

/**
 * This class maps ItemBindings on openHAB commands.
 * It also keeps track about possible uninitialized items within the map.
 * 
 * @author Patrik Pastuschek
 * @since 1.7.0
 *
 */
public class LCNItemMap extends HashMap<Command, LCNItemBinding> implements BindingConfig {

	/**Generated serial.*/
	private static final long serialVersionUID = 2356560584495496151L;
	/**Flag that is being set, if the map contains one or more uninitialized items.*/
	private boolean hasUninitialized = false;
	
	/**
	 * Maps an ItemBinding on a key. Also the uninitialized flag is being set, because new items need to be initialized.
	 * @param key An openHAB command.
	 * @param value The ItemBinding to map.
	 */
	@Override
	public LCNItemBinding put(Command key, LCNItemBinding value) {
		
		if (!value.isInitialized()) {
			this.hasUninitialized = true;
		}
		
		return super.put(key, value);
		
	}
	
	/**
	 * Returns if there are any uninitialized ItemBindings within this map.
	 * @return true if there are uninitialized ItemBindings.
	 */
	public boolean hasUninitialized() {
		return this.hasUninitialized;
	}
	
	/**
	 * Sets the initialized state of a single ItemBinding within this map.
	 * @param elem True if item was initialized, false otherwise.
	 */
	public void setInitialized(LCNItemBinding elem) {
		for (Command com : this.keySet()) {
			if (this.get(com).equals(elem)) {
				elem.setInitialized(true);
			}
		}
	}
	
	/**
	 * Removes all bindings for given item name.
	 * @param itemName The name of the item.
	 */
	public void remove(String itemName) {
		
		List<Command> toDelete = new ArrayList<Command>();
		
		for (Command cmd : this.keySet()) {
			
			if (this.get(cmd).getItem().getName().equals(itemName)) {
				toDelete.add(cmd);
			}
			
		}
		
		for (Command cmd : toDelete) {
			this.remove(cmd);
		}
		
	}

}
