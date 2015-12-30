/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.utils;

import java.math.BigDecimal;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility  class for openhab states.
 * 
 * @author Christian Sowada
 * @since 1.7.0
 */
public class StateUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(StateUtils.class);
	
	/**
	 * Converts an openhab command to a native java datatype
	 * @param command
	 * @return
	 */
	public static Object convertFromState(Command command) {
		
		if(command == OnOffType.ON) {
			return Boolean.TRUE;
			
		} else if(command == OnOffType.OFF) {
			return Boolean.FALSE;

		} else if(command instanceof DecimalType) {
			return ((DecimalType)command).toBigDecimal();

		}
		
		throw new RuntimeException("Sorry, data type " + 
		command.toString() + " not supported ...");
	}
	
	/**
	 * Converts a value to a openhab state
	 * @param value A value to converrt
	 * @return A converted state or null
	 */
	public static State convertToState(Object value) {

		if(value instanceof BigDecimal) {
			return new DecimalType((BigDecimal)value);
			
		} else if(value instanceof Boolean) {
			return (Boolean)value ? OnOffType.ON : OnOffType.OFF;
			
		} else if(value instanceof String) {
			return  new StringType((String)value);
			
		} else if(value == null) {
			return null;
			
		} else {
			logger.error("Unknown data type!");
			return null;
		}
	}
	
}
