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
package org.openhab.binding.lcn.input;

import org.openhab.binding.lcn.common.LcnAddrMod;
import org.openhab.binding.lcn.connection.Connection;

/**
 * Parent class of all inputs having an LCN module as its source.
 * 
 * @author Tobias J�ttner
 */
public abstract class Mod extends Input {

    /**
     * The physical ("unchanged") source address.
     * Segment 0 is not replaced here yet.
     */
    protected final LcnAddrMod physicalSourceAddr;

    /**
     * The logical source address (with the "real" source segment id).
     * Might not be valid if the {@link Connection} is not ready yet.
     */
    protected LcnAddrMod logicalSourceAddr = new LcnAddrMod();

    /**
     * Constructor with source address.
     * 
     * @param sourceAddr the physical ("unchanged") LCN source address
     */
    protected Mod(LcnAddrMod physicalSourceAddr) {
        this.physicalSourceAddr = physicalSourceAddr;
    }

    /**
     * Gets the logical source address (segment 0 replaced with the "real" segment id).
     * Valid after {@link #process(Connection)} has been executed.
     * 
     * @return the logical source address
     */
    public LcnAddrMod getLogicalSourceAddr() {
        return this.logicalSourceAddr;
    }

    /**
     * Initializes the logical source address (if the connection is ready).
     * {@inheritDoc}
     */
    @Override
    public void process(Connection conn) {
        if (conn.isReady()) { // Skip if we don't have all necessary bus info yet
            this.logicalSourceAddr = conn.physicalToLogical(this.physicalSourceAddr);
        }
    }

}
