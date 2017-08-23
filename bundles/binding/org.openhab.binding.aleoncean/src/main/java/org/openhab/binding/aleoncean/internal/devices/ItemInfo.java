/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package org.openhab.binding.aleoncean.internal.devices;

import java.util.List;
import org.openhab.binding.aleoncean.internal.AleonceanBindingConfig;
import org.openhab.binding.aleoncean.internal.converter.StandardConverter;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import eu.aleon.aleoncean.device.Device;
import eu.aleon.aleoncean.device.DeviceParameter;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public class ItemInfo {

    private final Device device;
    private final StandardConverter converter;
    private final DeviceParameter parameter;
    private final List<Class<? extends State>> acceptedDataTypes;
    private final List<Class<? extends Command>> acceptedCommandTypes;
    private final Class<? extends Item> itemType;

    public ItemInfo(final Device device,
                    final StandardConverter converter,
                    final AleonceanBindingConfig bindingConfig) {
        this.device = device;
        this.converter = converter;
        this.parameter = bindingConfig.getParameter();
        this.acceptedDataTypes = bindingConfig.getAcceptedDataTypes();
        this.acceptedCommandTypes = bindingConfig.getAcceptedCommandTypes();
        this.itemType = bindingConfig.getItemType();
    }

    public Device getDevice() {
        return device;
    }

    public StandardConverter getConverter() {
        return converter;
    }

    public DeviceParameter getParameter() {
        return parameter;
    }

    public List<Class<? extends State>> getAcceptedDataTypes() {
        return acceptedDataTypes;
    }

    public List<Class<? extends Command>> getAcceptedCommandTypes() {
        return acceptedCommandTypes;
    }

    public Class<? extends Item> getItemType() {
        return itemType;
    }

}
