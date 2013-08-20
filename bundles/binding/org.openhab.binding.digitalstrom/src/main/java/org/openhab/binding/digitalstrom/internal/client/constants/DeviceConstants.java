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
package org.openhab.binding.digitalstrom.internal.client.constants;

/**
 * @author 	Alexander Betker
 * @since	1.3.0
 * @version	digitalSTROM-API 1.14.5
 */
public interface DeviceConstants {
	
	/** digitalSTROM dim step for lights (this value is not in percent!) */
	public final static short DIMM_STEP_LIGHT	= 11;
	
	/** move step for roller shutters (this value is not in percent!) */
	public final static short MOVE_STEP_ROLLERSHUTTER = 11;
	
	/** move step for slats (this value is not in percent!) */
	public final static short MOVE_STEP_SLAT	= 11;
	
	/** default move step (this value is not in percent!)*/
	public final static short DEFAULT_MOVE_STEP = 11;
	
	/** */
	public final static short DEFAULT_MAX_OUTPUTVALUE = 255;
	
	/**  max output value if device (lamp -> yellow) is on */
	public final static short MAX_OUTPUT_VALUE_LIGHT = 255;
	
	/** is max open (special case: awning/marquee -> closed */
	public final static short MAX_ROLLERSHUTTER = 255;
	
	/** closed (special case: awning/marquee -> open */
	public final static short MIN_ROLLERSHUTTER = 0;
	
	public final static short MAX_SLAT_POSITION = 255;
	
	public final static short MIN_SLAT_POSITION = 0;
	
	/** you can't dim deeper than this value */
	public final static short MIN_DIMM_VALUE = 16;
	
	/** this is the index to get the outputvalue (min-, max value) of almost all devices */
	public final static short DEVICE_SENSOR_OUTPUT = 0;
	
	/** this index is needed to get the position of the slats (if device is a jalousie) */
	public final static short DEVICE_SENSOR_SLAT_OUTPUT = 4;

}
