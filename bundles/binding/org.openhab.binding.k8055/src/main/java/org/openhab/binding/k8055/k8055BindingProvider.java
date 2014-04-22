/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.k8055;

import org.openhab.core.binding.BindingProvider;
import org.openhab.binding.k8055.internal.k8055GenericBindingProvider.k8055BindingConfig;

/**
 * @author Anthony Green
 * @since 1.5.0
 */
public interface k8055BindingProvider extends BindingProvider {
	/**
	 * Returns the configuration for the item with the given name.
	 * 
	 * @param itemName
	 * @return The configuration if there is an item with the given name, null
	 *         otherwise.
	 */
	public k8055BindingConfig getItemConfig(String itemName);
}
