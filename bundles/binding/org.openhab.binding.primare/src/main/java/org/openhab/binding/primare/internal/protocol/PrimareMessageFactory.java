/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal.protocol;

import org.openhab.binding.primare.internal.protocol.PrimareMessage;

import org.openhab.core.types.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Abstract base class for Primare messages to be extended by model-specific
 * implementation classes.
 * The class is used for converting OpenHAB commands
 * to a byte array representation to be sent to the Primare.
 * 
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public abstract class PrimareMessageFactory {
    
	private static final Logger logger = 
		LoggerFactory.getLogger(PrimareMessageFactory.class);
    
	public abstract PrimareMessage getMessage(Command command, String deviceCmdString);
    

	/**
	 * Get messages for initializing the device
	 * 
	 * @return messages for device initialization
	 */
	public abstract PrimareMessage[] getInitMessages();

	
	/**
	 * Get messages for pinging the device
	 * 
	 * @return messages for device ping
	 */
	public abstract PrimareMessage[] getPingMessages();    
	
}
