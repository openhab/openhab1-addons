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
package org.openhab.binding.sonos;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.sonos.internal.Direction;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Type;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Karel Goderis
 * @since 1.1.0
 */

public enum SonosCommandType {


	PLAY {
		{
			command = "play";
			service = "AVTransport";
			action = "Play";
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.BIDIRECTIONAL;
		}
	},
	PAUSE {
		{
			command = "pause";
			service = "AVTransport";
			action = "Pause";
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.BIDIRECTIONAL;
		}
	},

	STOP {
		{
			command = "stop";
			service = "AVTransport";
			action = "Stop";
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.BIDIRECTIONAL;
		}
	},
	NEXT {
		{
			command = "next";
			service = "AVTransport";
			action = "Next";
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.BIDIRECTIONAL;
		}
	},
	PREVIOUS {
		{
			command = "previous";
			service = "AVTransport";
			action = "Previous";
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.BIDIRECTIONAL;
		}
	},

	SETLED {
		{
			command = "led";
			service = "DeviceProperties";
			action = "SetLEDState";
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.OUT;
		}
	},
	
	GETLED {
		{
			command = "led";
			service = "DeviceProperties";
			action = "GetLEDState";
			variable = "CurrentLEDState";
			typeClass = OnOffType.class;
			direction = Direction.IN;
			polling = true;
		}
	},

	ZONENAME {
		{
			command = "zonename";
			service = "DeviceProperties";
			action = null;
			variable = "ZoneName";
			typeClass = StringType.class;
			direction = Direction.IN;
		}
	},
	
	ZONEINFO {
		{
			command = null;
			service = "DeviceProperties";
			action = null;
			variable = null;
			typeClass = StringType.class;
			direction = Direction.IN;
			polling = true;
		}
	},
	
	ZONEGROUP {
		{
			command = "zonegroup";
			service = "ZoneGroupTopology";
			action = null;
			variable = "ZoneGroupState";
			typeClass = StringType.class;
			direction = Direction.IN;
		}
	},
	
	GROUPUUID {
		{
			command = "zonegroupid";
			service = "GroupManagement";
			action = null;
			variable = "LocalGroupUUID";
			typeClass = StringType.class;
			direction = Direction.IN;
		}
	},

	
	// TODO : GroupManagement, AddMember does not seem to work correctly
	ADDMEMBER {
		{
			command = "add";
			//service = "GroupManagement";
			//action = "AddMember";
			service="AVTransport";
			action="SetAVTransportURI";
			variable = null;
			typeClass = StringType.class;
			direction = Direction.OUT;
		}
	},
	
	// TODO: GroupManagement, RemoveMember does not seem to work properly.
	REMOVEMEMBER {
		{
			command = "remove";
			//service = "GroupManagement";
			//action = "RemoveMember";
			service = null;
			action = null;
			variable = null;
			typeClass = StringType.class;
			direction = Direction.OUT;
		}
	},
	

	
	COORDINATORLOCAL {
		{
			command = "localcoordinator";
			service = "GroupManagement";
			action = null;
			variable = "GroupCoordinatorIsLocal";
			typeClass = OnOffType.class;
			direction = Direction.IN;
		}
	},
	
	BECOMESTANDALONEGROUP {
		{
			command = "standalone";
			service = "AVTransport";
			action = "BecomeCoordinatorOfStandaloneGroup";
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.OUT;
		}
	},
	
	
//	// Not implemented yet
//	BECOMEZONEGROUPCOORDINATOR {
//		{
//			command = "coordinator";
//			service = "AVTransport";
//			action = "BecomeGroupCoordinator";
//			variable = null;
//			typeClass = OnOffType.class;
//			direction = Direction.OUT;
//		}
//	},
	
	
	// Not implemented yet
	CHANGECOORDINATOR {
		{
			command = "coordinator";
			service = "AVTransport";
			action = "ChangeCoordinator";
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.OUT;
		}
	},
	
	// Not implemented yet
	BECOMEZONEGROUPSOURCE {
		{
			command = "source";
			service = "AVTransport";
			action = "BecomeGroupCoordinatorAndSource";
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.OUT;
		}
	},

