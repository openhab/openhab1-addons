/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.gpio.linux;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.openhab.io.gpio.GPIO;
import org.openhab.io.gpio.GPIOPin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of <code>GPIO</code> interface for boards running
 * Linux OS. Based on kernel GPIO framework exposed to user space
 * through <code>sysfs</code> pseudo file system.
 * 
 * @author Dancho Penev
 * @since 1.5.0
 */
public class GPIOLinux implements GPIO, ManagedService {

	private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
	private static final String SYSFS_VFSTYPE = "sysfs";
	private static final String MTAB_PATH = "/proc/mounts";
	private static final String MTAB_FIELD_SEPARATOR = " ";
	private static final long GPIOLOCK_TIMEOUT = 10;
	private static final TimeUnit GPIOLOCK_TIMEOUT_UNITS = TimeUnit.SECONDS;
	private static final String PROP_DEBOUNCE_INTERVAL = "debounce";

	private static final Logger logger = LoggerFactory.getLogger(GPIOLinux.class);

	/** Path to directory where <code>sysfs</code> is mounted. */ 
	private String sysFS = null;

	/** Default debounce interval in milliseconds */
	private volatile long defaultDebounceInterval = 0;

	/** GPIO subsystem read/write lock. */
	private final ReentrantReadWriteLock gpioLock = new ReentrantReadWriteLock();
	
	/** Database for GPIO pins which are in use. */
	private final HashMap<GPIOPin, Integer> gpioRegistry = new HashMap<GPIOPin, Integer>();

	/**
	 * Discovers existing mount point for <code>sysfs</code> pseudo file system.
	 */
	public GPIOLinux() {
		try {
			sysFS = getMountPoint(SYSFS_VFSTYPE);
		} catch (IOException e) {
			logger.error("Automatic mount point discovering for pseudo file system '" + SYSFS_VFSTYPE + "' failed. "
					+ "If 'procfs' isn't mounted and mount point is set in configuration file this error can be omitted. "
					+ "Error: " + e.getMessage());
		}
	}

	/** 
	 * Called when <code>Configuration Admin</code> detects configuration
	 * change. Sets manually configured mount points for <code>sysfs</code>
	 * pseudo file systems, overwrites this what was discovered. Also set
	 * default debounce interval if exist. 
	 */
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {

		if (properties != null) {

			String propSysFS = (String) properties.get(SYSFS_VFSTYPE);
			if (propSysFS != null) {
				try {
					if (isFSMounted(SYSFS_VFSTYPE, propSysFS)) {
						sysFS = propSysFS;
					} else {
						logger.error("Configured mount point is invalid, '" + SYSFS_VFSTYPE + "' isn't mounted at '"
								+ propSysFS + "'");
						throw new ConfigurationException(SYSFS_VFSTYPE, "Configured mount point is invalid, '"
								+ SYSFS_VFSTYPE + "' isn't mounted at '" + propSysFS + "'");
					}
				} catch (IOException e) {
					logger.error("Checking whether pseudo file system '" + SYSFS_VFSTYPE + "' is mounted or not failed. "
							+ "If 'procfs' isn't mounted this error can be omitted. Error: " + e.getMessage());
					sysFS = propSysFS;
				}
			}

			String propDebounceInterval = (String) properties.get(PROP_DEBOUNCE_INTERVAL);
			if (propDebounceInterval != null) {
				try {
					long debounceInterval = Long.parseLong(propDebounceInterval);
					if (debounceInterval >= 0) {
						defaultDebounceInterval = debounceInterval;
					} else {
						logger.error("Configured " + PROP_DEBOUNCE_INTERVAL + " is invalid, must not be negative value");
						throw new ConfigurationException(PROP_DEBOUNCE_INTERVAL, "Configured " + PROP_DEBOUNCE_INTERVAL
								+ " is invalid, must not be negative value");	
					}
				} catch (NumberFormatException e) {
					logger.error("Configured " + PROP_DEBOUNCE_INTERVAL + " is invalid, must be numeric value");
					throw new ConfigurationException(PROP_DEBOUNCE_INTERVAL, "Configured " + PROP_DEBOUNCE_INTERVAL
							+ " is invalid, must be numeric value");
				}
			}
		}
	}

