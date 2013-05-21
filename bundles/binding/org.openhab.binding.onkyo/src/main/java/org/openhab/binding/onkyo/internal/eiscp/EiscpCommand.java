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
package org.openhab.binding.onkyo.internal.eiscp;


/**
 * Maps a Readable string to a corresponding {@link EiscpCommandRef}.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.3.0
 */
public enum EiscpCommand {
	
    POWER_OFF("PWR00", EiscpCommandRef.POWER_OFF),
    POWER_ON("PWR01", EiscpCommandRef.POWER_ON),
    POWER_QUERY("PWRQSTN", EiscpCommandRef.POWER_QUERY),
    
    UNMUTE("AMT00", EiscpCommandRef.UNMUTE),
    MUTE("AMT01", EiscpCommandRef.MUTE),
    MUTE_QUERY("AMTQSTN", EiscpCommandRef.MUTE_QUERY),

    VOLUME_UP("MVLUP", EiscpCommandRef.VOLUME_UP),
    VOLUME_DOWN("MVLDOWN", EiscpCommandRef.VOLUME_DOWN),
    VOLUME_QUERY("MVLQSTN", EiscpCommandRef.VOLUME_QUERY),
    VOLUME_SET("MVL", EiscpCommandRef.VOLUME_SET),
    SET_VOLUME("MVL", EiscpCommandRef.VOLUME_SET),
    VOLUME("MVL", EiscpCommandRef.VOLUME_SET),

    SOURCE_DVR("SLI00", EiscpCommandRef.SOURCE_DVR),
    SOURCE_SATELLITE("SLI01", EiscpCommandRef.SOURCE_SATELLITE),
    SOURCE_GAME("SLI02", EiscpCommandRef.SOURCE_GAME),
    SOURCE_AUXILIARY("SLI03", EiscpCommandRef.SOURCE_AUX),
    SOURCE_AUX("SLI03", EiscpCommandRef.SOURCE_AUX),
    SOURCE_VIDEO5("SLI04", EiscpCommandRef.SOURCE_VIDEO5),
    SOURCE_COMPUTER("SLI05", EiscpCommandRef.SOURCE_COMPUTER),
    SOURCE_PC("SLI05", EiscpCommandRef.SOURCE_COMPUTER),
    SOURCE_BLURAY("SLI10", EiscpCommandRef.SOURCE_BLURAY),
    SOURCE_TAPE1("SLI20", EiscpCommandRef.SOURCE_TAPE1),
    SOURCE_TAPE2("SLI21", EiscpCommandRef.SOURCE_TAPE2),
    SOURCE_PHONO("SLI22", EiscpCommandRef.SOURCE_PHONO),
    SOURCE_CD("SLI23", EiscpCommandRef.SOURCE_CD),
    SOURCE_FM("SLI24", EiscpCommandRef.SOURCE_FM),
    SOURCE_AM("SLI25", EiscpCommandRef.SOURCE_AM),
    SOURCE_TUNER("SLI26", EiscpCommandRef.SOURCE_TUNER),
    SOURCE_MUSICSERVER("SLI27", EiscpCommandRef.SOURCE_MUSICSERVER),
    SOURCE_INTERETRADIO("SLI28", EiscpCommandRef.SOURCE_INTERETRADIO),
    SOURCE_USB("SLI29", EiscpCommandRef.SOURCE_USB),
    SOURCE_USB_BACK("SLI2A", EiscpCommandRef.SOURCE_USB_BACK),
    SOURCE_NETWORK("SLI2C", EiscpCommandRef.SOURCE_NETWORK),
    SOURCE_MULTICH("SLI30", EiscpCommandRef.SOURCE_MULTICH),
    SOURCE_SIRIUS("SLI32", EiscpCommandRef.SOURCE_SIRIUS),
    SOURCE_UP("SLIUP", EiscpCommandRef.SOURCE_UP),
    SOURCE_DOWN("SLIDOWN", EiscpCommandRef.SOURCE_DOWN),
    SOURCE_QUERY("SLIQSTN", EiscpCommandRef.SOURCE_QUERY),

    VIDEO_WIDE_AUTO("VWM00", EiscpCommandRef.VIDEO_WIDE_AUTO),
    VIDEO_WIDE_43("VWM01", EiscpCommandRef.VIDEO_WIDE_43),
    VIDEO_WIDE_FULL("VWM02", EiscpCommandRef.VIDEO_WIDE_FULL),
    VIDEO_WIDE_ZOOM("VWM03", EiscpCommandRef.VIDEO_WIDE_ZOOM),
    VIDEO_WIDE_WIDEZOOM("VWM04", EiscpCommandRef.VIDEO_WIDE_WIDEZOOM),
    VIDEO_WIDE_SMARTZOOM("VWM05", EiscpCommandRef.VIDEO_WIDE_SMARTZOOM),
    VIDEO_WIDE_NEXT("VWMUP", EiscpCommandRef.VIDEO_WIDE_NEXT),
    VIDEO_WIDE_QUERY("VWMQSTN", EiscpCommandRef.VIDEO_WIDE_QUERY),
    
