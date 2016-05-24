/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.megadevice;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Petr Shatsillo
 * @since 0.0.1
 */
public interface MegaDeviceBindingProvider extends BindingProvider {

	public String getIP(String itemName);

	public String getPORT(String itemName);

	public String password(String itemName);
	
	public int getPollInterval(String itemName);

	Class<? extends Item> getItemType(String itemName);

}
