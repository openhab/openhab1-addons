/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.satel.command;

import java.util.Calendar;

import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command class for command that reads one record from the event log.
 *
 * @author Krzysztof Goworek
 * @since 1.9.0
 */
public class ReadEventCommand extends SatelCommandBase {

    private static final Logger logger = LoggerFactory.getLogger(ReadEventCommand.class);

    public static final byte COMMAND_CODE = (byte) 0x8c;

    /**
     * Event class: zone alarms, partition alarms, arming, troubles, etc.
     *
     * @author Krzysztof Goworek
     *
     */
    public enum EventClass {
        ZONE_ALARMS("zone and tamper alarms"),
        PARTITION_ALARMS("partition and expander alarms"),
        ARMING("arming, disarming, alarm clearing"),
        BYPASSES("zone bypasses and unbypasses"),
        ACCESS_CONTROL("access control"),
        TROUBLES("troubles"),
        USER_FUNCTIONS("user functions"),
        SYSTEM_EVENTS("system events");

        private String description;

        EventClass(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Creates new command class instance to read a record under given index.
     *
     * @param eventIndex
     *            index of event record to retrieve, -1 for the most recent one
     */
    public ReadEventCommand(int eventIndex) {
        super(COMMAND_CODE, getIndexBytes(eventIndex));
    }

    private static byte[] getIndexBytes(int index) {
        return new byte[] { (byte) ((index >> 16) & 0xff), (byte) ((index >> 8) & 0xff), (byte) (index & 0xff) };
    }

    /**
     * Checks whether response data contains valid event record.
     *
     * @return <code>true</code> if returned record is empty (likely the last
     *         record in the log)
     */
    public boolean isEmpty() {
        return (response.getPayload()[0] & 0x20) == 0;
    }

    /**
     * Checks whether event record is present in the response data.
     *
     * @return <code>true</code> if event data is present in the response
     */
    public boolean isEventPresent() {
        return (response.getPayload()[0] & 0x10) != 0;
    }

    /**
     * Returns date and time of the event.
     *
     * @return date and time of the event
     */
    public Calendar getTimestamp() {
        Calendar c = Calendar.getInstance();
        int yearBase = c.get(Calendar.YEAR) / 4;
        int yearMarker = (response.getPayload()[0] >> 6) & 0x03;
        int minutes = ((response.getPayload()[2] & 0x0f) << 8) + (response.getPayload()[3] & 0xff);

        c.set(Calendar.YEAR, 4 * yearBase + yearMarker);
        c.set(Calendar.MONTH, ((response.getPayload()[2] >> 4) & 0x0f) - 1);
        c.set(Calendar.DAY_OF_MONTH, response.getPayload()[1] & 0x1f);
        c.set(Calendar.HOUR_OF_DAY, minutes / 60);
        c.set(Calendar.MINUTE, minutes % 60);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    /**
     * Returns class of the event.
     *
     * @return event class of the event
     * @see EventClass
     */
    public EventClass getEventClass() {
        int eventClassIdx = (response.getPayload()[1] >> 5) & 0x07;
        return EventClass.values()[eventClassIdx];
    }

    /**
     * Returns number of partion the event is about.
     *
     * @return partition number
     */
    public int getPartition() {
        return ((response.getPayload()[4] >> 3) & 0x1f) + 1;
    }

    /**
     * Returns event code the describes the event. It can be used to retrieve description text for this event.
     *
     * @return event code
     * @see ReadEventDescCommand
     */
    public int getEventCode() {
        return ((response.getPayload()[4] & 0x03) << 8) + (response.getPayload()[5] & 0xff);
    }

    /**
     * Returns state restoration flag.
     *
     * @return <code>true</code> if this is restoration of some state (i.e.
     *         arming and disarming have the same code but different restoration
     *         flag)
     */
    public boolean isRestore() {
        return (response.getPayload()[4] & 0x04) != 0;
    }

    /**
     * Return source of the event.
     *
     * @return event source (zone number, user number, etc depending on event)
     */
    public int getSource() {
        return response.getPayload()[6] & 0xff;
    }

    /**
     * Returns object number for the event.
     *
     * @return object number (0..7)
     */
    public int getObject() {
        return (response.getPayload()[7] >> 5) & 0x07;
    }

    /**
     * Returns user control number for the event.
     *
     * @return user control number
     */
    public int getUserControlNumber() {
        return response.getPayload()[7] & 0x1f;
    }

    /**
     * Return index of previous event in the log. Can be used to iterate over tha event log.
     *
     * @return index of previous event record in the log
     */
    public int getNextIndex() {
        return (response.getPayload()[8] << 16) + ((response.getPayload()[9] & 0xff) << 8)
                + (response.getPayload()[10] & 0xff);
    }

    /**
     * Returns current event index.
     *
     * @return index of current record echoed by communication module
     */
    public int getCurrentIndex() {
        return (response.getPayload()[11] << 16) + ((response.getPayload()[12] & 0xff) << 8)
                + (response.getPayload()[13] & 0xff);
    }

    @Override
    protected boolean isResponseValid(SatelMessage response) {
        // validate response
        if (response.getCommand() != COMMAND_CODE) {
            logger.error("Invalid response code: {}", response.getCommand());
            return false;
        }
        if (response.getPayload().length != 14) {
            logger.error("Invalid payload length: {}", response.getPayload().length);
            return false;
        }
        return true;
    }

}
