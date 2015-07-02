/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp;

import java.util.Map;

import org.openhab.binding.sapp.internal.SappBindingConfig;
import org.openhab.binding.sapp.internal.SappPnmas;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Sapp items.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.

 * @author Paolo Denti
 * @since 1.8.0
 */
public interface SappBindingProvider extends BindingProvider {

    /**
     * @param itemName The name of the item
     * @return The bindingConfig for the itemName
     */
	SappBindingConfig getBindingConfig(String itemName);
	
    /**
     * @return The pnmas Map (by pnmas id)
     */
	Map<String, SappPnmas> getPnmasMap();

    /**
     * @param itemName The name of the item
     * @return The item with the given name
     */
    Item getItem(String itemName);
    
    /**
     * @return returns true is some bindings have been added; should be reset by the actual provider
     */
    boolean isFullRefreshNeeded();

    /**
     * @param fullRefreshNeeded should be used by the actual provider after a full refresh
     */
    void setFullRefreshNeeded(boolean fullRefreshNeeded);
}
