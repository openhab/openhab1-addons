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
package org.openhab.binding.fritzboxtr064.internal;

import org.openhab.core.types.Command;

/**
 * {@link ItemMap} for a SOAP service which supports writing values to the FritzBox.
 *
 * @author Michael Koch <tensberg@gmx.net>
 * @since 1.11.0
 */
public interface WritableItemMap extends ItemMap {

    /**
     *
     * @return TR064 service command for writing the item value.
     */
    String getWriteServiceCommand();

    /**
     * Get the input argument for the TR064 service command to set the item value.
     * Combines the argument name for the service call with the value derived from
     * the given command.
     *
     * @param cmd Command for which to set the new item value.
     * @return Input argument for setting the item value corresponding to the command.
     */
    InputArgument getWriteInputArgument(Command cmd);

}
