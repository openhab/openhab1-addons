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
package org.openhab.binding.aleoncean.internal.converter.paramcitemc;

import org.openhab.binding.aleoncean.internal.ActionIn;
import org.openhab.binding.aleoncean.internal.converter.ParameterClassItemClassConverter;
import org.openhab.binding.aleoncean.internal.devices.ItemInfo;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.aleon.aleoncean.values.RockerSwitchAction;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public class RockerSwitchActionRollerShutterItem extends ParameterClassItemClassConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RockerSwitchActionRollerShutterItem.class);

    public static final Class<?> PARAMETER_CLASS = RockerSwitchAction.class;
    public static final Class<? extends Item> ITEM_CLASS = RollershutterItem.class;

    private static final long SHORT_PRESS_TIMEOUT = 1000000000; // 1 second(s)

    private long lastUpPressedNanoSec = 0;
    private long lastUpReleasedNanoSec = 0;
    private long lastDownPressedNanoSec = 0;
    private long lastDownReleasedNanoSec = 0;

    public RockerSwitchActionRollerShutterItem(final ActionIn actionIn) {
        super(actionIn);
    }

    @Override
    public void commandFromOpenHAB(final EventPublisher eventPublisher,
                                   final String itemName, final ItemInfo itemInfo,
                                   final Command command) {
        // We map incoming rocker switch actions to control a roller shutter item.
        // Incoming commands and states are ignored.
    }

    @Override
    public void stateFromOpenHAB(final EventPublisher eventPublisher,
                                 final String itemName, final ItemInfo itemInfo,
                                 final State state) {
        // We map incoming rocker switch actions to control a roller shutter item.
        // Incoming commands and states are ignored.
    }

    @Override
    public void parameterFromDevice(final EventPublisher eventPublisher,
                                    final String itemName, final ItemInfo itemInfo,
                                    final Object value) {
        assert PARAMETER_CLASS.isAssignableFrom(value.getClass());

        final RockerSwitchAction action = (RockerSwitchAction) value;
        switch (getActionIn()) {
            case COMMAND:
            case DEFAULT:
                parameterFromDeviceCommand(eventPublisher, itemName, itemInfo, action);
                break;
            case STATE:
                LOGGER.warn("This converter supports no state action.");
                break;
            default:
                LOGGER.warn("Don't know how to handle action in type: {}", getActionIn());
                break;
        }
    }

    private void parameterFromDeviceCommand(final EventPublisher eventPublisher,
                                            final String itemName, final ItemInfo itemInfo,
                                            final RockerSwitchAction action) {
        final long curNanoSec = System.nanoTime();

        switch (action) {
            case DIM_UP_PRESSED:
                lastUpPressedNanoSec = curNanoSec;
                postCommand(eventPublisher, itemName, UpDownType.UP);
                break;
            case DIM_UP_RELEASED:
                lastUpReleasedNanoSec = curNanoSec;
                if (lastUpReleasedNanoSec - lastUpPressedNanoSec < SHORT_PRESS_TIMEOUT) {
                    postCommand(eventPublisher, itemName, StopMoveType.STOP);
                }
                break;
            case DIM_DOWN_PRESSED:
                lastDownPressedNanoSec = curNanoSec;
                postCommand(eventPublisher, itemName, UpDownType.DOWN);
                break;
            case DIM_DOWN_RELEASED:
                lastDownReleasedNanoSec = curNanoSec;
                if (lastDownReleasedNanoSec - lastDownPressedNanoSec < SHORT_PRESS_TIMEOUT) {
                    postCommand(eventPublisher, itemName, StopMoveType.STOP);
                }
                break;
            default:
                throw new AssertionError(action.name());
        }
    }

}
