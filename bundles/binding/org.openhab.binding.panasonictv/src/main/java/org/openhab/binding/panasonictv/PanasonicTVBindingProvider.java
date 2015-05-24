/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panasonictv;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface defines a method for retrieving the binding configuration by an item name.
 * 
 * @author Andre Heuer
 * @since 1.7.0
 */
public interface PanasonicTVBindingProvider extends BindingProvider {

	/**
	 * This method returns the item configuration based on the item name
	 * 
	 * @param item The name of the item
	 * @return Item/Binding configuration for the given item
	 */
	PanasonicTVBindingConfig getBindingConfigForItem(String item);
}
