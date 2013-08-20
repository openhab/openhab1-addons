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
package org.openhab.binding.digitalstrom.internal.client.constants;

import java.util.HashMap;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 * @version	digitalSTROM-API 1.14.5
 */
public enum OutputModeEnum {
	
	DISABLED	(0),
	SWITCHED	(16),
	DIMMED		(22),
	UP_DOWN		(33),
	SWITCHED_2	(35),
	SWITCH		(39),
	WIPE 		(40),
	POWERSAVE	(41),
	SLAT		(42);	
	
	private final int	mode;
	
	static final HashMap<Integer, OutputModeEnum> outputModes = new HashMap<Integer, OutputModeEnum>();
	
	static {
		for (OutputModeEnum out:OutputModeEnum.values()) {
			outputModes.put(out.getMode(), out);
		}
	}
	
	public static boolean containsMode(Integer mode) {
		return outputModes.keySet().contains(mode);
	}
	
	public static OutputModeEnum getMode(Integer mode) {
		return outputModes.get(mode);
	}
	
	private OutputModeEnum(int outputMode) {
		this.mode = outputMode;
	}
	
	public int getMode() {
		return mode;
	}

}
