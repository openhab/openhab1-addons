/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powermax.internal.message;

import org.openhab.binding.powermax.internal.state.PowerMaxPanelSettings;
import org.openhab.binding.powermax.internal.state.PowerMaxState;

/**
 * A class for ZONESTYPE message handling
 *
 * @author lolodomo
 * @since 1.9.0
 */
public class PowerMaxZonesTypeMessage extends PowerMaxBaseMessage {

    /**
     * Constructor
     *
     * @param message
     *            the received message as a buffer of bytes
     */
    public PowerMaxZonesTypeMessage(byte[] message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerMaxState handleMessage() {
        super.handleMessage();

        byte[] message = getRawData();
        int rowCnt = message[3] & 0x000000FF;

        for (int i = 1; i <= 8; i++) {
            int zoneIdx = (rowCnt - 1) * 8 + i;
            int zoneInfo = (message[3 + i] & 0x000000FF) - 0x0000001E;
            PowerMaxPanelSettings.getThePanelSettings().updateZoneInfo(zoneIdx, zoneInfo);
        }

        return null;
    }

}
