/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire_cuno;

import org.openhab.binding.onewire_cuno.internal.OnewireCunoBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * Provides methods for item configuration
 * 
 * @author Robert Delbrück
 * @since 1.7.0
 */
public interface OnewireCunoBindingProvider extends BindingProvider {

	/**
	 * returns the item config for the given itemname
	 * @param itemName itemname to return config for
	 * @return item configuration
	 */
	OnewireCunoBindingConfig getConfigForItemName(String itemName);

	/**
	 * return the item config for a given address
	 * @param address address to return config for
	 * @return item configuration
	 */
	OnewireCunoBindingConfig getConfigForAddress(String address);

}
