/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ucprelayboard;

/**
 * @author Robert Michalak
 * @since 1.8.0
 */
import org.openhab.binding.ucprelayboard.internal.UCPRelayConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

public interface UCPRelayBoardBindingProvider extends BindingProvider {

	/**
	 * Returns the binding configuration for an item
	 * @param itemName
	 * @return
	 */
	public UCPRelayConfig getRelayConfigForItem(String itemName);
	
	public Item getItemForRelayConfig(UCPRelayConfig config);
	
}