/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;

/**
 * Interface to implement for all command classes that implement the SET
 * commands like SET value.
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
public interface ZWaveSetCommands {
	/**
	 * Gets a SerialMessage with the SET command 
	 * @param value the value to set.
	 * @return the serial message
	 */
	public SerialMessage setValueMessage(int value) ;
}
