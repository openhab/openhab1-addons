/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.neohub;

import org.openhab.binding.neohub.internal.NeoStatProperty;
import org.openhab.core.binding.BindingProvider;

/**
 * NeoHub binding provider.
 * 
 * @author Sebastian Prehn
 * @since 1.5.0
 */
public interface NeoHubBindingProvider extends BindingProvider {
	/**
	 * Returns the NeoStatProperty that this binding is configured for.
	 * 
	 * @param itemName
	 *            must match one of the registered items. Otherwise an
	 *            IllegalStateException will be thrown.
	 * @return the property configured for this item in your *.item config file
	 */
	NeoStatProperty getNeoStatProperty(String itemName);

	/**
	 * Returns the name of the device that this binding is configured for.
	 * 
	 * @param itemName
	 *            must match one of the registered items. Otherwise an
	 *            IllegalStateException will be thrown.
	 * @return the neo stat device name configured for this item in your *.item
	 *         config file
	 */
	String getNeoStatDevice(String itemName);
}
