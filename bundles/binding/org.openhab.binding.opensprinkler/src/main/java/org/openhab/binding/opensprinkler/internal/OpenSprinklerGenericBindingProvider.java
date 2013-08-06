/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.opensprinkler.internal;

import org.openhab.binding.opensprinkler.OpenSprinklerBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration. A valid
 * items binding configuration file will look like the following:
 * 
 * <pre>
 * Switch Sprinkler_Station_0      "Station 0" { openSprinkler="0" }
 * Switch Sprinkler_Station_1      "Station 1" { openSprinkler="1" }
 * </pre>
 * 
 * <p>You can see in this example that two stations are exposed as items. 
 * The first station exposed is the 0th port (i.e. the left-most pin on the 
 * OpenSprinkler), and the second station is the 1st port (the 
 * second-to-left-most pin on the OpenSprinkler). Note that there is no 
 * requirement to use the stations in order - you can open and close any station. 
 * 
 * @author Jonathan Giles (http://www.jonathangiles.net)
 * @since 1.3.0
 */
public class OpenSprinklerGenericBindingProvider extends AbstractGenericBindingProvider implements OpenSprinklerBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "openSprinkler";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		OpenSprinklerBindingConfig config = new OpenSprinklerBindingConfig();
		
		//parse bindingconfig here ...
		config.stationNumber = Integer.parseInt(bindingConfig);
		
		addBindingConfig(item, config);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public int getStationNumber(String itemName) {
		OpenSprinklerBindingConfig config = (OpenSprinklerBindingConfig) bindingConfigs.get(itemName);
		return config.stationNumber;
	}
	

	/**
	 * Custom configuration for OpenSprinkler
	 */
	class OpenSprinklerBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		private int stationNumber = -1;
	}
	
}
