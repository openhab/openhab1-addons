/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.k8055.internal;
import org.openhab.binding.k8055.k8055BindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Anthony Green
 * @since 1.3.0
 */
public class k8055GenericBindingProvider extends AbstractGenericBindingProvider implements k8055BindingProvider {

	private static final Logger logger = 
			LoggerFactory.getLogger(k8055GenericBindingProvider.class);
	
	// Details of the available hardware
	static final int NUM_DIGITAL_INPUTS = 5;
	static final int NUM_DIGITAL_OUTPUTS = 8;
	static final int NUM_ANALOG_INPUTS = 2;
	static final int NUM_ANALOG_OUTPUTS = 2;
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "k8055";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		k8055BindingConfig config = parseBindingConfig(bindingConfig);
	
		// TODO: Validate binding types
//		if (config.ioType.equals(IOType.DIGITAL_IN) 
//				&& !(item instanceof ContactItem)) {
//			throw new BindingConfigParseException("item '" + item.getName()
//								+ "' is of type '" + item.getClass().getSimpleName()
//								+ "', only ContactItems are allowed - please check your *.items configuration");
//					
//		} else if (config.ioType.equals(IOType.DIGI) {
//		
//	
//		}
	
	}
	
	/**
	 * Parse binding string.  Examples of what is allowed are:
	 * 
	 * DIGITAL_IN:1
	 * ANALOG_IN:2
	 * DIGITAL_OUT:3 
	 * 
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		logger.debug("processing binding config: " + item.getName() + "; " + bindingConfig);
		addBindingConfig(item, parseBindingConfig(bindingConfig));		
	}
	
	protected k8055BindingConfig parseBindingConfig(String bindingConfig) throws BindingConfigParseException {
		k8055BindingConfig config = new k8055BindingConfig();
		
		String[] configParts = bindingConfig.split(":");
		if (configParts.length != 2) {
			throw new BindingConfigParseException("Unable to parse k8055 binding string: " + bindingConfig + ".  Incorrect number of colons.");
		}
		
		try {
			config.ioNumber = Integer.parseInt(configParts[1]);
			config.ioType = IOType.valueOf(configParts[0]);
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException("Unable to parse k8055 binding string: " + bindingConfig + ". Could not parse input number: " + configParts[1]);
		} catch (IllegalArgumentException e) {
			throw new BindingConfigParseException("Unable to parse k8055 binding string: " + bindingConfig + ". Invalid input type: " + configParts[0]);
		}		
		
		// Verify config is actually valid given the hardware
		if (config.ioNumber < 1) {
			throw new BindingConfigParseException("Unable to parse k8055 binding string: " + bindingConfig + ". IO channel must be greater than equal to 1 ");
		} else if ((config.ioNumber > NUM_DIGITAL_INPUTS && config.ioType.equals(IOType.DIGITAL_IN))
				|| (config.ioNumber > NUM_DIGITAL_OUTPUTS && config.ioType.equals(IOType.DIGITAL_OUT))
				|| (config.ioNumber > NUM_ANALOG_INPUTS && config.ioType.equals(IOType.ANALOG_IN))
				|| (config.ioNumber > NUM_ANALOG_OUTPUTS && config.ioType.equals(IOType.ANALOG_OUT))
				) {
			throw new BindingConfigParseException("Unable to parse k8055 binding string: " + bindingConfig + ". IO channel number was greater than the number of physicl channels ");
		}
		
		return config;
	}
	
	public class k8055BindingConfig implements BindingConfig {
		
		/**
		 * Type of input or output
		 */
		IOType ioType;
		
		/**
		 * Which Input or Output number is this?
		 */
		int ioNumber;
	}
	
	@Override
	public k8055BindingConfig getItemConfig(String itemName) {
		return (k8055BindingConfig) bindingConfigs.get(itemName);
	}
	
}
