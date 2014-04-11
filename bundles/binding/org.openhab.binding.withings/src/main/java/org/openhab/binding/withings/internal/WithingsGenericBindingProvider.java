/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.withings.internal;

import org.openhab.binding.withings.WithingsBindingConfig;
import org.openhab.binding.withings.WithingsBindingProvider;
import org.openhab.binding.withings.WithingsBindingConfig.MeasureType;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Dennis Nobel
 * @since 0.1.0
 */
public class WithingsGenericBindingProvider extends AbstractGenericBindingProvider implements WithingsBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "withings";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		MeasureType measureType = MeasureType.parse(bindingConfig);
		
		WithingsBindingConfig config = new WithingsBindingConfig(measureType);
		
		addBindingConfig(item, config);		
	}

	@Override
	public WithingsBindingConfig getItemConfig(String itemName) {
		return (WithingsBindingConfig) bindingConfigs.get(itemName);
	}
	
	
}
