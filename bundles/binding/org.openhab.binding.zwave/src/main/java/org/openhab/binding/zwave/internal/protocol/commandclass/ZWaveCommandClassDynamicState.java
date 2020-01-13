/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.Collection;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;

/**
 * Interface that command classes can implement to implement retrieval of
 * dynamic state information..
 * For instance to support getting dynamic values from a node.
 *
 * @author Jan-Willem Spuij
 * @author Chris Jackson
 * @since 1.3.0
 */
public interface ZWaveCommandClassDynamicState {
    /**
     * Gets the dynamic state information from the node. Returns queries that fetch dynamic
     * state information. These queries need to be completed to be able to proceed to the next
     * node phase. The queries are returned so that the node can handle processing
     * to proceed to the next node phase.
     *
     * @param refresh if true will request all dynamic states even if they are already initialised
     * @return the messages with the queries for dynamic values.
     */
    public Collection<SerialMessage> getDynamicValues(boolean refresh);
}
