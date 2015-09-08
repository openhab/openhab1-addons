/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome;

import org.openhab.binding.rwesmarthome.internal.RWESmarthomeGenericBindingProvider.RWESmarthomeBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author ollie-dev
 * @since 1.8.0
 */
public interface RWESmarthomeBindingProvider extends BindingProvider {

	/**
	 * Returns the item object by itemName.
	 */
	public Item getItem(String itemName);
	
	/**
	 * Returns the item object by logicaldeviceid.
	 */
	public Item getItemById(String id);
	
	/**
	 * Returns the item object by logicaldeviceid and param.
	 */
	public Item getItemByIdAndParam(String id, String param);

	/**
	 * Returns the bindingConfig by itemName.
	 */
	public RWESmarthomeBindingConfig getBindingFor(String itemName);
}
