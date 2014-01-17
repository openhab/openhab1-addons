/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal;

import org.openhab.binding.plcbus.PLCBusBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Implementation of a PLCBusBindingProvider
 * 
 * <p>
 * The syntax of the binding configuration strings accepted is the following:
 * <p>
 * <p>
 * <code>
 * 	plcbus="&lt;userCode&gt; &lt;unit&gt;"
 * </code>
 * </p>
 * 
 * <p>
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li><code>plcbus="B2 A1"</code></li>
 * </ul>
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class PLCBusGenericBindingProvider extends AbstractGenericBindingProvider implements PLCBusBindingProvider {

	@Override
	public String getBindingType() {
		return "plcbus";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		// all types of items are valid ...
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		PLCBusBindingConfig config = new PLCBusBindingConfig(bindingConfig);
		addBindingConfig(item, config);
	}
	
	@Override
	public PLCBusBindingConfig getConfigFor(String itemName) {
		return (PLCBusBindingConfig) bindingConfigs.get(itemName);
	}

}
