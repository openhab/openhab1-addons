/**ALWA
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zibase.internal;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.zapi.ZbResponse;
import fr.zapi.Zibase;

/**
 * Zibase item's config class
 * @author Julien Tiphaine
 *
 */
abstract class zibaseBindingConfig implements BindingConfig {

	/**
	 * generic logger
	 */
	static final Logger logger = LoggerFactory.getLogger(zibaseGenericBindingProvider.class);
	
	static protected final int POS_TYPE 	= 0;
	static protected final int POS_ID 		= 1;
	static protected final int POS_VALUES 	= 2;
	
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
		prefixClasses.put("VAR", org.openhab.binding.zibase.internal.zibaseBindingConfigVariable.class);
		prefixClasses.put("CAL", org.openhab.binding.zibase.internal.zibaseBindingConfigCalendar.class);
		prefixClasses.put("SCE", org.openhab.binding.zibase.internal.zibaseBindingConfigScenario.class);
		prefixClasses.put("SCR", org.openhab.binding.zibase.internal.zibaseBindingConfigScript.class);
		prefixClasses.put("CMD", org.openhab.binding.zibase.internal.zibaseBindingConfigCommand.class);
		prefixClasses.put("RCV", org.openhab.binding.zibase.internal.zibaseBindingConfigReceiver.class);
		prefixClasses.put("X10", org.openhab.binding.zibase.internal.zibaseBindingConfigX10Event.class);
		prefixClasses.put("ZWA", org.openhab.binding.zibase.internal.zibaseBindingConfigZwaveEvent.class);
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
	 * @param zibase
	 * @param parameters
	 */
	abstract public State getOpenhabStateFromZibaseValue(ZbResponse zbResponse);
	
	
	/**
	 * delegate config verification to the corresponding type of class
	 * @param bindingConfig
	 */
	static public boolean isConfigValid(String bindingConfig) {
		String[] configParameters = StringUtils.split(bindingConfig);
		zibaseBindingConfig itemConfig = zibaseBindingConfig.factory(configParameters);
		
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
	static public zibaseBindingConfig factory(String[] configParameters) {
		
		if (configParameters.length < 2) {
			logger.error("invalid config for item. There should be at least 2 values : type and id");
			return null;
		}
		
		Class<?> type = prefixClasses.get(configParameters[POS_TYPE]);
		if (type != null) {
			try {
				return (zibaseBindingConfig) type.getConstructor(String[].class).newInstance(configParameters); 
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
	public zibaseBindingConfig(String[] configParameters) {
		
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