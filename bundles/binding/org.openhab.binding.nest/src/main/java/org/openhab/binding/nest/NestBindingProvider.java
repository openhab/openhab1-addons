/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information between openHAB items and Nest items.
 * 
 * @author John Cocula
 * @since 1.7.0
 */
public interface NestBindingProvider extends BindingProvider {

	/**
	 * Queries the Nest property of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the Nest property of the Item identified by {@code itemName} if it has a Nest binding, <code>null</code>
	 *         otherwise
	 */
	String getProperty(String itemName);

	/**
	 * Queries whether this item can be read from the Nest API, for the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return <code>true</code> if this property can be read from the Nest API.
	 */
	boolean isInBound(String itemName);

	/**
	 * Queries whether this item can be written to the Nest API, for the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return <code>true</code> if this property can be written to the Nest API.
	 */
	boolean isOutBound(String itemName);
}
