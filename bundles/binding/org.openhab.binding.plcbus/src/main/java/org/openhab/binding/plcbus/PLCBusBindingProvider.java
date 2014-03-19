/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus;

import org.openhab.binding.plcbus.internal.PLCBusBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * Interface for PLCBusBindingProvider
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public interface PLCBusBindingProvider extends BindingProvider {

	/**
	 * Provides BindingConfig for ItemName
	 * 
	 * @param itemName Name of items
	 * @return BindingConfig
	 */
	PLCBusBindingConfig getConfigFor(String itemName);

}
