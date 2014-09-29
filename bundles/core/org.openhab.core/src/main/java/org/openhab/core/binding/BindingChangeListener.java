/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.binding;


/**
 * This interface must be implemented by classes which want to be notified by a
 * {@link BindingProvider} about changes in the binding configuration.
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 */
public interface BindingChangeListener {
	
	/**
	 * Called, if a single binding has changed. The given item could have been
	 * added or removed.
	 * 
	 * @param provider the binding provider where the binding has changed
	 * @param itemName the item name for which the binding has changed
	 */
	public void bindingChanged(BindingProvider provider, String itemName);
	
	/**
	 * Called, if all bindings (might) have changed.
	 * 
	 * @param provider the binding provider whose bindings have changed
	 */
	public void allBindingsChanged(BindingProvider provider);
	
}
