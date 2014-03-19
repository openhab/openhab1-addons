/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openenergymonitor;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Open Energy Monitor items.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public interface OpenEnergyMonitorBindingProvider extends BindingProvider {

	/**
	 * Returns the variable type to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a variable type.
	 * 
	 * @return the corresponding variable type to the given
	 *         <code>itemName</code> .
	 */
	public String getVariable(String itemName);

	/**
	 * Returns the transformation type to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a transformation type
	 * 
	 * @return the matching transformation type or <code>null</code> if no
	 *         matching transformation rule could be found.
	 */
	String getTransformationType(String itemName);

	/**
	 * Returns the transformation function to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a transformation function
	 * 
	 * @return the matching transformation function or <code>null</code> if no
	 *         matching transformation function could be found.
	 */
	String getTransformationFunction(String itemName);

}