	// Not implemented yet
	SETURI {
		{
			command = "playURI";
			service = "AVTransport";
			action = "SetAVTransportURI";
			variable = null;
			typeClass = StringType.class;
			direction = Direction.OUT;
		}
	},

	GETVOLUME {
		{
			command = "volume";
			service = "RenderingControl";
			action = null;
			variable = "VolumeMaster";
			typeClass = DecimalType.class;
			direction = Direction.IN;
		}
	},
	
	SETVOLUME {
		{
			command = "volume";
			service = "RenderingControl";
			action = "SetVolume";
			variable = null;
			typeClass = DecimalType.class;
			direction = Direction.OUT;
		}
	},
	
	GETMUTE {
		{
			command = "mute";
			service = "RenderingControl";
			action = null;
			variable = "MuteMaster";
			typeClass = OnOffType.class;
			direction = Direction.IN;	
		}
	},
	
	SETMUTE {
		{
			command = "mute";
			service = "RenderingControl";
			action = "SetMute";
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.OUT;
		}
	},

	TRANSPORTSTATE {
		{
			command = "transportstate";
			service = "AVTransport";
			action = null;
			variable = "TransportState";
			typeClass = StringType.class;
			direction = Direction.IN;
		}

	},
	
	PA {
		{
			// public address, e.g. bring all Sonos in one group, and stream audio from the line-in from the player that received the ON command
			command = "publicaddress";
			service = null;
			action = null;
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.OUT;
		}

	},
	
	// Not yet implemented
	STARTGROUPTRANSMISSION {
		{
			command = "transmit";
			service = "AudioIn";
			action = "StartTransmissionToGroup";
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.OUT;
		}

	},
	
	// Not yet implemented
	STOPGROUPTRANSMISSION {
		{
			command = "transmit";
			service = "AudioIn";
			action = "StopTransmissionToGroup";
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.OUT;
		}

	},
	
	LINEINCONNECTED {
		{
			command = "linein";
			service = "AudioIn";
			action = null;
			variable = "LineInConnected";
			typeClass = OnOffType.class;
			direction = Direction.IN;
		}

	},
	
	BROWSE {
		{
			command = null;
			service = "ContentDirectory";
			action = "Browse";
			variable = null;
			typeClass = StringType.class;
			direction = Direction.OUT;
		}

	},
	
	RADIO {
		{
			command = "radio";
			service = null;
			action = null;
			variable = null;
			typeClass = StringType.class;
			direction = Direction.OUT;
		}

	},
	
	SAVE {
		{
			command = "save";
			service = null;
			action = null;
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.OUT;
		}

	},
	
	RESTORE {
		{
			command = "restore";
			service = null;
			action = null;
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.OUT;
		}

	},

	
	SETALARM {
		{
			command = "alarm";
			service = null;
			action = null;
			variable = null;
			typeClass = OnOffType.class;
			direction = Direction.OUT;
		}
	},

	ALARMRUNNING {
		{
			command = "alarmrunning";
			service = "AVTransport";
			action = "";
			variable = "AlarmRunning";
			typeClass = OnOffType.class;
			direction = Direction.IN;
		}
	},
	
	SNOOZE {
		{
			command = "snooze";
			service = "AVTransport";
			action = "SnoozeAlarm";
			variable = null;
			typeClass = DecimalType.class;
			direction = Direction.OUT;
		}
	},

	
	RUNNINGALARMPROPERTIES {
		{
			command = "alarmproperties";
			service = "AVTransport";
			action = null;
			variable = "RunningAlarmProperties";
			typeClass = StringType.class;
			direction = Direction.IN;
			polling = true;
		}
	},
	
	MEDIAINFO {
		{
			command = null;
			service = "AVTransport";
			action = null;
			variable = null;
			typeClass = StringType.class;
			direction = Direction.IN;
			polling = true;
		}
	},
	
