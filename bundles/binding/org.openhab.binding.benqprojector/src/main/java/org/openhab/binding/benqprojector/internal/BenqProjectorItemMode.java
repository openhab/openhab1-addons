/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.benqprojector.internal;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/**
 * Define item modes with the ability to generate relevant
 * strings for sending to the projector for querying etc
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public enum BenqProjectorItemMode {
		
	POWER("pow", ResponseType.ON_OFF),
	MUTE("mute", ResponseType.ON_OFF),
	VOLUME("vol", ResponseType.NUMBER),
	LAMP_HOURS("ltim", ResponseType.NUMBER),
	SOURCE_STRING("sour", ResponseType.STRING),
	SOURCE_NUMBER("sour", ResponseType.SOURCE_MAPPING);
	
	public enum ResponseType {
		ON_OFF,
		NUMBER, 
		STRING,
		SOURCE_MAPPING;
	}
	
	private final String command;
	private final ResponseType responseType;
	
	private BenqProjectorItemMode(String command, ResponseType rType)
	{
		this.command = command;
		this.responseType = rType;
	}
	
	public String getItemModeCommandQueryString()
	{
		return getItemModeCommandSetString("?");
	}
	
	public String getItemModeCommandSetString(String value)
	{
		return "\r*"+this.command+"="+value+"#\r";
	}
	
	/**
	 * Parse ON/OFF query responses
	 * @param response
	 * @return On or Off state. Undefined if invalid.
	 */
	private State parseOnOffResponse(String response)
	{
		if (response.contains("OFF"))
		{
			return OnOffType.OFF;
		}
		if (response.contains("ON"))
		{
			return OnOffType.ON;
		}
		return UnDefType.UNDEF;
	}
	
	private State parseNumberResponse(String response)
	{		
		String[] responseParts = response.split("=");
		if (responseParts.length == 2)
		{
			return new DecimalType( Integer.parseInt(responseParts[1].substring(0, responseParts[1].length()-1)));
		}
		return UnDefType.UNDEF; 
	}
	
	private State parseStringResponse(String response)
	{		
		String[] responseParts = response.split("=");
		if (responseParts.length == 2)
		{
			return new StringType(responseParts[1].substring(0, responseParts[1].length()-1));
		}
		return UnDefType.UNDEF; 
	}
	
	public State parseResponse(String response)
	{
		switch (this.responseType)
		{
		case SOURCE_MAPPING:
			State s = parseStringResponse(response);
			if (s instanceof StringType)
			{
				StringType strT = (StringType)s;
				return new DecimalType(BenqProjectorSourceMapping.getMappingFromString(strT.toString()));
			} else return s;
		case NUMBER:
			return parseNumberResponse(response);
		case ON_OFF:
			return parseOnOffResponse(response);
		case STRING:
			return parseStringResponse(response);
		}
		return UnDefType.UNDEF;
	}
}
