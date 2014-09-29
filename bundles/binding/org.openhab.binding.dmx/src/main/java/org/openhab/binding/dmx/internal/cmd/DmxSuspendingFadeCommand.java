/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.cmd;

import org.openhab.binding.dmx.DmxService;
import org.openhab.binding.dmx.internal.config.DmxItem;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * DMX Suspending Fade Command. Similar to the DMX Fade Command, but this command will 
 * suspend any active fades before starting this fade.  After this fade has completed,
 * the suspended fade will be reactivated.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxSuspendingFadeCommand extends DmxFadeCommand {

	/**
	 * Create new suspending fade command from a configuration string.
	 * 
	 * @param item
	 *            to which the command applies.
	 * @param cmd
	 *            configuration string.
	 * @throws BindingConfigParseException
	 *             if parsing the configuration fails.
	 */
	public DmxSuspendingFadeCommand(DmxItem item, String cmd)
			throws BindingConfigParseException {
		super(item, cmd);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(DmxService service) {
		for (int c : item.getChannels()) {
			service.suspendChannel(c);
		}
		super.execute(service);
		for (int c : item.getChannels()) {
			service.addChannelResume(c);
		}
	}
	
}
