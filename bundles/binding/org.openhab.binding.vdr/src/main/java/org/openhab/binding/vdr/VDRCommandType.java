/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.vdr;

import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;

/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Wolfgang Willinghoefer
 * @since 0.9.0
 */
public enum VDRCommandType {

	POWEROFF {
		{
			command = "powerOff";
			type = OnOffType.OFF;
		}
	},

	POWERON {
		{
			command = "powerOff";
			type = OnOffType.ON;
		}
	},
	
	VOLUME_INCREASE {
		{
			command = "volume";
			type = IncreaseDecreaseType.INCREASE;
		}
	},

	VOLUME_DECREASE {
		{
			command = "volume";
			type = IncreaseDecreaseType.DECREASE;
		}
	},

	CHANNEL_INCREASE {
		{
			command = "channel";
			type = IncreaseDecreaseType.INCREASE;
		}
	},

	CHANNEL_DECREASE {
		{
			command = "channel";
			type = IncreaseDecreaseType.DECREASE;
		}
	},

	MESSAGE {
		{
			command = "message";
			type = null;
		}
	},
	
	RECORDING {
		{
			command = "recording";
			type = null;
		}
	};

	/** Represents the vdr command as it will be used in *.items configuration */
	String command;
	Command type;

	public String getVDRCommand() {
		return command;
	}

	public Command getCommandType() {
		return type;
	}

	public static VDRCommandType create(String vdrCommand, Command command) {

		if ("".equals(vdrCommand)) {
			return null;
		}

		for (VDRCommandType c : VDRCommandType.values()) {

			if (c.getVDRCommand().equals(vdrCommand)
					&& c.getCommandType() != null
					&& c.getCommandType().equals(command)) {
				return c;
			}
		}

		throw new IllegalArgumentException("cannot find playerCommand for '"
				+ vdrCommand + "'");
	}

}
