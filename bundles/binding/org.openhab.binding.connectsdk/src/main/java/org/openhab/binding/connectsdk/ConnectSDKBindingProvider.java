/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.connectsdk;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public interface ConnectSDKBindingProvider extends BindingProvider {

	/**
	 * Returns the property for this binding.
	 * 
	 * @param itemName
	 *            must match one of the registered items. Otherwise an
	 *            IllegalStateException will be thrown.
	 * @return the property configured for this item in your *.item config file
	 */String getPropertyForItem(String itemName);

	/**
	 * Returns the name for this binding.
	 * 
	 * @param itemName
	 *            must match one of the registered items. Otherwise an
	 *            IllegalStateException will be thrown.
	 * @return the device name configured for this item in your *.item config file
	 */
	String getDeviceForItem(String itemName);

	/**
	 * Returns the class for this binding.
	 * 
	 * @param itemName
	 *            must match one of the registered items. Otherwise an
	 *            IllegalStateException will be thrown.
	 * @return the class configured for this item in your *.item config file
	 */
	String getClassForItem(String itemName);
	
	

}
