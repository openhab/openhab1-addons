/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fatekplc.internal;

import org.openhab.binding.fatekplc.FatekPLCBindingProvider;
import org.openhab.binding.fatekplc.items.FatekPLCItem;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Implementation of binding provider for Fatek PLC.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public class FatekPLCGenericBindingProvider extends
		AbstractGenericBindingProvider implements FatekPLCBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "fatekplc";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {

		FatekPLCItem.validateItemType(item);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {

		FatekPLCItem config = FatekPLCItem.parseBindingConfiguration(item, bindingConfig);
		addBindingConfig(item, config);

		super.processBindingConfiguration(context, item, bindingConfig);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FatekPLCItem geFatektItem(String name) {
		return (FatekPLCItem) bindingConfigs.get(name);
	}
}
