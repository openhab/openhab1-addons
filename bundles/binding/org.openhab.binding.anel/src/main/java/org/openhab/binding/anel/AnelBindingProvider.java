/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.anel;

import org.openhab.binding.anel.internal.AnelCommandType;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * The binding provider.
 * 
 * @author paphko
 * @since 1.6.0
 */
public interface AnelBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            The name of the item to find the type for.
	 * @return the type of the Item identified by {@code itemName}.
	 */
	Class<? extends Item> getItemType(String itemName);

	/**
	 * Returns the command type to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            The item for which to find a command type.
	 * 
	 * @return the corresponding command type to the given <code>itemName</code>
	 *         .
	 */
	AnelCommandType getCommandType(String itemName);

	/**
	 * Returns the device id for this binding provider.
	 * 
	 * @param itemName
	 *            The item for which to fina a device name.
	 * @return the device if to the given <code>itemName</code>.
	 */
	String getDeviceId(String itemName);
}
