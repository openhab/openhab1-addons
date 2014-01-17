/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
