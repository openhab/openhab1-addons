/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.listener;

import java.util.EventListener;

/**
 * This Interface definies a Listener for Items which wanted to be updated
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
public interface InterfaceOneWireDevicePropertyWantsUpdateListener extends EventListener {

	/**
	 * This method must be implemenented by the classes, which implements the Listener
	 * 
	 * @param wantsUpdateEvent
	 */
	public void devicePropertyWantsUpdate(OneWireDevicePropertyWantsUpdateEvent wantsUpdateEvent);

}
