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

import java.util.Calendar;

import org.openhab.binding.plugwise.PlugwiseCommandType;
import org.openhab.binding.plugwise.protocol.AnnounceAwakeRequestMessage;
import org.openhab.binding.plugwise.protocol.AnnounceAwakeRequestMessage.AwakeReason;
import org.openhab.binding.plugwise.protocol.BroadcastGroupSwitchResponseMessage;
import org.openhab.binding.plugwise.protocol.InformationRequestMessage;
import org.openhab.binding.plugwise.protocol.Message;
import org.openhab.binding.plugwise.protocol.ModuleJoinedNetworkRequestMessage;

/**
 * A class that represents a Plugwise Switch device.
 *
 * The Switch is a mountable wireless switch with one or two buttons depending on what parts are in place. When one
 * button is used this corresponds to only using the left button.
 *
 * @author Wouter Born
 * @since 1.9.0
 */
public class Switch extends PlugwiseDevice {

    protected Stick stick;

    protected boolean leftButtonState;
    protected boolean rightButtonState;

    public Switch(String mac, Stick stick, String friendly) {
        super(mac, DeviceType.Switch, friendly);
        this.stick = stick;
    }

    public boolean getLeftButtonState() {
        return leftButtonState;
    }

    public boolean setLeftButtonState(boolean state) {
        leftButtonState = state;
        return true;
    }

    public boolean getRightButtonState() {
        return rightButtonState;
    }

    public boolean setRightButtonState(boolean state) {
        rightButtonState = state;
        return true;
    }

    public boolean setLeftButtonState(String state) {
        if (state != null) {
            if (state.equals("ON") || state.equals("OPEN") || state.equals("UP")) {
                return setLeftButtonState(true);
            } else if (state.equals("OFF") || state.equals("CLOSED") || state.equals("DOWN")) {
                return setLeftButtonState(false);
            }
        }
        return true;
    }

    public boolean setRightButtonState(String state) {
        if (state != null) {
            if (state.equals("ON") || state.equals("OPEN") || state.equals("UP")) {
                return setRightButtonState(true);
            } else if (state.equals("OFF") || state.equals("CLOSED") || state.equals("DOWN")) {
                return setRightButtonState(false);
            }
        }
        return true;
    }

    public void updateInformation() {
        InformationRequestMessage message = new InformationRequestMessage(MAC);
        stick.sendMessage(message);
    }

    @Override
    public boolean processMessage(Message message) {
        if (message != null) {

            Calendar timestamp;

            switch (message.getType()) {
                case ANNOUNCE_AWAKE_REQUEST:
                    AwakeReason awakeReason = ((AnnounceAwakeRequestMessage) message).getAwakeReason();
                    if (awakeReason == AwakeReason.Maintenance || awakeReason == AwakeReason.WakeupButton) {
                        updateInformation();
                    }
                    timestamp = ((AnnounceAwakeRequestMessage) message).getDateTimeReceived();
                    postUpdate(MAC, PlugwiseCommandType.LASTSEEN, timestamp);
                    return true;

                case BROADCAST_GROUP_SWITCH_RESPONSE:
                    timestamp = ((BroadcastGroupSwitchResponseMessage) message).getDateTimeReceived();
                    if (((BroadcastGroupSwitchResponseMessage) message).getPortMask() == 1) {
                        leftButtonState = ((BroadcastGroupSwitchResponseMessage) message).getPowerState();
                        postUpdate(MAC, PlugwiseCommandType.LEFTBUTTONSTATE, leftButtonState);
                        postUpdate(MAC, PlugwiseCommandType.LEFTBUTTONSTATESTAMP, timestamp);
                    } else if (((BroadcastGroupSwitchResponseMessage) message).getPortMask() == 2) {
                        rightButtonState = ((BroadcastGroupSwitchResponseMessage) message).getPowerState();
                        postUpdate(MAC, PlugwiseCommandType.RIGHTBUTTONSTATE, rightButtonState);
                        postUpdate(MAC, PlugwiseCommandType.RIGHTBUTTONSTATESTAMP, timestamp);
                    }
                    postUpdate(MAC, PlugwiseCommandType.LASTSEEN, timestamp);
                    return true;

                case MODULE_JOINED_NETWORK_REQUEST:
                    timestamp = ((ModuleJoinedNetworkRequestMessage) message).getDateTimeReceived();
                    postUpdate(MAC, PlugwiseCommandType.LASTSEEN, timestamp);
                    return true;

                default:
                    // Let's have the Stick a go at this message
                    return stick.processMessage(message);
            }

        } else {
            return false;
        }
    }

    @Override
    public boolean postUpdate(String MAC, PlugwiseCommandType type, Object value) {
        if (MAC != null && type != null && value != null) {
            stick.postUpdate(MAC, type, value);
            return true;
        } else {
            return false;
        }

    }
}
