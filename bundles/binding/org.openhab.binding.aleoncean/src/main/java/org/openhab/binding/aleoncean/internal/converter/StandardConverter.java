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
package org.openhab.binding.aleoncean.internal.converter;

import org.openhab.binding.aleoncean.internal.ActionIn;
import org.openhab.binding.aleoncean.internal.devices.ItemInfo;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import eu.aleon.aleoncean.device.Device;
import eu.aleon.aleoncean.device.DeviceParameter;
import eu.aleon.aleoncean.device.IllegalDeviceParameterException;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public abstract class StandardConverter {

    // public static final DeviceParameter PARAMETER = null;
    // public static final Class<?> PARAMETER_CLASS = null;
    // public static final Class<? extends Item> ITEM_CLASS = null;
    // public static final Class<? extends State> STATE_TYPE_CLASS = null;
    // public static final Class<? extends Command> COMMAND_TYPE_CLASS = null;
    // public static final String CONV_PARAM = null;
    //

    private boolean sendToDevice = true;
    private final ActionIn actionIn;

    public StandardConverter(final ActionIn actionIn) {
        this.actionIn = actionIn;
    }

    protected ActionIn getActionIn() {
        return actionIn;
    }

    protected void postUpdate(final EventPublisher eventPublisher, final String itemName, final State state) {
        eventPublisher.postUpdate(itemName, state);
    }

    protected void postCommand(final EventPublisher eventPublisher, final String itemName, final Command command) {
        eventPublisher.postCommand(itemName, command);
    }

    protected void sendCommand(final EventPublisher eventPublisher, final String itemName, final Command command) {
        eventPublisher.sendCommand(itemName, command);
    }

    protected void setByParameter(final Device device, final DeviceParameter parameter, final Object value) {
        if (!sendToDevice) {
            return;
        }

        try {
            device.setByParameter(parameter, value);
        } catch (final IllegalDeviceParameterException ex) {
            sendToDevice = false;
        }
    }

    public abstract void commandFromOpenHAB(final EventPublisher eventPublisher,
                                            final String itemName, final ItemInfo itemInfo,
                                            final Command command);

    public abstract void stateFromOpenHAB(final EventPublisher eventPublisher,
                                          final String itemName, final ItemInfo itemInfo,
                                          final State state);

    public abstract void parameterFromDevice(final EventPublisher eventPublisher,
                                             final String itemName, final ItemInfo itemInfo,
                                             final Object value);

}
