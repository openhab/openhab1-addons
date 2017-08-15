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
package org.openhab.binding.aleoncean.internal.converter.paramctypec;

import org.openhab.binding.aleoncean.internal.ActionIn;
import org.openhab.binding.aleoncean.internal.converter.NoValueException;
import org.openhab.binding.aleoncean.internal.converter.ParameterClassTypeClassConverter;
import org.openhab.binding.aleoncean.internal.devices.ItemInfo;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.aleon.aleoncean.values.WindowHandlePosition;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public class WindowHandlePositionDecimalType extends ParameterClassTypeClassConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WindowHandlePositionDecimalType.class);

    public static final Class<?> PARAMETER_CLASS = WindowHandlePosition.class;
    public static final Class<? extends State> STATE_TYPE_CLASS = DecimalType.class;
    public static final Class<? extends Command> COMMAND_TYPE_CLASS = DecimalType.class;

    private static final int DEC_WINDOW_HANDLE_POS_UP = 4;
    private static final int DEC_WINDOW_HANDLE_POS_DOWN = 8;
    private static final int DEC_WINDOW_HANDLE_POS_LEFT_OR_RIGHT = 48;

    public WindowHandlePositionDecimalType(final ActionIn actionIn) {
        super(actionIn);
    }

    private DecimalType windowHandlePositionToDecimalType(final WindowHandlePosition i) throws NoValueException {
        switch (i) {
            case UP:
                return new DecimalType(DEC_WINDOW_HANDLE_POS_UP);
            case DOWN:
                return new DecimalType(DEC_WINDOW_HANDLE_POS_DOWN);
            case LEFT_OR_RIGHT:
                return new DecimalType(DEC_WINDOW_HANDLE_POS_LEFT_OR_RIGHT);
            default:
                throw new NoValueException();
        }
    }

    private WindowHandlePosition decimalTypeToWindowHandlePosition(final DecimalType i) throws NoValueException {
        switch (i.intValue()) {
            case DEC_WINDOW_HANDLE_POS_UP:
                return WindowHandlePosition.UP;
            case DEC_WINDOW_HANDLE_POS_DOWN:
                return WindowHandlePosition.DOWN;
            case DEC_WINDOW_HANDLE_POS_LEFT_OR_RIGHT:
                return WindowHandlePosition.LEFT_OR_RIGHT;
            default:
                throw new NoValueException();
        }
    }

    @Override
    public void commandFromOpenHAB(final EventPublisher eventPublisher,
                                   final String itemName, final ItemInfo itemInfo,
                                   final Command command) {
        assert COMMAND_TYPE_CLASS.isAssignableFrom(command.getClass());
        try {
            setByParameter(itemInfo.getDevice(), itemInfo.getParameter(), decimalTypeToWindowHandlePosition((DecimalType) command));
        } catch (final NoValueException ex) {
        }
    }

    @Override
    public void stateFromOpenHAB(final EventPublisher eventPublisher,
                                 final String itemName, final ItemInfo itemInfo,
                                 final State state) {
        assert STATE_TYPE_CLASS.isAssignableFrom(state.getClass());
        try {
            setByParameter(itemInfo.getDevice(), itemInfo.getParameter(), decimalTypeToWindowHandlePosition((DecimalType) state));
        } catch (final NoValueException ex) {
        }
    }

    @Override
    public void parameterFromDevice(final EventPublisher eventPublisher,
                                    final String itemName, final ItemInfo itemInfo,
                                    final Object value) {
        assert PARAMETER_CLASS.isAssignableFrom(value.getClass());
        try {
            final DecimalType type = windowHandlePositionToDecimalType((WindowHandlePosition) value);
            switch (getActionIn()) {
                case COMMAND:
                    postCommand(eventPublisher, itemName, type);
                    break;
                case STATE:
                case DEFAULT:
                    postUpdate(eventPublisher, itemName, type);
                    break;
                default:
                    LOGGER.warn("Don't know how to handle action in type: {}", getActionIn());
                    break;
            }
        } catch (final NoValueException ex) {
        }
    }
}
