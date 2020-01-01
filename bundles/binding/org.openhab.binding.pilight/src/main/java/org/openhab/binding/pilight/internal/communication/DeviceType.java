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
package org.openhab.binding.pilight.internal.communication;

/**
 * Different types of devices in pilight
 *
 * @author Jeroen Idserda
 * @since 1.0
 */
public class DeviceType {

    public static Integer SERVER = -1;

    public static Integer SWITCH = 1;

    public static Integer DIMMER = 2;

    public static Integer VALUE = 3;

    public static Integer CONTACT = 6;

}
