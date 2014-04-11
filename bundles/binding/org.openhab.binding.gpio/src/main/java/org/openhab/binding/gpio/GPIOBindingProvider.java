/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gpio;

import org.openhab.core.binding.BindingProvider;

/**
 * GPIO binding providers interface.
 * 
 * @author Dancho Penev
 * @since 1.5.0
 */
public interface GPIOBindingProvider extends BindingProvider {

	public static final int PINNUMBER_UNDEFINED = -1;
	public static final long DEBOUNCEINTERVAL_UNDEFINED = -1;

	/**
	 * Query for configured pin number.
	 * 
	 * @param itemName the name of the item for which we make query
	 * @return the configured pin number
	 */
	public int getPinNumber(String itemName);

	/**
	 * Query for configured debounce interval.
	 * 
	 * @param itemName the name of the item for which we make query
	 * @return the configured debounce interval
	 */
	public long getDebounceInterval(String itemName);

	/**
	 * Query for configured activelow state.
	 * 
	 * @param itemName the name of the item for which we make query
	 * @return the configured activelow state
	 */
	public int getActiveLow(String itemName);

	/**
	 * Query for configured pin direction.
	 * 
	 * @param itemName the name of the item for which we make query
	 * @return the configured pin direction
	 */
	public int getDirection(String itemName);

	/**
	 * Query has item configuration or not.
	 * 
	 * @param itemName the name of the item for which we make query
	 * @return <code>true</code> if the item has configuration,
	 * 		<code>false</code> otherwise
	 */
	public boolean isItemConfigured(String itemName);
}
