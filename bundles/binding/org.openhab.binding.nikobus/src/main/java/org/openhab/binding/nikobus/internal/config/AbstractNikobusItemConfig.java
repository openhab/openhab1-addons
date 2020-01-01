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
package org.openhab.binding.nikobus.internal.config;

import org.openhab.binding.nikobus.internal.NikobusBinding;
import org.openhab.binding.nikobus.internal.core.NikobusCommandListener;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.types.Command;

/**
 * Base class for all Nikobus devices like buttons, switch modules, motion
 * sensors, etc.
 *
 * To add binding configuration support for new devices, extend this class.
 *
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public abstract class AbstractNikobusItemConfig implements BindingConfig, NikobusCommandListener {

    private String name;

    private String address;

    /**
     * Default constructor.
     * 
     * @param name
     *            item name.
     * @param address
     *            device address.
     */
    protected AbstractNikobusItemConfig(String name, String address) {
        this.name = name;
        this.address = address;
    }

    /**
     * @return address of Nikobus component.
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return name of the item which this binding is linked to
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Process an openHab command for the current item binding.
     * 
     * @param command
     *            openHab command
     */
    public abstract void processCommand(Command command, NikobusBinding binding) throws Exception;
}
