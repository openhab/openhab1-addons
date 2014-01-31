/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.configadmin;

import java.util.Collection;

import org.openhab.binding.configadmin.internal.ConfigAdminGenericBindingProvider.ConfigAdminBindingConfig;
import org.openhab.core.binding.BindingProvider;


/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and ConfigAdmin items.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public interface ConfigAdminBindingProvider extends BindingProvider {

	/**
	 * Returns the binding {@link ConfigAdminBindingConfig} corresponding to
	 * the given <code>itemName</code>
	 * 
	 * @param itemName
	 * @return the {@link ConfigAdminBindingConfig} for the given
	 * <code>itemName</code> or <code>null</code> if there is none.
	 */
	ConfigAdminBindingConfig getBindingConfig(String itemName);

	/**
	 * Returns all {@link ConfigAdminBindingConfig}s for the given <code>pid</code>
	 * or an empty Collection if there is none.
	 *   
	 * @param pid
	 * @return a collection of all {@link ConfigAdminBindingConfig}s for the 
	 * given <code>pid</code>. Can be empty if there is no config found.
	 */
	Collection<ConfigAdminBindingConfig> getBindingConfigByPid(String pid);
	
}
