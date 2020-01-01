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
package org.openhab.binding.lightwaverf.internal.message;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRfCommandOk;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfHeatInfoRequest;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfRoomDeviceMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfRoomMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfSerialMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfVersionMessage;

/**
 * @author Neil Renaud
 * @since 1.7.0
 */
public interface LightwaveRFMessageListener {

    public void roomDeviceMessageReceived(LightwaveRfRoomDeviceMessage message);

    public void roomMessageReceived(LightwaveRfRoomMessage message);

    public void serialMessageReceived(LightwaveRfSerialMessage message);

    public void okMessageReceived(LightwaveRfCommandOk message);

    public void versionMessageReceived(LightwaveRfVersionMessage message);

    public void heatInfoMessageReceived(LightwaveRfHeatInfoRequest command);

}
