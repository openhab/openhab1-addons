/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.networkhealth;

import org.openhab.core.binding.BindingProvider;



/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and NetworkHealth items.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public interface NetworkHealthBindingProvider extends BindingProvider {

	/**
	 * @return the corresponding hostname of the given <code>itemName</code>
	 */
	public String getHostname(String itemName);
	
	/**
	 * @return the corresponding port of the given <code>itemName</code>
	 */
	public int getPort(String itemName);
	
	/**
	 * @return the corresponding timeout of the given <code>itemName</code>
	 */
	public int getTimeout(String itemName);
	
}
