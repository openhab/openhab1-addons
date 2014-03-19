/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.Collection;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;

/**
 * Interface that command classes can implement to implement initialization.
 * For instance to support getting static values from a node, or handle dependencies
 * on other command classes.
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
public interface ZWaveCommandClassInitialization {
	/**
	 * Initializes a command class. During initialization some queries might need
	 * to be performed. These queries need to be completed to be able to proceed to the next
	 * node phase. The queries are returned so that the node can handle processing and counting
	 * to proceed to the next node phase.
	 * @return the messages with the queries for initialization.
	 */
	public Collection<SerialMessage> initialize();
}
