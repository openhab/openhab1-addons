/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
