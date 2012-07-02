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

import java.io.IOException;
import java.util.Dictionary;

import org.openhab.binding.configadmin.ConfigAdminBindingProvider;
import org.openhab.binding.configadmin.internal.ConfigAdminGenericBindingProvider.ConfigAdminBindingConfig;
import org.openhab.core.events.AbstractEventSubscriberBinding;
import org.openhab.core.types.Command;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * The {@link ConfigAdminBinding} provides access to the openHAB system 
 * configuration through items. The system configuration is done through property
 * files (one key-value-pair per line) with the extension '*.cfg'. By default
 * you will find the openhab_default.cfg.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class ConfigAdminBinding extends AbstractEventSubscriberBinding<ConfigAdminBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(ConfigAdminBinding.class);
	
	private ConfigurationAdmin configAdmin;
	
	
	public void addConfigurationAdmin(ConfigurationAdmin configAdmin) {
		this.configAdmin = configAdmin;
	}
	
	public void removeConfigurationAdmin(ConfigurationAdmin configAdmin) {
		this.configAdmin = null;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		if (configAdmin != null) {
			for (ConfigAdminBindingProvider provider : this.providers) {
				ConfigAdminBindingConfig bindingConfig = provider.getBindingConfig(itemName);
				if (bindingConfig != null) {
					try {
						String normalizedPid = bindingConfig.pid;
						if (!normalizedPid.contains(".")) {
							normalizedPid = "org.openhab." + bindingConfig.pid;
						}
						Configuration config = configAdmin.getConfiguration(normalizedPid);
						
						if (config != null) {
							Dictionary props = config.getProperties();
							props.put(bindingConfig.configParameter, command.toString());
							config.update(props);
							logger.debug("successfully updated configuration (pid={}, value={})", normalizedPid, command.toString());
						} else {
							logger.info("There is no configuration found for pid '{}'", normalizedPid);
						}
					} catch (IOException ioe) {
						logger.warn("Fetching configuration for pid '" + bindingConfig.pid + "' failed", ioe);
					}
				}
			}
		}
	}
	
	
}
