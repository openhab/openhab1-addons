/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.koubachi.internal;

import org.openhab.binding.koubachi.KoubachiBindingProvider;
import org.openhab.binding.koubachi.internal.api.KoubachiResourceType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides Koubachi binding information from it. It registers as a 
 * {@link KoubachiBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ koubachi="device:00066680190e:virtualBatteryLevel" }</code></li>
 *  <li><code>{ koubachi="device:00066680190e:nextTransmission" } </code></li>
 *  <li><code>{ koubachi="plant:129892:vdmMistLevel" }</code><li>
 *  <li><code>{ koubachi="plant:129892:vdmWaterInstruction" }</code><li>
 * </ul>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.2.0
 */
public class KoubachiGenericBindingProvider extends AbstractGenericBindingProvider implements KoubachiBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "koubachi";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof StringItem || item instanceof DateTimeItem
				|| item instanceof SwitchItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Number-, String-, DateTime- and SwitchItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
	
		String[] configParts = bindingConfig.split(":");
		if (configParts.length < 3 || configParts.length > 4) {
			throw new BindingConfigParseException("A Koubachi binding configuration for a property must consist of three or four parts - please verify your *.items file");
		} else if (configParts[2].equals("action") && configParts.length != 4) {
			throw new BindingConfigParseException("A Koubachi binding configuration for an action  must consist of four parts - please verify your *.items file");
		}

		KoubachiBindingConfig config = new KoubachiBindingConfig();
			config.resourceType = KoubachiResourceType.valueOf(configParts[0].toUpperCase());
			config.resourceId = configParts[1];
			if (configParts.length == 3) {
				// this is a binding for a property
				config.propertyName = configParts[2];
			} else {
				// this is a binding for a care action
				config.actionType = configParts[3];
			}
		
		addBindingConfig(item, config);		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public KoubachiResourceType getResourceType(String itemName) {
		KoubachiBindingConfig config = (KoubachiBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.resourceType: null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getResourceId(String itemName) {
		KoubachiBindingConfig config = (KoubachiBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.resourceId: null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPropertyName(String itemName) {
		KoubachiBindingConfig config = (KoubachiBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.propertyName: null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCareAction(String itemName) {
		KoubachiBindingConfig config = (KoubachiBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.actionType != null: false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getActionType(String itemName) {
		KoubachiBindingConfig config = (KoubachiBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.actionType: null;
	}

	/**
	 * @author Thomas.Eichstaedt-Engelen
	 * @since 1.2.0
	 */
	class KoubachiBindingConfig implements BindingConfig {
		
		KoubachiResourceType resourceType;
		String resourceId;
		String propertyName;
		String actionType;
		
		@Override
		public String toString() {
			return "KoubachiBindingConfig [resourceType=" + resourceType
					+ ", resourceId=" + resourceId + ", propertyName="
					+ propertyName + ", actionType=" + actionType + "]";
		}
		
	}
	
	
}
