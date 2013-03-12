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
