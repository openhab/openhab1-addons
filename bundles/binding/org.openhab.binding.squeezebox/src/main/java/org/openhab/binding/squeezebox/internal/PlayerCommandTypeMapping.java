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
package org.openhab.binding.squeezebox.internal;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;


/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Markus Wolters
 * @since 1.3.0
 */
public enum PlayerCommandTypeMapping {
	
	/* CMDs ******************************************************************************************************/
	
	VOLUME_INCREASE {
		{
			command = "volume_increase";
		}
	},

	VOLUME_DECREASE {
		{
			command = "volume_decrease";
		}
	},
	
	STOP {
		{
			command = "stop";
		}
	},
	
	PLAY {
		{
			command = "play";
			type = OnOffType.ON;
		}
	},

	PAUSE {
		{
			command = "pause";
			type = OnOffType.OFF;
		}
	},
	
	POWER_ON {
		{
			command = "powerOn";
			type = OnOffType.ON;
		}
	},

	POWER_OFF {
		{
			command = "powerOff";
			type = OnOffType.OFF;
		}
	},
	
	NEXT {
		{
			command = "next";
		}
	},
	
	PREV {
		{
			command = "prev";
		}
	},
	
	FILE {
		{
			command = "file";
		}
	},
	
	HTTP {
		{
			command = "http";
		}
	},
	
	MUTE {
		{
			command = "mute";
			type = OnOffType.ON;
		}
	},
	
	UNMUTE {
		{
			command = "unmute";
			type = OnOffType.OFF;
		}
		
	},
	
	ADD {
		{
			command = "add";
		}
	},
	
	REMOVE {
		{
			command = "remove";
		}
		
	},
	
	/* Vars ******************************************************************************************************/
		
	TITLE {
		{
			command = "title";
		}
	},
	
	VOLUME {
		{
			command = "volume";
		}
	},
	
	IS_PAUSED {
		{
			command = "isPaused";
		}
	},
	
	IS_POWERED {
		{
			command = "isPowered";
		}
	},
	
	IS_PLAYING {
		{
			command = "isPlaying";
		}
	},
	
	IS_STOPPED {
		{
			command = "isStopped";
		}
	},
	
	ARTIST {
		{
			command = "artist";
		}
	},
	
	
	ALBUM {
		{
			command = "album";
		}
	},
	
	COVERART {
		{
			command = "coverart";
		}
	},
	
	YEAR {
		{
			command = "year";
		}
	},
	
	REMOTETITLE {
		{
			command = "remotetitle";
		}
	},
	
	GENRE {
		{
			command = "genre";
		}
	},
	
	IS_MUTED {
		{
			command = "isMuted";
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

		throw new IllegalArgumentException("cannot find playerCommand for '" + playerCommand + "'");
	}
	
	
}
