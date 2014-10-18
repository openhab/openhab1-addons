/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo;


import org.openhab.binding.plclogo.internal.PLCLogoBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author g8kmh
 * @since 1.5.0
 */
public interface PLCLogoBindingProvider extends BindingProvider {		
	public PLCLogoBindingConfig getBindingConfig(String itemName);
	
	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	public Item getItemType(String itemName);

}

