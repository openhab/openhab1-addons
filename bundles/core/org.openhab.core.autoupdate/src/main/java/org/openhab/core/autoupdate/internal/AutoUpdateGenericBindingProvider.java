/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.autoupdate.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides AutoUpdate binding information from it. If no binding configuration
 * is provided <code>autoupdate</code> is evaluated to true. This means every 
 * received <code>Command</code> will update its corresponding <code>State</code>
 * by default.</p>
 * <p>This class registers as a {@link AutoUpdateBindingProvider} service as
 * well.</p>
 * 
 * <p>A valid binding configuration strings looks like this:
 * <ul>
 * 	<li><code>{ autoupdate="false" }</li>
 * </ul>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * 
 * @since 0.9.1
 */
public class AutoUpdateGenericBindingProvider extends AbstractGenericBindingProvider implements AutoUpdateBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "autoupdate";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// as AutoUpdate is a default binding, each binding type is valid
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		AutoUpdateBindingConfig config = new AutoUpdateBindingConfig();
		parseBindingConfig(bindingConfig, config);
		addBindingConfig(item, config);		
	}
	
	protected void parseBindingConfig(String bindingConfig, AutoUpdateBindingConfig config) throws BindingConfigParseException {
		if (StringUtils.isNotBlank(bindingConfig)) {
			try {
				config.autoupdate = Boolean.valueOf(bindingConfig.trim());
			} catch (IllegalArgumentException iae) {
				throw new BindingConfigParseException("The given parameter '" + bindingConfig.trim() + "' has to be set to either 'true' or 'false'.");
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean autoUpdate(String itemName) {
		AutoUpdateBindingConfig config = (AutoUpdateBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.autoupdate : null;
	}
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the AutoUpdate
	 * binding provider.
	 */
	static class AutoUpdateBindingConfig implements BindingConfig {
		boolean autoupdate;
	}

}
