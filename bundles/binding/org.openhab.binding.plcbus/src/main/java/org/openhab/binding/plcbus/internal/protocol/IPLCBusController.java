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
 * Interface for a PLCBusController
 *
 * @author Robin Lenz
 * @since 1.1.0
 */
public interface IPLCBusController {

    boolean switchOn(PLCUnit unit);

    boolean switchOff(PLCUnit unit);

    boolean bright(PLCUnit unit, int seconds);

    boolean dim(PLCUnit unit, int seconds);

    boolean fadeStop(PLCUnit unit);

    StatusResponse requestStatusFor(PLCUnit unit);

}
