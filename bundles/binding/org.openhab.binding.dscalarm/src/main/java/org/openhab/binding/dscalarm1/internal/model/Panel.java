/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm1.internal.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openhab.binding.dscalarm1.DSCAlarmBindingConfig;
import org.openhab.binding.dscalarm1.internal.DSCAlarmEvent;
import org.openhab.binding.dscalarm1.internal.protocol.APIMessage;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Panel represents the basic DSC Alarm System
 *
 * @author Russell Stephens
 * @since 1.6.0
 */
public class Panel extends DSCAlarmDevice {
    private static final Logger logger = LoggerFactory.getLogger(Panel.class);

    private int panelID;
    private Date panelTime = new Date(0);

    /**
     * Constructor
     *
     * @param panelId
     */
    public Panel(int panelID) {
        this.panelID = panelID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshItem(Item item, DSCAlarmBindingConfig config, EventPublisher publisher, int state, String description) {
        logger.debug("refreshItem(): Panel Item Name: {}", item.getName());

        String str = "";
        OnOffType onOffType;

        if (config != null) {
            if (config.getDSCAlarmItemType() != null) {
                switch (config.getDSCAlarmItemType()) {
                    case PANEL_CONNECTION:
                        publisher.postUpdate(item.getName(), new DecimalType(state));
                        break;
                    case PANEL_MESSAGE:
                        publisher.postUpdate(item.getName(), new StringType(description));
                        break;
                    case PANEL_SYSTEM_ERROR:
                        str = String.format("%03d: %s", state, ((state == 0) ? "No Error" : description));
                        publisher.postUpdate(item.getName(), new StringType(str));
                        break;
                    case PANEL_TIME:
                        str = getFormattedPanelTime(description);
                        publisher.postUpdate(item.getName(), new DateTimeType(str));
                        break;
                    case PANEL_TIME_STAMP:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_TIME_BROADCAST:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_COMMAND:
                        publisher.postUpdate(item.getName(), new DecimalType(state));
                        break;
                    case PANEL_TROUBLE_MESSAGE:
                        publisher.postUpdate(item.getName(), new StringType(description));
                        break;
                    case PANEL_TROUBLE_LED:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_SERVICE_REQUIRED:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_AC_TROUBLE:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_TELEPHONE_TROUBLE:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_FTC_TROUBLE:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_ZONE_FAULT:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_ZONE_TAMPER:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_ZONE_LOW_BATTERY:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_TIME_LOSS:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_FIRE_KEY_ALARM:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_PANIC_KEY_ALARM:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_AUX_KEY_ALARM:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    case PANEL_AUX_INPUT_ALARM:
                        onOffType = (state == 1) ? OnOffType.ON : OnOffType.OFF;
                        publisher.postUpdate(item.getName(), onOffType);
                        break;
                    default:
                        logger.debug("refreshItem(): Panel item not updated.");
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
        int apiCode = -1;
        boolean boolState;
        int state = 0;
        OnOffType onOffType;

        if (event != null) {
            apiMessage = event.getAPIMessage();
            apiCode = Integer.parseInt(apiMessage.getAPICode());
            String str = "";
            logger.debug("handleEvent(): Panel Item Name: {}", item.getName());

            if (config != null) {
                if (config.getDSCAlarmItemType() != null) {
                    switch (config.getDSCAlarmItemType()) {
                        case PANEL_CONNECTION:
                            publisher.postUpdate(item.getName(), new DecimalType(state));
                            break;
                        case PANEL_MESSAGE:
                            if (apiMessage != null) {
                                str = apiMessage.getAPIDescription();
                            }
                            publisher.postUpdate(item.getName(), new StringType(str));
                            break;

                        case PANEL_SYSTEM_ERROR:
                            int systemErrorCode = 0;

                            if (apiMessage != null) {
                                systemErrorCode = Integer.parseInt(apiMessage.getAPIData());
                                str = String.format("%03d: %s", systemErrorCode, apiMessage.getError());
                                publisher.postUpdate(item.getName(), new StringType(str));
                            }
                            break;
                        case PANEL_TROUBLE_MESSAGE:
                            if (apiMessage != null) {
                                str = apiMessage.getAPIDescription();
                            }
                            publisher.postUpdate(item.getName(), new StringType(str));
                            break;
                        case PANEL_TROUBLE_LED:
                            if (apiMessage != null) {
                                boolState = (apiCode == 840) ? true : false;

                                onOffType = boolState ? OnOffType.ON : OnOffType.OFF;
                                publisher.postUpdate(item.getName(), onOffType);
                            }
                            break;
                        case PANEL_TIME:
                            if (apiMessage != null) {
                                str = getFormattedPanelTime(apiMessage.getAPIData());
                                publisher.postUpdate(item.getName(), new DateTimeType(str));
                            }
                            break;
                        default:
                            logger.debug("handleEvent(): Panel item not updated.");
                            break;
                    }
                }
            }
        }
    }

    public String getFormattedPanelTime(String time) {

        SimpleDateFormat receivedSDF = new SimpleDateFormat("HHmmMMddyy");
        Date date = null;

        if (!time.isEmpty()) {
            try {
                date = receivedSDF.parse(time);
            } catch (ParseException e) {
                logger.error("setTimeDate(): Parse Exception occurred while trying parse date string - {}. ", e);
            }

            this.panelTime = date;
        }

        SimpleDateFormat returnSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        return returnSDF.format(this.panelTime);
    }
}
