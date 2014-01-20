/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pulseaudio;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Pulseaudio items (sinks).
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Tobias Bräutigam
 * @since 1.2.0
 */
public interface PulseaudioBindingProvider extends BindingProvider {

	/**
	 * @return the corresponding pulseaudio item name to the given <code>itemName</code>
	 */
	public String getItemName(String itemName);
	
	/**
	 * @return the corresponding openHAB item type to the given <code>itemName</code>
	 */
	public Class<? extends Item> getItemType(String itemName);
	
	/**
	 * @return the corresponding pulseaudio server id name to the given <code>itemName</code>
	 */
	public String getServerId(String itemName);
	
	/**
	 * @return the corresponding additional command to the given <code>itemName</code>
	 */
	public String getCommand(String itemName);
	
}
