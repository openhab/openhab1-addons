/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.koubachi;

import org.openhab.binding.koubachi.internal.api.KoubachiResourceType;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Koubachi items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.2.0
 */
public interface KoubachiBindingProvider extends BindingProvider {
	
	/**
	 * Returns the configured Koubachi resource type of the given {@code itemName}.
	 * 
	 * @param itemName the item for which to find a resource type.
	 * @return the type of the Item identified by {@code itemName}
	 */
	KoubachiResourceType getResourceType(String itemName);

	/**
	 * Returns the configured Koubachi resource id of the given {@code itemName}.
	 * 
	 * @param itemName the item for which to find a resource id.
	 * @return the resource id of the {@link Item} identified by {@code itemName}
	 */
	String getResourceId(String itemName);

	/**
	 * Returns the configured Koubachi property name of the given {@code itemName}.
	 * 
	 * @param itemName the item for which to find a property name.
	 * @return the property name of the {@link Item} identified by {@code itemName}
	 */
	String getPropertyName(String itemName);	

}
