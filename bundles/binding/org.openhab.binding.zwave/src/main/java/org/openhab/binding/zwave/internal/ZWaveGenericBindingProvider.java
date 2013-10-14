/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal;

import org.openhab.binding.zwave.ZWaveBindingAction;
import org.openhab.binding.zwave.ZWaveBindingConfig;
import org.openhab.binding.zwave.ZWaveBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Victor Belov
 * @author Brian Crosby
 * @since 1.3.0
 */
public class ZWaveGenericBindingProvider extends AbstractGenericBindingProvider implements ZWaveBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "zwave";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// All types are valid
		logger.trace("validateItemType({}, {})", item.getName(), bindingConfig);
	}

	/**
	 * Processes Z-Wave binding configuration string.
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		logger.trace("processBindingConfiguration({}, {})", item.getName(), bindingConfig);
		super.processBindingConfiguration(context, item, bindingConfig);
		String[] segments = bindingConfig.split(":");
		
		if (segments.length < 1 || segments.length > 3)
			throw new BindingConfigParseException("invalid number of segments in binding: " + bindingConfig);

		int nodeId;
		try{
			nodeId = Integer.parseInt(segments[0]);
		} catch (Exception e){
			throw new BindingConfigParseException(segments[1] + " is not a valid node id.");
		}

		int endpoint = 1;
		if(segments.length > 1){
			try{
				endpoint = Integer.parseInt(segments[1]);
			} catch (Exception e){
				throw new BindingConfigParseException(segments[1] + " is not a valid endpoint number.");
			}
		}

		ZWaveBindingAction action = ZWaveBindingAction.NONE; // default
		
		if(segments.length > 2) {
			action = ZWaveBindingAction.getZWaveBindingAction(segments[2].toUpperCase());
			if (action == null)
				throw new BindingConfigParseException(segments[2] + " is an unknown Z-Wave binding action.");
		}
		
		ZWaveBindingConfig config = new ZWaveBindingConfig(nodeId, endpoint, action);
		addBindingConfig(item, config);
	}

	/**
	 * Returns the binding configuration for a string.
	 * @return the binding configuration.
	 */
	public ZWaveBindingConfig getZwaveBindingConfig(String itemName) {
		return (ZWaveBindingConfig) this.bindingConfigs.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean autoUpdate(String itemName) {
		return false;
	}
	
}
