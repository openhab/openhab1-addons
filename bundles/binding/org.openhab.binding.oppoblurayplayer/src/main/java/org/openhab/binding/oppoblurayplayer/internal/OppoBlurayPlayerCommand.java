/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oppoblurayplayer.internal;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/**
 * Converts all the messages to/from the Oppo Bluray player
 * into to known commands. 
 * 
 * @author netwolfuk (http://netwolfuk.wordpress.com)
 * @since 1.9.0
 */

public enum OppoBlurayPlayerCommand {
	

	/*
	 QPL 
	 
		OK NO DISC
		OK LOADING
		OK OPEN
		OK CLOSE
		OK PLAY
		OK PAUSE
		OK STOP
		OK STEP
		OK FREV
		OK FFWD
		OK SFWD
		OK SREV
		OK SETUP
		OK HOME MENU
		OK MEDIA CENTER
	 */
	
	/*
	 UPL
	 
		DISC – No disc
		LOAD – Loading disc
		OPEN – Tray is open
		CLOS – Tray is closing
		PLAY – Playback is starting
		PAUS – Playback is paused
		STOP – Playback is stopped
		STPF – Forward frame-by-frame step mode
		STPR – Reverse frame-by-frame step mode
		FFWn – Fast forward mode. Where n is a number of 1... 5 to indicate the speed level
		FRVn – Fast reverse mode. Where n is a number of 1... 5 to indicate the	speed level
		SFWn – Slow forward mode. Where n is a number of 1...4 to indicate the speed level (1 = 1⁄2, 2 = 1⁄4, 3 = 1/8, 4 = 1/16)
		SRVn – Slow reverse mode. Where n is a number of 1...4 to indicate the speed level (1 = 1⁄2, 2 = 1⁄4, 3 = 1/8, 4 = 1/16)
		HOME – in home menu
		MCTR – in media center
	 */
	
	POWER_ON (OppoBlurayPlayerCommandType.POWER, OnOffType.ON,  "QPW", "PON", "OK ON", "UPW 1"),
	POWER_OFF (OppoBlurayPlayerCommandType.POWER,OnOffType.OFF,  "QPW", "POF", "OK OFF", "UPW 0"),
	
	PLAYBACK_STATUS_NO_DISC (OppoBlurayPlayerCommandType.PLAYBACK_STATUS, StringType.EMPTY, "QPL", "???", "OK NO DISC", "UPL DISC"),
	PLAYBACK_STATUS_LOADING (OppoBlurayPlayerCommandType.PLAYBACK_STATUS, StringType.EMPTY, "QPL", "???", "OK LOADING", "UPL LOAD"),
	PLAYBACK_STATUS_OPEN    (OppoBlurayPlayerCommandType.PLAYBACK_STATUS, StringType.EMPTY, "QPL", "EJT", "OK OPEN", "UPL OPEN"),
	PLAYBACK_STATUS_CLOSING (OppoBlurayPlayerCommandType.PLAYBACK_STATUS, StringType.EMPTY, "QPL", "EJT", "OK CLOSE", "UPL CLOS"),
	PLAYBACK_STATUS_PLAYING (OppoBlurayPlayerCommandType.PLAYBACK_STATUS, StringType.EMPTY, "QPL", "PLA", "OK PLAY", "UPL PLAY"),
	PLAYBACK_STATUS_PAUSED  (OppoBlurayPlayerCommandType.PLAYBACK_STATUS, StringType.EMPTY, "QPL", "PAU", "OK PAUSE", "UPL PAUS"),
	PLAYBACK_STATUS_STOPPED (OppoBlurayPlayerCommandType.PLAYBACK_STATUS, StringType.EMPTY, "QPL", "STP", "OK STOP", "UPL STOP"),
	PLAYBACK_STATUS_SETUP   (OppoBlurayPlayerCommandType.PLAYBACK_STATUS, StringType.EMPTY, "QPL", "SET", "OK SETUP", "????"),
	PLAYBACK_STATUS_HOME    (OppoBlurayPlayerCommandType.PLAYBACK_STATUS, StringType.EMPTY, "QPL", "HOM", "OK HOME MENU", "UPL HOME"),
	PLAYBACK_STATUS_MCTR    (OppoBlurayPlayerCommandType.PLAYBACK_STATUS, StringType.EMPTY, "QPL", "???", "OK MEDIA CENTER", "UPL MCTR"), 
	
