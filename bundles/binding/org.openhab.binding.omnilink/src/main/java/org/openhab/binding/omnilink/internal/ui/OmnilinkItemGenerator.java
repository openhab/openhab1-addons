/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.omnilink.internal.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.digitaldan.jomnilinkII.Connection;
import com.digitaldan.jomnilinkII.Message;
import com.digitaldan.jomnilinkII.OmniInvalidResponseException;
import com.digitaldan.jomnilinkII.OmniNotConnectedException;
import com.digitaldan.jomnilinkII.OmniUnknownMessageTypeException;
import com.digitaldan.jomnilinkII.MessageTypes.ObjectProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AreaProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AudioSourceProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AudioZoneProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.ButtonProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.ThermostatProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.UnitProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.ZoneProperties;

/**
 * THIS CLASS IS MESSY!
 * 
 * I created this class to auto create my items for me. It may be useful
 * to others, see OmnilinkBinding where this gets called and printed
 * to the log.
 * @author daniel
 *
 */
public class OmnilinkItemGenerator {
	Connection c;
	StringBuilder groups;
	StringBuilder items;
	HashMap<String, LinkedList<SiteItem>> rooms;
	LinkedList<SiteItem> lights;
	LinkedList<SiteItem> thermos;
	LinkedList<SiteItem> audioZones;
	LinkedList<SiteItem> audioSources;
	LinkedList<SiteItem> areas;
	LinkedList<SiteItem> zones;
	LinkedList<SiteItem> buttons;
	ArrayList<String>existingGroups;


	public static final String[] AUDIOCMD_HAIHIFI = {"Power","Source Step",
		"Vol Up", "Vol Down", "Mute"};

	public static final String[] AUDIOCMD_RUSSOUND = { "Power", "Source step",
		"Vol up ", "Vol down", "Mute", "Play", "Stop", "Pause", "Minus",
		"Plus", "Previous", "Next ", "Record", "Channel up",
		"Channel down", "Zero", "One", "Two", "Three", "Four", "Five",
		"Six", "Seven", "Eight", "Nine", "Plus ten", "Enter", "Last",
		"Sleep", "Guide", "Exit", "Info", "Menu", "Menu up", "Menu right",
		"Menu down", "Menu left", "Select", "Favorite 1", "Favorite 2" };

	public static final String[] AUDIOCMD_NUVO = { "Power", "Source step",
		"Vol up", "Vol down", "Mute", "Play", "Stop", "Pause", "Rewind",
		"Forward", "Fast rewind", "Fast forward", "Continuous", "Shuffle",
		"Group", "Disc", "Zero", "One", "Two", "Three", "Four", "Five",
		"Six", "Seven", "Eight", "Nine", "Plus ten", "Enter",
		"Hotkey zero", "Hotkey one", "Hotkey two", "Hotkey three",
		"Hotkey four", "Hotkey five", "Hotkey six", "Hotkey seven",
		"Hotkey eight", "Hotkey nine"

	};

	public static final String[] AUDIOCMD_NUVOGRAND = { "Power", "Source step",
		"Vol up", "Vol down", "Mute", "Play / Pause", "Stop (not used)",
		"Pause (not used)", "Previous", "Next", "Favorite 1", "Favorite 2",
		"Favorite 3", "Favorite 4", "Favorite 5", "Favorite 6",
		"Favorite 7", "Favorite 8", "Favorite 9", "Favorite 10",
		"Favorite 11", "Favorite 12", "Ok button down", "Ok button up",
		"Play / Pause button down", "Play / Pause button up",
		"Previous button down", "Previous button up", "Next button down",
		"Next button up", "Power / Mute button down",
		"Power / Mute button up", "Menu button down", "Menu button up",
		"Up button down", "Up button up", "Down button down",
		"Down button up"

	};

