/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.items;

import java.util.Collection;

/**
 * An item provider provides instances of {@link GenericItem}. These
 * items can be constructed from some static configuration files or
 * they can be derived from some dynamic logic.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public interface ItemProvider {
	
	/**
	 * Provides an array of items.
	 * 
	 * @return a collection of items
	 */
	Collection<Item> getItems();
	
	public void addItemChangeListener(ItemsChangeListener listener);
	
	public void removeItemChangeListener(ItemsChangeListener listener);
}
