/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.items;

/**
 * This exception is thrown by the {@link ItemRegistry} if an item could
 * not be found.
 * 
 * @author Kai Kreuzer
 *
 */
public class ItemNotFoundException extends ItemLookupException {

	public ItemNotFoundException(String name) {
		super("Item '" + name + "' could not be found in the item registry");
	}

	private static final long serialVersionUID = -3720784568250902711L;

}