	public static final String[] AUDIOCMD_XANTECH = { "Power",
		"Source select 1", "Source select 2", "Source select 3",
		"Source select 4", "Source select 5", "Source select 6",
		"Source select 7", "Source select 8", "Channel up", "Channel down",
		"Mute", "Play", "Stop", "Pause", "Rewind", "Forward", "Vol up",
		"Vol down", "Tier 2 power", "Tier 2 source select 1",
		"Tier 2 source select 2", "Tier 2 source select 3",
		"Tier 2 source select 4", "Tier 2 source select 5",
		"Tier 2 source select 6", "Tier 2 source select 7",
		"Tier 2 source select 8", "Tier 2 channel up",
		"Tier 2 channel down", "Tier 2 mute", "Tier 2 play", "Tier 2 stop",
		"Tier 2 pause", "Tier 2 rewind", "Tier 2 forward"

	};

	public static final String[] AUDIOCMD_SPEAKEERCRAFT = { "Source select 1",
		"Source select 2", "Source select 3", "Source select 4",
		"Source select 5", "Source select 6", "Source select 7",
		"Source select 8", null, "Mute", "Vol Up", "Power", "Vol Down",
		null, null, null, "One", "Two", "Three", "Four", "Five", "Six",
		"Seven", "Eight", "Nine", "Track", "Zero", "Disc", "Random",
		"Repeat", "Bass", "Treble", "Guide", "Menu", "Up", "Left",
		"Select", "Right", "Down", "Escape", "Info", "Rewind", "Forward",
		"Pause", "Play", "Stop"

	};

	public static final String IMG_LIGHT="omni_light";
	public static final String IMG_SECURITY="omni_security";
	public static final String IMG_THERMOS="omni_thermostat";
	public static final String IMG_AUDIO="omni_music";
	public static final String IMG_ZONE="omni_zone";
	public static final String IMG_BUTTON="omni_button";


	/**
	 * set the connection object to use
	 * @param c
	 */
	public OmnilinkItemGenerator(Connection c){
		this.c = c;
	}

	/**
	 * This generates a item and sitemap configuration.
	 * @return
	 * @throws IOException
	 * @throws OmniNotConnectedException
	 * @throws OmniInvalidResponseException
	 * @throws OmniUnknownMessageTypeException
	 */
	public String generateItemsAndGroups() throws IOException, OmniNotConnectedException, OmniInvalidResponseException, OmniUnknownMessageTypeException{
		groups = new StringBuilder();
		items = new StringBuilder();
		rooms = new LinkedHashMap<String, LinkedList<SiteItem>>();
		lights = new LinkedList<SiteItem>();
		thermos = new LinkedList<SiteItem>();
		audioZones = new LinkedList<SiteItem>();
		audioSources = new LinkedList<SiteItem>();
		areas = new LinkedList<SiteItem>();
		zones = new LinkedList<SiteItem>();
		buttons = new LinkedList<SiteItem>();

		existingGroups = new ArrayList<String>();

		groups.append("Group All\n");

		generateUnits();
		items.append("\n");
		generateThermos();
		items.append("\n");
		generateAudioZones();
		items.append("\n");
		generateAudioSource();
		items.append("\n");
		generateAreas();
		items.append("\n");
		generateZones();
		items.append("\n");
		generateButtons();
		items.append("\n");
		generateSiteMap();

		return groups.append("\n\n").append(items).toString();
	}

