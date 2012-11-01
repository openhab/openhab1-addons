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
package org.openhab.binding.plugwise;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.plugwise.internal.Stick;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.*;
import org.openhab.core.library.items.*;
import org.openhab.core.types.Type;
import org.quartz.Job;

/**
 * An Enum that defines the commands that can be use in binding configuration, providing info on what kind of Item this 
 * command be used, as well as the type of value it should return to the OH return time, and finally also the Job.class
 * that should be scheduled by Quartz - a Job.class that will poll the hardware for Updates
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public enum PlugwiseCommandType {

	/** The currentpower. */
	CURRENTPOWER {
		{
			command = "power";
			itemClass = NumberItem.class;
			typeClass = DecimalType.class;
			jobClass = Stick.PowerInformationJob.class;
		}
	},
	
	/** The currentpowerstamp. */
	CURRENTPOWERSTAMP {
		{
			command = "power-stamp";
			itemClass = DateTimeItem.class;
			typeClass = DateTimeType.class;
			jobClass = Stick.PowerInformationJob.class;
		}
	},
	
	/** The lasthourconsumption. */
	LASTHOURCONSUMPTION {
		{
			command = "lasthour";
			itemClass = NumberItem.class;
			typeClass = DecimalType.class;
			jobClass = Stick.PowerBufferJob.class;

		}
	},
	
	/** The lasthourconsumptionstamp. */
	LASTHOURCONSUMPTIONSTAMP {
		{
			command = "lasthour-stamp";
			itemClass = DateTimeItem.class;
			typeClass = DateTimeType.class;
			jobClass = Stick.PowerBufferJob.class;

		}
	},
	
	CURRENTCLOCK {
		{
			command = "clock";
			itemClass = StringItem.class;
			typeClass = StringType.class;
			jobClass = Stick.ClockJob.class;

		}
	},
	
	REALTIMECLOCK {
		{
			command = "realtime-clock";
			itemClass = DateTimeItem.class;
			typeClass = DateTimeType.class;
			jobClass = Stick.RealTimeClockJob.class;

		}
	},
		
	/** The currentstate. */
	CURRENTSTATE {
		{
			command = "state";
			itemClass = SwitchItem.class;
			typeClass = OnOffType.class;
			jobClass = Stick.InformationJob.class;
		}
	
	};		


	// Represents the Plugwise command as it will be used in *.items configuration
	String command;

	// class of the item supported by this command
	Class<? extends Item> itemClass;

	// type of the item supported by this command
	Class<? extends Type> typeClass;
	
	// class of the Job that will fetch the value(s) for this command. 
	// A single poll/query could yield values/updates for different command types
	Class<? extends Job> jobClass;
	
	
	/**
	 * Gets the plugwise command.
	 *
	 * @return the plugwise command
	 */
	public String getPlugwiseCommand() {
		return command;
	}
	
	/**
	 * Gets the item class.
	 *
	 * @return the item class
	 */
	public Class<? extends Item> getItemClass() {
		return itemClass;
	}

	/**
	 * Gets the type class.
	 *
	 * @return the type class
	 */
	public Class<? extends Type> getTypeClass() {
		return typeClass;
	}

	/**
	 * Gets the job class.
	 *
	 * @return the job class
	 */
	public Class<? extends Job> getJobClass() {
		return jobClass;
	}
	
	/**
	 * Validate binding.
	 *
	 * @param PlugwiseCommand command string e.g. message, volume, channel
	 * @param itemClass the item class
	 * @return true if item class can bound to PlugwiseCommand
	 */

	public static boolean validateBinding(String PlugwiseCommand, Class<? extends Item> itemClass) {
		boolean ret = false;
		for (PlugwiseCommandType c : PlugwiseCommandType.values()) {
			if (PlugwiseCommand.equals(c.getPlugwiseCommand())
					&& c.getItemClass().equals(itemClass)  && c.getPlugwiseCommand() != null) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * Gets the valid item types.
	 *
	 * @param PlugwiseCommand command string e.g. state,...
	 * @return simple name of all valid item classes
	 */
	
	public static String getValidItemTypes(String PlugwiseCommand) {
		String ret = "";
		for (PlugwiseCommandType c : PlugwiseCommandType.values()) {
			if (PlugwiseCommand.equals(c.getPlugwiseCommand()) && c.getPlugwiseCommand() != null) {
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

	
	/**
	 * Gets the command type.
	 *
	 * @param PlugwiseCommand the plugwise command
	 * @return the command type
	 */
	public static PlugwiseCommandType getCommandType(String PlugwiseCommand) {

		if ("".equals(PlugwiseCommand)) {
			return null;
		}

		for (PlugwiseCommandType c : PlugwiseCommandType.values()) {

			if (PlugwiseCommand.equals(c.getPlugwiseCommand())) {
				return c;
			}
		}

		throw new IllegalArgumentException("cannot find PlugwiseCommandType for '"
				+ PlugwiseCommand + "'");
	}

	
}
