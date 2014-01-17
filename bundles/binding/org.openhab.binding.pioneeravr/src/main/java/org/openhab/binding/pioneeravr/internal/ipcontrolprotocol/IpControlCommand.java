/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pioneeravr.internal.ipcontrolprotocol;


/**
 * Maps a Readable string to a corresponding {@link IpControlCommandRef}.
 * 
 * This enum is used for mapping a AV receiver command to the 
 * pioneer command string as specified in the protocol spec.
 * 
 * @author Rainer Ostendorf
 * @author based on the Onkyo binding by Pauli Anttila and others
 */
public enum IpControlCommand {
	
    // Main zone
	POWER_ON(		"PO", "PWR", IpControlCommandRef.POWER_ON),
	POWER_OFF(		"PF", "PWR", IpControlCommandRef.POWER_OFF),    
    POWER_QUERY(	"?P", "PWR",  IpControlCommandRef.POWER_QUERY),
    
    // Volume
    VOLUME_UP(		"VU", 	"", 	IpControlCommandRef.VOLUME_UP),
    VOLUME_DOWN(	"VD", 	"", 	IpControlCommandRef.VOLUME_DOWN),
    VOLUME_QUERY(	"?V", 	"VOL", 	IpControlCommandRef.VOLUME_QUERY),
    VOLUME_SET(		"%03dVL", "VOL",IpControlCommandRef.VOLUME_SET),
    
    // Mute
    MUTE(			"MO", "", IpControlCommandRef.MUTE),
    UNMUTE(			"MF", "", IpControlCommandRef.UNMUTE),
    MUTE_QUERY(		"?M", "MUT",  IpControlCommandRef.MUTE_QUERY),

    // Source / Input channel
    SOURCE_QUERY(		"?F", "FN", IpControlCommandRef.SOURCE_QUERY),
    SOURCE_UP(			"FU", "", IpControlCommandRef.SOURCE_UP),
    SOURCE_DOWN(		"FD", "", IpControlCommandRef.SOURCE_DOWN),
    SOURCE_SET(			"%02dFN", "FN", IpControlCommandRef.SOURCE_SET),
    SOURCE_DVD(			"04FN", "FN04", IpControlCommandRef.SOURCE_DVD),
    SOURCE_BD(			"25FN", "FN25", IpControlCommandRef.SOURCE_BD),
    SOURCE_TVSAT(		"05FN", "FN05", IpControlCommandRef.SOURCE_TV_SAT),
    SOURCE_DVR_BDR(		"15FN", "FN15", IpControlCommandRef.SOURCE_DVR_BDR),
    SOURCE_VIDEO1(		"10FN", "FN10", IpControlCommandRef.SOURCE_VIDEO1),
    SOURCE_VIDEO2(		"14FN", "FN14", IpControlCommandRef.SOURCE_VIDEO2),
    SOURCE_HDMI1(		"19FN", "FN19", IpControlCommandRef.SOURCE_HDMI1),
    SOURCE_HDMI2(		"20FN", "FN20", IpControlCommandRef.SOURCE_HDMI2),
    SOURCE_HDMI3(		"21FN", "FN21", IpControlCommandRef.SOURCE_HDMI3),
    SOURCE_HDMI4(		"22FN", "FN22", IpControlCommandRef.SOURCE_HDMI4),
    SOURCE_HDMI5(		"23FN", "FN23", IpControlCommandRef.SOURCE_HDMI5),
    SOURCE_HMG(			"26FN", "FN26", IpControlCommandRef.SOURCE_HMG),
    SOURCE_IPOD_USB(	"17FN", "FN17", IpControlCommandRef.SOURCE_IPOD),
    SOURCE_XMRADIO(		"18FN", "FN18", IpControlCommandRef.SOURCE_XMRADIO),
    SOURCE_CD(			"01FN", "FN01", IpControlCommandRef.SOURCE_CD),
    SOURCE_CDR_TAPE(	"03FN", "FN03", IpControlCommandRef.SOURCE_CDR_TAPE),
    SOURCE_TUNER(		"02FN", "FN02", IpControlCommandRef.SOURCE_TUNER),
    SOURCE_PHONO(		"00FN", "FN00", IpControlCommandRef.SOURCE_PHONO),
    SOURCE_MULTICH_IN(	"12FN", "FN12", IpControlCommandRef.SOURCE_MULTI_CH_IN),
    SOURCE_ADAPTER_PORT("33FN", "FN33", IpControlCommandRef.SOURCE_ADAPTER_PORT),
    SOURCE_HDMI_CYCL(	"31FN", "FN31", IpControlCommandRef.SOURCE_HDMI_CYCLIC),
    
