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
package org.openhab.binding.plcbus.internal;

import org.openhab.binding.plcbus.internal.protocol.PLCUnit;
import org.openhab.core.binding.BindingConfig;

/**
 * Configuration for a PLCBusBinding
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class PLCBusBindingConfig implements BindingConfig {

	private PLCUnit unit;
	private int seconds;
	
	PLCBusBindingConfig(String config) {
		seconds = 5;
		parse(config);
	}

	public PLCUnit getUnit() {
		return unit;
	}

	public int getSeconds() {
		return seconds;
	}

	private void parse(String config) {
		String[] parts = config.split(" ");

		if (parts.length >= 2) {
			unit = new PLCUnit(parts[0], parts[1]);

			if (parts.length >= 3) {
				seconds = Integer.parseInt(parts[2]);
			}
		}
	}
	

}