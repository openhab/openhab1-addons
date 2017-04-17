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
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Partition represents a controllable area within a DSC Alarm system
 *
 * @author Russell Stephens
 * @since 1.6.0
 */
public class Partition extends DSCAlarmDevice {
    private static final Logger logger = LoggerFactory.getLogger(Partition.class);

    private int partitionID;

    /**
     * Constructor
     *
     * @param partitionID
     */
    public Partition(int partitionID) {
        if (partitionID >= 1 && partitionID <= 8) {
            this.partitionID = partitionID;
        } else {
            this.partitionID = 1;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshItem(Item item, DSCAlarmBindingConfig config, EventPublisher publisher, int state,
            String description) {
        logger.debug("refreshItem(): Partition Item Name: {}", item.getName());

        if (config != null) {
            if (config.getDSCAlarmItemType() != null) {
                switch (config.getDSCAlarmItemType()) {
                    case PARTITION_STATUS:
                        publisher.postUpdate(item.getName(), new StringType(description));
                        break;
                    case PARTITION_ARM_MODE:
                        publisher.postUpdate(item.getName(), new DecimalType(state));
                        break;
                    case PARTITION_ARMED:
                        publisher.postUpdate(item.getName(), (state == 1) ? OnOffType.ON : OnOffType.OFF);
                        break;
                    case PARTITION_ENTRY_DELAY:
                        publisher.postUpdate(item.getName(), (state == 1) ? OnOffType.ON : OnOffType.OFF);
                        break;
                    case PARTITION_EXIT_DELAY:
                        publisher.postUpdate(item.getName(), (state == 1) ? OnOffType.ON : OnOffType.OFF);
                        break;
                    case PARTITION_IN_ALARM:
                        publisher.postUpdate(item.getName(), (state == 1) ? OnOffType.ON : OnOffType.OFF);
                        break;
                    case PARTITION_OPENING_CLOSING_MODE:
                        if (item instanceof NumberItem) {
                            publisher.postUpdate(item.getName(), new DecimalType(state));
                        }
                        if (item instanceof StringItem) {
                            publisher.postUpdate(item.getName(), new StringType(description));
                        }
                        break;
                    default:
                        logger.debug("refreshItem(): Partition item not updated.");
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
        int state = 0;
        int apiCode = -1;
        APIMessage apiMessage = null;
        String str = "";

        if (event != null) {
            apiMessage = event.getAPIMessage();
            apiCode = Integer.parseInt(apiMessage.getAPICode());
            str = apiMessage.getAPIName();
            logger.debug("handleEvent(): Partition Item Name: {}", item.getName());

            if (config != null) {
                if (config.getDSCAlarmItemType() != null) {
                    switch (config.getDSCAlarmItemType()) {
                        case PARTITION_STATUS:
                            switch (apiCode) {
                                case 650:
                                case 653:
                                case 654:
                                    state = 1;
                                    break;
                                case 651:
                                case 672:
                                case 673:
                                    state = 0;
                                    break;
                                default:
                                    break;
                            }
                            publisher.postUpdate(item.getName(), new StringType(str));
                            break;
                        case PARTITION_ARM_MODE:
                            if (apiCode == 652) {
                                state = Integer.parseInt(apiMessage.getMode()) + 1;
                            }
                            if (item instanceof NumberItem) {
                                publisher.postUpdate(item.getName(), new DecimalType(state));
                            }
                            if (item instanceof StringItem) {
                                publisher.postUpdate(item.getName(), new StringType(str));
                            }
                            break;
                        case PARTITION_OPENING_CLOSING_MODE:
                            switch (apiCode) {
                                case 700:
                                    state = 1;
                                    break;
                                case 701:
                                    state = 2;
                                    break;
                                case 702:
                                    state = 3;
                                    break;
                                case 750:
                                    state = 4;
                                    break;
                                case 751:
                                    state = 5;
                                    break;
                                default:
                                    state = 0;
                                    str = "";
                                    break;
                            }
                            if (item instanceof NumberItem) {
                                publisher.postUpdate(item.getName(), new DecimalType(state));
                            }
                            if (item instanceof StringItem) {
                                publisher.postUpdate(item.getName(), new StringType(str));
                            }
                            break;
                        default:
                            logger.debug("handleEvent(): Partition item not updated.");
                            break;
                    }
                }
            }
        }
    }
}
