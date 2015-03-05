/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal.protocol.spa20;

import org.openhab.binding.primare.internal.protocol.spa20.PrimareSPA20MessageFactory;
import org.openhab.binding.primare.internal.protocol.spa20.PrimareSPA20ResponseFactory;
import org.openhab.binding.primare.internal.protocol.PrimareSerialConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for Primare SP31/SP31.7/SPA20/SPA21 serial communication.
 * 
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public class PrimareSPA20SerialConnector extends PrimareSerialConnector {
	
	public PrimareSPA20SerialConnector(String deviceId, String serialPortName) {
		super(deviceId, serialPortName, 
		      new PrimareSPA20MessageFactory(),
		      new PrimareSPA20ResponseFactory());
	}
}
