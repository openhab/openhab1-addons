/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.configadmin.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.configadmin.ConfigAdminBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides ConfigAdmin binding information from it. It registers as a 
 * {@link ConfigAdminBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ configAdmin="dropbox:syncmode" }</code> - takes a String to 
 * 		reconfigure the sync mode of the optional dropbox binding</li>
 * 	<li><code>{ configAdmin="gcal:refresh" }</code> - takes a number to 
 * 		reconfigure the refresh interval of the GCal binding</li>
 * </ul>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class ConfigAdminGenericBindingProvider extends AbstractGenericBindingProvider implements ConfigAdminBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "configadmin";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// we will see what a sensible default will be ...
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (StringUtils.isNotBlank(bindingConfig)) {
			String[] elements = bindingConfig.split(":");
			if (elements.length == 2) {
				ConfigAdminBindingConfig config = 
					new ConfigAdminBindingConfig(item, normalizePid(elements[0]), elements[1]);
				addBindingConfig(item, config);		
			} else {
				throw new BindingConfigParseException("BindingConfig string must contain two elements separated by ':'");
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigAdminBindingConfig getBindingConfig(String itemName) {
		ConfigAdminBindingConfig config = (ConfigAdminBindingConfig) bindingConfigs.get(itemName);
		return config;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<ConfigAdminBindingConfig> getBindingConfigByPid(String pid) {
		Collection<ConfigAdminBindingConfig> result =
			new ArrayList<ConfigAdminGenericBindingProvider.ConfigAdminBindingConfig>();
		for (BindingConfig bindingConfig : bindingConfigs.values()) {
			if (bindingConfig instanceof ConfigAdminBindingConfig) {
				ConfigAdminBindingConfig bc = (ConfigAdminBindingConfig) bindingConfig;
				if (bc.normalizedPid.equals(pid)) {
					result.add(bc);
				}
			}
		}
		return result;
	}
	
	private static String normalizePid(String pid) {
		String normalizedPid = pid;
		if (!normalizedPid.contains(".")) {
			normalizedPid = "org.openhab." + pid;
		}
		return normalizedPid;
	}


	/**
	 * Holds the configuration details of one binding.
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 * @since 1.0.0
	 */
	public static class ConfigAdminBindingConfig implements BindingConfig {
		
		Item item;
		String normalizedPid;
		String configParameter;
		
		public ConfigAdminBindingConfig(Item item, String normalizedPid, String configParameter) {
			this.item = item;
			this.normalizedPid = normalizedPid;
			this.configParameter = configParameter;
		}
	}
	

}
