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
package org.openhab.core.binding.autoupdate.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.binding.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides AutoUpdate binding information from it. If no binding configuration
 * is provided <code>autoupdate</code> is evaluated to true. This means every 
 * received <code>Command</code> will update it's corresponding <code>State</code>
 * by default. If <code>autoupdate</code> has been configured to <code>true</code>.
 * the administrator is responsible for updating the <code>State</code> on his
 * own. This class registers as a {@link AutoUpdateBindingProvider} service as
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
	
	protected void parseBindingConfig(String bindingConfig, AutoUpdateBindingConfig config) {
		if (StringUtils.isNotBlank(bindingConfig)) {
			config.autoupdate = Boolean.valueOf(bindingConfig.trim());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean autoUpdate(String itemName) {
		AutoUpdateBindingConfig config = (AutoUpdateBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.autoupdate : true;
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