	/**
	 * This is by far the most complex method as units have the ability to be
	 * sub-grouped into rooms.  If units are in a room then they will be added to
	 * their own group which is a member of the Lights group.  
	 * @throws IOException
	 * @throws OmniNotConnectedException
	 * @throws OmniInvalidResponseException
	 * @throws OmniUnknownMessageTypeException
	 */
	private void generateUnits() throws IOException, OmniNotConnectedException, OmniInvalidResponseException, OmniUnknownMessageTypeException{

		//Group	Lights_GreatRoom	"Great Room"	(Lights)
		String groupString = "Group\t%s\t\"%s\"\t(%s)\n";

		//Dimmer	Lights_GreatRoom_MainLights_Switch	"Main Lights [%d%%]"	(Lights_GreatRoom)	{omnilink="unit:10"}
		String itemString = "%s\t%s\t\"%s\"\t(%s)\t{omnilink=\"unit:%d\"}\n";

		String groupName = "Lights";

		//Group	Lights	"Lights"	(All)
		groups.append(String.format(groupString,groupName,"Lights","All"));

		int objnum = 0;
		Message m;
		int currentRoom = 0;
		String currentRoomName = null;

		while ((m = c.reqObjectProperties(Message.OBJ_TYPE_UNIT, objnum, 1, ObjectProperties.FILTER_1_NAMED,
				ObjectProperties.FILTER_2_AREA_ALL, ObjectProperties.FILTER_3_ANY_LOAD)).getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
			UnitProperties o = ((UnitProperties) m);
			objnum = o.getNumber();

			boolean isInRoom = false;
			boolean isRoomController = false;
			if(o.getUnitType() == UnitProperties.UNIT_TYPE_HLC_ROOM || 
					o.getObjectType() == UnitProperties.UNIT_TYPE_VIZIARF_ROOM){
				currentRoom = objnum;

				//Lights_LivingRoom
				currentRoomName = cleanString(groupName + "_" + o.getName());

				//Make Sure we don't already have a group called this
				currentRoomName = addUniqueGroup(currentRoomName);

				groups.append(String.format(groupString,currentRoomName,o.getName(),groupName));
				rooms.put(currentRoomName, new LinkedList<SiteItem>());
				isInRoom = true;
				isRoomController = true;
			} else if(objnum < currentRoom + 8){
				isInRoom = true;
			}

			//clean the name to remove things like spaces
			String objName = cleanString(o.getName());

			String group = isInRoom ? currentRoomName : groupName;

			//name will be the room name for the first device and roomName_deviceName for sub devices
			String name = isRoomController ? objName : group + "_" + objName;

			//the label does not have to be cleaned, so set it from the object
			String label = o.getName() + " [%d%%]";

			SiteItem light  = new SiteItem(name, o.getName(), label);

			items.append(String.format(itemString,"Dimmer",name + "_Switch",label,group,objnum));


			if(isRoomController)
				items.append(String.format(itemString,"String",name + "_String",o.getName() + " [%s]",group,objnum));

			if(isInRoom)
				rooms.get(currentRoomName).add(light);
			else
				lights.add(light);

		}
	}

	/**
	 * Generates thermostat items
	 * @throws IOException
	 * @throws OmniNotConnectedException
	 * @throws OmniInvalidResponseException
	 * @throws OmniUnknownMessageTypeException
	 */
	private void generateThermos() throws IOException, OmniNotConnectedException, OmniInvalidResponseException, OmniUnknownMessageTypeException{

		String groupString = "Group\t%s\t\"%s\"\t(%s)\n";
		String itemString = "%s\t%s\t\"%s\"\t(%s)\t{omnilink=\"%s:%d\"}\n";
		String groupName = "Thermostats";
		groups.append(String.format(groupString,groupName,"Thermostats","All"));
		int objnum = 0;
		Message m;

		while ((m = c.reqObjectProperties(Message.OBJ_TYPE_THERMO, objnum, 1, ObjectProperties.FILTER_1_NAMED,
				ObjectProperties.FILTER_2_AREA_ALL, ObjectProperties.FILTER_3_ANY_LOAD)).getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
			ThermostatProperties o = ((ThermostatProperties) m);
			objnum = o.getNumber();
			String objName = cleanString(o.getName());
			String group = addUniqueGroup(groupName + "_" + objName);

			groups.append(String.format(groupString,group,o.getName(),"OmniThermostat"));

			thermos.add(new SiteItem(group, o.getName(), o.getName()));

			items.append(String.format(itemString,"Number", group + "_Temp","Temperature [%d °F]",group,"thermo_temp",objnum));
			items.append(String.format(itemString,"Number", group + "_CoolPoint","Cool Point [%d°F]",group,"thermo_cool_point",objnum));
			items.append(String.format(itemString,"Number", group + "_HeatPoint","Heat Point [%d°F]",group,"thermo_heat_point",objnum));
			items.append(String.format(itemString,"Number", group + "_System","System Mode [%d]",group,"thermo_system_mode",objnum));
			items.append(String.format(itemString,"Number", group + "_Fan","System Fan [%d]",group,"thermo_fan_mode",objnum));
			items.append(String.format(itemString,"Number", group + "_Hold","System Hold [%d]",group,"thermo_hold_mode",objnum));
		}
	}

