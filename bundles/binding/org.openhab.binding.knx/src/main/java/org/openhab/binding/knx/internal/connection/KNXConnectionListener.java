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
package org.openhab.binding.knx.internal.connection;

/**
 * This interface should be implemented by classes that need to be informed when
 * a KNX connection has been established or was lost.
 *
 * @author Andrea Giacosi
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public interface KNXConnectionListener {

    /**
     * The callback method that is used to notify listeners about a successful KNX connection
     */
    void connectionEstablished();

    /**
     * The callback method that is used to notify listeners about a lost KNX connection
     */
    void connectionLost();
}
