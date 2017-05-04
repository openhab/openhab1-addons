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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Zone represents a physical device such as a door, window, or motion sensor
 *
 * @author Russell Stephens
 * @since 1.6.0
 */
public class Zone extends DSCAlarmDevice {
    private static final Logger logger = LoggerFactory.getLogger(Zone.class);

    private int zoneID;
    private int partitionID;

    /**
     * Constructor
     *
     * @param partitionID
     * @param zoneID
     */
    public Zone(int partitionID, int zoneID) {
        if (partitionID >= 1 && partitionID <= 8) {
            this.partitionID = partitionID;
        }

        this.zoneID = zoneID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshItem(Item item, DSCAlarmBindingConfig config, EventPublisher publisher, int state, String description) {
        logger.debug("refreshItem(): Zone Item Name: {}", item.getName());

        if (config != null) {
            if (config.getDSCAlarmItemType() != null) {
                switch (config.getDSCAlarmItemType()) {
                    case ZONE_ALARM_STATUS:
                        publisher.postUpdate(item.getName(), new StringType(description));
                        break;
                    case ZONE_TAMPER_STATUS:
                        publisher.postUpdate(item.getName(), new StringType(description));
                        break;
                    case ZONE_FAULT_STATUS:
                        publisher.postUpdate(item.getName(), new StringType(description));
                        break;
                    case ZONE_GENERAL_STATUS:
                        publisher.postUpdate(item.getName(), (state == 1) ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
                        break;
                    case ZONE_BYPASS_MODE:
                        publisher.postUpdate(item.getName(), new DecimalType(state));
                        break;
                    case ZONE_IN_ALARM:
                        publisher.postUpdate(item.getName(), (state == 1) ? OnOffType.ON : OnOffType.OFF);
                        break;
                    case ZONE_TAMPER:
                        publisher.postUpdate(item.getName(), (state == 1) ? OnOffType.ON : OnOffType.OFF);
                        break;
                    case ZONE_FAULT:
                        publisher.postUpdate(item.getName(), (state == 1) ? OnOffType.ON : OnOffType.OFF);
                        break;
                    case ZONE_TRIPPED:
                        publisher.postUpdate(item.getName(), (state == 1) ? OnOffType.ON : OnOffType.OFF);
                        break;
                    default:
                        logger.debug("refreshItem(): Zone item not updated.");
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
        int apiCode = -1;
        APIMessage apiMessage = null;
        String str = "Status Unknown!";

        if (event != null) {
            apiMessage = event.getAPIMessage();
            apiCode = Integer.parseInt(apiMessage.getAPICode());
            str = apiMessage.getAPIName();
            logger.debug("handleEvent(): Zone Item Name: {}", item.getName());

            if (config != null) {
                if (config.getDSCAlarmItemType() != null) {
                    switch (config.getDSCAlarmItemType()) {
                        case ZONE_ALARM_STATUS:
                            publisher.postUpdate(item.getName(), new StringType(str));
                            break;
                        case ZONE_TAMPER_STATUS:
                            publisher.postUpdate(item.getName(), new StringType(str));
                            break;
                        case ZONE_FAULT_STATUS:
                            publisher.postUpdate(item.getName(), new StringType(str));
                            break;
                        case ZONE_GENERAL_STATUS:
                            publisher.postUpdate(item.getName(), (apiCode == 609) ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
                            break;
                        default:
                            logger.debug("handleEvent(): Zone item not updated.");
                            break;
                    }
                }
            }
        }
    }
}
