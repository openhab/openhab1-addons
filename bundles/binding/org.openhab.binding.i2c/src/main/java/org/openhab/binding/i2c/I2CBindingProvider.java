/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.i2c;

import org.openhab.core.binding.BindingProvider;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;

/**
 * @author Diego A. Fliess
 * @since 1.8.0
 */
public interface I2CBindingProvider extends BindingProvider {

	/**
	 * @return the busAddress
	 */
	public int getBusAddress(String itemName);

	/**
	 * @return the pin
	 */
	public Pin getPin(String itemName);

	/**
	 * @return the defaultState
	 */
	public PinState getDefaultState(String itemName);

	/**
	 * @return the direction
	 */
	public PinMode getPinMode(String itemName);
}
