/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon;

import org.openhab.binding.denon.internal.DenonBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Denon properties. 
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public interface DenonBindingProvider extends BindingProvider {
	
	/**
	 * Get configuration by item name 
	 * 
	 * @param itemName The name of the item
	 * @return The binding config for this item 
	 */
	public DenonBindingConfig getConfig(String itemName);
	
	/**
	 * Get configuration by instance and property
	 * 
	 * @param instance Name of the Denon receiver instance
	 * @param property Name of the property
	 * @return The binding config for this item 
	 */
	public DenonBindingConfig getConfig(String instance, String property);

}
