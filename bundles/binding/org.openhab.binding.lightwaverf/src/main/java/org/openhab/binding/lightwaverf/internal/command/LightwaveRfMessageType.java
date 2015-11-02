/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.command;

/**
 * The incoming type of a Lightwave message used to determine the
 * information needed to process the message.
 * @author Neil Renaud
 * @since 1.7.0
 */
public enum LightwaveRfMessageType {

	// A Message with a Room and Device ID
	ROOM_DEVICE, 
	// A Message with just a Room ID
	ROOM, 
	// A JSON based message that uses a serial number as an ID
	SERIAL, 
	// A Version message
	VERSION, 
	// An OK message acknowledging our message
	OK, 
	// A device registration message
	DEVICE_REGISTRATION,
	// A Request for Heat Info
	HEAT_REQUEST, 
	// A message that we don't process in the binding.
	NOT_PROCESSED;

}
