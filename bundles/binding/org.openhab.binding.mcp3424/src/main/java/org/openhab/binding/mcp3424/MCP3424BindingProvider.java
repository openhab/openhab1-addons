/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mcp3424;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

import com.pi4j.io.gpio.Pin;

/**
 * @author Alexander Falkenstern
 * @since 1.8.3
 */
public interface MCP3424BindingProvider extends BindingProvider {

	/**
	 * @return the busAddress
	 */
	public int getBusAddress(String itemName);

	/**
	 * @return the pin
	 */
	public Pin getPin(String itemName);

	/**
	 * @return the resolution
	 */
	public int getGain(String itemName);

    /**
     * @return the resolution
     */
    public int getResolution(String itemName);

    /**
     * @return configured item
     */
    public Item getItem(String itemName);
}
