/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wemo;

import org.openhab.binding.wemo.internal.WemoGenericBindingProvider.WemoChannelType;
import org.openhab.core.binding.BindingProvider;


/**
 * @author Hans-Jörg Merk
 * @since 1.6.0
 */
public interface WemoBindingProvider extends BindingProvider {

	/**
	 * Returns the friendlyName for the item with the given name.
	 * 
	 * @param itemName
	 * @return The friendlyName if there is an item with the given name, null
	 *         otherwise.
	 */
	public String getUDN(String itemName);
	
	/**
	 * Returns the channelType for the item with the given name.
	 * 
	 * @param itemName
	 * @return The channelType if there is an item with the given name, null
	 *         otherwise.
	 */
	public WemoChannelType getChannelType(String itemName);


}


