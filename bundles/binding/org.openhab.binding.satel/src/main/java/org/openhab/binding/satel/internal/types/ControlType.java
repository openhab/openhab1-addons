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
package org.openhab.binding.satel.internal.types;

import java.util.BitSet;

/**
 * Interface for all types of control.
 *
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public interface ControlType {

    /**
     * Returns Satel command to control state for this kind of object type.
     *
     * @return command identifier
     */
    byte getControlCommand();

    /**
     * Returns object type for this kind of control.
     *
     * @return Integra object type
     */
    ObjectType getObjectType();

    /**
     * Returns set of states that may change for this control type.
     *
     * @return command identifier
     */
    BitSet getControlledStates();

}
