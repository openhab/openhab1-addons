/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.akm868;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Michael Heckmann
 * @since 1.8.0
 */
public interface AKM868BindingProvider extends BindingProvider {

	/**
	 * Returns the item object by itemName.
	 */
	public Item getItem(String itemName);

	/**
	 * Returns the id of the object by itemName.
	 */
	public String getId(String itemName);

	/**
	 * Returns the channel of the object by itemName.
	 */
	public String getChannel(String itemName);

}
