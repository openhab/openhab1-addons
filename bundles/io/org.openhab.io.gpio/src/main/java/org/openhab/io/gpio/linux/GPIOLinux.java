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
import java.util.TreeMap;
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
	private static final String PROP_FORCE = "force";
	private static final String PROP_PINMAP = "pinmap";
	private static final String PINMAP_DEFAULT_VALUE = "none";

	private static final Logger logger = LoggerFactory.getLogger(GPIOLinux.class);

	/** Path to directory where <code>sysfs</code> is mounted. */ 
	private String sysFS = null;

	/** Default debounce interval in milliseconds */
	private volatile long defaultDebounceInterval = 0;

	/** Forcibly use the pins after unclean shutdown */
	private volatile boolean force = false;

	/**
	 * Pinmap file name without extension part, in case no pinmap file is
	 * used contains a special value <code>none<code> */
	private String pinMap = PINMAP_DEFAULT_VALUE;

	/** GPIO subsystem read/write lock. */
	private final ReentrantReadWriteLock gpioLock = new ReentrantReadWriteLock();
	
	/** Database for GPIO pins which are in use. */
	private final BidiMap gpioRegistry = new DualHashBidiMap();

	/** Pinmap lock, protects all changes to pinmap */
	private final ReentrantReadWriteLock pinMapLock = new ReentrantReadWriteLock();

	/** Bidirectional map pin Name<->Number, used by pinmap code */
	private BidiMap pinNameNumberMap = new DualHashBidiMap(); 

	/** Hash map pin Number->SysfsSuffix, used by pinmap code */
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
								+ propSysFS + "'.");
						throw new ConfigurationException(SYSFS_VFSTYPE, "Configured mount point is invalid, '"
								+ SYSFS_VFSTYPE + "' isn't mounted at '" + propSysFS + "'.");
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
						logger.error("Configured " + PROP_DEBOUNCE_INTERVAL + " is invalid, must not be negative value.");
						throw new ConfigurationException(PROP_DEBOUNCE_INTERVAL, "Configured " + PROP_DEBOUNCE_INTERVAL
								+ " is invalid, must not be negative value.");	
					}
				} catch (NumberFormatException e) {
					logger.error("Configured " + PROP_DEBOUNCE_INTERVAL + " is invalid, must be numeric value.");
					throw new ConfigurationException(PROP_DEBOUNCE_INTERVAL, "Configured " + PROP_DEBOUNCE_INTERVAL
							+ " is invalid, must be numeric value.");
				}
			}

			String propForce = (String) properties.get(PROP_FORCE);
			if (propForce != null) {
				force = Boolean.parseBoolean(propForce);
			}

			try {
				if (pinMapLock.writeLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
					try {
						pinMap = (String) properties.get(PROP_PINMAP);
						if (pinMap == null) {
							pinMap = PINMAP_DEFAULT_VALUE;
						}
						loadPinMap();
					} finally {
						pinMapLock.writeLock().unlock();
					}
				} else {
					/* Something wrong happened, throw an exception and move on or we are risking to block the whole system */
					throw new ConfigurationException(PROP_PINMAP, "Write GPIO pinmap lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString());
				}
			} catch (InterruptedException e) {
				throw new ConfigurationException(PROP_PINMAP, "The thread was interrupted while waiting for write GPIO pinmap lock.");
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

	/**
	 * Loads pinmap file into two map structures.
	 * Helper function for <code>updated()</code>.
	 * Assumes that write pinMapLock is held by the caller!
	 * 
	 * @throws IOException if reading or parsing of pinmap file fails
	 */
	private void loadPinMap() throws IOException {

		final String PINMAP_COMMENT = "#";
		final String PINMAP_FOLDER = ConfigDispatcher.getConfigFolder() + File.separator + "pinmaps";
		final String PINMAP_EXTENSION = ".pinmap";

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

			if (pinMapRecord.trim().isEmpty() || pinMapRecord.startsWith(PINMAP_COMMENT)) {
				continue;
			}

			String[] pinMapRecordFields = pinMapRecord.trim().split("\\s+");

			if (pinMapRecordFields.length < 2 || pinMapRecordFields.length > 3) {
				throw new IOException("Line " + lineNumber + ": Unsupported number of fields - " + pinMapRecordFields.length);
			}

			if (pinMapRecordFields[0].contains(":") || pinMapRecordFields[0].contains("\"")) {
				throw new IOException("Line " + lineNumber + ": Pin name contains illegal characters as double quotes and/or colon(s).");
			}

			try {
				pinNumber = Integer.parseInt(pinMapRecordFields[1]);
			} catch (NumberFormatException e) {
				throw new IOException("Line " + lineNumber + ": The value in pin number field is not numeric.");
			}

			if (newPinNameNumberMap.containsKey(pinMapRecordFields[0]) || newPinNameNumberMap.containsValue(pinNumber)) {
				throw new IOException("Line " + lineNumber + ": Duplicate pin name and/or number.");
			}

			newPinNameNumberMap.put(pinMapRecordFields[0], pinNumber);

			if (pinMapRecordFields.length == 3) {
				String sysfsSuffix = pinMapRecordFields[2].replace("${pinName}", pinMapRecordFields[0]).replace("${pinNumber}", pinMapRecordFields[1]);
				if (newPinNumberSysfsSuffixMap.containsValue(sysfsSuffix)) {
					throw new IOException("Line " + lineNumber + ": Duplicate sysfs suffix.");
				}
				newPinNumberSysfsSuffixMap.put(pinNumber, sysfsSuffix);
			}
		}

		pinNameNumberMap = newPinNameNumberMap;
		pinNumberSysfsSuffixMap = newPinNumberSysfsSuffixMap;
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
			throw new IOException("Mount point for '" + SYSFS_VFSTYPE + "' isn't configured and can't be determined.");
		}

		/* Sanity check, empty pin name is illegal. */
		if (pinName.isEmpty()) {
			throw new IllegalArgumentException("Unsupported argument for 'pinName' parameter (" + pinName + ").");
		}

		/* Acquiring write lock guarantees atomic check/set operation */ 
		try {
			if (gpioLock.writeLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
				try {

					Integer pinNumber = getPinNumberByName(pinName);

					if (gpioRegistry.containsValue(pinNumber)) {
						throw new IllegalArgumentException("The pin with number '" + pinNumber + "' is already registered.");
					}

					/* Exports the pin to user space. */
					try {
						Files.write(Paths.get(SYSFS_CLASS_GPIO + "export"), pinNumber.toString().getBytes());
					} catch (IOException e){
						if (force) {
							/* Forcibly use the pin as unexport it and export it again */
							Files.write(Paths.get(SYSFS_CLASS_GPIO + "unexport"), pinNumber.toString().getBytes());
							Files.write(Paths.get(SYSFS_CLASS_GPIO + "export"), pinNumber.toString().getBytes());
							logger.warn("The control on GPIO pin " + pinName + "(" + pinNumber + ") was forcibly acquired.");
						} else {
							throw new IOException(e);							
						}
					}
					
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
						throw new IOException("The thread was interrupted while waiting for read GPIO pinmap lock.");
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
			throw new IOException("The thread was interrupted while waiting for write GPIO lock.");
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
						throw new IllegalArgumentException("The pin object isn't registered.");
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
			throw new IOException("The thread was interrupted while waiting for write GPIO lock.");
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
							throw new IOException("Non-numeric 'pin' value is used without proper pinmap.", e);
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
			throw new IOException("The thread was interrupted while waiting for read GPIO pinmap lock.");
		}

		return pinNumber;
	}

	public String getHelp() {
		return "---GPIO sub commands---\n" +
			"gpio globals         : lists global variables\n" +
			"gpio pin NAME|NUMBER : shows detailed information about pin\n" +
			"gpio pinmap          : lists currently used pinmap\n" +
			"gpio pins            : lists registered pins\n";
}

	/**
	 * Implements GPIO console commands.
	 * 
	 * @param console command interpreter
	 * @return null
	 */
	public Object _gpio(CommandInterpreter console) {

		StringBuffer buffer = new StringBuffer();
		String argument = console.nextArgument();

		if (argument == null) {
			console.print("Error: Missing sub command.\n\n" + getHelp());
			return null;
		}

		if (argument.equals("globals")) {

			if (console.nextArgument() != null) {
				console.print("Error: Extra argument(s).\n\n" + getHelp());
			} else {
				console.println("debounce : " + defaultDebounceInterval + "\nforce    : " + force + "\npinmap   : " + pinMap + "\nsysfs    : " + sysFS);
			}
		} else if (argument.equals("pins")) {

			if (console.nextArgument() != null) {
				console.print("Error: Extra argument(s).\n\n" + getHelp());
				return null;
			}

			try {

				TreeMap<Integer, String> pinNumberNameMap = new TreeMap<Integer, String>();

				if (gpioLock.readLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
					try {
						if (!gpioRegistry.isEmpty()) {
							try {
								for (Object pin : gpioRegistry.keySet()) {
									pinNumberNameMap.put(((GPIOPin)pin).getPinNumber(), ((GPIOPin)pin).getPinName());
								}
							} catch (IOException e) {
								buffer.append("Error: " + e.getMessage() + "\n");
							}
						} else {
							buffer.append("No GPIO pins are currently used.\n");
						}
					} finally {
						gpioLock.readLock().unlock();
					}

					if (!pinNumberNameMap.isEmpty() && (buffer.length() == 0)) {
						buffer.append("Number     Name\n");
						for (Integer pinNumber : pinNumberNameMap.keySet()) {
							buffer.append(String.format("%1$-10d", pinNumber) + " " + pinNumberNameMap.get(pinNumber) + "\n");
						}
					}

					console.print(buffer);
				} else {
					/* Something wrong happened, print error and move on or we are risking to block the whole system */
					console.println("Error: Read GPIO lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString());
				}
			} catch (InterruptedException e) {
				console.println("Error: The thread was interrupted while waiting for read GPIO lock.");
			}
		} else if (argument.equals("pin")) {

			int pinNumber;

			argument = console.nextArgument();
			if (argument == null) {
				console.print("Error: Missing pin name|number argument.\n\n" + getHelp());
				return null;
			}

			if (console.nextArgument() != null) {
				console.print("Error: Extra argument(s).\n\n" + getHelp());
				return null;
			}

			try {
				pinNumber = getPinNumberByName(argument);
			} catch (IOException e) {
				console.print("Error: The argument isn't a valid pin name or number.\n\n" + getHelp());
				return null;
			}

			try {
				if (gpioLock.readLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
					try {
						if (gpioRegistry.containsValue(pinNumber)) {
							GPIOPin pin = (GPIOPin) gpioRegistry.getKey(pinNumber);
							try {
								buffer.append("number         : " + pin.getPinNumber() + "\n");
								buffer.append("name           : " + pin.getPinName() + "\n");
								buffer.append("activelow      : ");
								if (pin.getActiveLow() == GPIOPin.ACTIVELOW_DISABLED) {
									buffer.append("disabled\n");
								} else {
									buffer.append("enabled\n");
								}
								buffer.append("debounce       : " + pin.getDebounceInterval() + " ms\n");
								buffer.append("direction      : ");
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
								buffer.append("edge detection : ");
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
								buffer.append("value          : ");
								if (pin.getValue() == GPIOPin.VALUE_HIGH) {
									buffer.append("high\n");									
								} else {
									buffer.append("low\n");																		
								}
							} catch (IOException e) {
								buffer.setLength(0);
								buffer.append("Error: " + e.getMessage() + "\n");
							}
						} else {
							buffer.append("Pin '" + argument + "' isn't currently used.\n");
						}
					} finally {
						gpioLock.readLock().unlock();
					}

					console.print(buffer);
				} else {
					/* Something wrong happened, print error and move on or we are risking to block the whole system */
					console.println("Error: Read GPIO lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString());
				}
			} catch (InterruptedException e) {
				console.println("Error: The thread was interrupted while waiting for read GPIO lock.");
			}
		} else if (argument.equals("pinmap")) {

			if (console.nextArgument() != null) {
				console.print("Error: Extra argument(s).\n\n" + getHelp());
				return null;
			}

			try {
				if (pinMapLock.readLock().tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT_UNITS)) {
					try {
						if (!pinNameNumberMap.isEmpty()) {

							TreeMap<Integer, String> pinNumberNameMap = new TreeMap<Integer, String>();

							int largestNameLenght = 5;
							for (Object pinName : pinNameNumberMap.keySet()) {

								pinNumberNameMap.put((Integer) pinNameNumberMap.get(pinName), (String) pinName);

								int pinNameLenght = ((String)pinName).length();
								if (pinNameLenght > largestNameLenght) {
									largestNameLenght = pinNameLenght;
								}
							}							

							buffer.append("Number     Name  ");
							for (int i = 5; i < largestNameLenght; i++) {
								buffer.append(" ");
							}
							buffer.append("Suffix\n");

							for (Integer pinNumber : pinNumberNameMap.keySet()) {

								String pinName = pinNumberNameMap.get(pinNumber);

								buffer.append(String.format("%1$-10d", pinNumber) + " " + pinName);

								String sysfsSuffix = pinNumberSysfsSuffixMap.get(pinNumber);
								if (sysfsSuffix == null) {
									buffer.append("\n");
								} else {
									for (int i = pinName.length(); i < largestNameLenght; i++) {
										buffer.append(" ");
									}
									buffer.append(" " + sysfsSuffix + "\n");
								}
							}
						} else {
							buffer.append("No pinmap is configured or it's empty.\n");
						}
					} finally {
						pinMapLock.readLock().unlock();
					}

					console.print(buffer);
				} else {
					/* Something wrong happened, print error and move on or we are risking to block the whole system */
					console.println("Error: Read GPIO pinmap lock can't be aquired for " + LOCK_TIMEOUT + " " + LOCK_TIMEOUT_UNITS.toString());
				}
			} catch (InterruptedException e) {
				console.println("Error: The thread was interrupted while waiting for read GPIO pinmap lock.");
			}
		} else {
			console.print("Error: Unknown sub command.\n\n" + getHelp());
		}

		return null;
	}
}