	/**
	 * Generates audio zone items
	 * @throws IOException
	 * @throws OmniNotConnectedException
	 * @throws OmniInvalidResponseException
	 * @throws OmniUnknownMessageTypeException
	 */
	private void generateAudioZones() throws IOException, OmniNotConnectedException, OmniInvalidResponseException, OmniUnknownMessageTypeException{

		String groupString = "Group\t%s\t\"%s\"\t(%s)\n";
		String itemString = "%s\t%s\t\"%s\"\t(%s)\t{omnilink=\"%s:%d\"}\n";
		String itemStringKey = "%s\t%s\t\"%s\"\t(%s)\t{omnilink=\"%s:%d\",autoupdate=\"false\"}\n";
		String groupName = "AudioZones";
		groups.append(String.format(groupString,groupName,"Audio Zones","All"));
		int objnum = 0;
		Message m;

		while ((m = c.reqObjectProperties(Message.OBJ_TYPE_AUDIO_ZONE, objnum, 1, ObjectProperties.FILTER_1_NAMED,
				ObjectProperties.FILTER_2_NONE, ObjectProperties.FILTER_3_NONE)).getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
			AudioZoneProperties o = ((AudioZoneProperties) m);
			objnum = o.getNumber();

			String group = addUniqueGroup(groupName + "_" + cleanString(o.getName()));

			groups.append(String.format(groupString,group,o.getName(),groupName));
			//String name = group + "_" + cleanString(o.getName());

			audioZones.add(new SiteItem(group, o.getName(), o.getName()));

			items.append(String.format(itemString,"Switch", group + "_Power","Power",group,"audiozone_power",objnum));
			items.append(String.format(itemString,"Switch", group + "_Mute","Mute",group,"audiozone_mute",objnum));
			items.append(String.format(itemString,"Number", group + "_Source","Source: [%d]",group,"audiozone_source",objnum));
			items.append(String.format(itemString,"Dimmer", group + "_Volume","Voulme: [%d %%]",group,"audiozone_volume",objnum));
			items.append(String.format(itemString,"String", group + "_Text","Now Playing: [%s]",group,"audiozone_text",objnum));
			items.append(String.format(itemString,"String", group + "_Field1","Field 1 [%s]",group,"audiozone_field1",objnum));
			items.append(String.format(itemString,"String", group + "_Field2","Field 2 [%s]",group,"audiozone_field2",objnum));
			items.append(String.format(itemString,"String", group + "_Field3","Field 3 [%s]",group,"audiozone_field3",objnum));
			items.append(String.format(itemStringKey,"Number", group + "_Key","Key [%d]",group,"audiozone_key",objnum));
		}
	}
	
	/**
	 * Generates audio source items
	 * @throws IOException
	 * @throws OmniNotConnectedException
	 * @throws OmniInvalidResponseException
	 * @throws OmniUnknownMessageTypeException
	 */
	private void generateAudioSource() throws IOException, OmniNotConnectedException, OmniInvalidResponseException, OmniUnknownMessageTypeException{

		String groupString = "Group\t%s\t\"%s\"\t(%s)\n";
		String itemString = "%s\t%s\t\"%s\"\t(%s)\t{omnilink=\"%s:%d\"}\n";
		String groupName = "AudioSources";
		groups.append(String.format(groupString,groupName,"Audio Sources","All"));
		int objnum = 0;
		Message m;

		while ((m = c.reqObjectProperties(Message.OBJ_TYPE_AUDIO_SOURCE, objnum, 1, ObjectProperties.FILTER_1_NAMED,
				ObjectProperties.FILTER_2_NONE, ObjectProperties.FILTER_3_NONE)).getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
			AudioSourceProperties o = ((AudioSourceProperties) m);
			objnum = o.getNumber();

			String group = addUniqueGroup(groupName + "_" + cleanString(o.getName()));

			groups.append(String.format(groupString,group,o.getName(),"OmniAudioSources"));
			//String name = group + "_" + cleanString(o.getName());

			audioSources.add(new SiteItem(group, o.getName(), o.getName()));

			items.append(String.format(itemString,"String", group + "_Text","Now Playeing: [%s]",group,"audiosource_text",objnum));
			items.append(String.format(itemString,"String", group + "_Field1","Field 1 [%s]",group,"audiosource_field1",objnum));
			items.append(String.format(itemString,"String", group + "_Field2","Field 2 [%s]",group,"audiosource_field2",objnum));
			items.append(String.format(itemString,"String", group + "_Field3","Field 3 [%s]",group,"audiosource_field3",objnum));
		}
	}

