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

import java.util.EventListener;
import java.util.EventObject;

/**
 * This interface defines interface to receive status updates from LGTV
 * receiver.
 *
 * @author Martin Fluch
 * @since 1.6.0
 */
public interface LgtvEventListener extends EventListener {

    /**
     * Procedure for receive status update from LGTV.
     * 
     * @param data
     *            Received data.
     */
    void statusUpdateReceived(EventObject event, String ip, String data);

}
