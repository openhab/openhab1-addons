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
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.aleon.aleoncean.values.RockerSwitchAction;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public abstract class RockerSwitchActionOnOffType extends ParameterClassTypeClassConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RockerSwitchActionOnOffType.class);

    public static final Class<?> PARAMETER_CLASS = RockerSwitchAction.class;
    public static final Class<? extends State> STATE_TYPE_CLASS = OnOffType.class;
    public static final Class<? extends Command> COMMAND_TYPE_CLASS = OnOffType.class;

    protected abstract RockerSwitchAction onOffTypeToRockerSwitchAction(final OnOffType value) throws NoValueException;

    protected abstract OnOffType rockerSwitchActionToOnOffType(final RockerSwitchAction value) throws NoValueException;

    public RockerSwitchActionOnOffType(final ActionIn actionIn) {
        super(actionIn);
    }

    @Override
    public void commandFromOpenHAB(final EventPublisher eventPublisher,
                                   final String itemName, final ItemInfo itemInfo,
                                   final Command command) {
        assert COMMAND_TYPE_CLASS.isAssignableFrom(command.getClass());
        try {
            final RockerSwitchAction action = onOffTypeToRockerSwitchAction((OnOffType) command);
            setByParameter(itemInfo.getDevice(), itemInfo.getParameter(), action);
        } catch (final NoValueException ex) {
        }
    }

    @Override
    public void stateFromOpenHAB(final EventPublisher eventPublisher,
                                 final String itemName, final ItemInfo itemInfo,
                                 final State state) {
        assert STATE_TYPE_CLASS.isAssignableFrom(state.getClass());
        try {
            final RockerSwitchAction action = onOffTypeToRockerSwitchAction((OnOffType) state);
            setByParameter(itemInfo.getDevice(), itemInfo.getParameter(), action);
        } catch (final NoValueException ex) {
        }
    }

    @Override
    public void parameterFromDevice(final EventPublisher eventPublisher,
                                    final String itemName, final ItemInfo itemInfo,
                                    final Object value) {
        assert PARAMETER_CLASS.isAssignableFrom(value.getClass());
        try {
            final OnOffType type = rockerSwitchActionToOnOffType((RockerSwitchAction) value);
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
