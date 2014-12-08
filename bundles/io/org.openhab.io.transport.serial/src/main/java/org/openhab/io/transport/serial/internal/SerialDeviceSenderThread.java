/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.io.transport.serial.internal;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.openhab.io.transport.serial.SerialDeviceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sends data to serial device.
 * 
 * @author Johannes Engelke <info@johannes-engelke.de>
 * @since 1.7.0
*/
public class SerialDeviceSenderThread extends Thread {
	
	private final Logger logger = LoggerFactory.getLogger(SerialDeviceSenderThread.class);
	
	private final SerialDeviceHandler device;
	private final Queue<String> sendQueue = new ConcurrentLinkedQueue<String>();
	
	public SerialDeviceSenderThread(SerialDeviceHandler device) {
		super();
		this.device = device;
	}
	
	public void send(String message){
		sendQueue.add(message);
	}
	
	
	
	@Override
	public void run() {
		while(!isInterrupted()){
			String message = sendQueue.poll();
			if(message != null){
				if(!message.endsWith("\n")){
					message += "\n";
				}
				device.writeMessage(message);
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.error("Failed to sleep send thread: " + e);
			}
		}
	}

}