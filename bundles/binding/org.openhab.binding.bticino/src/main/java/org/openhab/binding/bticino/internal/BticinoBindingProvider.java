/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * @since 1.7.0
 */
public interface BticinoBindingProvider extends BindingProvider {
	
	/**
	 * Returns Bticino item configuration
	 * 
	 * @param itemName
	 *            item name
	 * @return Bticino item configuration
	 */
	BticinoBindingConfig getConfig(String itemName);
}