	VERBOSE_MODE_0			(OppoBlurayPlayerCommandType.VERBOSITY, StringType.EMPTY, "QVM", "SVM 0", "OK 0", "SVM OK 0"),
	VERBOSE_MODE_1			(OppoBlurayPlayerCommandType.VERBOSITY, StringType.EMPTY, "QVM", "SVM 1", "OK 1", "SVM OK 1"),
	VERBOSE_MODE_2			(OppoBlurayPlayerCommandType.VERBOSITY, StringType.EMPTY, "QVM", "SVM 2", "OK 2", "SVM OK 2"),
	VERBOSE_MODE_3			(OppoBlurayPlayerCommandType.VERBOSITY, StringType.EMPTY, "QVM", "SVM 3", "OK 3", "SVM OK 3"),
	
	INPUT_SOURCE_BD_PLAYER	(OppoBlurayPlayerCommandType.INPUT_SOURCE, StringType.EMPTY, "QIS", "SIS 0", "OK 0 BD-PLAYER", "SIS OK 0 BD-PLAYER"),
	INPUT_SOURCE_HDMI_FRONT	(OppoBlurayPlayerCommandType.INPUT_SOURCE, StringType.EMPTY, "QIS", "SIS 1", "OK 1 HDMI-FRONT", "SIS OK 1 HDMI-FRONT"),
	INPUT_SOURCE_HDMI_BACK	(OppoBlurayPlayerCommandType.INPUT_SOURCE, StringType.EMPTY, "QIS", "SIS 2", "OK 2 HDMI-BACK", "SIS OK 2 HDMI-BACK"),
	INPUT_SOURCE_ARC		(OppoBlurayPlayerCommandType.INPUT_SOURCE, StringType.EMPTY, "QIS", "SIS 3", "OK 3 ARC", "SIS OK 3 ARC"),
	INPUT_SOURCE_OPTICAL	(OppoBlurayPlayerCommandType.INPUT_SOURCE, StringType.EMPTY, "QIS", "SIS 4", "OK 4 OPTICAL", "SIS OK 4 OPTICAL"),
	INPUT_SOURCE_COAXIAL	(OppoBlurayPlayerCommandType.INPUT_SOURCE, StringType.EMPTY, "QIS", "SIS 5", "OK 5 COAXIAL", "SIS OK 5 COAXIAL"),
	INPUT_SOURCE_USB_AUDIO	(OppoBlurayPlayerCommandType.INPUT_SOURCE, StringType.EMPTY, "QIS", "SIS 6", "OK 6 USB-AUDIO", "SIS OK 6 USB-AUDIO"),
	
	// TODO: Volume level needs some more thought.
	// Toggling mute (MUT) off will return actual volume level.
	// Currently only 0% and 100% are catered for.
	VOLUME_LEVEL_000		(OppoBlurayPlayerCommandType.VOLUME_LEVEL, DecimalType.ZERO,  "QVL", "SVL 0", "OK 0", "UVL OK 000"),
	VOLUME_LEVEL_100		(OppoBlurayPlayerCommandType.VOLUME_LEVEL, DecimalType.ZERO,  "QVL", "SVL 100", "OK 100", "UVL OK 100"),
	VOLUME_LEVEL_MUTE		(OppoBlurayPlayerCommandType.VOLUME_LEVEL, OnOffType.OFF,  "QVL", "SVL MUT", "OK MUT", "UVL OK MUT"),
	
