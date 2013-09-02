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