    // Listening mode
    LISTENING_MODE( 		"%04dSR", "SR", IpControlCommandRef.LISTENING_MODE ),
	LISTENING_MODE_QUERY( "?L", "LM", IpControlCommandRef.LISTENING_MODE_QUERY ),
	
	// tone control
	TONE_ON( 	 "TO1", "TO", IpControlCommandRef.TONE_ON ),
	TONE_BYPASS( "TO0", "TO", IpControlCommandRef.TONE_BYPASS ),
	TONE_QUERY(  "?TO", "TO",  IpControlCommandRef.TONE_QUERY),
    
	// bass control
	BASS_INCREMENT( "BI", "", IpControlCommandRef.BASS_INCREMENT ),
	BASS_DECREMENT( "BD", "", IpControlCommandRef.BASS_DECREMENT  ),
	BASS_QUERY( 	"?BA", "BA", IpControlCommandRef.BASS_QUERY ),
	
	// treble control
	TREBLE_INCREMENT(	"TI", "", IpControlCommandRef.TREBLE_INCREMENT ),
	TREBLE_DECREMENT(	"TD", "", IpControlCommandRef.TREBLE_DECREMENT),
	TREBLE_QUERY(		"?TR", "TR", IpControlCommandRef.TREBLE_QUERY),
	
	// Speaker configuration
	SPEAKERS( 	 	"%01dSPK", 	"SPK", 	IpControlCommandRef.SPEAKERS   ),
	SPEAKERS_OFF(	"0SPK", 	"SPK0", IpControlCommandRef.SPEAKERS_OFF),
	SPEAKERS_A(		"1SPK", 	"SPK1", IpControlCommandRef.SPEAKERS_A),
	SPEAKERS_B(		"2SPK", 	"SPK2", IpControlCommandRef.SPEAKERS_B),
	SPEAKERS_A_B(	"3SPK", 	"SPK3", IpControlCommandRef.SPEAKERS_A_B),
	
	// HDMI outputs configuration
	HDMI_OUTPUT( "%01dHO", "HO", IpControlCommandRef.HDMI_OUTPUT),
	HDMI_OUT_ALL( "0HO", "HO0", IpControlCommandRef.HDMI_OUT_ALL ),
	HDMI_OUT_1( "1HO", "HO1", IpControlCommandRef.HDMI_OUT_1 ),
	HDMI_OUT_2( "2HO", "HO2", IpControlCommandRef.HDMI_OUT_2 ),
	
	// HDMI audio configuration
	HDMI_AUDIO_AMP( "0HA", "HA0", IpControlCommandRef.HDMI_AUDIO_AMP ),
	HDMI_AUDIO_THROUGH( "1HA", "HA1", IpControlCommandRef.HDMI_AUDIO_THROUGH ),
	
	// PQLS setting
	PQLS_OFF( "0PQ", "PQ0", IpControlCommandRef.HDMI_AUDIO_THROUGH ),
	PQLS_AUTO( "1PQ", "PQ1", IpControlCommandRef.PQLS_AUTO ),
	
