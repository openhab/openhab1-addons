/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal.protocol.spa20;

import org.openhab.binding.primare.internal.protocol.PrimareResponseFactory;
import org.openhab.binding.primare.internal.protocol.spa20.PrimareSPA20Response;

import org.openhab.core.types.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Class for creating Primare SP31.7/SP31/SPA20/SPA21 responses
 * from data received from the Primare device
 * 
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public class PrimareSPA20ResponseFactory extends PrimareResponseFactory {
    
	private static final Logger logger = 
		LoggerFactory.getLogger(PrimareSPA20ResponseFactory.class);
	
	public PrimareSPA20Response getResponse(byte[] message) {
		return new PrimareSPA20Response(message);
	}

}
