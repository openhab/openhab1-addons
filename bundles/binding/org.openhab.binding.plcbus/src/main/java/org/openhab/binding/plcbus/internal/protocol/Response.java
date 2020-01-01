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
package org.openhab.binding.plcbus.internal.protocol;

/**
 * Representation of Response from PLCBus
 *
 * @author Robin Lenz
 * @since 1.1.0
 */
public class Response {

    private boolean acknowlagement;
    private int firstParameter;
    private int secondParameter;
    protected Command command;

    public Response(boolean acknowlagement, Command command, int firstParameter, int secondParameter) {
        this.acknowlagement = acknowlagement;
        this.command = command;
        this.firstParameter = firstParameter;
        this.secondParameter = secondParameter;
    }

    public boolean isAcknowlagement() {
        return acknowlagement;
    }

    public int getFirstParameter() {
        return firstParameter;
    }

    public int getSecondParameter() {
        return secondParameter;
    }

}
