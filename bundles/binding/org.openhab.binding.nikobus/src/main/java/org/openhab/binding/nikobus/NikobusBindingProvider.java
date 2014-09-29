/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikobus;

import java.util.List;

import org.openhab.binding.nikobus.internal.config.AbstractNikobusItemConfig;
import org.openhab.binding.nikobus.internal.core.NikobusModule;
import org.openhab.core.binding.BindingProvider;

/**
 * Binding Provider for Nikobus.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public interface NikobusBindingProvider extends BindingProvider {

	/**
	 * Get the item binding with the given itemName.
	 * 
	 * @param itemName
	 *            name of the item
	 * @return NikobusItemBinding or null if none found
	 */
	public AbstractNikobusItemConfig getItemConfig(String itemName);

	/**
	 * Lookup a module by its' address.
	 * 
	 * @param name
	 *            e.g. 3444-1
	 * @return NikobusModule
	 */
	public NikobusModule getModule(String moduleAddress);

	/**
	 * Get list of all registered modules
	 */
	public List<NikobusModule> getAllModules();

}