	// Zone 2 control
	ZONE2_POWER_ON( 		"APO", 		"APR0", IpControlCommandRef.ZONE2_POWER_ON ),
	ZONE2_POWER_OFF(		"APF", 		"APR1", IpControlCommandRef.ZONE2_POWER_OFF ),
	ZONE2_POWER_QUERY(		"?AP", 		"APR",  IpControlCommandRef.ZONE2_POWER_QUERY ),
	ZONE2_INPUT( 			"%02dZS",	"Z2F",  IpControlCommandRef.ZONE2_INPUT ),
	ZONE2_INPUT_DVD(		"04ZS",		"Z2F04", IpControlCommandRef.ZONE2_INPUT_DVD ),
	ZONE2_INPUT_TV_SAT(		"05ZS",		"Z2F05", IpControlCommandRef.ZONE2_INPUT_TV_SAT ),
	ZONE2_INPUT_DVR_BDR(	"15ZS",		"Z2F15", IpControlCommandRef.ZONE2_INPUT_DVR_BDR ),
	ZONE2_INPUT_VIDEO1(		"10ZS",		"Z2F10", IpControlCommandRef.ZONE2_INPUT_VIDEO1 ),
	ZONE2_INPUT_VIDEO2(		"14ZS",		"Z2F14", IpControlCommandRef.ZONE2_INPUT_VIDEO2 ),
	ZONE2_INPUT_HMG(		"26ZS",		"Z2F26", IpControlCommandRef.ZONE2_INPUT_HMG ),
	ZONE2_INPUT_IPOD(		"17ZS",		"Z2F17", IpControlCommandRef.ZONE2_INPUT_IPOD ),
	ZONE2_INPUT_XMRADIO(	"18ZS",		"Z2F18", IpControlCommandRef.ZONE2_INPUT_XMRADIO ),
	ZONE2_INPUT_CD(			"01ZS",		"Z2F01", IpControlCommandRef.ZONE2_INPUT_CD ),
	ZONE2_INPUT_CDR_TAPE(	"03ZS",		"Z2F03", IpControlCommandRef.ZONE2_INPUT_CDR_TAPE ),
	ZONE2_INPUT_TUNER(		"02ZS",		"Z2F02", IpControlCommandRef.ZONE2_INPUT_TUNER ),
	ZONE2_INPUT_ADAPTER(	"33ZS",		"Z2F33", IpControlCommandRef.ZONE2_INPUT_ADAPTER ),
	ZONE2_INPUT_SIRIUS(		"27ZS",		"Z2F27", IpControlCommandRef.ZONE2_INPUT_SIRIUS ),
	ZONE2_INPUT_QUERY(		"?ZS",		"Z2F",   IpControlCommandRef.ZONE2_INPUT_QUERY ),
	ZONE2_VOLUME_UP(		"ZU",		"", 	IpControlCommandRef.ZONE2_VOLUME_UP ),
	ZONE2_VOLUME_DOWN(		"ZD",		"", 	IpControlCommandRef.ZONE2_VOLUME_DOWN ),
	ZONE2_VOLUME(			"%02ZV",	"ZV", 	IpControlCommandRef.ZONE2_VOLUME ),
	ZONE2_VOLUME_QUERY(		"?ZV",		"ZV", 	IpControlCommandRef.ZONE2_VOLUME_QUERY ),
	ZONE2_MUTE(				"Z2MO",		"Z2MUT0", IpControlCommandRef.ZONE2_MUTE ),
	ZONE2_UNMUTE(			"Z2MF",		"Z2MUT1", IpControlCommandRef.ZONE2_UNMUTE ),
	ZONE2_MUTE_QUERY(		"?Z2M",		"Z2MUT", IpControlCommandRef.ZONE2_MUTE_QUERY ),
	
