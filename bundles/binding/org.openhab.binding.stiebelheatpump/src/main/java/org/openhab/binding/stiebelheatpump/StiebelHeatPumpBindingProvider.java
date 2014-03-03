/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Peter Kreutzer
 * @since 1.4.0
 */
public interface StiebelHeatPumpBindingProvider extends BindingProvider {
	
	/**
	 * Returns the configured parameter for the given <code>itemName</code>. If
	 * no parameter has been configured or the itemName is unknown, <code>null<code> is returned
	 * 
	 * @param itemName the item to find the paramter for
	 * @return the configured parameter or <code>null<code> if nothing is configured or the itemName is unknown
	 */
	public String getParameter(String itemName);
	
}
