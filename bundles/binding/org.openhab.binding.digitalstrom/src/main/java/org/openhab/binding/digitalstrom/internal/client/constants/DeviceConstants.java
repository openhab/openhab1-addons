/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
