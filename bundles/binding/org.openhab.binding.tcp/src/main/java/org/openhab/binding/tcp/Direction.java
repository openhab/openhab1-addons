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
package org.openhab.binding.tcp;

/**
 * Each binding configuration can define a direction. This can be used to qualify, limit, control, ...
 * whatever is received/sent from the binding
 *
 * IN : for data/updates coming *from* the binding
 * OUT: for data/commands to be sent *to* the binding
 * BIDIRECTIONAL : for data/commands that be sent or be updated at the same time
 *
 *
 * @author Karel Goderis
 * @since 1.1.0
 *
 **/

public enum Direction {
    IN,
    OUT,
    BIDIRECTIONAL
}
