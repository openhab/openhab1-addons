/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tacmi;

import org.openhab.binding.tacmi.internal.TACmiMeasureType;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Timo Wendt
 * @since 1.8.0
 */
public interface TACmiBindingProvider extends BindingProvider {

	/**
	 * Returns the binding configuration string.
	 * 
	 * @param itemName
	 * @return
	 */
	String getConfigurationString(String itemName);

	/**
	 * Returns the binding canNode string.
	 * 
	 * @param itemName
	 * @return
	 */

	int getCanNode(String itemName);

	/**
	 * Returns the binding portType string.
	 * 
	 * @param itemName
	 * @return
	 */
	String getPortType(String itemName);

	/**
	 * Returns the binding portNumber.
	 * 
	 * @param itemName
	 * @return
	 */
	int getPortNumber(String itemName);

	/**
	 * Returns the binding measureType string.
	 * 
	 * @param itemName
	 * @return
	 */
	TACmiMeasureType getMeasureType(String itemName);

}
