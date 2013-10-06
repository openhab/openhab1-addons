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
package org.openhab.binding.wago.internal;

import org.openhab.binding.wago.wagoBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Kaltofen
 * @since 1.3.0
 */
public class wagoGenericBindingProvider extends AbstractGenericBindingProvider implements wagoBindingProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(wagoGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "wago";
	}
	
	@Override
	public wagoBindingConfig getConfig(String name) {
		return (wagoBindingConfig) bindingConfigs.get(name);
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
		
		// TODO
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		//TODO parse bindingconfig here ...
		if(bindingConfig != null) {
			wagoBindingConfig config = new wagoBindingConfig(item, bindingConfig);
			addBindingConfig(item, config);
		} else {
			logger.warn("binding configuration is empty -> aborting item configuration.");
		}
	}
	
	
	public class wagoBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		private Item item = null;
		
		String couplerName;
		int module;
		int channel;
		
		public wagoBindingConfig(Item item, String conf) {
			this.item = item;
			
			try {
				String attributes[] = conf.split(":");
				
				couplerName = attributes[0];
				module = Integer.parseInt(attributes[1]) - 1; // -1 so that the user can easily transfer the values from the EA-config.
				channel = Integer.parseInt(attributes[2]) - 1;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		protected State translateBoolean2State(boolean b) {
			Class<? extends State> c = item.getState().getClass();
			Class<? extends Item> itemClass = item.getClass();
			
			if (c == UnDefType.class && itemClass == SwitchItem.class) {
				return b ? OnOffType.ON : OnOffType.OFF;
			}
			else if (c == UnDefType.class && itemClass == ContactItem.class) {
				return b ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
			}
			else if (c == OnOffType.class && itemClass == SwitchItem.class) {
				return b ? OnOffType.ON : OnOffType.OFF;
			}
			else if (c == OpenClosedType.class && itemClass == SwitchItem.class) {
				return b ? OnOffType.ON : OnOffType.OFF;
			}
			else if (c == OnOffType.class && itemClass == ContactItem.class) {
				return b ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
			}
			else if (c == OpenClosedType.class && itemClass == ContactItem.class) {
				return b ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
			} else {
				return UnDefType.UNDEF;
			}
		}
		
		public Item getItem() {
			return item;
		}
		
		State getItemState() {
			return item.getState();
		}
	}	
}