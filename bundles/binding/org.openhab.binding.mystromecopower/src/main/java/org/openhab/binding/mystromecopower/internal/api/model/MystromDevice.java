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
package org.openhab.binding.mystromecopower.internal.api.model;

/**
 * Class model for a mystrom eco power device.
 *
 * @since 1.8.0
 * @author Jordens Christophe
 *
 */
public class MystromDevice {
    /**
     * The id of the device on the mystrom server.
     */
    public String id;

    /**
     * The name of the device on the mystrom server.
     */
    public String name;

    /**
     * The state of the device, can be: on, off or offline.
     */
    public String state;

    /**
     * The power the device is consuming in Watt.
     */
    public String power;

    /**
     * Device type. mst: master, eth: ethernet, sw:switch, mtr:?, swg:?, tph:?
     */
    public String type;
}
