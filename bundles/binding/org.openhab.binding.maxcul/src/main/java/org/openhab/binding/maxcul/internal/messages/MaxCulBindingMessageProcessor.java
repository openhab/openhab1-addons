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
package org.openhab.binding.maxcul.internal.messages;

/**
 * Interface for MaxCul binding message processors
 *
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public interface MaxCulBindingMessageProcessor {

    /**
     * Process filtered CUL message in MAX! mode
     * 
     * @param data
     *            Raw data of packet
     * @param broadcast
     *            True if a broadcast packet or is snooped and has a valid dest
     *            addr, false if is addressed to us
     */
    void maxCulMsgReceived(String data, boolean broadcast);
}
