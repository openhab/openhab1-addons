/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean;

import org.enocean.java.address.EnoceanParameterAddress;
import org.enocean.java.common.EEPId;
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
