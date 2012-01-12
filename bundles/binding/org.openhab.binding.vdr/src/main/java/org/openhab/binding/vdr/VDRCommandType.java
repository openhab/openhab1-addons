/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.vdr;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;

/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Wolfgang Willinghoefer
 * @since 0.9.0
 */
public enum VDRCommandType {

	POWEROFF {
		{
			command = "powerOff";
			type = OnOffType.OFF;
			itemClass = SwitchItem.class;
		}
	},

	POWERON {
		{
			command = "powerOff";
			type = OnOffType.ON;
			itemClass = SwitchItem.class;
		}
	},

	VOLUME_UP {
		{
			command = "volume";
			type = OnOffType.ON;
			itemClass = SwitchItem.class;
		}
	},

	VOLUME_DOWN {
		{
			command = "volume";
			type = OnOffType.OFF;
			itemClass = SwitchItem.class;
		}
	},

	VOLUME {
		{
			command = "volume";
			type = null;
			itemClass = NumberItem.class;
		}
	},

	CHANNEL_UP {
		{
			command = "channel";
			type = OnOffType.ON;
			itemClass = SwitchItem.class;
		}
	},

	CHANNEL_DOWN {
		{
			command = "channel";
			type = OnOffType.OFF;
			itemClass = SwitchItem.class;
		}
	},

	CHANNEL {
		{
			command = "channel";
			type = null;
			itemClass = NumberItem.class;
		}
	},

	MESSAGE {
		{
			command = "message";
			type = null;
			itemClass = StringItem.class;
		}
	},

	RECORDING {
		{
			command = "recording";
			type = null;
			itemClass = SwitchItem.class;
		}
	};

	/** Represents the vdr command as it will be used in *.items configuration */
	String command;
	Command type;
	Class<? extends Item> itemClass;

	public String getVDRCommand() {
		return command;
	}

	public Command getCommandType() {
		return type;
	}

	public Class<? extends Item> getItemClass() {
		return itemClass;
	}

	/**
	 * 
	 * @param vdrCommand command string e.g. message, volume, channel
	 * @param itemClass class to validate
	 * @return true if item class can bound to vdrCommand
	 */

	public static boolean validateBinding(String vdrCommand, Class<? extends Item> itemClass) {
		boolean ret = false;
		for (VDRCommandType c : VDRCommandType.values()) {
			if (c.getVDRCommand().equals(vdrCommand)
					&& c.getItemClass().equals(itemClass)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param vdrCommand command string e.g. message, volume, channel
	 * @return simple name of all valid item classes
	 */
	public static String getValidItemTypes(String vdrCommand) {
		String ret = "";
		for (VDRCommandType c : VDRCommandType.values()) {
			if (c.getVDRCommand().equals(vdrCommand)) {
				if (StringUtils.isEmpty(ret)) {
					ret = c.getItemClass().getSimpleName();
				} else {
					if (!ret.contains(c.itemClass.getSimpleName())) {
						ret = ret + ", " + c.getItemClass().getSimpleName();
					}
				}
			}
		}
		return ret;
	}

	public static VDRCommandType create(String vdrCommand, Command command) {

		if ("".equals(vdrCommand)) {
			return null;
		}

		for (VDRCommandType c : VDRCommandType.values()) {

			if (c.getVDRCommand().equals(vdrCommand)
					&& ((c.getCommandType() == null) || (c.getCommandType() != null && c
							.getCommandType().equals(command)))) {
				return c;
			}
		}

		throw new IllegalArgumentException("cannot find playerCommand for '"
				+ vdrCommand + "'");
	}
}