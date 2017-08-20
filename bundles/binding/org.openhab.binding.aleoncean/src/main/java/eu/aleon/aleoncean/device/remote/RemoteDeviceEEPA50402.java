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
 *    Stephan Meyer - split out code for EPP A5-04-02
 */
package eu.aleon.aleoncean.device.remote;

import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.radio.RadioPacket4BS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPA504;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPA50402;
import eu.aleon.aleoncean.rxtx.ESP3Connector;

/**
 * Implementation for EPP A5-04-02.
 * @author Stephan Meyer {@literal <smeyersdev@gmail.com>}
 */
public class RemoteDeviceEEPA50402 extends RemoteDeviceEEPA504 {

  
    public RemoteDeviceEEPA50402(final ESP3Connector conn,
                                 final EnOceanId addressRemote,
                                 final EnOceanId addressLocal) {
        super(conn, addressRemote, addressLocal);
    }
    
    @Override
    protected final UserDataEEPA504 createUserData(final RadioPacket4BS packet){
    	return  new UserDataEEPA50402(packet.getUserDataRaw());
    }
}
