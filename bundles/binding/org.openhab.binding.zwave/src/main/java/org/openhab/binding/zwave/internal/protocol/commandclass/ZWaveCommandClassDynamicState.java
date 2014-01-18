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
 * Interface that command classes can implement to implement retrieval of
 * dynamic state information..
 * For instance to support getting dynamic values from a node.
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
public interface ZWaveCommandClassDynamicState {
	/**
	 * Gets the dynamic state information from the node. Returns queries that fetch dynamic
	 * state information. These queries need to be completed to be able to proceed to the next
	 * node phase. The queries are returned so that the node can handle processing and counting
	 * to proceed to the next node phase.
	 * @return the messages with the queries for dynamic values.
	 */
	public Collection<SerialMessage> getDynamicValues();
}
