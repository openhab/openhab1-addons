/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wemo;

import org.openhab.core.binding.BindingProvider;


/**
 * @author Hans-Jörg Merk
 * @since 1.6.0
 */
public interface WemoBindingProvider extends BindingProvider {

	/**
	 * Get the friendly name for the specified item
	 * 
	 * @param itemName
	 *            The item whose friendly name is required
	 * @return The openHAB class type
	 */
	public String getWemoFriendlyName(String itemName);
	

}


