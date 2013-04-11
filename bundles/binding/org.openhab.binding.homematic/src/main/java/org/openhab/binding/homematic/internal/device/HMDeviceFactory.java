/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.homematic.internal.device;

import org.openhab.binding.homematic.internal.ccu.CCURF;
import org.openhab.binding.homematic.internal.device.physical.rf.DefaultHMRFDevice;
import org.openhab.binding.homematic.internal.device.physical.rf.HMRFDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides means of creating devices from DeviceDescription objects.
 * 
 * @author Mathias Ewald
 * 
 */
public class HMDeviceFactory {

    private static final Logger logger = LoggerFactory.getLogger(HMDeviceFactory.class);

    public static HMRFDevice createRFDevice(CCURF ccu, String address) {
        if (ccu == null) {
            throw new IllegalArgumentException("ccu must no be null");
        }
        if (address == null) {
            throw new IllegalArgumentException("address must no be null");
        }
        try {
            return new DefaultHMRFDevice(ccu, address);
        } catch (RuntimeException e) {
            logger.trace("Could not create Device with address " + address, e);
            return null;
        }
    }

}
