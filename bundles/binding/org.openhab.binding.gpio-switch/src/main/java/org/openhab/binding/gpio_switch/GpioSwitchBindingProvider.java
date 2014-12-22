/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gpio_switch;

import org.openhab.core.binding.BindingProvider;
import org.openhab.io.gpio_raspberry.item.GpioIOItemConfig;

/**
 * @author Robert Delbrück
 * @since 1.6.0
 */
public interface GpioSwitchBindingProvider extends BindingProvider {

	boolean isItemConfigured(String itemName);
	
	GpioIOItemConfig getItemConfig(String itemName);

}
