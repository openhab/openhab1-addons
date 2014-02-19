/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmlsmeter;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Peter Kreutzer
 * @author Günter Speckhofer
 * @since 1.4.0
 */
public interface DmlsMeterBindingProvider extends BindingProvider {
	
	/**
	 * Returns the configured obis for the given <code>itemName</code>. If
	 * no obis has been configured or the itemName is unknown, <code>null<code> is returned
	 * 
	 * @param itemName the item to find the obis for
	 * @return the configured obis or <code>null<code> if nothing is configured or the itemName is unknown
	 */
	public String getObis(String itemName);

}
