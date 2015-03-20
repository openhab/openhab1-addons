/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarm.config;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/** 
 * Implementing classes should register themselves as a service in order to be taken into account.
 * 
 * @author Volker Daube
 * @since 1.7.0
 *
 */
public interface AlarmBindingProvider extends BindingProvider {

	/**
	 * Return all AlarmConditions for a given item or null if non exists of
	 * the itemName cannot be found.
	 * 
	 * @param itemName The name of the item
	 * @return Iterable of all AlarmCOnditions for the item or null
	 */
	public Iterable<AlarmCondition> getAlarmConditions(final String itemName);
	
    /**
     * Retrieves an item from the bindings cache.
     * @param itemName
     *            The name of the item
     * @return The item with the given name
     */
    Item getItem(final String itemName);
    
}
