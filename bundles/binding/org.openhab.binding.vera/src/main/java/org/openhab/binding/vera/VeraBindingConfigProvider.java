/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera;

import org.openhab.core.binding.BindingProvider;

/**
 * The Vera-binding specific {@link BindingProvider} implementation 
 * responsible for parsing {@link VeraBindingConfig}s from <code>.items</code> files.
 * 
 * @author Matthew Bowman
 * @since 1.6.0
 */
public interface VeraBindingConfigProvider extends BindingProvider {

	/**
	 * Returns the {@link VeraBindingConfig} for the given <code>itemName</code>.
	 * 
	 * @param itemName the {@link Item} name to lookup 
	 * @return the {@link VeraBindingConfig} for the <code>itemName</code>; 
	 * 	or <code>null</code> it one could not be found
	 */
	public VeraBindingConfig getBindingConfig(String itemName);
	
}
