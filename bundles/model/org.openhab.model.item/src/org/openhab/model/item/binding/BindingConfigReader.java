/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.item.binding;

import org.openhab.core.items.Item;

/**
 * This interface must be implemented by services, which can parse the generic 
 * binding configuration string used in the {@link GenericItemProvider}.
 *  
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public interface BindingConfigReader {

	/**
	 * This defines the type of binding this reader will process, e.g. "knx".
	 * 
	 * @return the type of the binding
	 */
	public String getBindingType();
	
	/**
	 * Validates if the type of <code>item</code> is valid for this binding. 
	 * 
	 * @param item the item whose type is validated
	 * @param bindingConfig the config string which could be used to refine the
	 * validation
	 * 
	 * @throws BindingConfigParseException if the type of <code>item</code> is
	 * invalid for this binding
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException;
	
	/**
	 * This method is called by the {@link GenericItemProvider} whenever it comes
	 * across a binding configuration string for an item.
	 * 
	 * @param context a string of the context from where this item comes from. Usually the file name of the config file
	 * @param item the item for which the binding is defined
	 * @param bindingConfig the configuration string that must be processed
	 * 
	 * @throws BindingConfigParseException if the configuration string is not valid
	 */
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException;
	
	/**
	 * Removes all configuration information for a given context. This is usually called if a config file is reloaded,
	 * so that the old values are removed, before the new ones are processed.
	 * 
	 * @param context the context of the configurations that should be removed
	 */
	public void removeConfigurations(String context);
}
