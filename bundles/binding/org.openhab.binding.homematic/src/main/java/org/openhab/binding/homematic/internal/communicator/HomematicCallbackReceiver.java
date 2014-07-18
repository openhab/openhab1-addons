/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator;

/**
 * CallbackReceiver defines those methods invoked by a Homematic server.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public interface HomematicCallbackReceiver {

	/**
	 * Called when the Homematic server is sending a multicall message.
	 */
	public void event(String interfaceId, String address, String parameterKey, Object value);

	/**
	 * Called when the Homematic server detects a new device.
	 */
	public void newDevices(String interfaceId, Object[] deviceDescriptions);

}
