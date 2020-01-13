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
package org.openhab.binding.sapp;

import java.util.Map;

import org.openhab.binding.sapp.internal.configs.SappBindingConfig;
import org.openhab.binding.sapp.internal.model.SappPnmas;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information between openHAB items and Sapp items.
 *
 * Implementing classes should register themselves as a service in order to be taken into account.
 *
 * @author Paolo Denti
 * @since 1.8.0
 */
public interface SappBindingProvider extends BindingProvider {

    /**
     * @param itemName
     *            The name of the item
     * @return The bindingConfig for the itemName
     */
    SappBindingConfig getBindingConfig(String itemName);

    /**
     * @return The pnmas Map (by pnmas id)
     */
    Map<String, SappPnmas> getPnmasMap();

    /**
     * @param address
     *            virtual address
     * @return returns cached virtual address or null if not found
     */
    Integer getVirtualCachedValue(int address);

    /**
     * @param address
     *            virtual address
     * @param value
     *            virtual value
     */
    void setVirtualCachedValue(int address, int value);

    /**
     * @param address
     *            input address
     * @return returns cached input address or null if not found
     */
    Integer getInputCachedValue(int address);

    /**
     * @param address
     *            input address
     * @param value
     *            input value
     */
    void setInputCachedValue(int address, int value);

    /**
     * @param address
     *            output address
     * @return returns cached output address or null if not found
     */
    Integer getOutputCachedValue(int address);

    /**
     * @param address
     *            output address
     * @param value
     *            output value
     */
    void setOutputCachedValue(int address, int value);

    /**
     * @return Set of items to be refreshed
     */
    SappUpdatePendingRequestsProvider getSappUpdatePendingRequests();
}
