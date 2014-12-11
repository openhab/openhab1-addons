/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.message;
/**
 * Interface to receive Insteon messages from the modem.
 *
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public interface MsgListener {
	/**
	 * Invoked whenever a valid message comes in from the modem
	 * @param msg the message received
	 * @param fromPort on which port (e.g. '/dev/ttyUSB0') this message arrived
	 */
	public abstract void msg(Msg msg, String fromPort);
}
