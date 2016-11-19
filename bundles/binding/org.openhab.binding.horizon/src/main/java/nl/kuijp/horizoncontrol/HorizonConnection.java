/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package nl.kuijp.horizoncontrol;

import java.io.Closeable;
import java.io.IOException;

/**
 * Class representing the connection to the HorizonBox
 * 
 * @author J Kuijpers
 * @since 1.9.0
 */
class HorizonConnection implements Closeable {

	private RfbProto rfb;

	HorizonConnection(String host, int port) throws Exception {
		rfb = new RfbProto(host, port);
	}

	/**
	 * Sends the key commands to the {@link RfbProto}. The Integer representing the
	 * key is defined in {@link Keys}
	 *
	 */
	public void sendKey(final Integer key) throws Exception {
		rfb.writeKeyDown(key);
		rfb.writeKeyUp(key);
		rfb.writeBuffer();
	}

	/**
	 * Closes the {@link RfbProto}
	 *
	 */
	public void close() throws IOException {
		rfb.close();
	}
}