	// zone 3 control
	ZONE3_POWER_ON( 		"BPO", "BPR0", 	IpControlCommandRef.ZONE3_POWER_ON ),
	ZONE3_POWER_OFF(		"BPF", "BPR1",	IpControlCommandRef.ZONE3_POWER_OFF ),
	ZONE3_POWER_QUERY(		"?BP", "BPR", 	IpControlCommandRef.ZONE3_POWER_QUERY ),
	ZONE3_INPUT( 			"%02dZT","Z3F", IpControlCommandRef.ZONE3_INPUT ),
	ZONE3_INPUT_DVD(		"04ZT","Z3F04", IpControlCommandRef.ZONE3_INPUT_DVD ),
	ZONE3_INPUT_TV_SAT(		"05ZT","Z3F05", IpControlCommandRef.ZONE3_INPUT_TV_SAT ),
	ZONE3_INPUT_DVR_BDR(	"15ZT","Z3F15", IpControlCommandRef.ZONE3_INPUT_DVR_BDR ),
	ZONE3_INPUT_VIDEO1(		"10ZT","Z3F10", IpControlCommandRef.ZONE3_INPUT_VIDEO1 ),
	ZONE3_INPUT_VIDEO2(		"14ZT","Z3F14", IpControlCommandRef.ZONE3_INPUT_VIDEO2 ),
	ZONE3_INPUT_HMG(		"26ZT","Z3F26", IpControlCommandRef.ZONE3_INPUT_HMG ),
	ZONE3_INPUT_IPOD(		"17ZT","Z3F17", IpControlCommandRef.ZONE3_INPUT_IPOD ),
	ZONE3_INPUT_XMRADIO(	"18ZT","Z3F18", IpControlCommandRef.ZONE3_INPUT_XMRADIO ),
	ZONE3_INPUT_CD(			"01ZT","Z3F01", IpControlCommandRef.ZONE3_INPUT_CD ),
	ZONE3_INPUT_CDR_TAPE(	"03ZT","Z3F03", IpControlCommandRef.ZONE3_INPUT_CDR_TAPE ),
	ZONE3_INPUT_TUNER(		"02ZT","Z3F02", IpControlCommandRef.ZONE3_INPUT_TUNER ),
	ZONE3_INPUT_ADAPTER(	"33ZT","Z3F33", IpControlCommandRef.ZONE3_INPUT_ADAPTER ),
	ZONE3_INPUT_SIRIUS(		"27ZT","Z3F27", IpControlCommandRef.ZONE3_INPUT_SIRIUS ),
	ZONE3_INPUT_QUERY(		"?ZT","Z3F", 	IpControlCommandRef.ZONE3_INPUT_QUERY ),
	ZONE3_VOLUME_UP(		"YU","YV", 		IpControlCommandRef.ZONE3_VOLUME_UP ),
	ZONE3_VOLUME_DOWN(		"YD","YV", 		IpControlCommandRef.ZONE3_VOLUME_DOWN ),
	ZONE3_VOLUME(			"%02YV","YV", 	IpControlCommandRef.ZONE3_VOLUME ),
	ZONE3_VOLUME_QUERY(		"?YV","YV", 	IpControlCommandRef.ZONE3_VOLUME_QUERY ),
	ZONE3_MUTE(				"Z3MO","Z3MUT0",IpControlCommandRef.ZONE3_MUTE ),
	ZONE3_UNMUTE(			"Z3MF","Z3MUT1",IpControlCommandRef.ZONE3_UNMUTE ),
	ZONE3_MUTE_QUERY(		"?Z3M","Z3MUT", IpControlCommandRef.ZONE3_MUTE_QUERY ),
	
	// radio tuner
	TUNER_FREQ_INCREMENT(	"TFI", "", 		IpControlCommandRef.TUNER_FREQ_INCREMENT),
	TUNER_FREQ_DECREMENT(	"TFD", "", 		IpControlCommandRef.TUNER_FREQ_DECREMENT),
	TUNER_FREQ_QUERY_AM(	"?FR", "FRA", 	IpControlCommandRef.TUNER_FREQ_QUERY_AM ),
	TUNER_FREQ_QUERY_FM(	"?FR", "FRF",	IpControlCommandRef.TUNER_FREQ_QUERY_FM ),
	TUNER_BAND(				"TB", "FRF", 	IpControlCommandRef.TUNER_BAND),
	TUNER_PRESET(			"%01dTP", "PR", IpControlCommandRef.TUNER_PRESET),
	TUNER_CLASS(			"TC", "PR", 	IpControlCommandRef.TUNER_CLASS),
	TUNER_PRESET_INCREMENT(	"TPI", "", 		IpControlCommandRef.TUNER_PRESET_INCREMENT),
	TUNER_PRESET_DECREMENT(	"TPD", "", 		IpControlCommandRef.TUNER_PRESET_DECREMENT),
	TUNER_PRESET_QUERY(		"?TP", "", 		IpControlCommandRef.TUNER_PRESET_QUERY),
	
