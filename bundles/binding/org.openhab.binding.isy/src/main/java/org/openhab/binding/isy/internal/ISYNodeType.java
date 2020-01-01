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
package org.openhab.binding.isy.internal;

/**
 * These are the node types in ISY that are supported / translated.
 * 
 * @author Jon Bullen
 * @since 1.10.0
 */
public enum ISYNodeType {
    SWITCH,
    GROUP,
    CONTACT,
    THERMOSTAT,
    NUMBER,
    STRING,
    DIMMER,
    LOCK,
    HEARTBEAT
}
