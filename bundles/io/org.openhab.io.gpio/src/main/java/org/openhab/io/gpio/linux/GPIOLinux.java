/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.gpio.linux;

import java.io.File;
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

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.openhab.config.core.ConfigDispatcher;
import org.openhab.io.gpio.GPIO;
import org.openhab.io.gpio.GPIOPin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
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
public class GPIOLinux implements GPIO, ManagedService, CommandProvider {

	private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
	private static final String SYSFS_VFSTYPE = "sysfs";
	private static final String MTAB_PATH = "/proc/mounts";
	private static final String MTAB_FIELD_SEPARATOR = " ";
	private static final long LOCK_TIMEOUT = 10;
	private static final TimeUnit LOCK_TIMEOUT_UNITS = TimeUnit.SECONDS;
	private static final String PROP_DEBOUNCE_INTERVAL = "debounce";
	private static final String PROP_PINMAP = "pinmap";
	private static final String PINMAP_FIELD_SEPARATOR = "\t";
	private static final String PINMAP_DEFAULT_VALUE = "none";

	private static final Logger logger = LoggerFactory.getLogger(GPIOLinux.class);

	/** Path to directory where <code>sysfs</code> is mounted. */ 
	private String sysFS = null;

	/** Default debounce interval in milliseconds */
	private volatile long defaultDebounceInterval = 0;

	/** Pinmap */
	private String pinMap = PINMAP_DEFAULT_VALUE;

	/** GPIO subsystem read/write lock. */
	private final ReentrantReadWriteLock gpioLock = new ReentrantReadWriteLock();
	
	/** Database for GPIO pins which are in use. */
	private final BidiMap gpioRegistry = new DualHashBidiMap();

	private final ReentrantReadWriteLock pinMapLock = new ReentrantReadWriteLock();

	/** Bidirectional map pin Name<->Number */
	private BidiMap pinNameNumberMap = new DualHashBidiMap(); 

	private HashMap<Integer, String> pinNumberSysfsSuffixMap = new HashMap<Integer, String>();

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
	 * pseudo file systems, overwrites this what was discovered. Also sets
	 * default debounce interval if exist. Sets and loads <code>pinmap</code>
	 * file.
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

