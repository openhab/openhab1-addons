/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.configadmin.internal;

import java.io.IOException;
import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.configadmin.ConfigAdminBindingProvider;
import org.openhab.binding.configadmin.internal.ConfigAdminGenericBindingProvider.ConfigAdminBindingConfig;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * <p>The {@link ConfigAdminBinding} provides access to the openHAB system 
 * configuration through items. The system configuration is done through property
 * files (one key-value-pair per line) with the extension '*.cfg'.
 * <p>This Binding is also registered as {@link ConfigurationListener} at the 
 * {@link ConfigurationAdmin} so all changes to configured items are posted to
 * the openHAB event bus as well. 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class ConfigAdminBinding extends AbstractBinding<ConfigAdminBindingProvider> implements ConfigurationListener {

	private static final Logger logger = LoggerFactory.getLogger(ConfigAdminBinding.class);
	
	private ConfigurationAdmin configAdmin;
	
	private EventBusInitializer initializer = null;
	
	
	public void addConfigurationAdmin(ConfigurationAdmin configAdmin) {
		this.configAdmin = configAdmin;
	}
	
	public void removeConfigurationAdmin(ConfigurationAdmin configAdmin) {
		this.configAdmin = null;
	}
	
	public void activate() {
		initializer = new EventBusInitializer();
		initializer.start();
	}
	
	public void deactivate() {
		initializer.setInterrupted(true);
	}
		

	/**
	 * Returns a {@link State} which is inherited from the {@link Item}s
	 * accepted DataTypes. The call is delegated to the  {@link TypeParser}. If
	 * <code>item</code> is <code>null</code> the {@link StringType} is used.
	 *  
	 * @param item
	 * @param stateAsString
	 * 
	 * @return a {@link State} which type is inherited by the {@link TypeParser}
	 * or a {@link StringType} if <code>item</code> is <code>null</code> 
	 */
	private State createState(Item item, String stateAsString) {
		if (item != null) {
			return TypeParser.parseState(item.getAcceptedDataTypes(), stateAsString);
		}
		else {
			return StringType.valueOf(stateAsString);
		}
	}
	
	/**
	 * <p>Returns the {@link Configuration} with the given pid from the 
	 * {@link ConfigurationAdmin}-Service or null if <code>bindingConfig</code>
	 * is null.</p>
	 * <p><b>Note:</b>If there are Configuration items configured for the given
	 * pid an empty {@link Configuration} is returned.
	 *  
	 * @param bindingConfig
	 * @return a Configuration (which could be empty if there are no entries for
	 * the given pid) or <code>null</code> if the given <code>bindingConfig</code>
	 * is null.
	 */
	private Configuration getConfiguration(ConfigAdminBindingConfig bindingConfig) {
		Configuration result = null;
		if (bindingConfig != null) {
			try {
				result = configAdmin.getConfiguration(bindingConfig.normalizedPid);
			} catch (IOException ioe) {
				logger.warn("Fetching configuration for pid '" + bindingConfig.normalizedPid + "' failed", ioe);
			}
		}
		return result;
	}
	
	/**
	 * Gets the given configParameter from the given <code>config</code> transforms
	 * the value to a {@link State} and posts this State to openHAB event bus.
	 * 
	 * @param config the {@link Configuration} which contains the data to post
	 * @param bindingConfig contains the name of the configParameter which is
	 * the key for the data to post an update for.
	 */
	private void postUpdate(Configuration config, ConfigAdminBindingConfig bindingConfig) {
		if (config != null) {
			String stateAsString = (String)
				config.getProperties().get(bindingConfig.configParameter);
			if (StringUtils.isNotBlank(stateAsString)) {
				State state = createState(bindingConfig.item, stateAsString);
				eventPublisher.postUpdate(bindingConfig.item.getName(), state);
			} else {
				logger.debug("config parameter '{}:{}' has value 'null'. It won't be posted to the event bus hence.", bindingConfig.normalizedPid, bindingConfig.configParameter);
			}
		}
	}
	
	
	/**
	 * @{inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		if (configAdmin != null) {
			for (ConfigAdminBindingProvider provider : this.providers) {
				ConfigAdminBindingConfig bindingConfig = provider.getBindingConfig(itemName);
				Configuration config = getConfiguration(bindingConfig);
				if (config != null) {
					Dictionary props = config.getProperties();
					props.put(bindingConfig.configParameter, command.toString());
					try {
						config.update(props);
					} catch (IOException ioe) {
						logger.error("updating Configuration '{}' with '{}' failed", bindingConfig.normalizedPid, command.toString());
					}
					logger.debug("successfully updated configuration (pid={}, value={})", bindingConfig.normalizedPid, command.toString());
				} else {
					logger.info("There is no configuration found for pid '{}'", bindingConfig.normalizedPid);
				}
			}
		}
	}

	/**
	 * @{inheritDoc}
	 * 
	 * Whenever a {@link Configuration} is updated all items for the given
	 * <code>pid</code> are queried and updated. Since the {@link ConfigurationEvent}
	 * contains no information which key changed we have to post updates for
	 * all configured items.
	 */
	@Override
	public void configurationEvent(ConfigurationEvent event) {
		// we do only care for updates of existing configs!
		if (ConfigurationEvent.CM_UPDATED == event.getType()) {
			try {
				Configuration config = configAdmin.getConfiguration(event.getPid());
				for (ConfigAdminBindingProvider provider : this.providers) {
					for (ConfigAdminBindingConfig bindingConfig : provider.getBindingConfigByPid(event.getPid())) {
						postUpdate(config, bindingConfig);
					}
				}				
			} catch (IOException ioe) {
				logger.warn("Fetching configuration for pid '" + event.getPid() + "' failed", ioe);
			}
		}
	}

	
	/**
	 * Initializes all configured items asynchronously.
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 * @since 1.0.0
	 */
	private class EventBusInitializer extends Thread {
		
		private boolean interrupted = false;

		public EventBusInitializer() {
			setName("ConfigurationAdmin EventBus Initializer");
		}
		
		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
		}
		
		@Override
		public void run() {
			for (ConfigAdminBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					if (interrupted) {
						break;
					}
					ConfigAdminBindingConfig bindingConfig = provider.getBindingConfig(itemName);
					Configuration config = getConfiguration(bindingConfig);
					postUpdate(config, bindingConfig);
				}
			}
		}
		
	}
	
	
}