	/**
	 * Determines mount point for given file system type. 
	 * 
	 * @param vfsType the type of file system to search for
	 * @return the first found mount point if the file system is mounted,
	 * 		<code>null</code> otherwise
	 * @throws IOException if reading of "mtab" file fails
	 */
	private String getMountPoint(String vfsType) throws IOException {

		List<String> mtabLines = Files.readAllLines(Paths.get(MTAB_PATH), DEFAULT_ENCODING);
		
		for(String mtabRecord : mtabLines) {
			String[] mtabRecordFields = mtabRecord.split(MTAB_FIELD_SEPARATOR);

			if (mtabRecordFields[2].compareToIgnoreCase(vfsType) == 0) {
				return mtabRecordFields[1];
			}
		}

		return null;
	}

	/**
	 * Checks whether file system from given type is mounted or not
	 * at specific location.
	 * 
	 * @param vfsType the type of file system to check
	 * @param mountPoint directory at which file system should be
	 * 		mounted
	 * @return <code>true</code> if the file system of provided type is
	 * 		mounted at provided location, <code>false</code> otherwise
	 * @throws IOException if reading of "mtab" file fails
	 */
	private boolean isFSMounted(String vfsType, String mountPoint) throws IOException {

		List<String> mtabLines = Files.readAllLines(Paths.get(MTAB_PATH), DEFAULT_ENCODING);
		
		for(String mtabRecord : mtabLines) {

			String[] mtabRecordFields = mtabRecord.split(MTAB_FIELD_SEPARATOR);

			if ((mtabRecordFields[2].compareToIgnoreCase(vfsType) == 0) && (mtabRecordFields[1].compareToIgnoreCase(mountPoint) == 0)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 *  Exports the pin to user space, creates and initializes the
	 *  backend object representing the pin. Updates the registry for
	 *  initialized pins. 
	 */
	public GPIOPin reservePin(Integer pinNumber) throws IOException {

		final String SYSFS_CLASS_GPIO = sysFS + "/class/gpio/";

		GPIOPinLinux pin = null;

		/* Variable 'sysFS' may be null if mandatory pseudo file system 'sysfs' isn't mounted or mount point can't be determined. */
		if (sysFS == null) {
			throw new IOException("Mount point for '" + SYSFS_VFSTYPE + "' isn't configured and can't be determined");
		}

		/* Sanity check, negative pin number is illegal. */
		if (pinNumber < 0) {
			throw new IllegalArgumentException("Unsupported argument for 'pinNumber' parameter (" + pinNumber + ")");
		}

		/* Acquiring write lock guarantees atomic check/set operation */ 
		try {
			if (gpioLock.writeLock().tryLock(GPIOLOCK_TIMEOUT, GPIOLOCK_TIMEOUT_UNITS)) {
				try {
					if (gpioRegistry.containsValue(pinNumber)) {
						throw new IllegalArgumentException("The pin with number '" + pinNumber + "' is already registered");
					}

					/* Exports the pin to user space. */
					Files.write(Paths.get(SYSFS_CLASS_GPIO + "export"), pinNumber.toString().getBytes());
					
					/* Create backend object */
					pin = new GPIOPinLinux(pinNumber, SYSFS_CLASS_GPIO + "gpio" + pinNumber, defaultDebounceInterval);

					/* Register the pin */
					gpioRegistry.put(pin, pinNumber);
				} finally {
					gpioLock.writeLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Write GPIO lock can't be aquired for " + GPIOLOCK_TIMEOUT + " " + GPIOLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for write GPIO lock");
		}

		return pin;
	}

	/**
	 * Removes the pin from internal registry, uninitialize the object
	 * and stop pin export to user space.
	 */
	public void releasePin(GPIOPin pin) throws IOException {
		
		try {
			if (gpioLock.writeLock().tryLock(GPIOLOCK_TIMEOUT, GPIOLOCK_TIMEOUT_UNITS)) {
				try {

					final String SYSFS_CLASS_GPIO = sysFS + "/class/gpio/";
					
					/* Unregister the pin */
					Integer pinNumber = gpioRegistry.remove(pin);
					if (pinNumber == null) {
						throw new IllegalArgumentException("The pin object isn't registered");
					}

					((GPIOPinLinux) pin).stopEventProcessing();

					/* May throw "IllegalArgumentException" if the pin isn't exported */ 
					Files.write(Paths.get(SYSFS_CLASS_GPIO + "unexport"), pinNumber.toString().getBytes());
				} finally {
					gpioLock.writeLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Write GPIO lock can't be aquired for " + GPIOLOCK_TIMEOUT + " " + GPIOLOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for write GPIO lock");
		}
	}

	public long getDefaultDebounceInterval() {
		return defaultDebounceInterval;
	}
}
