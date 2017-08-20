/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.device.remote;

import eu.aleon.aleoncean.device.DeviceEEPF60202;
import eu.aleon.aleoncean.device.RemoteDevice;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.rxtx.ESP3Connector;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class RemoteDeviceEEPF60202 extends DeviceEEPF60202 implements RemoteDevice {

    public RemoteDeviceEEPF60202(final ESP3Connector conn, final EnOceanId addressRemote, final EnOceanId addressLocal) {
        super(conn, addressRemote, addressLocal);
    }

}
