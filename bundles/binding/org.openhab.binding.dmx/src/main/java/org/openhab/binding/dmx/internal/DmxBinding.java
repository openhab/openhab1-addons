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
package org.openhab.binding.dmx.internal;

import org.openhab.binding.dmx.DmxBindingProvider;
import org.openhab.binding.dmx.DmxService;
import org.openhab.binding.dmx.internal.config.DmxItem;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DMX event binding class. Processes all the command events and forwards them
 * if required.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxBinding extends AbstractBinding<DmxBindingProvider> {

	private static final Logger logger = 
			LoggerFactory.getLogger(DmxBinding.class);

	private DmxService dmxService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveCommand(String itemName, Command command) {

		if (dmxService == null) {
			logger.warn("No DMX Service available.");
			return;
		}

		// get the item's corresponding dmx binding
		DmxItem itemBinding = null;
		for (DmxBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				itemBinding = provider.getBindingConfig(itemName);
				break;
			}
		}

		if (itemBinding == null) {
			return;
		}

		dmxService.suspend(true);
		itemBinding.processCommand(dmxService, command);
		dmxService.suspend(false);

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

}
