/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom;

import java.util.List;
import java.util.Set;

import org.openhab.binding.digitalstrom.internal.config.DigitalSTROMBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Alexander Betker
 * @author Alex Maier
 * @since 1.3.0
 */
public interface DigitalSTROMBindingProvider extends BindingProvider {
	
	/**
	 * Returns the configuration for the item with the given name.
	 * @param itemName
	 * @return The configuration if there is an item with the given name, null
	 *         otherwise.
	 */
	public DigitalSTROMBindingConfig getItemConfig(String itemName);
	
	public List<String> getItemNamesByDsid(String dsid);
	
	public Set<Item> getItemNamesByContext(String context);

	public List<DigitalSTROMBindingConfig> getAllCircuitConsumptionItems();
	
	public List<DigitalSTROMBindingConfig> getAllDeviceConsumptionItems();	
	
}
