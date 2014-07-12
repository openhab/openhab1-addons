/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;
/**
 * Listens to query responses from the DeviceQuerier
 * 
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public interface DeviceQueryListener {
	/**
	 * Notifies the listener that a successful device query has happened
	 * @param devAddress the insteon address of the device that has been queried
	 */
	public void deviceQueryComplete(InsteonAddress devAddress);
}
