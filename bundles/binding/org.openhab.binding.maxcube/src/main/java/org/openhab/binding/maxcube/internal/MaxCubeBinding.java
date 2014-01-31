/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.maxcube.MaxCubeBindingProvider;
import org.openhab.binding.maxcube.internal.message.C_Message;
import org.openhab.binding.maxcube.internal.message.Configuration;
import org.openhab.binding.maxcube.internal.message.Device;
import org.openhab.binding.maxcube.internal.message.DeviceInformation;
import org.openhab.binding.maxcube.internal.message.H_Message;
import org.openhab.binding.maxcube.internal.message.HeatingThermostat;
import org.openhab.binding.maxcube.internal.message.L_Message;
import org.openhab.binding.maxcube.internal.message.M_Message;
import org.openhab.binding.maxcube.internal.message.Message;
import org.openhab.binding.maxcube.internal.message.MessageType;
import org.openhab.binding.maxcube.internal.message.S_Command;
import org.openhab.binding.maxcube.internal.message.ShutterContact;
import org.openhab.binding.maxcube.internal.message.WallMountedThermostat;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The RefreshService polls the MAX!Cube frequently and updates the list of
 * configurations and devices. The refresh interval can be changed via
 * openhab.cfg.
 * 
 * Note that the MAX Cube has a lock out that only allows a maximum of 36s of
 * transmissions (1%) in total in 1 hour. This means that if too many S messages
 * are sent then the cube no longer sends the data out.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public class MaxCubeBinding extends AbstractActiveBinding<MaxCubeBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(MaxCubeBinding.class);

	/** The IP address of the MAX!Cube LAN gateway */
	private static String ip;

	/**
	 * The port of the MAX!Cube LAN gateway as provided at
	 * http://www.elv.de/controller.aspx?cid=824&detail=10&detail2=3484
	 */
	private static int port = 62910;

	/** The refresh interval which is used to poll given MAX!Cube */
	private static long refreshInterval = 10000;

	/**
	 * Configuration and device lists, kept during the overall lifetime of the
	 * binding
	 */
	private ArrayList<Configuration> configurations = new ArrayList<Configuration>();;
	private ArrayList<Device> devices = new ArrayList<Device>();;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "MAX!Cube Refresh Service";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate() {
		super.activate();
		setProperlyConfigured(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() {
		Socket socket = null;
		BufferedReader reader = null;

		if (ip == null) {
			logger.debug("Update prior to completion of interface IP configuration");
			return;
		}
		try {
			String raw = null;

			socket = new Socket(ip, port);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			boolean cont = true;
			while (cont) {
				raw = reader.readLine();
				if (raw == null) {
					cont = false;
					continue;
				}

				Message message;
				try {
					message = processRawMessage(raw);

					message.debug(logger);

					if (message != null) {
						if (message.getType() == MessageType.M) {
							M_Message msg = (M_Message) message;
							for (DeviceInformation di : msg.devices) {
								Configuration c = null;
								for (Configuration conf : configurations) {
									if (conf.getSerialNumber().equalsIgnoreCase(di.getSerialNumber())) {
										c = conf;
										break;
									}
								}

								if (c != null) {
									configurations.remove(c);
								}

								c = Configuration.create(di);
								configurations.add(c);

								c.setRoomId(di.getRoomId());
							}
						} else if (message.getType() == MessageType.C) {
							Configuration c = null;
							for (Configuration conf : configurations) {
								if (conf.getSerialNumber().equalsIgnoreCase(((C_Message) message).getSerialNumber())) {
									c = conf;
									break;
								}
							}

							if (c == null) {
								configurations.add(Configuration.create(message));
							} else {
								c.setValues((C_Message) message);
							}
						} else if (message.getType() == MessageType.L) {
							Collection<? extends Device> tempDevices = ((L_Message) message).getDevices(configurations);

							for (Device d : tempDevices) {
								Device existingDevice = findDevice(d.getSerialNumber(), devices);
								if (existingDevice == null) {
									devices.add(d);
								} else {
									devices.remove(existingDevice);
									devices.add(d);
								}
							}

							logger.debug("{} devices found.", devices.size());

							// the L message is the last one, while the reader
							// would hang trying to read a new line and
							// eventually the
							// cube will fail to establish
							// new connections for some time
							cont = false;
						}
					}
				} catch (Exception e) {
					logger.info("Failed to process message received by MAX! protocol.");
					logger.debug(Utils.getStackTrace(e));
				}
			}

			socket.close();

			for (MaxCubeBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					String serialNumber = provider.getSerialNumber(itemName);

					Device device = findDevice(serialNumber, devices);

					if (device == null) {
						logger.info("Cannot find MAX!cube device with serial number '{}'", serialNumber);

						if (logger.isDebugEnabled()) {
							StringBuilder sb = new StringBuilder();
							sb.append("Available MAX! devices are:");
							for (Device d : devices) {
								sb.append("\n\t");
								sb.append(d.getSerialNumber());
							}
							logger.debug(sb.toString());
						}
						continue;
					}

					switch (device.getType()) {
					case HeatingThermostatPlus:
					case HeatingThermostat:
						if (provider.getBindingType(itemName) == BindingType.VALVE) {
							eventPublisher.postUpdate(itemName, ((HeatingThermostat) device).getValvePosition());
						} else if (provider.getBindingType(itemName) == BindingType.BATTERY) {
							eventPublisher.postUpdate(itemName, ((HeatingThermostat) device).getBatteryLow());
						} else {
							eventPublisher.postUpdate(itemName, ((HeatingThermostat) device).getTemperatureSetpoint());
						}
						break;
					case ShutterContact:
						if (provider.getBindingType(itemName) == BindingType.BATTERY) {
							eventPublisher.postUpdate(itemName, ((ShutterContact) device).getBatteryLow());
						} else {
							eventPublisher.postUpdate(itemName, ((ShutterContact) device).getShutterState());
						}
						break;
					case WallMountedThermostat:
						eventPublisher.postUpdate(itemName, ((WallMountedThermostat) device).getTemperatureSetpoint());
						break;
					default:
						// no further devices supported yet
					}
				}
			}
		} catch (UnknownHostException e) {
			logger.info("Cannot establish connection with MAX!Cube lan gateway while connecting to '{}'", ip);
			logger.debug(Utils.getStackTrace(e));
		} catch (IOException e) {
			logger.info("Cannot read data from MAX!Cube lan gateway while connecting to '{}'", ip);
			logger.debug(Utils.getStackTrace(e));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		logger.debug("Received command from {}", itemName);

		// resolve serial number for item
		String serialNumber = null;

		for (MaxCubeBindingProvider provider : providers) {
			serialNumber = provider.getSerialNumber(itemName);

			if (serialNumber.equals(null))
				continue;

			// send command to MAX!Cube LAN Gateway
			Device device = findDevice(serialNumber, devices);

			if (device == null) {
				logger.debug("Cannot send command to device with serial number {}, device not listed.", serialNumber);
				continue;
			}

			String rfAddress = device.getRFAddress();

			if (command instanceof DecimalType) {
				DecimalType decimalType = (DecimalType) command;
				S_Command cmd = new S_Command(rfAddress, device.getRoomId(), decimalType.doubleValue());
				String commandString = cmd.getCommandString();

				Socket socket = null;
				try {
					socket = new Socket(ip, port);
					DataOutputStream stream = new DataOutputStream(socket.getOutputStream());

					byte[] b = commandString.getBytes();
					stream.write(b);
					socket.close();

				} catch (UnknownHostException e) {
					logger.warn("Cannot establish connection with MAX!cube lan gateway while sending command to '{}'", ip);
					logger.debug(Utils.getStackTrace(e));
				} catch (IOException e) {
					logger.warn("Cannot write data from MAX!Cube lan gateway while connecting to '{}'", ip);
					logger.debug(Utils.getStackTrace(e));
				}
				logger.debug("Command Sent to {}", ip);
			}
		}
	}

	private Device findDevice(String serialNumber, ArrayList<Device> devices) {
		for (Device device : devices) {
			if (device.getSerialNumber().toUpperCase().equals(serialNumber)) {
				return device;
			}
		}
		return null;
	}

	/**
	 * Processes the raw TCP data read from the MAX protocol, returning the
	 * corresponding Message.
	 * 
	 * @param raw
	 *            the raw data provided read from the MAX protocol
	 * @return the correct message for the given raw data
	 */
	private Message processRawMessage(String raw) {

		if (raw.startsWith("H:")) {
			return new H_Message(raw);
		} else if (raw.startsWith("M:")) {
			return new M_Message(raw);
		} else if (raw.startsWith("C:")) {
			return new C_Message(raw);
		} else if (raw.startsWith("L:")) {
			return new L_Message(raw);
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			ip = (String) config.get("ip");
			if (StringUtils.isBlank(ip)) {
				ip = discoveryGatewayIp();
			}

			String portString = (String) config.get("port");
			if (portString != null && !portString.isEmpty()) {
				if (port > 0 && port <= 65535) {
					port = Integer.parseInt(portString);
				}
			}

			String refreshIntervalString = (String) config.get("refreshInterval");
			if (refreshIntervalString != null && !refreshIntervalString.isEmpty()) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
		} else {
			ip = discoveryGatewayIp();
		}
		setProperlyConfigured(ip != null);
	}
	
	/**
	 * Discovers the MAX!CUbe Lan Gateway IP adress. 
	 * @return the cube IP if available, a blank string otherwise.
	 * @throws ConfigurationException
	 */
	private String discoveryGatewayIp() throws ConfigurationException {
		String ip = MaxCubeDiscover.discoverIp();
		if (ip == null) {	
			throw new ConfigurationException("maxcube:ip", "IP address for MAX!Cube must be set");
		} else {
			logger.info("Discovered MAX!Cube lan gateway at '{}'", ip);
		}
		return ip;
	}
}
