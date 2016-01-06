/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal.protocol;

import java.util.Arrays;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for Primare responses
 * 
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public abstract class PrimareResponse {

	private static final Logger logger = LoggerFactory.getLogger(PrimareResponse.class);

	protected byte[] message;
	
	/**
	 * Check if a response message from Primare is relevant for a Primare command.
         * Since responses arrive asynchronously
	 * 
         * @param deviceCmdString
         *              Primare command as specified in items configuration file
	 * @return boolean
	 */
	public abstract boolean isRelevantFor(String deviceCmdString);


	/**
	 * Convert received response message containing a Primare device variable value
	 * to suitable OpenHAB state for the given itemType
	 * 
	 * @param itemType
	 * 
	 * @return openHAB state
	 */
	public State openHabState(Class<? extends Item> itemType) {
		State state = UnDefType.UNDEF;
		
		try {
			int index;
			String s;
		
			if (itemType == SwitchItem.class) {
				index = (int) message[2];
				state = index == 0 ? OnOffType.OFF : OnOffType.ON;
			
			} else if (itemType == NumberItem.class) {
				index = (int) message[2];
				state = new DecimalType(index);
			
			} else if (itemType == DimmerItem.class) {
				index = (int) message[2];
				state = new PercentType(index);
				
			} else if (itemType == RollershutterItem.class) {
				index = (int) message[2];
				state = new PercentType(index);
				
			} else if (itemType == StringItem.class) {
				s = new String(Arrays.copyOfRange(message, 2, message.length-2));
				state = new StringType(s);
			}
		} catch (Exception e) {
			logger.debug("Cannot convert value '{}' to data type {}", message[1], itemType);
		}
		
		return state;
	}
	
}

