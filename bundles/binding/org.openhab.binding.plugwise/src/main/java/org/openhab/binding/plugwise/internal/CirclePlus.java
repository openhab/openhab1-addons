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
package org.openhab.binding.plugwise.internal;

import org.joda.time.DateTime;
import org.openhab.binding.plugwise.PlugwiseCommandType;
import org.openhab.binding.plugwise.protocol.AcknowledgeMessage;
import org.openhab.binding.plugwise.protocol.ClockSetRequestMessage;
import org.openhab.binding.plugwise.protocol.Message;
import org.openhab.binding.plugwise.protocol.RealTimeClockGetRequestMessage;
import org.openhab.binding.plugwise.protocol.RealTimeClockGetResponseMessage;
import org.openhab.binding.plugwise.protocol.RoleCallRequestMessage;
import org.openhab.binding.plugwise.protocol.RoleCallResponseMessage;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that represents a Plugwise Circle+ device
 *
 * Circle+ are special Circles. Typically there is one Circle+ in a Plugwise network, and it serves as a master
 * controller in the network, providing Clock data to the other Circles, relay information to the Stick and so forth
 *
 * Every 24h the Clock of the Circle+ is set identical to the hosts' Clock
 *
 * The Circle+ also does "RoleCall"s, e.g. polling the Plugwise network in order to make an inventory of all the
 * availble Circles
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class CirclePlus extends Circle {

    private static final Logger logger = LoggerFactory.getLogger(CirclePlus.class);

    public static final String CIRCLE_PLUS_JOB_DATA_KEY = "CirclePlus";

    protected DateTime realtimeClock;

    public CirclePlus(String mac, Stick stick, String friendly) {
        super(mac, stick, friendly);
        type = DeviceType.CirclePlus;
    }

    public boolean setClock() {
        return setClock(DateTime.now());
    }

    public boolean setClock(DateTime stamp) {
        ClockSetRequestMessage message = new ClockSetRequestMessage(MAC, stamp);
        stick.sendMessage(message);
        return true;
    }

    /**
     * Role calling is basically asking the Circle+ to return all the devices known to it. Up to 64 devices
     * are supported in a PW network, and role calling is done by sequentially sendng RoleCallMessages for all
     * possible IDs in the network (ID = number from 1 to 63)
     *
     * @param id of the device to rolecall
     */
    public void roleCall(int id) {
        if (id >= 0 && id < 64) {
            RoleCallRequestMessage request = new RoleCallRequestMessage(MAC, id);
            stick.sendMessage(request);
        }
    }

    public DateTime getRealTimeClock() {
        if (realtimeClock != null) {
            return realtimeClock;
        } else {
            updateRealTimeClock();
            return null;
        }
    }

    public void updateRealTimeClock() {
        RealTimeClockGetRequestMessage message = new RealTimeClockGetRequestMessage(MAC);
        stick.sendMessage(message);
    }

    @Override
    public boolean processMessage(Message message) {
        if (message != null) {
            switch (message.getType()) {
                case DEVICE_ROLECALL_RESPONSE:
                    if (((RoleCallResponseMessage) message).getNodeID() < 63
                            && !((RoleCallResponseMessage) message).getNodeMAC().equals("FFFFFFFFFFFFFFFF")) {
                        // add e new node
                        PlugwiseDevice device = stick.getDeviceByMAC(((RoleCallResponseMessage) message).getNodeMAC());

                        if (device == null) {
                            // currently it is always assumed the device is a Circle, it would be better to
                            // detect the actual device type by sending an InformationRequestMessage to the MAC
                            // and use the device type in the InformationResponseMessage for dynamically adding
                            // devices
                            device = new Circle(((RoleCallResponseMessage) message).getNodeMAC(), stick,
                                    ((RoleCallResponseMessage) message).getNodeMAC());
                            stick.addDevice(device);
                            logger.debug("Added a Circle with MAC {} to the cache", device.getMAC());
                        }

                        if (device instanceof Circle) {
                            ((Circle) device).updateInformation();
                            ((Circle) device).calibrate();
                        }

                        // check if there is any other on the network
                        roleCall(((RoleCallResponseMessage) message).getNodeID() + 1);
                    }
                    return true;

                case REALTIMECLOCK_GET_RESPONSE:
                    realtimeClock = ((RealTimeClockGetResponseMessage) message).getTime();
                    postUpdate(MAC, PlugwiseCommandType.REALTIMECLOCK, realtimeClock);
                    return true;

                case ACKNOWLEDGEMENT:
                    if (((AcknowledgeMessage) message).isExtended()) {
                        switch (((AcknowledgeMessage) message).getExtensionCode()) {

                            case CLOCKSET:
                                logger.debug("Circle+ Clock is set");
                                break;

                            default:
                                return stick.processMessage(message);
                        }
                    }

                default:
                    return super.processMessage(message);
            }
        } else {
            return false;
        }
    }

    public static class SetClockJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {

            // get the reference to the Stick
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            CirclePlus circlePlus = (CirclePlus) dataMap.get(CIRCLE_PLUS_JOB_DATA_KEY);
            circlePlus.setClock();
        }
    }
}
