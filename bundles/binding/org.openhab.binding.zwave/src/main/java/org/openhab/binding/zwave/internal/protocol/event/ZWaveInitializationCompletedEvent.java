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
package org.openhab.binding.zwave.internal.protocol.event;

/**
 * ZWave Network Initialization Completed event.
 * Indicates that the ZWaveController has completed
 * the initialization phase and is ready to start
 * accepting commands and receiving events.
 *
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class ZWaveInitializationCompletedEvent extends ZWaveEvent {

    /**
     * Constructor. Creates a new instance of the ZWaveTransactionCompletedEvent
     * class.
     *
     * @param nodeId the nodeId of the event. Must be set to the controller node.
     */
    public ZWaveInitializationCompletedEvent(int nodeId) {
        super(nodeId);
    }

}