	// iPod control
	IPOD_PLAY( 			"00IP", "", IpControlCommandRef.IPOD_PLAY ),
	IPOD_PAUSE( 		"01IP", "", IpControlCommandRef.IPOD_PAUSE ),
	IPOD_STOP( 			"02IP", "", IpControlCommandRef.IPOD_STOP),
	IPOD_PREVIOS( 		"03IP", "", IpControlCommandRef.IPOD_PREVIOS),
	IPOD_NEXT( 			"04IP", "", IpControlCommandRef.IPOD_NEXT),
	IPOD_REV( 			"05IP", "", IpControlCommandRef.IPOD_REV),
	IPOD_FWD( 			"06IP", "", IpControlCommandRef.IPOD_FWD),
	IPOD_REPEAT( 		"07IP", "", IpControlCommandRef.IPOD_REPEAT),
	IPOD_SHUFFLE( 		"08IP", "", IpControlCommandRef.IPOD_SHUFFLE),
	IPOD_DISPLAY( 		"09IP", "", IpControlCommandRef.IPOD_DISPLAY),
	IPOD_CONTROL( 		"10IP", "", IpControlCommandRef.IPOD_CONTROL),
	IPOD_CURSOR_UP( 	"13IP", "", IpControlCommandRef.IPOD_CURSOR_UP),
	IPOD_CURSOR_DOWN(	"14IP", "", IpControlCommandRef.IPOD_CURSOR_DOWN),
	IPOD_CURSOR_LEFT(	"15IP", "", IpControlCommandRef.IPOD_CURSOR_LEFT),
	IPOD_CURSOR_RIGHT(	"16IP", "", IpControlCommandRef.IPOD_CURSOR_RIGHT),
	IPOD_ENTER( 		"17IP", "", IpControlCommandRef.IPOD_ENTER),
	IPOD_RETURN( 		"18IP", "", IpControlCommandRef.IPOD_RETURN),
	IPOD_TOP_MENU( 		"19IP", "", IpControlCommandRef.IPOD_TOP_MENU),
	IPOD_KEY_OFF( 		"KOF", 	"", IpControlCommandRef.IPOD_KEY_OFF),
	
	ADAPTER_PLAY_PAUSE(	"20BT", "", IpControlCommandRef.ADAPTER_PLAY_PAUSE ),
	ADAPTER_PLAY(		"10BT", "", IpControlCommandRef.ADAPTER_PLAY ),
	ADAPTER_PAUSE(		"11BT", "", IpControlCommandRef.ADAPTER_PAUSE ),
	ADAPTER_STOP(		"12BT", "", IpControlCommandRef.ADAPTER_STOP ),
	ADAPTER_PREVIOUS(	"13BT", "", IpControlCommandRef.ADAPTER_PREVIOUS ),
	ADAPTER_NEXT(		"14BT", "", IpControlCommandRef.ADAPTER_NEXT ),
	ADAPTER_REV(		"15BT", "", IpControlCommandRef.ADAPTER_REV ),
	ADAPTER_FWD(		"16BT", "", IpControlCommandRef.ADAPTER_FWD ),
    
