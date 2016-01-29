/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.edimax.internal;

import org.openhab.binding.edimax.EdimaxBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Binding Provider implementation.
 * 
 * @author Heinz
 *
 */
public class EdimaxGenericBindingProvider extends
		AbstractGenericBindingProvider implements EdimaxBindingProvider {

	@Override
	public String getBindingType() {
		return "edimax";
	}

	public EdimaxBindingConfiguration getConfig(String itemName) {
		return (EdimaxBindingConfiguration) bindingConfigs.get(itemName);
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		new EdimaxBindingConfiguration().parse(item, bindingConfig);
	}

	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		EdimaxBindingConfiguration config = new EdimaxBindingConfiguration();
		config.parse(item, bindingConfig);
		addBindingConfig(item, config);
	}

}
