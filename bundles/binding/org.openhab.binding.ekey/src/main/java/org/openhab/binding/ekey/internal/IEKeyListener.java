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
package org.openhab.binding.ekey.internal;

import at.fhooe.mc.schlgtwt.parser.UniformPacket;

/**
 * Classes that implement this interface are able to get informed
 * by the packetreceiver for new packets
 * 
 * @author Paul Schlagitweit
 * @since 1.5.0
 */
public interface IEKeyListener {

    /**
     * Inform all the interested items in this method
     * 
     * @param ekeyRecord contains all the information received by the <code>EKeyPacketReceiver</code>
     */
    public void publishUpdate(UniformPacket ekeyRecord);
}