	/**
	 * Generates button items
	 * @throws IOException
	 * @throws OmniNotConnectedException
	 * @throws OmniInvalidResponseException
	 * @throws OmniUnknownMessageTypeException
	 */
	private void generateButtons() throws IOException, OmniNotConnectedException, OmniInvalidResponseException, OmniUnknownMessageTypeException{

		String groupString = "Group\t%s\t\"%s\"\t(%s)\n";
		String itemString = "%s\t%s\t\"%s\"\t(%s)\t{omnilink=\"%s:%d\",autoupdate=\"false\"}\n";
		String groupName = "Buttons";
		groups.append(String.format(groupString,groupName,"Buttons","All"));

		int objnum = 0;
		Message m;

		while ((m =  c.reqObjectProperties(Message.OBJ_TYPE_BUTTON, objnum, 1, ObjectProperties.FILTER_1_NAMED,
				ObjectProperties.FILTER_2_AREA_ALL, ObjectProperties.FILTER_3_NONE)).getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
			ButtonProperties o = ((ButtonProperties) m);
			objnum = o.getNumber();

			//String group = addUniqueGroup(groupName + "_" + cleanString(o.getName()));

			//groups.append(String.format(groupString,group,o.getName(),groupName));
			String name = groupName + "_" + cleanString(o.getName());

			buttons.add(new SiteItem(name, o.getName(), o.getName()));

			items.append(String.format(itemString,"String", name,o.getName(),groupName,"button",objnum));
		}
	}

	/**
	 * Generates zone items
	 * @throws IOException
	 * @throws OmniNotConnectedException
	 * @throws OmniInvalidResponseException
	 * @throws OmniUnknownMessageTypeException
	 */
	private void generateZones() throws IOException, OmniNotConnectedException, OmniInvalidResponseException, OmniUnknownMessageTypeException{

		String groupString = "Group\t%s\t\"%s\"\t(%s)\n";
		String itemString = "%s\t%s\t\"%s\"\t(%s)\t{omnilink=\"%s:%d\"}\n";
		String groupName = "Zones";
		groups.append(String.format(groupString,groupName,"Zones","All"));

		int objnum = 0;
		Message m;

		while ((m =  c.reqObjectProperties(Message.OBJ_TYPE_ZONE, objnum, 1, ObjectProperties.FILTER_1_NAMED,
				ObjectProperties.FILTER_2_AREA_ALL, ObjectProperties.FILTER_3_ANY_LOAD)).getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
			ZoneProperties o = ((ZoneProperties) m);
			objnum = o.getNumber();

			String group = addUniqueGroup(groupName + "_" + cleanString(o.getName()));

			groups.append(String.format(groupString,group,o.getName(),groupName));
			//String name = group + "_" + cleanString(o.getName());

			zones.add(new SiteItem(group, o.getName(), o.getName()));

			items.append(String.format(itemString,"Contact", group + "_Current","Current: [%s]",group,"zone_status_current",objnum));
			items.append(String.format(itemString,"String", group + "_Latched","Latched [%s]",group,"zone_status_latched",objnum));
			items.append(String.format(itemString,"String", group + "_Arming","Arming [%s]",group,"zone_status_arming",objnum));
			items.append(String.format(itemString,"String", group + "_All","Status  [%s]",group,"zone_status_all",objnum));
		}
	}