			try {
				if (pinMapLock.writeLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
					try {
						pinMap = (String) properties.get(PROP_PINMAP);
						if (pinMap == null) {
							pinMap = PINMAP_DEFAULT_VALUE;
						}
					} finally {
						pinMapLock.writeLock().unlock();
					}
				} else {
					/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
					throw new ConfigurationException(PROP_PINMAP, "Write GPIO pinmap lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString());
				}
			} catch (InterruptedException e) {
				throw new ConfigurationException(PROP_PINMAP, "The thread was interrupted while waiting for write GPIO pinmap lock");
			}

			try {
				loadPinMap();
			} catch (IOException e){
				logger.error("Error occured while loading pinmap file: " + e.getMessage());
				throw new ConfigurationException(PROP_PINMAP, "Error occured while loading pinmap file: " + e.getMessage());	
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

	private void loadPinMap() throws IOException {

		final String PINMAP_FOLDER = ConfigDispatcher.getConfigFolder() + File.separator + "pinmaps";
		final String PINMAP_EXTENSION = ".pinmap";

		try {
			if (pinMapLock.writeLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
				try {
					if (pinMap.equals(PINMAP_DEFAULT_VALUE)) {
						pinNameNumberMap.clear();
						pinNumberSysfsSuffixMap.clear();
						return;
					}

					List<String> pinMapLines = Files.readAllLines(Paths.get(PINMAP_FOLDER + File.separator + pinMap + PINMAP_EXTENSION), DEFAULT_ENCODING);

					BidiMap newPinNameNumberMap = new DualHashBidiMap(); 
					HashMap<Integer, String> newPinNumberSysfsSuffixMap = new HashMap<Integer, String>();
					int pinNumber;
					int lineNumber = 0;

					for(String pinMapRecord : pinMapLines) {

						lineNumber++;

						if (pinMapRecord.isEmpty() || pinMapRecord.startsWith("#")) {
							continue;
						}

						String[] pinMapRecordFields = pinMapRecord.split(PINMAP_FIELD_SEPARATOR);

						if (pinMapRecordFields.length < 2 || pinMapRecordFields.length > 3) {
							throw new IOException("Line " + lineNumber + ": Unsupported number of fields - " + pinMapRecordFields.length);
						}

						if (pinMapRecordFields[0].isEmpty() || pinMapRecordFields[1].isEmpty()) {
							throw new IOException("Line " + lineNumber + ": Pin name and/or number are missing");
						}

						try {
							pinNumber = Integer.parseInt(pinMapRecordFields[1]);
						} catch (NumberFormatException e) {
							throw new IOException("Line " + lineNumber + ": The value in pin number field is not numeric");
						}

						if (newPinNameNumberMap.containsKey(pinMapRecordFields[0]) || newPinNameNumberMap.containsValue(pinNumber)) {
							throw new IOException("Line " + lineNumber + ": Duplicate pin name and/or number");
						}

						newPinNameNumberMap.put(pinMapRecordFields[0], pinNumber);

						if (pinMapRecordFields.length == 3) {
							String sysfsSuffix = pinMapRecordFields[2].replace("${pinName}", pinMapRecordFields[0]).replace("${pinNumber}", pinMapRecordFields[1]);
							if (newPinNumberSysfsSuffixMap.containsValue(sysfsSuffix)) {
								throw new IOException("Line " + lineNumber + ": Duplicate sysfs suffix");
							}
							newPinNumberSysfsSuffixMap.put(pinNumber, sysfsSuffix);
						}
					}

					pinNameNumberMap = newPinNameNumberMap;
					pinNumberSysfsSuffixMap = newPinNumberSysfsSuffixMap;
				} finally {
					pinMapLock.writeLock().unlock();
				}
			} else {
				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Write GPIO pinmap lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for write GPIO pinmap lock");
		}
	}

	/**
	 *  Exports the pin to user space, creates and initializes the
	 *  backend object representing the pin. Updates the registry for
	 *  initialized pins. 
	 */
	public GPIOPin reservePin(String pinName) throws IOException {

		final String SYSFS_CLASS_GPIO = sysFS + "/class/gpio/";

		GPIOPinLinux pin = null;

		/* Variable 'sysFS' may be null if mandatory pseudo file system 'sysfs' isn't mounted or mount point can't be determined. */
		if (sysFS == null) {
			throw new IOException("Mount point for '" + SYSFS_VFSTYPE + "' isn't configured and can't be determined");
		}

		/* Sanity check, empty pin name is illegal. */
		if (pinName.isEmpty()) {
			throw new IllegalArgumentException("Unsupported argument for 'pinName' parameter (" + pinName + ")");
		}

		/* Acquiring write lock guarantees atomic check/set operation */ 
		try {
			if (gpioLock.writeLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
				try {

					Integer pinNumber = getPinNumberByName(pinName);

					if (gpioRegistry.containsValue(pinNumber)) {
						throw new IllegalArgumentException("The pin with number '" + pinNumber + "' is already registered");
					}

					/* Exports the pin to user space. */
					Files.write(Paths.get(SYSFS_CLASS_GPIO + "export"), pinNumber.toString().getBytes());
					
					try {
						if (pinMapLock.readLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
							try {
								/* Create backend object */
								if (pinNumberSysfsSuffixMap.containsKey(pinNumber)) {
									pin = new GPIOPinLinux(pinNumber, pinName, SYSFS_CLASS_GPIO + "gpio" + pinNumberSysfsSuffixMap.get(pinNumber), defaultDebounceInterval);
								} else {
									pin = new GPIOPinLinux(pinNumber, pinName, SYSFS_CLASS_GPIO + "gpio" + pinNumber, defaultDebounceInterval);						
								}
							} finally {
								pinMapLock.readLock().unlock();
							}
						} else {
							/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
							throw new IOException("Read GPIO pinmap lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString());
						}
					} catch (InterruptedException e) {
						throw new IOException("The thread was interrupted while waiting for read GPIO pinmap lock");
					}

					/* Register the pin */
					gpioRegistry.put(pin, pinNumber);
				} finally {
					gpioLock.writeLock().unlock();
				}
			} else {

				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Write GPIO lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString());
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
			if (gpioLock.writeLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
				try {

					final String SYSFS_CLASS_GPIO = sysFS + "/class/gpio/";
					
					/* Unregister the pin */
					Integer pinNumber = (Integer) gpioRegistry.remove(pin);
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
				throw new IOException("Write GPIO lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for write GPIO lock");
		}
	}

	public long getDefaultDebounceInterval() {
		return defaultDebounceInterval;
	}

	public int getPinNumberByName(String pinName) throws IOException {

		int pinNumber;

		try {
			if (pinMapLock.readLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
				try {
					if (pinNameNumberMap.containsKey(pinName)) {
						pinNumber = (Integer) pinNameNumberMap.get(pinName);			
					} else {
						try {
							pinNumber = Integer.parseInt(pinName);
						} catch (NumberFormatException e) {
							throw new IOException("Unsupported, not numeric 'pin' value", e);
						}
					}
				} finally {
					pinMapLock.readLock().unlock();
				}
			} else {
				/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
				throw new IOException("Read GPIO pinmap lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString());
			}
		} catch (InterruptedException e) {
			throw new IOException("The thread was interrupted while waiting for read GPIO pinmap lock");
		}

		return pinNumber;
	}

	public String getHelp() {

		StringBuffer buffer = new StringBuffer();

		buffer.append("---GPIO commands---\n\t");
		buffer.append("gpio globals - lists global variables\n\t");
		buffer.append("gpio pin NUMBER - shows detailed information about pin\n\t");
		buffer.append("gpio pinmap - lists currently used pinmap\n\t");
		buffer.append("gpio pins - lists registered pins\n");

		return buffer.toString();
}

	public Object _gpio(CommandInterpreter console) {

		StringBuffer buffer = new StringBuffer();
		String argument = console.nextArgument();

		if (argument == null) {
			console.println("Error: missing argument(s).");
			console.println();
			console.print(getHelp());
			return null;
		}

		if (argument.equals("globals")) {
			console.println(PROP_DEBOUNCE_INTERVAL + ": " + defaultDebounceInterval);
			console.println(PROP_PINMAP + ": " + pinMap);
			console.println(SYSFS_VFSTYPE + ": " + sysFS);			
		} else if (argument.equals("pins")) {
			try {
				if (gpioLock.readLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
					try {
						if (!gpioRegistry.isEmpty()) {
							buffer.append("Number\tName\n");
							try {
								for (Object pin : gpioRegistry.keySet()) {
									buffer.append(((GPIOPin)pin).getPinNumber() + "\t" + ((GPIOPin)pin).getPinName() + "\n");
								}
							} catch (IOException e) {
								buffer.setLength(0);
								buffer.append("Error: " + e.getMessage() + "\n");
							}
						}
					} finally {
						gpioLock.readLock().unlock();
					}
				} else {
					/* Something wrong happened, print error and move on or we are risking to block the whole system */
					buffer.append("Error: Read GPIO lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString() + "\n");
				}
			} catch (InterruptedException e) {
				buffer.append("Error: The thread was interrupted while waiting for read GPIO lock\n");
			}
			if (buffer.length() > 0) {
				console.print(buffer);
			}
		} else if (argument.equals("pin")) {

			int pinNumber;

			argument = console.nextArgument();
			if (argument == null) {
				console.println("Error: too few arguments.");
				console.println();
				console.print(getHelp());
				return null;
			}

			try {
				pinNumber = getPinNumberByName(argument);
			} catch (IOException e) {
				console.println("Error: the argument isn't a valid pin.");
				console.println();
				console.print(getHelp());
				return null;
			}

			try {
				if (gpioLock.readLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
					try {
						if (gpioRegistry.containsValue(pinNumber)) {
							GPIOPin pin = (GPIOPin) gpioRegistry.getKey(pinNumber);
							try {
								buffer.append("Number:            " + pin.getPinNumber() + "\n");
								buffer.append("Name:              " + pin.getPinName() + "\n");
								buffer.append("ActiveLow:         ");
								if (pin.getActiveLow() == GPIOPin.ACTIVELOW_DISABLED) {
									buffer.append("disabled\n");
								} else {
									buffer.append("enabled\n");
								}
								buffer.append("Debounce Interval: " + pin.getDebounceInterval() + " ms\n");
								buffer.append("Direction:         ");
								switch (pin.getDirection()) {
									case GPIOPin.DIRECTION_IN:
										buffer.append("in\n");
										break;
									case GPIOPin.DIRECTION_OUT:
										buffer.append("out\n");
										break;
									case GPIOPin.DIRECTION_OUT_HIGH:
										buffer.append("out high\n");
										break;
									case GPIOPin.DIRECTION_OUT_LOW:
										buffer.append("out low\n");
										break;
								}
								buffer.append("Edge Detection:    ");
								switch (pin.getEdgeDetection()) {
									case GPIOPin.EDGEDETECTION_BOTH:
										buffer.append("both\n");
										break;
									case GPIOPin.EDGEDETECTION_FALLING:
										buffer.append("falling\n");
										break;
									case GPIOPin.EDGEDETECTION_NONE:
										buffer.append("none\n");
										break;
									case GPIOPin.EDGEDETECTION_RISING:
										buffer.append("rising\n");
										break;
								}
								buffer.append("Value:             " + pin.getValue() + "\n");
							} catch (IOException e) {
								buffer.setLength(0);
								buffer.append("Error: " + e.getMessage() + "\n");
							}
						} else {
							buffer.append("Pin '" + argument + "' isn't in use\n");
						}
					} finally {
						gpioLock.readLock().unlock();
					}
				} else {
					/* Something wrong happened, print error and move on or we are risking to block the whole system */
					buffer.append("Error: Read GPIO lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString() + "\n");
				}
			} catch (InterruptedException e) {
				buffer.append("Error: The thread was interrupted while waiting for read GPIO lock\n");
			}
			if (buffer.length() > 0) {
				console.print(buffer);
			}
		} else if (argument.equals("pinmap")) {
			try {
				if (pinMapLock.readLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
					try {
						if (!pinNameNumberMap.isEmpty()) {
							buffer.append("Number\tName\tSysfs Suffix\n");
							for (Object pinName : pinNameNumberMap.keySet()) {
								int pinNumber = (Integer) pinNameNumberMap.get(pinName);
								String sysfsSuffix = pinNumberSysfsSuffixMap.get(pinNumber);
								buffer.append(pinNumber + "\t" + pinName + "\t" + sysfsSuffix + "\n");
							}
						}
					} finally {
						pinMapLock.readLock().unlock();
					}
				} else {
					/* Something wrong happened, print error and move on or we are risking to block the whole system */
					buffer.append("Error: Read GPIO pinmap lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString() + "\n");
				}
			} catch (InterruptedException e) {
				buffer.append("Error: The thread was interrupted while waiting for read GPIO pinmap lock\n");
			}
			if (buffer.length() > 0) {
				console.print(buffer);
			}
		} else {
			console.println("Error: unknown argument.");
			console.println();
			console.print(getHelp());
		}

		return null;
	}
}
