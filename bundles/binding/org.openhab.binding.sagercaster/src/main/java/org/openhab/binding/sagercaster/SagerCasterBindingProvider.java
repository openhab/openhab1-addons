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
package org.openhab.binding.sagercaster;

import org.openhab.binding.sagercaster.internal.CommandType;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public interface SagerCasterBindingProvider extends BindingProvider {

    /**
     * @return all the binding config matching a given <code>commandType</code>
     */
    public Iterable<String> getItemNamesBy(CommandType commandType);

    /**
     * @return the binding config to the given <code>itemName</code>
     */
    public SagerCasterBindingConfig getConfig(String itemName);
}
