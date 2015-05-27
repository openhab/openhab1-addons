/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sagercaster;

import org.openhab.binding.sagercaster.internal.CommandType;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public interface SagerCasterBindingProvider extends BindingProvider {

	/**
	 * @return all the binding config matching a given <code>commandType</code>
	 */
	public Iterable<String> getItemNamesBy(CommandType commandType);
	
    /**
     * @return the binding config to the given <code>itemName</code>
     */
	public SagerCasterBindingConfig getConfig(String itemName);
}
