/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro;

import org.openhab.binding.astro.internal.config.AstroBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * The interface to implement to provide a binding for Astro.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public interface AstroBindingProvider extends BindingProvider {

	/**
	 * Returns the AstroBindingConfig for an item by name.
	 */
	public AstroBindingConfig getBindingFor(String itemName);

	/**
	 * Returns the item object by itemName.
	 */
	public Item getItem(String itemName);

	/**
	 * Returns true, if the specified binding is available.
	 */
	public boolean hasBinding(AstroBindingConfig bindingConfig);
}
