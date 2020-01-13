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
package org.openhab.io.transport.cul.internal;

import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.CULHandler;

/**
 * Internal interface for the CULManager. CULHandler should always implement this.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public interface CULHandlerInternal<T extends CULConfig> extends CULHandler {

    public void open() throws CULDeviceException;

    public void close();

    public boolean hasListeners();

    public void sendWithoutCheck(String message) throws CULCommunicationException;

    public T getConfig();

}
