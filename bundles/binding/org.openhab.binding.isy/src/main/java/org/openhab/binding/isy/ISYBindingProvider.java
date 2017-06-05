/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
