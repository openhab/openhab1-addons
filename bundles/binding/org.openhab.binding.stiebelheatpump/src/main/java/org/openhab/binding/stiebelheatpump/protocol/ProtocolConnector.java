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
package org.openhab.binding.stiebelheatpump.protocol;

import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;

public interface ProtocolConnector {

    void connect();

    void disconnect();

    byte get() throws StiebelHeatPumpException;

    short getShort() throws StiebelHeatPumpException;

    void get(byte abyte0[]) throws StiebelHeatPumpException;

    void mark();

    void reset();

    void write(byte abyte0[]) throws StiebelHeatPumpException;

    void write(byte byte0) throws StiebelHeatPumpException;

}