    LISTEN_MODE_STEREO("LMD00", EiscpCommandRef.LISTEN_MODE_STEREO),
    LISTEN_MODE_ALCHANSTEREO("LMD0C", EiscpCommandRef.LISTEN_MODE_ALCHANSTEREO),
    LISTEN_MODE_AUDYSSEY_DSX("LMD16", EiscpCommandRef.LISTEN_MODE_AUDYSSEY_DSX),
    LISTEN_MODE_PLII_MOVIE_DSX("LMDA0", EiscpCommandRef.LISTEN_MODE_PLII_MOVIE_DSX),
    LISTEN_MODE_PLII_MUSIC_DSX("LMDA1", EiscpCommandRef.LISTEN_MODE_PLII_MUSIC_DSX),
    LISTEN_MODE_PLII_GAME_DSX("LMDA2", EiscpCommandRef.LISTEN_MODE_PLII_GAME_DSX),
    LISTEN_MODE_NEO_CINEMA_DSX("LMDA3", EiscpCommandRef.LISTEN_MODE_NEO_CINEMA_DSX),
    LISTEN_MODE_NEO_MUSIC_DSX("LMDA4", EiscpCommandRef.LISTEN_MODE_NEO_MUSIC_DSX),
    LISTEN_MODE_NEURAL_SURROUND_DSX("LMDA5", EiscpCommandRef.LISTEN_MODE_NEURAL_SURROUND_DSX),
    LISTEN_MODE_NEURAL_DIGITAL_DSX("LMDA6", EiscpCommandRef.LISTEN_MODE_NEURAL_DIGITAL_DSX),
    LISTEN_MODE_QUERY("LMDQSTN", EiscpCommandRef.LISTEN_MODE_QUERY),

    ZONE2_POWER_ON("ZPW01", EiscpCommandRef.ZONE2_POWER_ON),
    ZONE2_POWER_SBY("ZPW00", EiscpCommandRef.ZONE2_POWER_SBY),
    ZONE2_POWER_QUERY("ZPWQSTN", EiscpCommandRef.ZONE2_POWER_QUERY),
    ZONE2_SOURCE_DVR("SLZ00", EiscpCommandRef.ZONE2_SOURCE_DVR),
    ZONE2_SOURCE_SATELLITE("SLZ01", EiscpCommandRef.ZONE2_SOURCE_SATELLITE),
    ZONE2_SOURCE_GAME("SLZ02", EiscpCommandRef.ZONE2_SOURCE_GAME),
    ZONE2_SOURCE_AUX("SLZ03", EiscpCommandRef.ZONE2_SOURCE_AUX),
    ZONE2_SOURCE_VIDEO5("SLZ04", EiscpCommandRef.ZONE2_SOURCE_VIDEO5),
    ZONE2_SOURCE_COMPUTER("SLZ05", EiscpCommandRef.ZONE2_SOURCE_COMPUTER),
    ZONE2_SOURCE_BLURAY("SLZ10", EiscpCommandRef.ZONE2_SOURCE_BLURAY),
    ZONE2_SOURCE_QUERY("SLZQSTN", EiscpCommandRef.ZONE2_SOURCE_QUERY),
    //ZONE2_SOURCE_OFF("SLZ7F", EiscpCommandRef.ZONE2_SOURCE_OFF), // not supported

