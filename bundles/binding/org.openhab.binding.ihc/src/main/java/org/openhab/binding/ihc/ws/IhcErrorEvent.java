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
package org.openhab.binding.ihc.ws;

import java.util.EventObject;

/**
 * IHC controller error event.
 *
 * @author Pauli Anttila
 * @since 1.6.0
 */
public class IhcErrorEvent extends EventObject {

    private static final long serialVersionUID = 3224923200664904390L;

    public IhcErrorEvent(Object source) {
        super(source);
    }

    /**
     * Invoked when fatal error occurred during communication to IHC controller.
     * 
     * @param e
     *            Reason for error.
     * 
     */
    public void StatusUpdateEventReceived(IhcExecption e) {
    }

}
