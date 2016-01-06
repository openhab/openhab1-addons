/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal.communicator;

import org.openhab.binding.rwesmarthome.internal.RWESmarthomeContext;


/**
 * StateHolder to hold the RWE Smarthome context.
 * 
 * @author ollie-dev
 *
 */
public class StateHolder {
	private RWESmarthomeContext context;
	
	/**
	 * Constructor with context.
	 * 
	 * @param context
	 */
	public StateHolder(RWESmarthomeContext context) {
		this.context = context;
	}

	/**
	 * Destroys the cache.
	 */
	public void destroy() {

	}
}
