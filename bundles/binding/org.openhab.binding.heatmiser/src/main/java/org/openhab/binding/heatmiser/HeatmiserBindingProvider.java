/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.heatmiser;

import java.util.List;

import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserThermostat.Functions;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * Heatmiser binding provider
 * 
 * @author Chris Jackson
 * @since 1.3.0
 */
public interface HeatmiserBindingProvider extends BindingProvider {
	public List<String> getBindingItemsAtAddress(int addr);
	public Functions getFunction(String itemName);
	public int getAddress(String itemName);
	public Class<? extends Item> getItemType(String itemName);
}
