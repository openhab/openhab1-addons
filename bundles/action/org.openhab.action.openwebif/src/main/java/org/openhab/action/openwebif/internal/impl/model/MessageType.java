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
package org.openhab.action.openwebif.internal.impl.model;

/**
 * Definition of all MessageTypes.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public enum MessageType {
    WARNING,
    INFO,
    ERROR;

    /**
     * Returns the id to send to the receiver.
     */
    public String getId() {
        switch (this) {
            case WARNING:
                return "1";
            case INFO:
                return "2";
            case ERROR:
                return "3";
        }
        return null;
    }
}
