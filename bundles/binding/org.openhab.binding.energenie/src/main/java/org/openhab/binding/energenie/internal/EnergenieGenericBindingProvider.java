/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.energenie.internal;
import org.openhab.binding.energenie.EnergenieBindingProvider; 
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * This class can parse information from the generic binding format. It
 * registers as a {@link EnergenieBindingProvider} service as well.
 * </p>
 * 
 * <p>
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li><code>{energenie="pms1;1"}</code> - Controls socket 1 on the first PMS-LAN.
 * </li>
 * <li>
 * <code>{energenie="pms2;3"} - Controls socket 3 on the second PMS-LAN.</code>
 * </ul>
 * 
 * @author Hans-Jörg Merk
 * @since 1.6.0
 */
public class EnergenieGenericBindingProvider extends AbstractGenericBindingProvider implements EnergenieBindingProvider {

	static final Logger logger = LoggerFactory
			.getLogger(EnergenieGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "energenie";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only SwitchItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		try {
			if (bindingConfig != null) {
				String[] configParts = bindingConfig.split(";");
				if (configParts.length < 2 || configParts.length >2) {
					throw new BindingConfigParseException ("energenie binding configuration must have two parts");
				}
				BindingConfig energenieBindingConfig = (BindingConfig) new EnergenieBindingConfig(configParts[0], configParts[1]);
				addBindingConfig(item,energenieBindingConfig);
		
	} else {
		logger.warn("bindingConfig is NULL (item=" + item
				+ ") -> processing bindingConfig aborted!");
		}
	} catch (ArrayIndexOutOfBoundsException e) {
		logger.warn("bindingConfig is invalid (item=" + item
				+ ") -> processing bindingConfig aborted!");
		}
	}
	@Override
	public EnergenieBindingConfig getItemConfig(String itemName) {
		return (EnergenieBindingConfig) bindingConfigs.get(itemName);
	}
	

	

	
}
