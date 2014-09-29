/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.bluetooth.internal;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

import org.openhab.binding.bluetooth.BluetoothEventHandler;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

/**
 * This class is registered as a service and depends on {@link BluetoothEventHandler}s to process the results
 * of the device discovery.
 * 
 * When this service is activated, the bluetooth discovery runs as a separate thread and retriggers a device discovery
 * in a configurable frequency.
 * 
 * The device discovery is not done, if there are no active event handlers registered.
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public class BTDeviceDiscoveryService extends Thread implements ManagedService {
	
	private static final Logger logger = LoggerFactory.getLogger(BTDeviceDiscoveryService.class); 

	// the namespace for configuration entries
	private static final String SERVICE_PID = "bluetooth";
	
	// allows the user to define a refresh rate
	private static final String REFRESH_RATE = "refresh";

	/** the frequency of how often a new Bluetooth device scan is triggered in seconds */
	public int refreshRate = 30;
	
	// a set to collect all devices currently in range
	private Set<RemoteDevice> newDevices = new HashSet<RemoteDevice>();
	
	// a copy of the last discovery result to be able to see when devices disappear
	private Set<RemoteDevice> oldDevices = newDevices;
	
	final private Set<BluetoothEventHandler> eventHandler = Collections.synchronizedSet(new HashSet<BluetoothEventHandler>());
	
	// for interrupting this thread
	private boolean interrupted = false;

	public BTDeviceDiscoveryService() {
		super("Bluetooth Discovery Service");
	}
	
	public void activate() {
		// start the separate thread for listening to the bluetooth device
		start();
	}
	
	public void deactivate() {
		// try to end the thread smoothly
		setInterrupted(true);
	}
	
	public void addBluetoothEventHandler(BluetoothEventHandler handler) {
		eventHandler.add(handler);
	}

	public void removeBluetoothEventHandler(BluetoothEventHandler handler) {
		eventHandler.remove(handler);
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if(config!=null) {
			String value = (String) config.get(REFRESH_RATE);
			try {
				int newRefreshRate = Integer.parseInt(value);
				refreshRate = newRefreshRate;
			} catch(NumberFormatException e) {
				logger.warn("Invalid configuration value '{}' for parameter '" + SERVICE_PID + ":" + REFRESH_RATE+ "'", value);
			}
		}
	}

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}

	@Override
	public void run() {
		try {
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			logger.debug("Initializing local bluetooth device ({}, {})", localDevice.getBluetoothAddress(), localDevice.getFriendlyName());
			DiscoveryAgent agent = localDevice.getDiscoveryAgent();
			final Object inquiryCompletedEvent = new Object();

			// this is the call back for the bluetooth driver
			DiscoveryListener listener = new DiscoveryListener() {
				
				public void deviceDiscovered(RemoteDevice btDevice,
						DeviceClass cod) {
					if (!newDevices.contains(btDevice)) {
						newDevices.add(btDevice);
						if (!oldDevices.contains(btDevice)) {							
							BluetoothDevice device = toBluetoothDevice(btDevice);
							logger.debug("Device discovered: {}", device.toString());
							for(BluetoothEventHandler handler : eventHandler) {
								handler.handleDeviceInRange(device);
							}
						}
					}
				}

				public void inquiryCompleted(int discType) {
					// check if any device has disappeared
					for (RemoteDevice btDevice : oldDevices) {
						if (newDevices.contains(btDevice))
							continue;
						BluetoothDevice device = toBluetoothDevice(btDevice);
						logger.debug("Device out of range: {}", device.toString());
						for(BluetoothEventHandler handler : eventHandler) {
							handler.handleDeviceOutOfRange(device);
						}
					}

					oldDevices = new HashSet<RemoteDevice>(newDevices);
					
					// we now pass the list of all devices in range to the event handlers
					Iterable<BluetoothDevice> devices = Iterables.transform(newDevices, new Function<RemoteDevice, BluetoothDevice>() {
						public BluetoothDevice apply(RemoteDevice from) {
							return toBluetoothDevice(from);
						}
					});
					for(BluetoothEventHandler handler : eventHandler) {
						handler.handleAllDevicesInRange(devices);
					}
					
					newDevices.clear();
					synchronized (inquiryCompletedEvent) {
						// tell the main thread that we are done
						inquiryCompletedEvent.notifyAll();
					}
				}

				public void serviceSearchCompleted(int transID, int respCode) {
				}

				public void servicesDiscovered(int transID,
						ServiceRecord[] servRecord) {
				}
			};

			// this is the main loop, which will run as long as the thread is not marked as interrupted
			while (!interrupted) {
				long starttime = new Date().getTime();
				
				// check if we at all need to run the bluetooth discovery
				boolean runDiscovery = false;
				for(BluetoothEventHandler handler : eventHandler) {
					if(handler.isActive()) {
						runDiscovery = true;
						break;
					}
				}
				
				if(runDiscovery) {
					synchronized (inquiryCompletedEvent) {
						try {
							logger.debug("Launching bluetooth device discovery...");
							boolean started = agent.startInquiry(
									DiscoveryAgent.GIAC, listener);
							if (started) {
								inquiryCompletedEvent.wait();
							}
						} catch (BluetoothStateException e) {
							logger.error("Error while starting the bluetooth agent", e);
						} catch (InterruptedException e) {
							interrupted = true;
						}
					}
				}
				if(!interrupted) {
					// let this thread sleep until the next discovery should be done
					long sleeptime = refreshRate * 1000L - (new Date().getTime() - starttime);
					if(sleeptime>0) {
						logger.debug("Sleeping for {} s...", sleeptime/1000.0);
						try {
							Thread.sleep(sleeptime);
						} catch(InterruptedException e) {
							interrupted = true;
						}
					}
				}
			}
		} catch (BluetoothStateException e) {
			logger.error("Error while initializing local bluetooth device.", e);
		}
	}
	
	/**
	 * transforms a {@link RemoteDevice} object from the bluecove API to our own datastructure for bluetooth devices
	 * 
	 * @param btDevice the device coming from the bluecove API
	 * @return an instance of our own bluetooth device data structure
	 */
	private static BluetoothDevice toBluetoothDevice(RemoteDevice btDevice) {
		String address = btDevice.getBluetoothAddress();
		String friendlyName = "";
		try {
			friendlyName = btDevice.getFriendlyName(false);
		} catch (IOException e) {
			// no friendly name accessible, let's ignore that
		}
		boolean paired = btDevice.isTrustedDevice();
		return new BluetoothDevice(address, friendlyName, paired);
	}
}
