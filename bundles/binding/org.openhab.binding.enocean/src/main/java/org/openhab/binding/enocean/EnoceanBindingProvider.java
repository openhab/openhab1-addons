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
package org.openhab.binding.enocean;

import org.opencean.core.address.EnoceanParameterAddress;
import org.opencean.core.common.EEPId;
import org.openhab.binding.enocean.internal.profiles.Profile;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 */
public interface EnoceanBindingProvider extends BindingProvider {

    /**
     * @return the parameter address to the given <code>itemName</code>
     */
    EnoceanParameterAddress getParameterAddress(String itemName);

    /**
     * @return the EEP (EnOcean Equipment Profile) to the given
     *         <code>itemName</code>
     */
    EEPId getEEP(String itemName);

    /**
     * @param itemName
     *            The name of the item
     * @return The item with the given name
     */
    Item getItem(String itemName);

    /**
     * Returns the configured profile (if set).
     *
     * @param itemName
     *            The name of the item
     * @return The profile configured in the item line, NULL if not set.
     */
    Class<Profile> getCustomProfile(String itemName);

}
