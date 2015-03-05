/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal.protocol;

import org.openhab.core.items.Item;
import org.openhab.core.types.State;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for Primare responses
 * 
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public abstract class PrimareResponse {

	private static final Logger logger = LoggerFactory.getLogger(PrimareResponse.class);

	protected byte[] message;
	
	public abstract boolean is_relevant_for(String deviceCmdString);
	public abstract State openHabState(Class<? extends Item> itemType);
	
}

