/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.davis.datatypes;

import org.openhab.binding.davis.internal.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents all valid datafields which could be processed by this binding
 * 
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public enum DavisCommandType {

	LOOP {
		{
			command = "LOOP 1";
			responsetype = Constants.RESPONSE_TYPE_ACK;
			responselimitertype = Constants.RESPONSE_LIMITER_TYPE_FIXED_SIZE;
			responselength = 99;
			crcchecktype = Constants.CRC_CHECK_TYPE_VAR1;
		}
	},

	LOOP2 {
		{
			command = "LPS 2 1";
			responsetype = Constants.RESPONSE_TYPE_ACK;
			responselimitertype = Constants.RESPONSE_LIMITER_TYPE_FIXED_SIZE;
			responselength = 99;
			crcchecktype = Constants.CRC_CHECK_TYPE_VAR1;
		}
	},
	
	GETTIME {
		{
			command = "GETTIME";
			responsetype = Constants.RESPONSE_TYPE_ACK;
			responselimitertype = Constants.RESPONSE_LIMITER_TYPE_FIXED_SIZE;
			responselength = 8;
			crcchecktype = Constants.CRC_CHECK_TYPE_VAR1;
		}
	},
	
	NVER {
		{
			command = "NVER";
			responsetype = Constants.RESPONSE_TYPE_OK;
			responselimitertype = Constants.RESPONSE_LIMITER_TYPE_CRLF;
			responselength = 0;
			crcchecktype = Constants.CRC_CHECK_TYPE_NONE;
		}
	},
	
	RECEIVERS {
		{
			command = "RECEIVERS";
			responsetype = Constants.RESPONSE_TYPE_OK;
			responselimitertype = Constants.RESPONSE_LIMITER_TYPE_FIXED_SIZE;
			responselength = 1;
			crcchecktype = Constants.CRC_CHECK_TYPE_NONE;
		}
	},
	
	RXCHECK {
		{
			command = "RXCHECK";
			responsetype = Constants.RESPONSE_TYPE_OK;
			responselimitertype = Constants.RESPONSE_LIMITER_TYPE_CRLF;
			responselength = 0;
			crcchecktype = Constants.CRC_CHECK_TYPE_NONE;
		}
	},
	
	VER {
		{
			command = "VER";
			responsetype = Constants.RESPONSE_TYPE_OK;
			responselimitertype = Constants.RESPONSE_LIMITER_TYPE_CRLF;
			responselength = 0;
			crcchecktype = Constants.CRC_CHECK_TYPE_NONE;
		}
	},
	
	WRD {
		{
			command = new String(new byte[]{'W', 'R', 'D', 0x12, 0x4d});
			responsetype = Constants.RESPONSE_TYPE_ACK;
			responselimitertype = Constants.RESPONSE_LIMITER_TYPE_FIXED_SIZE;
			responselength = 1;
			crcchecktype = Constants.CRC_CHECK_TYPE_NONE;
		}
	},
	
	BARDATA {
		{
			command = "BARDATA";
			responsetype = Constants.RESPONSE_TYPE_OK;
			responselimitertype = Constants.RESPONSE_LIMITER_TYPE_MULTIPLE_CRLF;
			responselength = 9;
			crcchecktype = Constants.CRC_CHECK_TYPE_NONE;
		}
	};

	
	Logger logger = LoggerFactory.getLogger(DavisCommandType.class);
	
	
	String command;
	int responsetype;
	int responselimitertype;
	int responselength;
	int crcchecktype;
	
	/**
	 * @return command key
	 */
	public String getCommand() {
		return command;
	}
	
	
	/**
	 * @return the responsetype
	 */
	public int getResponsetype() {
		return responsetype;
	}



	/**
	 * @return the responselimitertype
	 */
	public int getResponselimitertype() {
		return responselimitertype;
	}



	/**
	 * @return the responselength
	 */
	public int getResponselength() {
		return responselength;
	}



	/**
	 * @return the crcchecktype
	 */
	public int getCrcchecktype() {
		return crcchecktype;
	}



	/**
	 * Get a specific command.
	 * 
	 * @param command
	 *            command key
	 * @return DavisCommandType identified by command
	 */
	public static DavisCommandType getCommandTypeByCommand(String command) {
		for (DavisCommandType entry : values()) {
			if (entry.command.equals(command)) {
				return entry;
			}
		}
		return null;
	}
	
}
