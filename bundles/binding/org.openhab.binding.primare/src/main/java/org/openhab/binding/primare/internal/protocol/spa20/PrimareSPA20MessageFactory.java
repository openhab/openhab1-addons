/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal.protocol.spa20;

import org.openhab.binding.primare.internal.protocol.PrimareMessageFactory;
import org.openhab.binding.primare.internal.protocol.spa20.PrimareSPA20Command;
import org.openhab.binding.primare.internal.protocol.spa20.PrimareSPA20Message;


import org.openhab.core.types.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class for Primare SP31.7/SP31/SPA20/SPA21 messages
 * This class is used for converting OpenHAB commands
 * to a byte array representation to be sent to the Primare.
 * 
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public class PrimareSPA20MessageFactory extends PrimareMessageFactory {
	
	private static final Logger logger = 
		LoggerFactory.getLogger(PrimareSPA20MessageFactory.class);
	
	
	public PrimareSPA20Message getMessage(Command command, PrimareSPA20Command deviceCmd) {
		return new PrimareSPA20Message(command, deviceCmd);
	}
    
	public PrimareSPA20Message getMessage(Command command, String deviceCmdString) {
		return getMessage(command, PrimareSPA20Command.valueOf(deviceCmdString));
	}


	// Request full status at connection initialization
	public PrimareSPA20Message[] getInitMessages() {
		return new PrimareSPA20Message[] { getMessage(null, PrimareSPA20Command.VERBOSE_ON),  // send response after update
						   getMessage(null, PrimareSPA20Command.ALL_QUERY) };
	}
	
	public PrimareSPA20Message[] getPingMessages() {
		return new PrimareSPA20Message[] { getMessage(null, PrimareSPA20Command.VERBOSE_ON), // send response after update
						   getMessage(null, PrimareSPA20Command.POWER_QUERY) };
		
	}
	
}
