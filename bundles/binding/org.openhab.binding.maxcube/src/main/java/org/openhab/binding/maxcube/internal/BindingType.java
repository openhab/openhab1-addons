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
package org.openhab.binding.maxcube.internal;

/**
 * This enumeration represents the different binding types provided by the MAX!Cube generic binding.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public enum BindingType {
    VALVE,
    BATTERY,
    MODE,
    ACTUAL,
    CONNECTION_ERROR
}