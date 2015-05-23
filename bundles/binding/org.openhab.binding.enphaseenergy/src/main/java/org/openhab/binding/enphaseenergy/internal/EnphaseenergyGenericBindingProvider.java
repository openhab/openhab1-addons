/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enphaseenergy.internal;

import org.openhab.binding.enphaseenergy.EnphaseenergyBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * <p>
 * Valid bindings for the main device are:
 * <ul>
 * <li><code>{ enphaseenergy="&lt;system_id&gt;#Property" }</code></li>
 * <ul>
 * <li><code>{ enphaseenergy="12345#modules" }</code></li>
 * <li><code>{ enphaseenergy="12345#size_w" }</code></li>
 * <li><code>{ enphaseenergy="12345#current_power" }</code></li>
 * <li><code>{ enphaseenergy="12345#energy_today" }</code></li>
 * <li><code>{ enphaseenergy="12345#energy_lifetime" }</code></li>
 * <li><code>{ enphaseenergy="12345#summary_date" }</code></li>
 * <li><code>{ enphaseenergy="12345#source" }</code></li>
 * <li><code>{ enphaseenergy="12345#status" }</code></li>
 * <li><code>{ enphaseenergy="12345#operational_at" }</code></li>
 * <li><code>{ enphaseenergy="12345#last_report_at" }</code></li>
 * </ul>
 * </li> </ul>
 * 
 * @author Markus Fritze
 * @since 1.7.0
 */
public class EnphaseenergyGenericBindingProvider extends
		AbstractGenericBindingProvider implements EnphaseenergyBindingProvider {

	private static Logger logger = LoggerFactory.getLogger(EnphaseenergyGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "enphaseenergy";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(final Item item, final String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof DateTimeItem || item instanceof StringItem)) {
			throw new BindingConfigParseException(
				"item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() + 
				"', only NumberItems, DateTimeItems and StringItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnphaseenergyItemType getItemType(String itemName){ 
		final EnphaseenergyBindingConfig config = 
			(EnphaseenergyBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.measureType : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getSystemId(final String itemName) {
		final EnphaseenergyBindingConfig config = 
			(EnphaseenergyBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.systemId : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(final String context,
			final Item item, final String bindingConfig) throws BindingConfigParseException {
		logger.debug("Processing binding configuration: '{}'", bindingConfig);

		super.processBindingConfiguration(context, item, bindingConfig);

		final EnphaseenergyBindingConfig config = new EnphaseenergyBindingConfig();

		final String[] configParts = bindingConfig.split("#");
		config.systemId = Integer.parseInt(configParts[0]);;
		config.measureType = EnphaseenergyItemType.fromString(configParts[1]);

		logger.debug("Adding binding: {}", config);

		addBindingConfig(item, config);
	}
	
		
	private static class EnphaseenergyBindingConfig implements BindingConfig {

		Integer systemId;
		EnphaseenergyItemType measureType;

		@Override
		public String toString() {
			return "EnphaseenergyBindingConfig [systemId=" + this.systemId
					+ ", measure=" + this.measureType.getMeasure() + "]";
		}
	}
	
}
