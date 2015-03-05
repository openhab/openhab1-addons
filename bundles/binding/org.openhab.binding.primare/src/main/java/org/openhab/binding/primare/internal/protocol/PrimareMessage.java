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

import org.openhab.binding.primare.internal.protocol.PrimareUtils;

import org.openhab.core.types.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Base class for Primare messages
 * 
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public abstract class PrimareMessage {
	
	private static final Logger logger = LoggerFactory.getLogger(PrimareMessage.class);
    
	protected byte[] message;
    
	public byte[] raw() {
		return message;
	}
    
	public byte[] escaped() {
		return PrimareUtils.escapeMessage(message);
	}
}

