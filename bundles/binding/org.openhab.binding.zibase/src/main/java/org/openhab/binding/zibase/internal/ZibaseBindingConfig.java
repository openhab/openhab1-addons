/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zibase.internal;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.zapi.Zibase;

/**
 * Zibase item's config class
 * 
 * Parent class for all item types. 
 * This class is also used as a factory for all item's config
 * 
 * @author Julien Tiphaine
 * @since 1.7.0
 * 
 */
abstract class ZibaseBindingConfig implements BindingConfig {

	/**
	 * generic logger
	 */
	static final Logger logger = LoggerFactory.getLogger(ZibaseGenericBindingProvider.class);
	
	/**
	 * Position of item's type in configuration array
	 */
	static protected final int POS_TYPE 	= 0;
	
	/**
	 * Position of item's RfID in configuration array
	 */
	static protected final int POS_ID 		= 1;
	
	/**
	 * Start position of item's values in configuration array
	 */
	static protected final int POS_VALUES 	= 2;
	
	/**
	 * Separator for items configuration values
	 */
	static protected final String CONFIG_SEPARATOR = ",";
	
	/**
	 * protocol for command's item, type of value to get for value to update
	 */
	final protected String[] values;
	
	/**
	 * list of possible values for prefix
	 */
	static final HashMap<String, Class<?>> prefixClasses;
	static {
		prefixClasses = new HashMap<String, Class<?>>();
		prefixClasses.put("VAR", org.openhab.binding.zibase.internal.ZibaseBindingConfigVariable.class);	// Variable
		prefixClasses.put("SCE", org.openhab.binding.zibase.internal.ZibaseBindingConfigScenario.class);	// Scenario
		prefixClasses.put("RCV", org.openhab.binding.zibase.internal.ZibaseBindingConfigReceiver.class);	// Receiver
		prefixClasses.put("EMT", org.openhab.binding.zibase.internal.ZibaseBindingConfigEmitter.class);		// Emitter
	}

	
	/**
	 * Send the appropriate command to zibase depending on zibase item type
	 * @param zibase
	 * @param command
	 * @param int set to megative value if no dim is needed
	 */
	abstract public void sendCommand(Zibase zibase, Command command, int dim);
	
	/**
	 * Tell whether given config string is valid
	 * @param zibase
	 * @param parameters
	 */
	abstract protected boolean isItemConfigValid();
	
	/**
	 * Tell wether given config string is valid
	 * @param zbResponseStr a ZbResponse instance as string
	 */
	abstract public State getOpenhabStateFromZibaseValue(Zibase zibase, String zbResponseStr);
	
	
	/**
	 * delegate config verification to the corresponding type of class
	 * @param bindingConfig
	 */
	static public boolean isConfigValid(String bindingConfig) {
		String[] configParameters = StringUtils.split(bindingConfig,ZibaseBindingConfig.CONFIG_SEPARATOR);
		ZibaseBindingConfig itemConfig = ZibaseBindingConfig.factory(configParameters);
		
		if(itemConfig == null) {
			return false;
		} else {
			return itemConfig.isItemConfigValid();
		}
	}
	
	
	/**
	 * factory to get config from given parameters
	 * @param configParameters
	 * @return
	 */
	static public ZibaseBindingConfig factory(String[] configParameters) {
		
		// any item must have at least type and RdID values
		if (configParameters.length < 2) {
			logger.error("invalid config for item. There should be at least 2 values : type and id");
			return null;
		}
		
		// fetch config Class depending on item's configuration value
		Class<?> type = prefixClasses.get(configParameters[POS_TYPE]);
		if (type != null) {
			try {
				return (ZibaseBindingConfig) type.getConstructor(String[].class).newInstance((Object) configParameters); 
			} catch(Exception ex) {
				logger.error(ex.toString());
			}
		} else {
			logger.error("item type not supported : " + configParameters[POS_TYPE]);
		}
		
		return null;
	}
	
	
	/**
	 * Constructor
	 * @param pId Rfid of the item
	 * @param pProtocol protocol for command item, type of value to get for value to update
	 */
	public ZibaseBindingConfig(String[] configParameters) {
		
		this.values = configParameters;
	}
	
	
	/**
	 * get id
	 * @return
	 */
	public String getId() {
		return values[POS_ID];
	}
	
	/**
	 * get type
	 * @return
	 */
	public String getType() {
		return values[POS_TYPE];
	}
}
