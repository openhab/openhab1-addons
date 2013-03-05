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
package org.openhab.binding.dmx.internal;

import org.openhab.binding.dmx.DmxBindingProvider;
import org.openhab.binding.dmx.DmxService;
import org.openhab.binding.dmx.DmxStatusUpdateListener;
import org.openhab.binding.dmx.internal.config.DmxColorItem;
import org.openhab.binding.dmx.internal.config.DmxDimmerItem;
import org.openhab.binding.dmx.internal.config.DmxItem;
import org.openhab.binding.dmx.internal.config.DmxSwitchItem;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@literal
 * 
 * DMX Binding provider. The DMX binding currently supports the following item
 * types: Switch, Dimmer and Color.
 * 
 * The DMX binding configuration contains a channel configuration and 0 or more
 * command configurations. It has the following structure:
 * 
 * 		dmx="CHANNEL[<channel-config>], <OPENHAB-COMMAND>[<dmx-command>], <OPENHAB-COMMAND>[<dmx-command>], ..."
 * 	
 * Channel Configuration:
 * 
 *  There can be only one channel configuration structure per item and 
 *  it contains the following structure:
 *  
 *  	CHANNEL[<universe>:<channels>/<footprint>:<status-update-frequency>]
 *  
 *  <universe>:	DMX universe id, should be 0 in most cases
 *  <channels>:	csv list of DMX channel numbers. DMX commands are multiplied on the available channels.
 * 	<channel-width>:	optional width of the DMX channels on device (e.g 1 for switch, 3 for rgb, 4 for rgbw). 
 * 					When used, only a single channel may be specified in <channels>.
 *  <status-update-frequency>	:	optional delay in ms between status updates for continuously changing values.  
 *  								If omitted or a value < 100 is used, no status updates are sent.
 *  
 *  
 * 	Command Configuration: 
 * 
 * The command configuration has the following structure:
 * 		
 * 		<OPENHAB-COMMAND>[<dmx-command>|<command-variables>|<command-variables|..]
 * 
 * <OPENHAB-COMMAND>	: openhab command to override, e.g. ON, OFF, INCREASE, DECREASE, ...
 * <dmx-command>		: dmx-command name, e.g. FADE
 * <command-variables	: repeatable command variable sections for use with the DMX command
 * 
 * For the details on the different command structures, consult the corresponding classes like DmxFadeCommand.
 * 
 * }
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxGenericBindingProvider extends AbstractGenericBindingProvider implements DmxBindingProvider {

	private static final Logger logger = 
		LoggerFactory.getLogger(DmxGenericBindingProvider.class);

	private DmxService dmxService;

	private EventPublisher eventPublisher;
	
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "dmx";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof DimmerItem || item instanceof ColorItem)) {
			throw new BindingConfigParseException("Item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', which is not supported by the DMX Binding.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {

		if (dmxService == null) {
			logger.error(
					"DMX Service unavailable. Cannot process item configuration for {}",
					item.getName());
			return;
		}

		super.processBindingConfiguration(context, item, bindingConfig);
		String config = (bindingConfig == null) ? "" : bindingConfig
				.replaceAll(" ", "").toUpperCase();
		logger.trace("Binding item: {} with configuration {}", item.getName(),
				config);

		DmxItem itemBinding = null;
		if (item instanceof ColorItem) {
			itemBinding = new DmxColorItem(item.getName(), config, this);
		} else if (item instanceof DimmerItem) {
			itemBinding = new DmxDimmerItem(item.getName(), config, this);
		} else if (item instanceof SwitchItem) {
			itemBinding = new DmxSwitchItem(item.getName(), config, this);
		} else {
			throw new BindingConfigParseException("Unsupported item type "
					+ item.getClass().getSimpleName());
		}

		if (itemBinding.isStatusListener()) {

			final DmxStatusUpdateListener dmxStatusListener = itemBinding;
			final String itemName = item.getName();

			logger.trace("Registering status listener for item {} ",
					item.getName());
			dmxService.registerStatusListener(dmxStatusListener);

			// add binding change listener to clean up status listeners on item
			// removal
			addBindingChangeListener(new BindingChangeListener() {

				@Override
				public void bindingChanged(BindingProvider provider,
						String changedItemName) {
					if (itemName.equals(changedItemName)
							&& !provider.providesBindingFor(itemName)) {
						logger.trace("Removing status listener for item {}",
								itemName);
						dmxService.unregisterStatusListener(dmxStatusListener);
					}
				}

				@Override
				public void allBindingsChanged(BindingProvider provider) {
					if (!provider.providesBindingFor(itemName)) {
						logger.trace("Removing status listener for item {}",
								itemName);
						dmxService.unregisterStatusListener(dmxStatusListener);
					}
				}
			});

		}

		addBindingConfig(item, itemBinding);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DmxItem getBindingConfig(String itemName) {
		return (DmxItem) bindingConfigs.get(itemName);
	}

	/**
	 * DmxService loaded via DS.
	 */
	public void setDmxService(DmxService dmxService) {
		this.dmxService = dmxService;
	}

	/**
	 * DmxService unloaded via DS.
	 */
	public void unsetDmxService(DmxService dmxService) {
		this.dmxService = null;
	}

	/**
	 * Event publisher loaded via DS.
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	/**
	 * Event publisher unloaded via DS.
	 */
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postUpdate(String itemName, State state) {
		if (eventPublisher != null) {
			logger.trace("Sending status update to {} : {}", itemName, state);
			eventPublisher.postUpdate(itemName, state);
		} else {
			logger.error("Could not send status update.  Event Publisher is missing");
		}
	}

}
