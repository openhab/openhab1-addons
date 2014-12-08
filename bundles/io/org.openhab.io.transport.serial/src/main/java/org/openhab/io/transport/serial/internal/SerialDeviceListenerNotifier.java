/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */


package org.openhab.io.transport.serial.internal;

import org.openhab.io.transport.serial.SerialListener;

/**
 * Notifies serial device listeners about updates.
 * 
 * @author Johannes Engelke <info@johannes-engelke.de>
 * @since 1.7.0
*/
public class SerialDeviceListenerNotifier implements Runnable {
	
	private SerialListener listener;
	private String data;
	
	public SerialDeviceListenerNotifier(SerialListener listener, String data) {
		this.listener = listener;
		this.data = data;
	}

	@Override
	public void run() {
		listener.dataRecived(data);
	}

}