	/**
	 * Generates are items
	 * @throws IOException
	 * @throws OmniNotConnectedException
	 * @throws OmniInvalidResponseException
	 * @throws OmniUnknownMessageTypeException
	 */
	private void generateAreas() throws IOException, OmniNotConnectedException, OmniInvalidResponseException, OmniUnknownMessageTypeException{

		String groupString = "Group\t%s\t\"%s\"\t(%s)\n";
		String itemString = "%s\t%s\t\"%s\"\t(%s)\t{omnilink=\"%s:%d\"}\n";
		String groupName = "Areas";
		groups.append(String.format(groupString,groupName,"Areas","All"));

		int objnum = 0;
		Message m;

		while ((m = c.reqObjectProperties(Message.OBJ_TYPE_AREA, objnum, 1, ObjectProperties.FILTER_1_NAMED,
				ObjectProperties.FILTER_2_NONE, ObjectProperties.FILTER_3_NONE)).getMessageType() == Message.MESG_TYPE_OBJ_PROP) {
			AreaProperties o = ((AreaProperties) m);
			objnum = o.getNumber();

			String group = addUniqueGroup(groupName + "_" + cleanString(o.getName()));

			groups.append(String.format(groupString,group,o.getName(),groupName));
			//String name = group + "_" + cleanString(o.getName());

			areas.add(new SiteItem(group, o.getName(), o.getName()));

			items.append(String.format(itemString,"Number", group + "_ExitDelay", "Exit Delay: [%d]",group,"area_status_exit_delay",objnum));
			items.append(String.format(itemString,"Number", group + "_EntryDelay", "Entry Delay: [%d]",group,"area_status_entry_delay",objnum));
			items.append(String.format(itemString,"Number", group + "_ExitTimer", "Exit Timer: [%d]",group,"area_status_exit_timer",objnum));
			items.append(String.format(itemString,"Number", group + "_EntryTimer", "Entry Timer: [%d]",group,"area_status_entry_timer",objnum));
			items.append(String.format(itemString,"String", group + "_Mode", "Mode: [%s]",group,"area_status_mode",objnum));
			items.append(String.format(itemString,"String", group + "_Alarm", "Alarm: [%s]",group,"area_status_alarm",objnum));

		}
	}

