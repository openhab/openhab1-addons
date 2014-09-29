/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
