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
package org.openhab.binding.lgtv.internal;

import java.util.EventObject;

/**
 * The listener interface for receiving status updates from LGTV.
 *
 * @author Martin Fluch
 * @since 1.6.0
 */
public class LgtvStatusUpdateEvent extends EventObject {

    private static final long serialVersionUID = -2256210413245865703L;

    public LgtvStatusUpdateEvent(Object source) {
        super(source);
    }

    /**
     * Invoked when received status updates from LGTV.
     * 
     * @param data
     *            Data from receiver.
     */
    public void StatusUpdateEventReceived(String ip, String data) {
    }

}