    // Home Media Gateway (HMG) control
    HMG_NUMKEY(		"%02dNW","",IpControlCommandRef.HMG_NUMKEY ),
    HMG_NUMKEY_0(	"00NW", "", IpControlCommandRef.HMG_NUMKEY0 ),
    HMG_NUMKEY_1(	"01NW", "", IpControlCommandRef.HMG_NUMKEY1 ),
    HMG_NUMKEY_2(	"02NW", "", IpControlCommandRef.HMG_NUMKEY2 ),
    HMG_NUMKEY_3(	"03NW", "", IpControlCommandRef.HMG_NUMKEY3 ),
    HMG_NUMKEY_4(	"04NW", "", IpControlCommandRef.HMG_NUMKEY4 ),
    HMG_NUMKEY_5(	"05NW", "", IpControlCommandRef.HMG_NUMKEY5 ),
    HMG_NUMKEY_6(	"06NW", "", IpControlCommandRef.HMG_NUMKEY6 ),
    HMG_NUMKEY_7(	"07NW", "", IpControlCommandRef.HMG_NUMKEY7 ),
    HMG_NUMKEY_8(	"08NW", "", IpControlCommandRef.HMG_NUMKEY8 ),
    HMG_NUMKEY_9(	"09NW", "", IpControlCommandRef.HMG_NUMKEY9 ),
    HMG_PLAY(		"10NW", "", IpControlCommandRef.HMG_PLAY ),
    HMG_PAUSE(		"11NW", "", IpControlCommandRef.HMG_PAUSE ),
    HMG_PREV(		"12NW", "", IpControlCommandRef.HMG_PREVIOUS ),
    HMG_NEXT(		"13NW", "", IpControlCommandRef.HMG_NEXT ),
    HMG_DISPLAY(	"18NW", "", IpControlCommandRef.HMG_DISPLAY ),
    HMG_STOP(		"20NW", "", IpControlCommandRef.HMG_STOP ),
    HMG_UP(			"26NW", "", IpControlCommandRef.HMG_UP ),
    HMG_DOWN(		"27NW", "", IpControlCommandRef.HMG_DOWN ),
    HMG_RIGHT(		"28NW", "", IpControlCommandRef.HMG_RIGHT ),
    HMG_LEFT(		"29NW", "", IpControlCommandRef.HMG_LEFT ),
    HMG_ENTER(		"30NW", "", IpControlCommandRef.HMG_ENTER ),
    HMG_RETURN(		"31NW", "", IpControlCommandRef.HMG_RETURN ),
    HMG_PROGRAM(	"32NW", "", IpControlCommandRef.HMG_PROGRAM ),
    HMG_CLEAR(		"33NW", "", IpControlCommandRef.HMG_CLEAR ),
    HMG_REPEAT(		"34NW", "", IpControlCommandRef.HMG_REPEAT ),
    HMG_RANDOM(		"35NW", "", IpControlCommandRef.HMG_RANDOM ),
    HMG_MENU(		"36NW", "", IpControlCommandRef.HMG_MENU ),
    HMG_EDIT(		"37NW", "", IpControlCommandRef.HMG_EDIT ),
    HMG_CLASS(		"38NW", "", IpControlCommandRef.HMG_CLASS ),
    
    // display info text
    DISPLAY_INFO_QUERY("?FL", "FL", IpControlCommandRef.DISPLAY_INFO_QUERY)
    
    ;
    
    private String command; // the command string to send (may contain format syntax)
    private String response; // the expected response start. incoming responses are matched against this
	private IpControlCommandRef commandRef; // reference to the OpenHAB command configured in the binding
	
	
	private IpControlCommand(String command, String response, IpControlCommandRef commandRef) {
		this.command = command;
		this.response = response;
		this.commandRef = commandRef;
	}
	
	
	/**
	 * @return the ip control command string (example 'PO')
	 */
	public String getCommand() {
		return command;
	}
	
	/**
	 * @return the expected response (example 'PWR0')
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @return the command constant reference
	 */
	public IpControlCommandRef getCommandRef() {
		return commandRef;
	}
	
	/**
	 * @param command the command to find a matching command name for.
	 * @return the commandName that is associated with the passed command.
	 */
	public static IpControlCommand getCommandByCommandStr(String command) {
		for (IpControlCommand candidate : values()) {
			if (candidate.getCommand().equals(command)) {
				return candidate;
			}
		}
		throw new IllegalStateException("There is no matching commandName for command '" + command + "'");
	}
	
	/**
	 * @param response the response to find a matching command name for.
	 * @return the commandName that is associated with the passed command.
	 */
	public static IpControlCommand getCommandByResponseStr(String response) {
		for (IpControlCommand candidate : values()) {
			if (candidate.getResponse().equals(response)) {
				return candidate;
			}
		}
		throw new IllegalStateException("There is no matching commandName for response '" + response + "'");
	}

	/**
	 * @param commandRef the Command to find a matching command name.
	 * @return the commandName that is associated with the passed command.
	 */
	public static IpControlCommand getCommandByCommandRef(int commandRef) {
		for (IpControlCommand candidate : values()) {
			if (candidate.getCommandRef().getCommand() == commandRef) {
				return candidate;
			}
		}
		throw new IllegalStateException("There is no matching commandName for commandRef '" + commandRef + "'");
	}
	
}
