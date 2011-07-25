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
package org.openhab.binding.mpd.internal;

import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;


/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Thomas.Eichstaedt-Engelen
 */
public enum PlayerCommandTypeMapping {

	PLAY {
		{
			command = "play";
			type = OnOffType.ON;
		}
	},

	STOP {
		{
			command = "stop";
			type = OnOffType.OFF;
		}
	},

	VOLUME {
		{
			command = "volume";
		}
	},

	VOLUME_INCREASE {
		{
			command = "volume_increase";
			type = IncreaseDecreaseType.INCREASE;
		}
	},

	VOLUME_DECREASE {
		{
			command = "volume_decrease";
			type = IncreaseDecreaseType.DECREASE;
		}
	},
	
	SKIP_NEXT {
		{
			command = "skip_next";
			type = IncreaseDecreaseType.INCREASE;
		}
	},
	
	SKIP_PREVIOUS {
		{
			command = "skip_previous";
			type = IncreaseDecreaseType.DECREASE;
		}
	};

	/** Represents the player command as it will be used in *.items configuration */
	String command;
	
	/** The corresponding openHAB-{@link Type} or {@link UnDefType}<code>.NULL</code> by default */
	Type type = UnDefType.NULL;

	public String getPlayerCommand() {
		return command;
	}

	public Type getType() {
		return type;
	}

	public static PlayerCommandTypeMapping fromString(String playerCommand) {

		if ("".equals(playerCommand)) {
			return null;
		}

		for (PlayerCommandTypeMapping c : PlayerCommandTypeMapping.values()) {

			if (c.getPlayerCommand().equals(playerCommand)) {
				return c;
			}
		}

		throw new IllegalArgumentException("cannot find playerCommand for '"
				+ playerCommand + "'");
	}
	
	
}
