/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