    NETUSB_OP_PLAY("NTCPLAY", EiscpCommandRef.NETUSB_OP_PLAY),
    NETUSB_OP_STOP("NTCSTOP", EiscpCommandRef.NETUSB_OP_STOP),
    NETUSB_OP_PAUSE("NTCPAUSE", EiscpCommandRef.NETUSB_OP_PAUSE),
    NETUSB_OP_TRACKUP("NTCTRUP", EiscpCommandRef.NETUSB_OP_TRACKUP),
    NETUSB_OP_TRACKDWN("NTCTRDN", EiscpCommandRef.NETUSB_OP_TRACKDWN),
    NETUSB_OP_FF("NTCFF", EiscpCommandRef.NETUSB_OP_FF),
    NETUSB_OP_REW("NTCREW", EiscpCommandRef.NETUSB_OP_REW),
    NETUSB_OP_REPEAT("NTCREPEAT", EiscpCommandRef.NETUSB_OP_REPEAT),
    NETUSB_OP_RANDOM("NTCRANDOM", EiscpCommandRef.NETUSB_OP_RANDOM),
    NETUSB_OP_DISPLAY("NTCDISPLAY", EiscpCommandRef.NETUSB_OP_DISPLAY),
    NETUSB_OP_RIGHT("NTCRIGHT", EiscpCommandRef.NETUSB_OP_RIGHT),
    NETUSB_OP_LEFT("NTCLEFT", EiscpCommandRef.NETUSB_OP_LEFT),
    NETUSB_OP_UP("NTCUP", EiscpCommandRef.NETUSB_OP_UP),
    NETUSB_OP_DOWN("NTCDOWN", EiscpCommandRef.NETUSB_OP_DOWN),
    NETUSB_OP_SELECT("NTCSELECT", EiscpCommandRef.NETUSB_OP_SELECT),
    NETUSB_OP_1("NTC1", EiscpCommandRef.NETUSB_OP_1),
    NETUSB_OP_2("NTC2", EiscpCommandRef.NETUSB_OP_2),
    NETUSB_OP_3("NTC3", EiscpCommandRef.NETUSB_OP_3),
    NETUSB_OP_4("NTC4", EiscpCommandRef.NETUSB_OP_4),
    NETUSB_OP_5("NTC5", EiscpCommandRef.NETUSB_OP_5),
    NETUSB_OP_6("NTC6", EiscpCommandRef.NETUSB_OP_6),
    NETUSB_OP_7("NTC7", EiscpCommandRef.NETUSB_OP_7),
    NETUSB_OP_8("NTC8", EiscpCommandRef.NETUSB_OP_8),
    NETUSB_OP_9("NTC9", EiscpCommandRef.NETUSB_OP_9),
    NETUSB_OP_0("NTC0", EiscpCommandRef.NETUSB_OP_0),
    NETUSB_OP_DELETE("NTCDELETE", EiscpCommandRef.NETUSB_OP_DELETE),
    NETUSB_OP_CAPS("NTCCAPS", EiscpCommandRef.NETUSB_OP_CAPS),
    NETUSB_OP_SETUP("NTCSETUP", EiscpCommandRef.NETUSB_OP_SETUP),
    NETUSB_OP_RETURN("NTCRETURN", EiscpCommandRef.NETUSB_OP_RETURN),
    NETUSB_OP_CHANUP("NTCCHUP", EiscpCommandRef.NETUSB_OP_CHANUP),
    NETUSB_OP_CHANDWN("NTCCHDN", EiscpCommandRef.NETUSB_OP_CHANDWN),
    NETUSB_OP_MENU("NTCMENU", EiscpCommandRef.NETUSB_OP_MENU),
    NETUSB_OP_TOPMENU("NTCTOP", EiscpCommandRef.NETUSB_OP_TOPMENU),
    
    NETUSB_SONG_ARTIST_QUERY("NATQSTN", EiscpCommandRef.NETUSB_SONG_ARTIST_QUERY),
    NETUSB_SONG_ALBUM_QUERY("NALQSTN", EiscpCommandRef.NETUSB_SONG_ALBUM_QUERY),
    NETUSB_SONG_TITLE_QUERY("NTIQSTN", EiscpCommandRef.NETUSB_SONG_TITLE_QUERY),
    NETUSB_SONG_ELAPSEDTIME_QUERY("NTMQSTN", EiscpCommandRef.NETUSB_SONG_ELAPSEDTIME_QUERY),
    NETUSB_SONG_TRACK_QUERY("NTRQSTN", EiscpCommandRef.NETUSB_SONG_TRACK_QUERY),
    NETUSB_PLAY_STATUS_QUERY("NSTQSTN", EiscpCommandRef.NETUSB_PLAY_STATUS_QUERY);	
    
    
    private String command;
	private EiscpCommandRef commandRef;
	
	
	private EiscpCommand(String command, EiscpCommandRef commandRef) {
		this.command = command;
		this.commandRef = commandRef;
	}
	
	
	/**
	 * @return the iscp command string (example 'SLI10')
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return the iscp command constant reference
	 */
	public EiscpCommandRef getCommandRef() {
		return commandRef;
	}
	
	/**
	 * @param command the command to find a matching command name for.
	 * @return the commandName that is associated with the passed command.
	 */
	public static EiscpCommand getCommandByCommandStr(String command) {
		for (EiscpCommand candidate : values()) {
			if (candidate.getCommand().equals(command)) {
				return candidate;
			}
		}
		throw new IllegalStateException("There is no matching commandName for command '" + command + "'");
	}

	/**
	 * @param commandRef the eISCP Command to find a matching command name.
	 * @return the commandName that is associated with the passed eISCP command.
	 */
	public static EiscpCommand getCommandByCommandRef(int commandRef) {
		for (EiscpCommand candidate : values()) {
			if (candidate.getCommandRef().getCommand() == commandRef) {
				return candidate;
			}
		}
		throw new IllegalStateException("There is no matching commandName for commandRef '" + commandRef + "'");
	}
	
}
