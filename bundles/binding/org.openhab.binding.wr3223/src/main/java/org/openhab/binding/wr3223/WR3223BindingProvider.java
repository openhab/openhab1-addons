/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wr3223;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Michael Fraefel
 * @since 1.7.0
 */
public interface WR3223BindingProvider extends BindingProvider {
	
	/**
	 * Provides an array of all item names of this provider for a given binding type
	 * @param bindingType the binding type of the items
	 * @return an array of all item names of this provider for the given binding type
	 */
	public String[] getItemNamesForType(WR3223CommandType bindingType);
	
	public WR3223CommandType getWR3223CommandTypeForItemName(String itemName);

}
