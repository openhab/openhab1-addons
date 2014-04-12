/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.omnilink.internal;

import java.util.LinkedList;
import java.util.List;

import org.openhab.binding.omnilink.internal.model.Area;
import org.openhab.binding.omnilink.internal.model.Thermostat;
import org.openhab.binding.omnilink.internal.model.Unit;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digitaldan.jomnilinkII.MessageUtils;
import com.digitaldan.jomnilinkII.MessageTypes.properties.UnitProperties;

/**
 * Maps item commands to omni specific command(s)
 * 
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class OmniLinkCommandMapper {

	private static final Logger logger = LoggerFactory
			.getLogger(OmniLinkCommandMapper.class);

	/**
	 * Link an item and configuration to one or more commands
	 * @param item that sends the command
	 * @param config of the item
	 * @param command list
	 * @return
	 */
	public static List<OmniLinkControllerCommand> getCommand(Item item,
			OmniLinkBindingConfig config, Command command) {
		List<OmniLinkControllerCommand> commands = new LinkedList<OmniLinkControllerCommand>();
		int cmd = 0;
		// most commands have no p1
		int param1 = 0;
		// most commands use the object number for p2, nut not all
		int param2 = config.getDevice().getProperties().getNumber();
		
		logger.debug("Trying to match command to object type "
				+ config.getObjectType());
		
		switch (config.getObjectType()) {
		case UNIT: {
			Unit unit = (Unit) config.getDevice();
			boolean resendToChildren = false;
			if (command == OnOffType.ON) {
				cmd = OmniLinkCmd.CMD_UNIT_ON.getNumber();
			} else if (command == OnOffType.OFF) {
				cmd = OmniLinkCmd.CMD_UNIT_OFF.getNumber();
			} else if (command == IncreaseDecreaseType.INCREASE) {
				cmd = OmniLinkCmd.CMD_UNIT_UPB_BRIGHTEN_STEP_1.getNumber();
				resendToChildren = true;
			} else if (command == IncreaseDecreaseType.DECREASE) {
				cmd = OmniLinkCmd.CMD_UNIT_UPB_DIM_STEP_1.getNumber();
				resendToChildren = true;
			} else if (command instanceof PercentType) {
				int level = ((PercentType) command).intValue();
				if (level == 0 || level == 100) {
					cmd = level == 0 ? OmniLinkCmd.CMD_UNIT_OFF.getNumber()
							: OmniLinkCmd.CMD_UNIT_ON.getNumber();
				} else {
					cmd = OmniLinkCmd.CMD_UNIT_PERCENT.getNumber();
					param1 = level;
					resendToChildren = true;
				}
			} else if (command instanceof StringType
					&& unit.getProperties().getUnitType() == UnitProperties.UNIT_TYPE_HLC_ROOM) {
				int roomNum = (unit.getProperties().getNumber() + 7) / 8;
				// every room has 6 links, the 3rd is where link A starts,
				// so in room 1 linkA=link3 linkB=link4 linkc=link6 linkd=link7
				String str = ((StringType) command).toString().toLowerCase();
				int linkNum = 0;
				if (str.contains("scene a"))
					linkNum = 0;
				else if (str.contains("scene b"))
					linkNum = 1;
				else if (str.contains("scene c"))
					linkNum = 2;
				else if (str.contains("scene d"))
					linkNum = 3;
				else
					break;
				param1 = 0;
				param2 = ((roomNum * 6) - 3) + linkNum;
				cmd = OmniLinkCmd.CMD_UNIT_UPB_LINK_ON.getNumber();
			}
			// if this is a room, send the command to all the other units
			if (resendToChildren
					&& unit.getProperties().getUnitType() == UnitProperties.UNIT_TYPE_HLC_ROOM) {
				for (int i = unit.getProperties().getNumber() + 1; i < unit
						.getProperties().getNumber() + 8; i++) {
					commands.add(new OmniLinkControllerCommand(cmd, param1, i));
				}
			} else {
				commands.add(new OmniLinkControllerCommand(cmd, param1, param2));
			}
		}
			break;
		case THERMO_COOL_POINT: {
			if (command == IncreaseDecreaseType.INCREASE) {
				param1 = 1;
				cmd = OmniLinkCmd.CMD_THERMO_RAISE_LOWER_COOL.getNumber();
			} else if (command == IncreaseDecreaseType.DECREASE) {
				param1 = -1;
				cmd = OmniLinkCmd.CMD_THERMO_RAISE_LOWER_COOL.getNumber();
			} else if (command instanceof DecimalType) {
				Thermostat thermo = (Thermostat) config.getDevice();
				cmd = OmniLinkCmd.CMD_THERMO_SET_COOL_POINT.getNumber();
				if (thermo.isCelsius())
					param1 = MessageUtils.CToOmni(((DecimalType) command)
							.intValue());
				else
					param1 = MessageUtils.FtoOmni(((DecimalType) command)
							.intValue());

			} else {
				logger.debug("Could not find matching command for " + command);
				break;
			}
			commands.add(new OmniLinkControllerCommand(cmd, param1, param2));

		}
			break;
		case THERMO_HEAT_POINT: {
			if (command == IncreaseDecreaseType.INCREASE) {
				param1 = 1;
				cmd = OmniLinkCmd.CMD_THERMO_RAISE_LOWER_HEAT.getNumber();
			} else if (command == IncreaseDecreaseType.DECREASE) {
				param1 = -1;
				cmd = OmniLinkCmd.CMD_THERMO_RAISE_LOWER_HEAT.getNumber();
			} else if (command instanceof DecimalType) {
				Thermostat thermo = (Thermostat) config.getDevice();
				cmd = OmniLinkCmd.CMD_THERMO_SET_HEAT_POINT.getNumber();
				if (thermo.isCelsius())
					param1 = MessageUtils.CToOmni(((DecimalType) command)
							.intValue());
				else
					param1 = MessageUtils.FtoOmni(((DecimalType) command)
							.intValue());
			} else {
				logger.debug("Could not find matching command for " + command);
				break;
			}
			commands.add(new OmniLinkControllerCommand(cmd, param1, param2));
		}
			break;
		case THERMO_SYSTEM_MODE: {
			if (command instanceof DecimalType) {
				cmd = OmniLinkCmd.CMD_THERMO_SET_SYSTEM_MODE.getNumber();
				param1 = ((DecimalType) command).intValue();
				commands.add(new OmniLinkControllerCommand(cmd, param1, param2));
			} else if (command instanceof StringType) {
				int mode = Thermostat.getModeForString(command.toString());
				if (mode >= 0) {
					cmd = OmniLinkCmd.CMD_THERMO_SET_SYSTEM_MODE.getNumber();
					param1 = mode;
					commands.add(new OmniLinkControllerCommand(cmd, param1,
							param2));
				}
			}
		}
			break;
		case THERMO_FAN_MODE: {
			if (command instanceof DecimalType) {
				cmd = OmniLinkCmd.CMD_THERMO_SET_FAN_MODE.getNumber();
				param1 = ((DecimalType) command).intValue();
				commands.add(new OmniLinkControllerCommand(cmd, param1, param2));
			}
		}
			break;
		case THERMO_HOLD_MODE: {
			if (command == OnOffType.ON) {
				param1 = 255;
			} else if (command == OnOffType.OFF) {
				param1 = 0;
			} else if (command instanceof DecimalType) {
				int val = ((DecimalType) command).intValue();
				param1 = val > 0 ? 255 : 0;
			} else {
				break;
			}
			cmd = OmniLinkCmd.CMD_THERMO_SET_HOLD_MODE.getNumber();
			commands.add(new OmniLinkControllerCommand(cmd, param1, param2));
		}
			break;
		case AUDIOZONE_POWER: {
			if (command == OnOffType.ON) {
				param1 = 1;
			} else if (command == OnOffType.OFF) {
				param1 = 0;
			} else if (command instanceof DecimalType) {
				int val = ((DecimalType) command).intValue();
				param1 = val > 0 ? 1 : 0;
			} else {
				break;
			}
			cmd = OmniLinkCmd.CMD_AUDIO_ZONE_SET_ON_MUTE.getNumber();
			commands.add(new OmniLinkControllerCommand(cmd, param1, param2));
		}
			break;
		case AUDIOZONE_MUTE: {
			if (command == OnOffType.ON) {
				param1 = 3;
			} else if (command == OnOffType.OFF) {
				param1 = 2;
			} else if (command instanceof DecimalType) {
				int val = ((DecimalType) command).intValue();
				param1 = val > 0 ? 3 : 2;
			} else {
				break;
			}
			cmd = OmniLinkCmd.CMD_AUDIO_ZONE_SET_ON_MUTE.getNumber();
			commands.add(new OmniLinkControllerCommand(cmd, param1, param2));
		}
			break;
		case AUDIOZONE_SOURCE: {
			if (command instanceof DecimalType) {
				param1 = ((DecimalType) command).intValue();
				cmd = OmniLinkCmd.CMD_AUDIO_ZONE_SET_SOURCE.getNumber();
				commands.add(new OmniLinkControllerCommand(cmd, param1, param2));

			}
		}
			break;
		case AUDIOZONE_VOLUME: {
			if (command instanceof DecimalType) {
				param1 = ((DecimalType) command).intValue();
				cmd = OmniLinkCmd.CMD_AUDIO_ZONE_SET_VOLUME.getNumber();
				commands.add(new OmniLinkControllerCommand(cmd, param1, param2));
			}
		}
			break;
		case AUDIOZONE_KEY: {
			if (command instanceof DecimalType) {
				param1 = ((DecimalType) command).intValue();
				cmd = OmniLinkCmd.CMD_AUDIO_ZONE_SELECT_KEY.getNumber();
				commands.add(new OmniLinkControllerCommand(cmd, param1, param2));
			}
		}
			break;
		case AREA_STATUS_MODE: {
			if (command instanceof StringType) {
				// this needs to go in the area class
				// param1 = ((DecimalType)command).intValue();
				Area area = (Area) config.getDevice();
				int mode = area.getModeForString(command.toString());
				if (mode >= 0) {
					cmd = OmniLinkCmd.CMD_SECURITY_OMNI_DISARM.getNumber()
							+ mode;
					// TODO:Get the code number from the binding, or better yet
					// get the code and validate it with the connection
					param1 = 1;
					commands.add(new OmniLinkControllerCommand(cmd, param1,
							param2));
				}
			}
		}
			break;
		case BUTTON: {
			if (command instanceof StringType) {
				// if anything continue
				// if(command.toString() != null &&
				// command.toString().length() > 0){
				cmd = OmniLinkCmd.CMD_BUTTON.getNumber();
				commands.add(new OmniLinkControllerCommand(cmd, param1, param2));
				// }
			}
		}
			break;
		default:
			break;
		}
		// return new OmniLinkControllerCommand(cmd, param1, param2);
		return commands;
	}

}
