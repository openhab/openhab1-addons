/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import java.util.List;

/**
 * {@link ItemMap} for a SOAP service which takes input arguments.
 *
 * @author Michael Koch <tensberg@gmx.net>
 * @since 1.11.0
 */
public interface ParametrizedItemMap extends ItemMap {

    /**
     * Get the list of input arguments for a particular configuration.
     * Creates the {@link InputArgument input arguments} list by combining the
     * argument names of the item map with the {@link ItemConfiguration#getArgumentValues() argument values} configured
     * by the user.
     *
     * @param config Configuration of the item.
     * @return List of input arguments for the given configuration.
     */
    List<InputArgument> getConfigInputArguments(ItemConfiguration config);

}
