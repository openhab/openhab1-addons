/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lgtv.lginteraction;

/**
 * This enum lists every possible command/variable receiver.
 * 
 * @author Martin Fluch
 * @since 1.6.0
 */
public enum LgTvCommand {

	POWER("POWER", 0, 0, "1", "POWER"), N0("N0", 1, 0, "2", "Number 0"), N1("N1", 2, 0, "3", "Number 1"), N2("N2", 3,
			0, "4", "Number 2"), N3("N3", 4, 0, "5", "Number 3"), N4("N4", 5, 0, "6", "Number 4"), N5("N5", 6, 0, "7",
			"Number 5"), N6("N6", 7, 0, "8", "Number 6"), N7("N7", 8, 0, "9", "Number 7"), N8("N8", 9, 0, "10",
			"Number 8"), N9("N9", 10, 0, "11", "Number 9"),

	KEY_UP("KEY_UP", 11, 0, "12", "UP key among remote Controller’s 4 direction keys"), KEY_DOWN("KEY_DOWN", 12, 0,
			"13", "DOWN key among remote Controller’s 4 direction keys"), KEY_LEFT("KEY_LEFT", 13, 0, "14",
			"LEFT key among remote Controller’s 4 direction keys"), KEY_RIGHT("KEY_RIGHT", 14, 0, "15",
			"RIGHT key among remote Controller’s 4 direction keys"),

	KEY_OK("KEY_OK", 15, 0, "20", "OK"), KEY_HOME("KEY_HOME", 16, 0, "21", "Home menu"), KEY_MENU("KEY_MENU", 17, 0,
			"22", "Menu key (same with Home menu key)"), KEY_BACK("KEY_BACK", 18, 0, "23", "Previous key (Back)"),

	VOLUME_UP("VOLUME_UP", 19, 0, "24", "Volume up"), VOLUME_DOWN("VOLUME_DOWN", 20, 0, "25", "Volume down"), KEY_MUTE(
			"KEY_MUTE", 21, 0, "26", "Mute (toggle)"),

	CHANNEL_UP("CHANNEL_UP", 22, 0, "27", "Channel UP (+)"), CHANNEL_DOWN("CHANNEL_DOWN", 23, 0, "28",
			"Channel DOWN (-)"), KEY_BLUE("KEY_BLUE", 24, 0, "29", "Blue key of data broadcast"), KEY_GREEN(
			"KEY_GREEN", 25, 0, "30", "Green key of data broadcast"), KEY_RED("KEY_RED", 26, 0, "31",
			"Red key of data broadcast"), KEY_YELLOW("KEY_YELLOW", 27, 0, "32", "Yellow key of data broadcast"), KEY_PLAY(
			"KEY_PLAY", 28, 0, "33", "Play"), KEY_PAUSE("KEY_PAUSE", 29, 0, "34", "Pause"), KEY_STOP("KEY_STOP", 30, 0,
			"35", "Stop"), KEY_FF("KEY_FF", 31, 0, "36", "Fast forward (FF)"), KEY_REW("KEY_REW", 32, 0, "37",
			"Rewind (REW)"), KEY_SF("KEY_SF", 33, 0, "38", "Skip Forward"), KEY_SB("KEY_SB", 34, 0, "39",
			"Skip Backward"),

	KEY_RECORD("KEY_RECORD", 35, 0, "40", "Record"), KEY_RECORDLIST("KEY_RECORDLIST", 36, 0, "41", "Recording list"),

	KEY_REPEAT("KEY_REPEAT", 37, 0, "42", "Repeat"), KEY_LIVETV("KEY_LIVETV", 38, 0, "43", "Live TV"), KEY_EPG(
			"KEY_EPG", 39, 0, "44", "EPG"), KEY_CURRENTPROG("KEY_CURRENTPROG", 40, 0, "45",
			"Current program information"), KEY_ASPECT("KEY_ASPECT", 41, 0, "46", "Aspect ratio"), KEY_EXTERNALINPUT(
			"KEY_EXTERNALINPUT", 42, 0, "47", "External input"), KEY_PIP("KEY_PIP", 43, 0, "48", "PIP secondary video"), KEY_SUBTITLE(
			"KEY_SUBTITLE", 44, 0, "49", "Show / Change subtitle"), KEY_PROGRAMLIST("KEY_PROGRAMLIST", 45, 0, "50",
			"Program list"),

