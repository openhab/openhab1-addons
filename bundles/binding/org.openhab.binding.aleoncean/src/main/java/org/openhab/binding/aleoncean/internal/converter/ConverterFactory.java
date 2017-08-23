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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.openhab.binding.aleoncean.internal.ActionIn;
import org.openhab.binding.aleoncean.internal.converter.paramcitemc.RockerSwitchActionRollerShutterItem;
import org.openhab.binding.aleoncean.internal.converter.paramctypec.BooleanOnOffType;
import org.openhab.binding.aleoncean.internal.converter.paramctypec.BooleanUpDownType;
import org.openhab.binding.aleoncean.internal.converter.paramctypec.DoubleDecimalType;
import org.openhab.binding.aleoncean.internal.converter.paramctypec.IntegerDecimalType;
import org.openhab.binding.aleoncean.internal.converter.paramctypec.LongDecimalType;
import org.openhab.binding.aleoncean.internal.converter.paramctypec.RockerSwitchActionDecimalType;
import org.openhab.binding.aleoncean.internal.converter.paramctypec.RockerSwitchActionOnOffTypeDownPressedReleased;
import org.openhab.binding.aleoncean.internal.converter.paramctypec.RockerSwitchActionOnOffTypePressedUpDown;
import org.openhab.binding.aleoncean.internal.converter.paramctypec.RockerSwitchActionOnOffTypeReleasedUpDown;
import org.openhab.binding.aleoncean.internal.converter.paramctypec.RockerSwitchActionOnOffTypeUpPressedReleased;
import org.openhab.binding.aleoncean.internal.converter.paramctypec.RockerSwitchActionUpDownType;
import org.openhab.binding.aleoncean.internal.converter.paramctypec.WindowHandlePositionDecimalType;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.aleon.aleoncean.device.DeviceParameter;
import eu.aleon.aleoncean.device.IllegalDeviceParameterException;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public class ConverterFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterFactory.class);

    private static final String FIELD_PARAMETER = "PARAMETER";
    private static final String FIELD_PARAMETER_CLASS = "PARAMETER_CLASS";
    private static final String FIELD_ITEM_CLASS = "ITEM_CLASS";
    private static final String FIELD_STATE_TYPE_CLASS = "STATE_TYPE_CLASS";
    private static final String FIELD_COMMAND_TYPE_CLASS = "COMMAND_TYPE_CLASS";
    private static final String FIELD_CONV_PARAM = "CONV_PARAM";

    private static final List<Class<? extends ParameterItemClassConverter>> PARAMITEMC;
    private static final List<Class<? extends ParameterTypeClassConverter>> PARAMTYPEC;
    private static final List<Class<? extends ParameterClassItemClassConverter>> PARAMCITEMC;
    private static final List<Class<? extends ParameterClassTypeClassConverter>> PARAMCTYPEC;

    static {
        final List<Class<? extends ParameterItemClassConverter>> list = new LinkedList<>();
        PARAMITEMC = Collections.unmodifiableList(list);
    }

    static {
        final List<Class<? extends ParameterTypeClassConverter>> list = new LinkedList<>();
        PARAMTYPEC = Collections.unmodifiableList(list);
    }

    static {
        final List<Class<? extends ParameterClassItemClassConverter>> list = new LinkedList<>();

        list.add(RockerSwitchActionRollerShutterItem.class);

        PARAMCITEMC = Collections.unmodifiableList(list);
    }

    static {
        final List<Class<? extends ParameterClassTypeClassConverter>> list = new LinkedList<>();

        list.add(BooleanOnOffType.class);

        list.add(BooleanUpDownType.class);

        list.add(DoubleDecimalType.class);

        list.add(IntegerDecimalType.class);

        list.add(LongDecimalType.class);

        list.add(RockerSwitchActionDecimalType.class);

        // If multiple stuff suffers the requirements, the first one is used.
        // To use "PressedUpDown" if no CONV_PARAM is used, we have to place it before the other ones.
        list.add(RockerSwitchActionOnOffTypePressedUpDown.class);
        list.add(RockerSwitchActionOnOffTypeDownPressedReleased.class);
        list.add(RockerSwitchActionOnOffTypeReleasedUpDown.class);
        list.add(RockerSwitchActionOnOffTypeUpPressedReleased.class);

        list.add(RockerSwitchActionUpDownType.class);

        list.add(WindowHandlePositionDecimalType.class);

        PARAMCTYPEC = Collections.unmodifiableList(list);
    }

    private static boolean checkParameter(final Class<? extends StandardConverter> converterClass,
                                          final DeviceParameter parameter) {
        try {
            final Field fieldParameter = converterClass.getField(FIELD_PARAMETER);
            final DeviceParameter converterParameter = (DeviceParameter) fieldParameter.get(null);

            return converterParameter.equals(parameter);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            return false;
        }
    }

    private static boolean checkParameterClass(final Class<? extends StandardConverter> converterClass,
                                               final Class<?> parameterClass) {
        try {
            final Field fieldParameterClass = converterClass.getField(FIELD_PARAMETER_CLASS);
            final Class<?> converterParameterClass = (Class<?>) fieldParameterClass.get(null);

            return converterParameterClass.equals(parameterClass);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            return false;
        }
    }

    private static boolean checkItemClass(final Class<? extends StandardConverter> converterClass,
                                          final Class<? extends Item> itemClass) {
        try {
            final Field fieldItemClass = converterClass.getField(FIELD_ITEM_CLASS);
            final Class<? extends Item> converterItemClass = (Class<? extends Item>) fieldItemClass.get(null);

            return converterItemClass.equals(itemClass);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            return false;
        }
    }

    private static boolean checkTypeClass(final Class<? extends StandardConverter> converterClass,
                                          final List<Class<? extends State>> acceptedDataTypes,
                                          final List<Class<? extends Command>> acceptedCommandTypes) {
        try {
            final Field fieldStateType = converterClass.getField(FIELD_STATE_TYPE_CLASS);
            final Field fieldCommandType = converterClass.getField(FIELD_COMMAND_TYPE_CLASS);

            final Class<? extends State> converterStateTypeClass = (Class<? extends State>) fieldStateType.get(null);
            final Class<? extends Command> converterCommandTypeClass = (Class<? extends Command>) fieldCommandType.get(null);

            return acceptedDataTypes.contains(converterStateTypeClass) && acceptedCommandTypes.contains(converterCommandTypeClass);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            return false;
        }
    }

    private static boolean checkConvParam(final Class<? extends StandardConverter> converterClass,
                                          final String convParam) {
        if (convParam == null) {
            return true;
        }

        try {
            final Field fieldConvParam = converterClass.getField(FIELD_CONV_PARAM);
            final String converterConvParam = (String) fieldConvParam.get(null);

            return converterConvParam.equals(convParam);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            return false;
        }
    }

    /**
     * Get a converter class for the given arguments.
     *
     * We differ between four converters.
     * The converter is chosen in that order:
     * - [paramitemc] parameter to item type (class)
     * - [paramtypec] parameter to one state / command (class)
     * - [paramcitemc] parameter type (class) to item type (class)
     * - [paramctypec] parameter type (class) to one state / command (class)
     *
     * @param parameter
     * @param itemClass
     * @param acceptedDataTypes
     * @param acceptedCommandTypes
     * @param convParam
     * @return Return the converter class that should be used (if found). If no converter could be found, null is
     *         returned.
     */
    public static Class<? extends StandardConverter> getConverterClass(final DeviceParameter parameter,
                                                                       final Class<? extends Item> itemClass,
                                                                       final List<Class<? extends State>> acceptedDataTypes,
                                                                       final List<Class<? extends Command>> acceptedCommandTypes,
                                                                       final String convParam) {
        final Class<?> parameterClass;
        try {
            parameterClass = DeviceParameter.getSupportedClass(parameter);
        } catch (final IllegalDeviceParameterException ex) {
            LOGGER.warn("Illegal device parameter '{}'\n{}", parameter, ex);
            return null;
        }

        for (final Class<? extends ParameterItemClassConverter> converterClass : PARAMITEMC) {
            if (checkParameter(converterClass, parameter)
                && checkItemClass(converterClass, itemClass)
                && checkConvParam(converterClass, convParam)) {
                return converterClass;
            }
        }

        for (final Class<? extends ParameterTypeClassConverter> converterClass : PARAMTYPEC) {
            if (checkParameter(converterClass, parameter)
                && checkTypeClass(converterClass, acceptedDataTypes, acceptedCommandTypes)
                && checkConvParam(converterClass, convParam)) {
                return converterClass;
            }
        }

        for (final Class<? extends ParameterClassItemClassConverter> converterClass : PARAMCITEMC) {
            if (checkParameterClass(converterClass, parameterClass)
                && checkItemClass(converterClass, itemClass)
                && checkConvParam(converterClass, convParam)) {
                return converterClass;
            }
        }

        for (final Class<? extends ParameterClassTypeClassConverter> converterClass : PARAMCTYPEC) {
            if (checkParameterClass(converterClass, parameterClass)
                && checkTypeClass(converterClass, acceptedDataTypes, acceptedCommandTypes)
                && checkConvParam(converterClass, convParam)) {
                return converterClass;
            }
        }

        return null;
    }

    public static StandardConverter createFromClass(final Class<? extends StandardConverter> clazz,
                                                    final ActionIn actionIn) {
        Constructor<? extends StandardConverter> constructor;
        try {
            constructor = clazz.getConstructor(ActionIn.class);
        } catch (final NoSuchMethodException ex) {
            LOGGER.warn("ItemParameterConverter constructor not found.\n{}", ex);
            return null;
        } catch (final SecurityException ex) {
            LOGGER.warn("Search for ItemParameterConverter constructor throws security exception.\n{}", ex);
            return null;
        }
        try {
            return constructor.newInstance(actionIn);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOGGER.warn("Converter creation failed (class: {}).\n{}", clazz, ex);
            return null;
        }
    }

    private ConverterFactory() {
    }

}
