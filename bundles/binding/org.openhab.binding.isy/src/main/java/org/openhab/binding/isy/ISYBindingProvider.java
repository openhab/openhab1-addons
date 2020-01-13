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
package org.openhab.binding.isy;

import java.util.Collection;

import org.openhab.core.binding.BindingProvider;

/**
 * 
 * @author Tim Diekmann
 * @since 1.10.0
 */
public interface ISYBindingProvider extends BindingProvider {

    /**
     * Returns a Device/Node Address from a Item name in the *.items file
     * 
     * @param itemName
     *            configured Item name in the *.items file with a ISY binding
     * @return ISY binding of the *.items file
     */
    public ISYBindingConfig getBindingConfigFromItemName(String itemName);

    /**
     * Returns a Device/Node Address from a Item name in the *.items file
     * 
     * @param address
     *            configured address in the *.items file with a ISY binding
     * @param cmd
     *            Control name of ISY control
     * @return Collection of matching ISY bindings of the *.items file
     */
    public Collection<ISYBindingConfig> getBindingConfigFromAddress(String address, String cmd);
}
