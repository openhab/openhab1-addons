/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex;

import org.openhab.binding.plex.internal.PlexBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Plex properties. 
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public interface PlexBindingProvider extends BindingProvider {
	
	/**
	 * Get binding config by machine ID and property
	 * 
	 * @param machineIdentifier Plex machine ID
	 * @param property Name of the property 
	 * @return The binding config for parameters or null if not found 
	 */
	public PlexBindingConfig getConfig(String machineIdentifier, String property);
	
	
	/**
	 * Get binding config by itemName
	 * 
	 * @param itemName Name of the item in openHAB
	 * @return The binding config for itemName or null if not found 
	 */
	public PlexBindingConfig getConfig(String itemName);
	
}
