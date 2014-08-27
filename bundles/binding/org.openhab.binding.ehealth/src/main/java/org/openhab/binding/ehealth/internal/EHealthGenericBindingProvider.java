/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ehealth.internal;

import org.openhab.binding.ehealth.EHealthBindingProvider;
import org.openhab.binding.ehealth.protocol.EHealthSensorPropertyName;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides Libelium eHealth binding information from it. It registers as a 
 * {@link EHealthBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ ehealth="bpm" }</code></li>
 *  <li><code>{ ehealth="oxygenSaturation" } </code></li>
 *  <li><code>{ ehealth="skinResistance" }</code><li>
 *  <li><code>{ ehealth="skinConductance" }</code><li>
 * </ul>
 * 
 * @author Thomas Eichstaedt-Engelen
 * @since 1.6.0
 */
public class EHealthGenericBindingProvider extends AbstractGenericBindingProvider implements EHealthBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "ehealth";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException(
				"item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() +
				"', only NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		EHealthBindingConfig config = new EHealthBindingConfig();

		EHealthSensorPropertyName propertyName;
		try {
			propertyName = EHealthSensorPropertyName.getPropertyName(bindingConfig.trim());
		} catch (Exception e) {
			throw new BindingConfigParseException("Invalid sensor property name '" + bindingConfig + "' > please fix your *.items accordingly!");
		}
		
		config.itemType = item.getClass();
		config.sensorPropertyName = propertyName;

		addBindingConfig(item, config);
	}
	
	
	/**
	 * @{inheritDoc
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		EHealthBindingConfig config = (EHealthBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public EHealthSensorPropertyName getSensorPropertyName(String itemName) {
		EHealthBindingConfig config = (EHealthBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.sensorPropertyName : null;
	}
	
	
	class EHealthBindingConfig implements BindingConfig {
		public Class<? extends Item> itemType;
		public EHealthSensorPropertyName sensorPropertyName;
	}

	
}