	/*
	  QDT
	  	OK BD-MV
		OK DVD-VIDEO
		OK DVD-AUDIO
		OK SACD
		OK CDDA
		OK HDCD
		OK DATA-DISC
	  
	  
	  UDT - Disc Type Update:
		Sent when a new disc type is detected.
		Possible Parameters: 4 chars
		BDMV - Blu-ray Disc
		DVDV – DVD-Video
		DVDA – DVD-Audio
		SACD
		CDDA
		HDCD
		DATA – Data disc
		VCD2 – VCD 2.0
		SVCD - SVCD
		Example: @UDT DVDV
	 */
							//	Type								State			   Qry	  Cmd	 Response	 Event
	DISC_TYPE_BLURAY_DISC	(OppoBlurayPlayerCommandType.DISC_TYPE, StringType.EMPTY,  "QDT", "???", "OK BD-MV", "UDT BDMV"),
	DISC_TYPE_DVD_VIDEO		(OppoBlurayPlayerCommandType.DISC_TYPE, StringType.EMPTY,  "QDT", "???", "OK DVD-VIDEO", "UDT DVDV"),
	DISC_TYPE_DVD_AUDIO		(OppoBlurayPlayerCommandType.DISC_TYPE, StringType.EMPTY,  "QDT", "???", "OK DVD-AUDIO", "UDT DVDA"),
	DISC_TYPE_SACD			(OppoBlurayPlayerCommandType.DISC_TYPE, StringType.EMPTY,  "QDT", "???", "OK SACD", "UDT SACD"),
	DISC_TYPE_CDDA			(OppoBlurayPlayerCommandType.DISC_TYPE, StringType.EMPTY,  "QDT", "???", "OK CDDA", "UDT CDDA"),
	DISC_TYPE_HDCD			(OppoBlurayPlayerCommandType.DISC_TYPE, StringType.EMPTY,  "QDT", "???", "OK HDCD", "UDT HDCD"),
	DISC_TYPE_DATA_DISC		(OppoBlurayPlayerCommandType.DISC_TYPE, StringType.EMPTY,  "QDT", "???", "OK DATA-DISC", "UDT DATA"),
	DISC_TYPE_DATA_VCD2		(OppoBlurayPlayerCommandType.DISC_TYPE, StringType.EMPTY,  "QDT", "???", "OK VCD2", "UDT VCD2"),
	DISC_TYPE_DATA_SVCD		(OppoBlurayPlayerCommandType.DISC_TYPE, StringType.EMPTY,  "QDT", "???", "OK SVCD", "UDT SVCD"),
	
	/*
	
		QHD - Query HDMI resolution	
			OK 480P
			OK 720P50
			OK 1080P60
			OK AUTO
			
		SHD 
			SDI
			SDP
			720P
			1080I
			1080P
			SRC
			AUTO
		UHD
			unknown if UDH updates are sent.
			TODO: Monitor log and see.
	*/
	
