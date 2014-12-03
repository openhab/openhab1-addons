/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight;

import org.openhab.binding.pilight.internal.PilightBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and pilight items.
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public interface PilightBindingProvider extends BindingProvider {
	
	/**
	 * Get binding configuration based on openHAB itemName  
	 * 
	 * @param itemName itemName in openHAB
	 * @return The binding config for {@code itemName} or null if not found
	 */
	public PilightBindingConfig getBindingConfig(String itemName);
	
	/**
	 * Get binding configuration based on pilight references 
	 * 
	 * @param instance Name of the pilight instance 
	 * @param location Location of the device in pilight
	 * @param device Name of the device in pilight
	 * @return The binding config for the pilight references or null if not found
	 */
	public PilightBindingConfig getBindingConfig(String instance, String location, String device);
}