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
package org.openhab.io.transport.cul;

/**
 * Listen to received events from the CUL. These events can be either received
 * data or an exception thrown while trying to read data.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public interface CULListener {

    public void dataReceived(String data);

    public void error(Exception e);

}