	/**
	 * Generates a crude sitemap from our generated items.
	 * @throws IOException
	 * @throws OmniNotConnectedException
	 * @throws OmniInvalidResponseException
	 * @throws OmniUnknownMessageTypeException
	 */
	private void generateSiteMap() throws IOException, OmniNotConnectedException, OmniInvalidResponseException, OmniUnknownMessageTypeException{
		StringBuilder output = new StringBuilder();
		//begin sitemap
		output.append("sitemap home label=\"Main Menu\"{\n");
		output.append("Frame {\n\n");
		//begin lights
		output.append("Text label=\"Lights\"icon=\"" + IMG_LIGHT + "\" {\n");
		output.append("Frame {\n");

		for(String room : rooms.keySet()){
			LinkedList<SiteItem> units = rooms.get(room);
			SiteItem fLight = units.removeFirst();

			output.append(String.format("Text label=\"%s [%%s]\" item=%s_String icon=\"%s\" valuecolor=[==Off=\"white\",!=Off=\"green\"]{\n", fLight.getObjName(),fLight.getName(),IMG_LIGHT));
			output.append(String.format("Frame item=%s_Switch label=\"%s [%%s%%%%]\"{\n", fLight.getName(), fLight.getObjName()));
			output.append(String.format("Switch item=%s_Switch label=\"Power\" icon=\"\" mappings=[\"0\"=Off, \"100\"=On]\n", fLight.getName()));
			output.append(String.format("Slider item=%s_Switch label=\"Dimmer\" icon=\"\"\n", fLight.getName()));
			output.append(String.format("Switch item=%s_String label=\"Scene\" icon=\"\" mappings=[\"Scene A\"=A, \"Scene B\"=B, \"Scene C\"=C, \"Scene D\"=D]\n", fLight.getName()));

			output.append("}\nFrame label=\"All Lights\"{\n");
			for(SiteItem l : units){
				output.append(String.format("Slider item=%s_Switch switchSupport\n", l.getName()));
			}
			output.append("}\n}\n");

		}
		if(lights.size() > 0){
			output.append("Text label=\"Other\" {\n");
			output.append("Frame{\n");
			for(SiteItem l : lights){
				output.append(String.format("Slider item=%s_Switch switchSupport\n", l.getName()));
			}
			output.append("}\n}\n");
		}
		//end lights
		output.append("}\n}\n");

		//begin thermos
		if(thermos.size() > 0){
			output.append("Text label=\"Thermostats\"icon=\"" +  IMG_THERMOS + "\" {\n");
			output.append("Frame {\n");

			for(SiteItem thermo : thermos){
				output.append(String.format("Text label=\"%s  [%%d°F]\" item=%s_Temp icon=\"%s\" {\n", thermo.getObjName(),thermo.getName(),IMG_THERMOS));
				output.append(String.format("Frame item=%s_Temp label=\"%s [%%d°F]\"{\n", thermo.getName(), thermo.getObjName()));

				output.append(String.format("Setpoint item=%s_CoolPoint minValue=32 maxValue=100 step=1\n icon=\"%s-cool\"", thermo.getName(),IMG_THERMOS));
				output.append(String.format("Setpoint item=%s_HeatPoint minValue=32 maxValue=100 step=1\n icon=\"%s-heat\"", thermo.getName(),IMG_THERMOS));
				output.append(String.format("Switch item=%s_System label=\"Mode\" icon=\"%s\" mappings=[\"0\"=Off, \"1\"=Heat, \"2\"=Cool,\"3\"=Auto,\"4\"=Emer]\n", thermo.getName(), IMG_THERMOS));
				output.append(String.format("Switch item=%s_Fan label=\"Fan\" mappings=[\"0\"=Auto, \"1\"=On]\n", thermo.getName()));
				output.append(String.format("Switch item=%s_Hold label=\"Hold\" mappings=[\"0\"=Off, \"1\"=On]\n", thermo.getName()));
				output.append("}\n}\n");

			}
			//end thermos
			output.append("}\n}\n");

		}
		//begin audioZones
		if(audioZones.size() > 0){
			output.append("Text label=\"Audio\" icon=\"" + IMG_AUDIO + "\"{\n");	
			output.append("Frame {\n");
			for(SiteItem audio : audioZones){
				output.append(String.format("Text label=\"%s  [%%s]\" item=%s_Power icon=\"%s\" valuecolor=[==OFF=\"white\",!=OFF=\"green\"]{\n", audio.getObjName(),audio.getName(),IMG_AUDIO));
				output.append(String.format("Frame item=%s_Volume label=\"%s  [%%s%%%%]\"{\n", audio.getName(), audio.getObjName()));
				output.append(String.format("Switch item=%s_Power icon=\"\" label=\"Power\"\n", audio.getName()));
				output.append(String.format("Switch item=%s_Mute  icon=\"\" label=\"Mute\"\n", audio.getName()));
				output.append(String.format("Switch item=%s_Source icon=\"\" label=\"Source\" mappings=[\"1\"=XM, \"2\"=iPod, \"3\"=Tivo,\"4\"=Sqz1,\"5\"=Air,\"6\"=Sqz2]\n", audio.getName()));
				output.append(String.format("Slider item=%s_Volume icon=\"\" label=\"Volume\"\n", audio.getName()));

				int [] features = c.reqSystemFeatures().getFeatures();
				String [] audioCmd = null;
				for (int i = 0; i < features.length; i++) {
					switch (features[i]) {
					case 1: // Nuvo Concerto
					case 2: // Nuvo Essentials/Simplese
						audioCmd = AUDIOCMD_NUVO;
						break;
					case 3: // Nuvo Grand
						audioCmd = AUDIOCMD_NUVOGRAND;
						break;
					case 4: // Russound
						audioCmd = AUDIOCMD_RUSSOUND;
						break;
					case 5: // HAI Hi-Fi
						audioCmd = AUDIOCMD_HAIHIFI;
						break;
					case 6: // XANTECH
						audioCmd = AUDIOCMD_XANTECH;
						;
						break;
					case 7: // SpeakerCraft
						audioCmd = AUDIOCMD_SPEAKEERCRAFT;
						break;
					default:
						break;
					}
					if (audioCmd != null)
						break;
				}


				//we should do something smarter here

				//for russound starting at index 5 + 1
				//"Play", "Stop", "Pause", "Minus" Plus", "Previous", "Next ",
				if(audioCmd.length > 12)
					output.append(String.format("Switch item=%s_Key label=\"\" mappings=[\"6\"=\">\", \"7\"=\"[ ]\", \"8\"=\"||\",\"9\"=\"-\",\"10\"=\"+\",\"11\"=\"<<\",\"12\"=\">>\"]\n", audio.getName()));

				output.append(String.format("Text item=%s_Text label=\"[%%s]\"\n", audio.getName()));
				output.append(String.format("Text item=%s_Field1 label=\"[%%s]\"\n", audio.getName()));
				output.append(String.format("Text item=%s_Field2 label=\"[%%s]\"\n", audio.getName()));
				output.append(String.format("Text item=%s_Field3 label=\"[%%s]\"\n", audio.getName()));
				output.append("}\n}\n");

			}

			//end audioZones
			output.append("}\n}\n");


			//begin zones
			if(zones.size() > 0){

				output.append("Text label=\"Zones\" icon=\"" + IMG_SECURITY + "\" {\n");
				output.append("Frame {\n");
				for(SiteItem zone : zones){
					output.append(String.format("Text label=\"%s\" item=%s_Current{\n", zone.getObjName(),zone.getName()));
					output.append(String.format("Frame label=\"%s\"{\n", zone.getObjName()));
					output.append(String.format("Switch item=%s_Arming label=\"\" icon=\"\" mappings=[\"bypass\"=Bypass, \"restore\"=Restore]\n", zone.getName()));
					output.append(String.format("Text item=%s_Current icon=\"\" label=\"Current: [%%s]\"\n", zone.getName(), zone.getObjName()));
					output.append(String.format("Text item=%s_Latched label=\"Latched: [%%s]\"", zone.getName(), zone.getObjName()));
					output.append(String.format("Text item=%s_Arming label=\"Arming: [%%s]\"\n", zone.getName(), zone.getObjName()));
					output.append("}\n}\n");
				}
				//end zones
				output.append("}\n}\n");
			}

			//begin buttons
			if(buttons.size() > 0){
				output.append("Text label=\"Buttons\" icon=\"" + IMG_BUTTON + "\"{\n");
				output.append("Frame {\n");

				for(SiteItem button : buttons){
					output.append(String.format("Switch item=%s label=\"%s\" icon=\"\" mappings=[\"push\"=\"Push\"]\n", button.getName(), button.getObjName()));
				}
				//end buttons
				output.append("}\n}\n");
			}

		}
		//end sitemap
		output.append("\n\n}\n}\n");
		System.out.println(output.toString());
	}


	private String addUniqueGroup(String name ){
		//dont allow duplicate group names
		int i = 0;
		String tmpName = name;
		while(existingGroups.contains(tmpName))
			tmpName = name + (i++);
		existingGroups.add(tmpName);
		return tmpName;
	}



	protected static String cleanString(String string){
		return string.replaceAll("[^A-Za-z0-9_-]", "");
	}

	private class SiteItem {
		private String name;
		private String objName;
		private String label;

		public SiteItem(String name, String objName, String label) {
			super();
			this.name = name;
			this.objName = objName;
			this.label = label;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getObjName() {
			return objName;
		}
		public void setObjName(String objName) {
			this.objName = objName;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}

	}

}
