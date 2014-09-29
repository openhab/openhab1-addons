/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.energenie;

import org.openhab.core.binding.BindingProvider;
import org.openhab.binding.energenie.internal.EnergenieBindingConfig;

/**
 * @author Hans-Jörg Merk
 * @since 1.6.0
 */
public interface EnergenieBindingProvider extends BindingProvider {

	/**
	 * Returns the configuration for the item with the given name.
	 * 
	 * @param itemName
	 * @return the configuration if there is an item with the given name, null otherwise.
	 */
	
	public EnergenieBindingConfig getItemConfig(String itemName);

}
