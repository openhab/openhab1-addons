/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * @author Tom De Vlaminck
 * @serial 1.0
 * @since 1.5.0
 * 
 */
package org.openhab.binding.bticino.internal;

import org.openhab.binding.bticino.internal.BticinoGenericBindingProvider.BticinoBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can map openHAB items to
 * Bticino binding types.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Tom De Vlaminck
 * @serial 1.0
 * @since 1.5.0
 */
public interface BticinoBindingProvider extends BindingProvider
{
	/**
	 * Returns Bticino item configuration
	 * 
	 * @param itemName
	 *            item name
	 * @return Bticino item configuration
	 */
	BticinoBindingConfig getConfig(String itemName);
}
