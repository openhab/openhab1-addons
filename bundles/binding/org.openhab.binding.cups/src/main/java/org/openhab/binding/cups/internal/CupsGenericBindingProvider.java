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
package org.openhab.binding.cups.internal;

import org.apache.commons.lang.StringUtils;
import org.cups4j.WhichJobsEnum;
import org.openhab.binding.cups.CupsBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides Cups binding information from it. It registers as a 
 * {@link CupsBindingProvider} service as well.</p>
 * 
 * <p>The syntax of the binding configuration strings accepted is the following:<p>
 * <p><code>
 * 	cups="printerName"
 * </code></p>
 * 
 * @author Tobias Bräutigam
 * @since 1.1.0
 */
public class CupsGenericBindingProvider extends AbstractGenericBindingProvider implements CupsBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(CupsGenericBindingProvider.class);
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "cups";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only NumberItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		CupsBindingConfig config = new CupsBindingConfig();
		String[] parts = bindingConfig.trim().split("#");
		config.printerName = parts[0];
		if (parts.length==2) {
			try {
				config.whichJobs = WhichJobsEnum.valueOf(WhichJobsEnum.class, parts[1].toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new BindingConfigParseException("WhichJobs-Part "+parts[1]+" is unknown (use one of "+StringUtils.join(WhichJobsEnum.values(),", ")+")");
			} catch (NullPointerException e) {
				// empty WhichJobs-Part -> use default
				config.whichJobs = WhichJobsEnum.NOT_COMPLETED;
			}
		}
									
		addBindingConfig(item, config);
	}
		
	
	/**
	 * {@inheritDoc}
	 */
	public String getPrinterName(String itemName) {
		CupsBindingConfig config = (CupsBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.printerName : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public WhichJobsEnum getWhichJobs(String itemName) {
		CupsBindingConfig config = (CupsBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.whichJobs : WhichJobsEnum.NOT_COMPLETED;
	}
	
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the Cups binding 
	 * provider.
	 * 
	 * @author Tobias Bräutigam
	 */
	static private class CupsBindingConfig implements BindingConfig {
		public String printerName;
		public WhichJobsEnum whichJobs = WhichJobsEnum.NOT_COMPLETED;
	}	
	

}
