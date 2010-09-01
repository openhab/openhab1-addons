package org.openhab.binding.bluetooth.internal;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The bluetooth service runs as a seperate thread and retriggers a device discovery
 * in a configurable frequency.
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public class BTDeviceDiscoveryService extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(BTDeviceDiscoveryService.class); 

	private Set<RemoteDevice> newDevices = new HashSet<RemoteDevice>();
	private Set<RemoteDevice> oldDevices = newDevices;
	
	private boolean interrupted = false;

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}

	@Override
	public void run() {
		try {
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			logger.info("Initializing local bluetooth device ({}, {})", localDevice.getBluetoothAddress(), localDevice.getFriendlyName());
			DiscoveryAgent agent = localDevice.getDiscoveryAgent();
			final Object inquiryCompletedEvent = new Object();

			// this is the call back for the bluetooth driver
			DiscoveryListener listener = new DiscoveryListener() {
				
				public void deviceDiscovered(RemoteDevice btDevice,
						DeviceClass cod) {
					if (!newDevices.contains(btDevice)) {
						newDevices.add(btDevice);
						if (!oldDevices.contains(btDevice)) {
							try {
								logger.debug("Device discovered: {} ({})", btDevice.getBluetoothAddress(), btDevice.getFriendlyName(true));
							} catch (IOException e) {
								// no friendly name accessible, let's ignore that
							}
						}
					}
				}

				public void inquiryCompleted(int discType) {
					// check if any device has disappeared
					for (RemoteDevice device : oldDevices) {
						if (newDevices.contains(device))
							continue;
						try {
							logger.debug("Device out of range: {} ({})", device.getBluetoothAddress(), device.getFriendlyName(false));
						} catch (IOException e) {
							// no friendly name accessible, let's ignore that
						}
					}

					oldDevices = new HashSet<RemoteDevice>(newDevices);
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

			while (!interrupted) {
				long starttime = new Date().getTime();
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
				if(!interrupted) {
					long sleeptime = BluetoothConfig.refreshRate * 1000L - (new Date().getTime() - starttime);
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
}
