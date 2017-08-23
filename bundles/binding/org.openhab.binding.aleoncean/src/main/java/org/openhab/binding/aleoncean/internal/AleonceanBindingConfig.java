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
package org.openhab.binding.aleoncean.internal;

import java.util.List;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.aleon.aleoncean.device.DeviceParameter;
import eu.aleon.aleoncean.device.StandardDevice;
import eu.aleon.aleoncean.device.SupportedDevice;
import eu.aleon.aleoncean.packet.EnOceanId;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public class AleonceanBindingConfig implements BindingConfig {

    private static final String KEY_REMOTEID = "REMOTEID";
    private static final String KEY_LOCALID = "LOCALID";
    private static final String KEY_TYPE = "TYPE";
    private static final String KEY_PARAMETER = "PARAMETER";
    private static final String KEY_CONVPARAM = "CONVPARAM";
    private static final String KEY_ACTION_IN = "ACTIONI";

    private static final Logger LOGGER = LoggerFactory.getLogger(AleonceanBindingConfig.class);

    private final EnOceanId remoteId = EnOceanId.getBroadcast();
    private final EnOceanId localId = EnOceanId.getBroadcast();
    private final Class<? extends StandardDevice> type;
    private final DeviceParameter parameter;
    private final String convParam;
    private final ActionIn actionIn;
    private Class<? extends Item> itemType;
    private List<Class<? extends State>> acceptedDataTypes;
    private List<Class<? extends Command>> acceptedCommandTypes;

    public AleonceanBindingConfig(final String config) throws BindingConfigParseException {

        String valueRemoteId = null;
        String valueLocalId = null;
        String valueType = null;
        String valueParameter = null;
        String valueConvParam = null;
        String valueActionIn = null;

        final String[] pairs = config.split(",");
        for (final String pair : pairs) {
            final String[] kv = pair.split("=");
            if (kv.length != 2) {
                final String message = String.format("Key value pairs (key=value NOT '%s') expected.", pair);
                LOGGER.warn(message);
                throw new BindingConfigParseException(message);
            }

            switch (kv[0]) {
                case KEY_REMOTEID:
                    valueRemoteId = kv[1];
                    break;
                case KEY_LOCALID:
                    valueLocalId = kv[1];
                    break;
                case KEY_TYPE:
                    valueType = kv[1];
                    break;
                case KEY_PARAMETER:
                    valueParameter = kv[1];
                    break;
                case KEY_CONVPARAM:
                    valueConvParam = kv[1];
                    break;
                case KEY_ACTION_IN:
                    valueActionIn = kv[1];
                    break;
                default:
                    final String message = String.format("Unknown parameter '%s'.", kv[0]);
                    LOGGER.warn(message);
                    throw new BindingConfigParseException(message);
            }

        }

        if (valueRemoteId != null) {
            try {
                this.remoteId.fill(valueRemoteId);
            } catch (/* NumberFormatException | */final IllegalArgumentException e) {
                final String message = String.format("Invalid remote ID: %s", valueRemoteId);
                LOGGER.warn(message);
                throw new BindingConfigParseException(message);
            }
        }

        if (valueLocalId != null) {
            try {
                this.localId.fill(valueLocalId);
            } catch (/* NumberFormatException | */final IllegalArgumentException e) {
                final String message = String.format("Invalid local ID: %s", valueRemoteId);
                LOGGER.warn(message);
                throw new BindingConfigParseException(message);
            }
        }

        if (valueType != null) {
            this.type = SupportedDevice.getClassForIdent(valueType);
            if (this.type == null) {
                final String message = String.format("Unknown type (%s).", valueType);
                LOGGER.warn(message);
                throw new BindingConfigParseException(message);
            }
        } else {
            final String message = "No value for the type is set.";
            LOGGER.warn(message);
            throw new BindingConfigParseException(message);
        }

        this.parameter = DeviceParameter.fromString(valueParameter);
        if (this.parameter == null) {
            final String message = String.format("Parameter missing or unknown (%s).", valueParameter);
            LOGGER.warn(message);
            throw new BindingConfigParseException(message);
        }

        this.convParam = valueConvParam;

        if (valueActionIn != null) {
            try {
                actionIn = ActionIn.parse(valueActionIn);
            } catch (final IllegalArgumentException e) {
                final String message = String.format("Cannot parse action in parameter: %s", valueActionIn);
                LOGGER.warn(message);
                throw new BindingConfigParseException(message);
            }
        } else {
            actionIn = ActionIn.DEFAULT;
        }
    }

    public EnOceanId getRemoteId() {
        return remoteId;
    }

    public EnOceanId getLocalId() {
        return localId;
    }

    public Class<? extends StandardDevice> getType() {
        return type;
    }

    public DeviceParameter getParameter() {
        return parameter;
    }

    public String getConvParam() {
        return convParam;
    }

    public ActionIn getActionIn() {
        return actionIn;
    }

    public List<Class<? extends State>> getAcceptedDataTypes() {
        return acceptedDataTypes;
    }

    public void setAcceptedDataTypes(final List<Class<? extends State>> acceptedDataTypes) {
        this.acceptedDataTypes = acceptedDataTypes;
    }

    public List<Class<? extends Command>> getAcceptedCommandTypes() {
        return acceptedCommandTypes;
    }

    public void setAcceptedCommandTypes(final List<Class<? extends Command>> acceptedCommandTypes) {
        this.acceptedCommandTypes = acceptedCommandTypes;
    }

    public Class<? extends Item> getItemType() {
        return itemType;
    }

    public void setItemType(final Class<? extends Item> itemType) {
        this.itemType = itemType;
    }

}
