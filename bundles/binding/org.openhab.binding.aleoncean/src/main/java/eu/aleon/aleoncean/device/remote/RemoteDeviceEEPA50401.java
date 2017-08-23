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
 *    Stephan Meyer - split out code for EPP A5-04-01
 */
package eu.aleon.aleoncean.device.remote;

import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.radio.RadioPacket4BS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPA504;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPA50401;
import eu.aleon.aleoncean.rxtx.ESP3Connector;

/**
 * Implementation for EPP A5-04-01.
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 * @author Stephan Meyer {@literal <smeyersdev@gmail.com>}
 */
public class RemoteDeviceEEPA50401 extends RemoteDeviceEEPA504 {

	public RemoteDeviceEEPA50401(final ESP3Connector conn,
								final EnOceanId addressRemote,
								final EnOceanId addressLocal) {
		super(conn, addressRemote, addressLocal);
	}

	@Override
	protected UserDataEEPA504 createUserData(RadioPacket4BS packet) {
		return  new UserDataEEPA50401(packet.getUserDataRaw());
	}

}
