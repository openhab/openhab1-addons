/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.milight;

import org.openhab.binding.milight.internal.MilightBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * The interface to implement for classes that want to contribute binding configurations for Milight.
 * 
 * @author Hans-Joerg Merk
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public interface MilightBindingProvider extends BindingProvider {
	
	/**
	 * Returns the configuration for the item with the given name.
	 * 
	 * @param itemName
	 * @return The configuration if there is an item with the given name, null
	 *         otherwise.
	 */
	public MilightBindingConfig getItemConfig(String itemName);

}
