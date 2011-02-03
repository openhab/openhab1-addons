/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.exec.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.openhab.binding.exec.ExecBindingProvider;
import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Binding to send the magic Wake-on-LAN packet to the given MAC-address
 * <p>
 * Valid configurations looks like:
 * <ul>
 * <li>{ wol="192.168.1.0#00:1f:d0:93:f8:b7" }</li>
 * <li>{ wol="192.168.1.0#00-1f-d0-93-f8-b7" }</li>
 * </ul>
 * 
 * @author Thomas.Eichstaedt-Engelen
 */
public class ExecBinding extends AbstractEventSubscriber {

	private static final Logger logger = LoggerFactory.getLogger(ExecBinding.class);

	/** to keep track of all binding providers */
	private Collection<ExecBindingProvider> providers = new HashSet<ExecBindingProvider>();
	
	
	public void addBindingProvider(ExecBindingProvider provider) {
		this.providers.add(provider);
	}

	public void removeBindingProvider(ExecBindingProvider provider) {
		this.providers.remove(provider);
	}
	
	
	@Override
	public void receiveCommand(String itemName, Command command) {
		
		ExecBindingProvider provider = this.providers.iterator().next();
		
		String commandLine = 
			provider.getCommandLine(itemName, command.toString());
		if (commandLine != null && !commandLine.isEmpty()) {
			executeCommand(commandLine);
		}
	}

	private void executeCommand(String commandLine) {
		try {
			Runtime.getRuntime().exec(commandLine);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