	CURRENTTRACK {
		{
			command = "currenttrack";
			service = "AVTransport";
			action = null;
			variable = "CurrentURIFormatted";
			typeClass = StringType.class;
			direction = Direction.IN;
			polling = true;

		}	
	
	};		


	/** Represents the Sonos command as it will be used in *.items configuration */
	// openhab command associated with this upnp cpmmand, e.g. used in items
	String command;
	// name of upnp service template 
	String service;
	// name of upnp action/command, must be defined in service template. put null for complex commands that go beyond simple assynchronous execution
	// WARNING: NOT USED ANYMORE BUT I KEPT IT IN HERE TO MAKE THE CODE / UPNP MORE READABLE
	String action;
	// if action == null, then variable indicates the name of the GENA variable we need to process/use
	String variable;
	// type of the item supported by this command
	Class<? extends Type> typeClass;
	// direction of the openhab command, eg IN, OUT or BIDIRECTIONAL. that we will accept in conjunction with this command
	Direction direction;
	// true if a variable need to be polled pro-actively, e.g. values are not returned as part of a GENA subscription
	boolean polling = false;
	
	
	
	public String getSonosCommand() {
		return command;
	}

	public String getService() {
		return service;
	}

	public String getAction() {
		return action;
	}

	public String getVariable() {
		return variable;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * @return the polling
	 */
	public boolean isPolling() {
		return polling;
	}

	public Class<? extends Type> getTypeClass() {
		return typeClass;
	}

	/**
	 * 
	 * @param SonosCommand command string e.g. message, volume, channel
	 * @param action class to validate
	 * @return true if item class can bound to SonosCommand
	 */

	public static boolean validateBinding(SonosCommandType type, Item item) {
		if(type !=null && item != null && item.getAcceptedDataTypes().contains(type.getTypeClass())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param sonosCommand command string e.g. message, volume, channel
	 * @return simple name of all valid item classes
	 */
	
	public static String getValidItemTypes(String sonosCommand) {
		String ret = "";
		for (SonosCommandType c : SonosCommandType.values()) {
			if (sonosCommand.equals(c.getSonosCommand()) && c.getSonosCommand() != null) {
				if (StringUtils.isEmpty(ret)) {
					ret = c.getTypeClass().getSimpleName();
				} else {
					if (!ret.contains(c.getTypeClass().getSimpleName())) {
						ret = ret + ", " + c.getTypeClass().getSimpleName();
					}
				}
			}
		}
		return ret;
	}


	public static List<SonosCommandType> getSubscriptions(){
		List<SonosCommandType> result = new ArrayList<SonosCommandType>();
		for(SonosCommandType c: SonosCommandType.values()){
			if(c.getVariable() != null && c.getSonosCommand() != null && c.isPolling() == false){
				result.add(c);
			}
		}
		return result;
	}
	
	public static List<SonosCommandType> getPolling(){
		List<SonosCommandType> result = new ArrayList<SonosCommandType>();
		for(SonosCommandType c: SonosCommandType.values()){
			if(c.isPolling()) {
//			if(c.getVariable() != null && c.getSonosCommand() != null && c.isPolling()){
				result.add(c);
			}
		}
		return result;
	}
	
	public static SonosCommandType getCommandType(String sonosCommand, Direction direction) throws SonosIllegalCommandTypeException {

		if ("".equals(sonosCommand)) {
			return null;
		}

		for (SonosCommandType c : SonosCommandType.values()) {

			if (sonosCommand.equals(c.getSonosCommand()) && c.getDirection().equals(direction)) {
				return c;
			}
		}

		throw new SonosIllegalCommandTypeException("Cannot find sonosCommandType for '"
				+ sonosCommand + "' with direction '"+direction.toString()+"'");
	}

	public static List<SonosCommandType> getCommandByVariable(
			String stateVariable) {
		List<SonosCommandType> result = new ArrayList<SonosCommandType>();
		for(SonosCommandType c: SonosCommandType.values()){
			if(c.getVariable() != null && c.getVariable().equals(stateVariable)){
				result.add(c);
			}
		}
		return result;
	}
	
}