/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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