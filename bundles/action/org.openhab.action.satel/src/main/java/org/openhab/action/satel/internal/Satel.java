/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.satel.internal;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.binding.satel.command.ReadDeviceInfoCommand;
import org.openhab.binding.satel.command.ReadDeviceInfoCommand.DeviceType;
import org.openhab.binding.satel.command.ReadEventCommand;
import org.openhab.binding.satel.command.ReadEventCommand.EventClass;
import org.openhab.binding.satel.command.ReadEventDescCommand;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains the methods that are made available in scripts and rules
 * for Satel.
 *
 * @author Krzysztof Goworek
 * @since 1.9.0
 */
public class Satel {

    private static final Logger logger = LoggerFactory.getLogger(Satel.class);
    private static Map<String, EventDescription> eventDescriptions = new ConcurrentHashMap<String, EventDescription>();

    /**
     * Checks current connection status to the communication module.
     *
     * @return <code>true</code> if connection is established
     */
    @ActionDoc(text = "Check current connection status to Satel communication module", returns = "<code>true</code>, if connected and <code>false</code> otherwise.")
    public static boolean satelIsConnected() {
        if (!SatelActionService.isProperlyConfigured) {
            logger.debug("Satel action is not yet configured - execution aborted!");
            return false;
        }
        if (SatelActionService.satelCommModule == null) {
            logger.debug("Satel communication module not available - execution aborted!");
            return false;
        }
        if (!SatelActionService.satelCommModule.isConnected()) {
            logger.debug("Satel communication module is not connected");
            return false;
        }
        return true;
    }

    /**
     * Read event log record from the alarm system for given index.
     * For the most recent record pass <code>-1</code> as index,
     * next records can be retrieved by using next index value in the record data (the last field in a record).
     *
     * Fields in the record:
     * <ol>
     * <li>timestamp - {@link DateTimeType}</li>
     * <li>partition - String</li>
     * <li>event class - {@link EventClass}</li>
     * <li>event code - Integer}</li>
     * <li>state restoration flag - Boolean</li>
     * <li>event description - String</li>
     * <li>kind of description - Integer</li>
     * <li>source - Integer</li>
     * <li>object - Integer</li>
     * <li>user control number - Integer</li>
     * <li>next record index - Integer</li>
     * <li>current record index - Integer</li>
     * </ol>
     *
     * <p>
     * The details about each field meaning can be found in protocol documentation.
     * </p>
     *
     * @param eventIndex event log record index to read
     * @return event as array of objects
     */
    @ActionDoc(text = "Returns record from the event log for given index", returns = "Array of values: timestamp, partition, event class, event code, restore, event description, kind og description, source, object, user control object, next event index, current event index")
    public static Object[] satelReadEvent(
            @ParamDoc(name = "eventIndex", text = "index of event to read, -1 for the most recent") int eventIndex) {
        if (!satelIsConnected()) {
            logger.warn("Not connected to communication module or Satel binding not available.");
            return null;
        }
        ReadEventCommand cmd = new ReadEventCommand(eventIndex);
        if (!SatelActionService.satelCommModule.sendCommand(cmd)) {
            logger.error("Unable to read record for given index: {}", eventIndex);
            return null;
        } else if (cmd.isEmpty()) {
            logger.warn("No record under given index: {}", eventIndex);
            return null;
        } else {
            EventDescription desc = getEventDescription(cmd.getEventCode(), cmd.isRestore());
            return new Object[] { new DateTimeType(cmd.getTimestamp()), cmd.getPartition(), cmd.getEventClass(),
                    cmd.getEventCode(), cmd.isRestore(), desc.eventText, desc.descKind, cmd.getSource(),
                    cmd.getObject(), cmd.getUserControlNumber(), cmd.getNextIndex(), cmd.getCurrentIndex() };
        }
    }

    /**
     * Reads name of alarm system's device.
     * Device types:
     * <ul>
     * <li>PARTITION</li>
     * <li>ZONE</li>
     * <li>USER</li>
     * <li>EXPANDER</li>
     * <li>LCD</li>
     * <li>OUTPUT</li>
     * <li>TIMER</li>
     * <li>TELEPHONE</li>
     * <li>OBJECT</li>
     * </ul>
     *
     * @param deviceType type of the device
     * @param deviceNumber number of the device
     * @return string representing device's name or <code>null</code> if device is not present or an error occurred
     */
    @ActionDoc(text = "Returns name of a device.")
    public static String satelReadDeviceName(
            @ParamDoc(name = "deviceType", text = "type of the device, one of: PARTITION, ZONE, USER, EXPANDER, LCD, OUTPUT, TIMER, TELEPHONE, OBJECT") String deviceType,
            @ParamDoc(name = "deviceNumber", text = "number of the device to read") int deviceNumber) {
        if (!satelIsConnected()) {
            logger.warn("Not connected to communication module or Satel binding not available.");
            return null;
        }

        DeviceType devType;
        try {
            devType = DeviceType.valueOf(deviceType.toUpperCase());
        } catch (Exception e) {
            logger.warn("Invalid device type given: {}", deviceType);
            return null;
        }
        ReadDeviceInfoCommand cmd = new ReadDeviceInfoCommand(devType, deviceNumber);
        if (!SatelActionService.satelCommModule.sendCommand(cmd)) {
            logger.warn("Unable to read device info: {}, {}", deviceType, deviceNumber);
            return null;
        }

        try {
            return cmd.getName(SatelActionService.satelCommModule.getTextEncoding());
        } catch (UnsupportedEncodingException e) {
            logger.error("Unsupported encoding: {}", SatelActionService.satelCommModule.getTextEncoding());
            return null;
        }
    }

    @ActionDoc(text = "Overrides configured user code. It will be used for all operations that require authorization.")
    public static void satelSetUserCode(@ParamDoc(name = "userCode", text = "user code to set") String userCode) {
        if (SatelActionService.satelCommModule == null) {
            logger.debug("Satel communication module not available - execution aborted!");
        } else {
            SatelActionService.satelCommModule.setUserCode(userCode);
        }
    }

    @ActionDoc(text = "Reverts user code to the one configured in settings.")
    public static void satelResetUserCode() {
        if (SatelActionService.satelCommModule == null) {
            logger.debug("Satel communication module not available - execution aborted!");
        } else {
            SatelActionService.satelCommModule.resetUserCode();
        }
    }

    private static class EventDescription {
        String eventText;
        int descKind;

        EventDescription(String eventText, int descKind) {
            this.eventText = eventText;
            this.descKind = descKind;
        }
    }

    private static EventDescription getEventDescription(int eventCode, boolean restore) {
        String mapKey = String.format("%d_%b", eventCode, restore);
        EventDescription eventDesc = eventDescriptions.get(mapKey);
        if (eventDesc != null) {
            return eventDesc;
        }
        ReadEventDescCommand cmd = new ReadEventDescCommand(eventCode, restore, true);
        if (!SatelActionService.satelCommModule.sendCommand(cmd)) {
            logger.error("Unable to read event description: {}, {}", eventCode, restore);
            return new EventDescription("Unable to read", 0);
        } else {
            try {
                eventDesc = new EventDescription(cmd.getText(SatelActionService.satelCommModule.getTextEncoding()),
                        cmd.getKind());
                eventDescriptions.put(mapKey, eventDesc);
                return eventDesc;
            } catch (UnsupportedEncodingException e) {
                logger.error("Unsupported encoding: {}", SatelActionService.satelCommModule.getTextEncoding());
                return new EventDescription("Unsupported encoding", 0);
            }
        }
    }

}
