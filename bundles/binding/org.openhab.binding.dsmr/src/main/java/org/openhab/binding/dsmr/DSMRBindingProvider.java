/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and DSMR data.
 * 
 * @author M.Volaart
 * @since 1.7.0
 */
public interface DSMRBindingProvider extends BindingProvider {
	/**
	 * Returns the DSMR item the binding represents
	 * 
	 * @param String OpenHab itemName to get the DSMR item for
	 * 
	 * @return String containing the DSMR item
	 */
	public String getItem(String itemName);
}
