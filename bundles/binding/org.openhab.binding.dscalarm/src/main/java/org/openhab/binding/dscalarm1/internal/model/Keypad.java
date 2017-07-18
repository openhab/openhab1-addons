/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm1.internal.model;

import org.openhab.binding.dscalarm1.DSCAlarmBindingConfig;
import org.openhab.binding.dscalarm1.internal.DSCAlarmEvent;
import org.openhab.binding.dscalarm1.internal.protocol.APIMessage;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Keypad is the central administrative unit of the DSC Alarm System
 *
 * @author Russell Stephens
 * @since 1.6.0
 */
public class Keypad extends DSCAlarmDevice {
    private static final Logger logger = LoggerFactory.getLogger(Keypad.class);

    private int keypadID;
    private String ledStates[] = { "Off", "On", "Flashing" };

    /**
     * Constructor
     *
     * @param keypadId
     */
    public Keypad(int keypadID) {
        this.keypadID = keypadID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshItem(Item item, DSCAlarmBindingConfig config, EventPublisher publisher, int state, String description) {
        logger.debug("refreshItem(): Keypad Item Name: {}", item.getName());

        if (config != null) {
            if (config.getDSCAlarmItemType() != null) {
                switch (config.getDSCAlarmItemType()) {
                    case KEYPAD_READY_LED:
                    case KEYPAD_ARMED_LED:
                    case KEYPAD_MEMORY_LED:
                    case KEYPAD_BYPASS_LED:
                    case KEYPAD_TROUBLE_LED:
                    case KEYPAD_PROGRAM_LED:
                    case KEYPAD_FIRE_LED:
                    case KEYPAD_BACKLIGHT_LED:
                    case KEYPAD_AC_LED:
                        if (item instanceof NumberItem) {
                            publisher.postUpdate(item.getName(), new DecimalType(state));
                        }
                        if (item instanceof StringItem) {
                            publisher.postUpdate(item.getName(), new StringType(ledStates[state]));
                        }
                        break;
                    case KEYPAD_LCD_UPDATE:
                    case KEYPAD_LCD_CURSOR:
                        publisher.postUpdate(item.getName(), new StringType(description));
                        break;
                    default:
                        logger.debug("refreshItem(): Keypad item not updated.");
                        break;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleEvent(Item item, DSCAlarmBindingConfig config, EventPublisher publisher, DSCAlarmEvent event) {
        APIMessage apiMessage = null;
        int state;

        if (event != null) {
            apiMessage = event.getAPIMessage();
            logger.debug("handleEvent(): Keypad Item Name: {}", item.getName());

            if (config != null) {
                if (config.getDSCAlarmItemType() != null) {
                    switch (config.getDSCAlarmItemType()) {
                        case KEYPAD_READY_LED:
                        case KEYPAD_ARMED_LED:
                        case KEYPAD_MEMORY_LED:
                        case KEYPAD_BYPASS_LED:
                        case KEYPAD_TROUBLE_LED:
                        case KEYPAD_PROGRAM_LED:
                        case KEYPAD_FIRE_LED:
                        case KEYPAD_BACKLIGHT_LED:
                        case KEYPAD_AC_LED:
                            state = Integer.parseInt(apiMessage.getAPIData().substring(1));
                            if (item instanceof NumberItem) {
                                publisher.postUpdate(item.getName(), new DecimalType(state));
                            }
                            if (item instanceof StringItem) {
                                publisher.postUpdate(item.getName(), new StringType(ledStates[state]));
                            }
                            break;
                        case KEYPAD_LCD_UPDATE:
                        case KEYPAD_LCD_CURSOR:
                            publisher.postUpdate(item.getName(), new StringType(apiMessage.getAPIData()));
                            break;
                        default:
                            logger.debug("handleEvent(): Keypad item not updated.");
                            break;
                    }
                }
            }
        }
    }
}