	HDMI_RESOLUTION_SDI_480		(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD SDI", "OK 480I", "???"),
	HDMI_RESOLUTION_SDI_576		(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD SDI", "OK 576I", "???"),
	HDMI_RESOLUTION_SDP_480		(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD SDP", "OK 480P", "???"),
	HDMI_RESOLUTION_SDP_576		(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD SDP", "OK 576P", "???"),
	
	HDMI_RESOLUTION_720P_24		(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 720P", "OK 720P25", "???"),
	HDMI_RESOLUTION_720P_25		(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 720P", "OK 720P30", "???"),
	HDMI_RESOLUTION_720P_30		(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 720P", "OK 720P30", "???"),
	HDMI_RESOLUTION_720P_50		(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 720P", "OK 720P50", "???"),
	HDMI_RESOLUTION_720P_60		(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 720P", "OK 720P60", "???"),
	
	HDMI_RESOLUTION_1080I_24	(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 1080I", "OK 1080I25", "???"),
	HDMI_RESOLUTION_1080I_25	(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 1080I", "OK 1080I30", "???"),
	HDMI_RESOLUTION_1080I_30	(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 1080I", "OK 1080I30", "???"),
	HDMI_RESOLUTION_1080I_50	(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 1080I", "OK 1080I50", "???"),
	HDMI_RESOLUTION_1080I_60	(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 1080I", "OK 1080I60", "???"),
	
	HDMI_RESOLUTION_1080P_24	(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 1080P", "OK 1080P25", "???"),
	HDMI_RESOLUTION_1080P_25	(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 1080P", "OK 1080P30", "???"),
	HDMI_RESOLUTION_1080P_30	(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 1080P", "OK 1080P30", "???"),
	HDMI_RESOLUTION_1080P_50	(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 1080P", "OK 1080P50", "???"),
	HDMI_RESOLUTION_1080P_60	(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD 1080P", "OK 1080P60", "???"),
	
	HDMI_RESOLUTION_SRC			(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD SRC", "OK SRC", "???"),
	HDMI_RESOLUTION_AUTO		(OppoBlurayPlayerCommandType.HDMI_RESOLUTION, StringType.EMPTY,  "QHD", "SHD AUTO", "OK AUTO", "???"),
	
	; // Semicolon for end of Enum declarations 
	
	final static String TO_PLAYER_PREFIX="#";		
	final static String FROM_PLAYER_PREFIX="@";		
	
	private OppoBlurayPlayerCommandType type;
	private State state;
	private String query;
	private String command;
	private String response;
	private String queryResponseVerbose;
	private String commandResponseVerbose;
	private String eventVerbose;
	

	private OppoBlurayPlayerCommand(final OppoBlurayPlayerCommandType type, 
									final State state,
									final String query,
									final String command,
									final String response,
									final String event
								   ) {
		this.state = state;
		this.type = type;
		this.query = TO_PLAYER_PREFIX + query;
		this.command = TO_PLAYER_PREFIX + command;
		this.response = FROM_PLAYER_PREFIX + response;
		this.queryResponseVerbose = FROM_PLAYER_PREFIX + query + " " + response;
		this.commandResponseVerbose = FROM_PLAYER_PREFIX + command + " " + response;
		this.eventVerbose = FROM_PLAYER_PREFIX + event;
	}
			
	public static OppoBlurayPlayerCommand findMessageForCommand(OppoBlurayPlayerCommandType commandType, Command message) throws OppoBlurayPlayerException {
		for (OppoBlurayPlayerCommand c : OppoBlurayPlayerCommand.values()) {
			if (c.type.equals(commandType)){
				State s = convertValueToOpenHabState(c.type.getItemClass(), message.toString());
				if (c.state.equals(s)){
					return c;
				}
			}
		}
		throw new OppoBlurayPlayerUnknownCommandException("No command found to match message " + message);
		
	}
	
	public static OppoBlurayPlayerCommand findMatchingCommandFromResponse(String message) throws OppoBlurayPlayerUnknownCommandException {
		for (OppoBlurayPlayerCommand c : OppoBlurayPlayerCommand.values()) {
			if (
					c.response.equals(message) || 
					c.queryResponseVerbose.equals(message) || 
					c.commandResponseVerbose.equals(message) || 
					c.eventVerbose.equals(message)
					) {
				
				return c;
			}
		}
		throw new OppoBlurayPlayerUnknownCommandException("No command found to match message " + message);
		
	}
	
	public OppoBlurayPlayerCommandType getOppoBlurayPlayerCommandType(){
		return type;
	}

	public String getCommandString() {
		return command;
	}
	
	public String getQuery() {
		return query;
	}
	
	public Class<? extends Item> getItemType() {
		return type.getItemClass();
	}
	
	/**
	 * Convert player value to OpenHAB state.
	 * 
	 * @param itemType
	 * @param data
	 * 
	 * @return
	 * @throws OppoBlurayPlayerException 
	 */
	 public static State convertValueToOpenHabState(Class<? extends Item> itemType, String data) throws OppoBlurayPlayerException {
		State state = UnDefType.UNDEF;

		try {
		
			if (itemType == SwitchItem.class) {
				state = data.equalsIgnoreCase("OFF") ? OnOffType.OFF : OnOffType.ON;
				
			} else if (itemType == NumberItem.class) {
				state = new DecimalType(data);
				
			} else if (itemType == DimmerItem.class) {
				state = new PercentType(data);
				
			} else if (itemType == RollershutterItem.class) {
				state = new PercentType(data);
				
			} else if (itemType == StringItem.class) {
				state = new StringType(data);
			}
		} catch (Exception e) {
			throw new OppoBlurayPlayerException("Cannot convert value '" + data + "' to data type " + itemType);
		}
		
		return state;
	 }
	 
	 public State getState() {
		return state;
	 }

}