	KEY_TELETEXT("KEY_TELETEXT", 46, 0, "51", "Tele Text"), KEY_MARK("KEY_MARK", 47, 0, "52", "Mark"), KEY_3DVIDEO(
			"KEY_3DVIDEO", 48, 0, "400", "3D Video"), KEY_3DLR("KEY_3DLR", 49, 0, "401", "3D L/R"), KEY_DASH(
			"KEY_DASH", 50, 0, "402", "Dash (-)"), KEY_PREVCHANNEL("KEY_PREVCHANNEL", 51, 0, "403",
			"Previous channel (Flash back)"), KEY_FAVORITE("KEY_FAVORITE", 52, 0, "404", "Favorite channel"), KEY_QUICKMENU(
			"KEY_QUICKMENU", 53, 0, "405", "Quick menu"), KEY_TEXTOPTION("KEY_TEXTOPTION", 54, 0, "406", "Text Option"), KEY_AUDIODESCR(
			"KEY_AUDIODESCR", 55, 0, "407", "Audio Description"), KEY_NETCAST("KEY_NETCAST", 56, 0, "408",
			"NetCast key (same with Home menu)"),

	KEY_ENGERGYSAVE("KEY_ENGERGYSAVE", 57, 0, "409", "Energy saving"), KEY_AVMODE("KEY_AVMODE", 58, 0, "410",
			"A/V mode"), KEY_SIMPLINK("KEY_SIMPLINK", 59, 0, "411", "SIMPLINK"), KEY_EXIT("KEY_EXIT", 60, 0, "412",
			"Exit"), KEY_RESERVAT("KEY_RESERVAT", 61, 0, "413", "Reservation programs list"), PIP_CHANNEL_UP(
			"PIP_CHANNEL_UP", 62, 0, "414", "PIP channel UP"), PIP_CHANNEL_DOWN("PIP_CHANNEL_DOWN", 63, 0, "415",
			"PIP channel DOWN"), KEY_SWITCHPSEC("KEY_SWITCHPSEC", 64, 0, "416",
			"Switching between primary/secondary video"), KEY_MYAPPS("KEY_MYAPPS", 65, 0, "417", "My Apps"),

	// special commands with Commandinterpreter !=0
	REQUESTPAIRKEY("REQUESTPAIRKEY", 66, 1, "", "MF: Request Pair Key"), SENDPAIRKEY("SENDPAIRKEY", 67, 2, "",
			"MF: Send Pair Key"), CHANNEL_SET("CHANNEL_SET", 68, 3, "", "MF: Set Channel"), VOLUME_CURRENT(
			"VOLUME_CURRENT", 69, 4, "LEVEL", "MF: Get Current Volume"), VOLUME_ISMUTED("VOLUME_ISMUTED", 70, 4,
			"ISMUTED", "MF: Get Is Muted"), CHANNEL_CURRENTNUMBER("CHANNEL_CURRENTNUMBER", 71, 5, "NUMBER",
			"MF: Get Current Channel Number"), CHANNEL_CURRENTNAME("CHANNEL_CURRENTNAME", 72, 5, "NAME",
			"MF: Get Current Channel Name"), CHANNEL_CURRENTPROG("CHANNEL_CURRENTPROG", 73, 5, "PROG",
			"MF: Get Current Program Name"), GET_CHANNELS("GET_CHANNELS", 74, 6, "", "MF: Get all Channels"), GET_APPS(
			"GET_APPS", 75, 7, "", "MF: Get all Apps"), APP_EXECUTE("APP_EXECUTE", 76, 8, "", "MF: Execute Application"), APP_TERMINATE(
			"APP_TERMINATE", 77, 9, "", "MF: Terminate Application"), CONNECTION_STATUS("CONNECTION_STATUS", 78, 10,
			"", "MF: Get Binding Connection Status"), VOLUME_SET("VOLUME_SET", 79, 11, "", "MF: Set Volume"), BROWSER_URL(
			"BROWSER_URL", 80, 12, "", "MF: Set Browser Url"), ;

	private String command;
	private int commandRef;
	private int commandType;
	private String lgcmd;
	private String description;

	private LgTvCommand(String command, int commandRef, int mytype, String lgc, String descr) {
		this.command = command;
		this.commandRef = commandRef;
		this.commandType = mytype;
		this.lgcmd = lgc;
		this.description = descr;
	}

	public String getCommand() {
		return command;
	}

	public String getLgSendCommand() {
		return lgcmd;
	}

	public int getCommandType() {
		return commandType;
	}

	public int getCommandRef() {
		return commandRef;
	}

	/**
	 * @param command
	 *            the command to find a matching command name for.
	 * @return the commandName that is associated with the passed command.
	 */
	public static LgTvCommand getCommandByCommandStr(String command) {
		for (LgTvCommand candidate : values()) {
			if (candidate.getCommand().equals(command)) {
				return candidate;
			}
		}
		throw new IllegalStateException("There is no matching commandName for command '" + command + "'");
	}

	/**
	 * @param commandRef
	 *            the LgTv Command to find a matching command name.
	 * @return the commandName that is associated with the passed LgTv command.
	 */
	public static LgTvCommand getCommandByCommandRef(int commandRef) {
		for (LgTvCommand candidate : values()) {
			if (candidate.getCommandRef() == commandRef) {
				return candidate;
			}
		}
		throw new IllegalStateException("There is no matching commandName for commandRef '" + commandRef + "'");
	}

}
