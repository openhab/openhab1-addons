/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.packet;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class SmartAckCode {

    /**
     * This code is not defined and used internally to signal an unset one.
     */
    public static final byte SA_UNDEF = (byte) 0;

    /**
     * Set / reset Smart Ack learn mode.
     */
    public static final byte SA_WR_LEARNMODE = (byte) 1;

    /**
     * Get Smart Ack learn mode state.
     */
    public static final byte SA_RD_LEARNMODE = (byte) 2;

    /**
     * Used for Smart Ack to add or delete a mailbox of a client.
     */
    public static final byte SA_WR_LEARNCONFIRM = (byte) 3;

    /**
     * Send Smart Ack learn request (client).
     */
    public static final byte SA_WR_CLIENTLEARNRQ = (byte) 4;

    /**
     * Send reset command to a Smart Ack client.
     */
    public static final byte SA_WR_RESETS = (byte) 5;

    /**
     * Get Smart Ack learned sensors / mailboxes.
     */
    public static final byte SA_RD_LEARNEDCLIENTS = (byte) 6;

    /**
     * Set number of reclaim attempts.
     */
    public static final byte SA_WR_RECLAIMS = (byte) 7;

    /**
     * Activate / deactivate post master functionality.
     */
    public static final byte SA_WR_POSTMASTER = (byte) 8;

    private SmartAckCode() {
    }

}
