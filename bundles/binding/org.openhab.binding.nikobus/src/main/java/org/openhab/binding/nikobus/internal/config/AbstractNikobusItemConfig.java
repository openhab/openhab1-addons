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
package org.openhab.binding.nikobus.internal.config;

import org.openhab.binding.nikobus.internal.NikobusBinding;
import org.openhab.binding.nikobus.internal.core.NikobusCommandListener;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.types.Command;

/**
 * Base class for all Nikobus devices like buttons, switch modules, motion
 * sensors, etc.
 * 
 * To add binding configuration support for new devices, extend this class.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public abstract class AbstractNikobusItemConfig implements BindingConfig,
		NikobusCommandListener {

	private String name;

	private String address;

	/**
	 * Default constructor.
	 * 
	 * @param name
	 *            item name.
	 * @param address
	 *            device address.
	 */
	protected AbstractNikobusItemConfig(String name, String address) {
		this.name = name;
		this.address = address;
	}

	/**
	 * @return address of Nikobus component.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return name of the item which this binding is linked to
	 */
	public String getName() {
		return name;
	}

	/**
	 * Process an openHab command for the current item binding.
	 * 
	 * @param command
	 *            openHab command
	 */
	public abstract void processCommand(Command command, NikobusBinding binding) throws Exception;
}
