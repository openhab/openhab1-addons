/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.serial;


import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.openhab.io.transport.serial.internal.SerialDevice;
import org.openhab.io.transport.serial.internal.SerialDeviceListenerNotifier;
import org.openhab.io.transport.serial.internal.SerialDeviceSenderThread;

/**
 * Handles communication with serial device.
 * 
 * @author Johannes Engelke <info@johannes-engelke.de>
 * @since 1.7.0
*/
public class SerialDeviceHandler extends SerialDevice implements SerialPortEventListener {
	
	private List<SerialListener> listeners = new ArrayList<SerialListener>();
	
	private SerialDeviceSenderThread senderThread = new SerialDeviceSenderThread(this);
	
	private Executor reciveExecutor = Executors.newCachedThreadPool();
	

	public SerialDeviceHandler(String deviceName) {
		super(deviceName);
	}
	
	public SerialDeviceHandler(String key, String value) {
		super(key, value);
	}	
	
	
	public void send(String message) throws IOException{
		this.writeMessage(message);
	}
	
	public void registerListener(SerialListener listener){
		if(listener != null){
			listeners.add(listener);
		}
	}
	
	public void unregisterListener(SerialListener listener){
		if(listener != null){
			listeners.remove(listener);
		}
	}
	
	public List<SerialListener> getListeners(){
		return listeners;
	}
	
	public void open() throws SerialDeviceException{
		this.openHardware();
		this.senderThread.start();
		this.serialPort.notifyOnDataAvailable(true);
		try {
			this.serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
			log.error("To many listeners: " + e);
		}
	}
	
	public void close() {
		this.senderThread.interrupt();
		this.closeHardware();
		
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String message = br.readLine();
				log.debug("Received raw message from serial device: " + message);
				for(SerialListener listener : listeners){
					reciveExecutor.execute(new SerialDeviceListenerNotifier(listener, message));
				}
			} catch (IOException e) {
				log.error("Exception while reading from serial port", e);
			}
		}
		
	}
}