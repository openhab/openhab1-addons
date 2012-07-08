/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
