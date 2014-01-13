/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.maxcube.MaxCubeBindingProvider;
import org.openhab.binding.maxcube.internal.message.C_Message;
import org.openhab.binding.maxcube.internal.message.Configuration;
import org.openhab.binding.maxcube.internal.message.Device;
import org.openhab.binding.maxcube.internal.message.DeviceType;
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
import org.openhab.core.library.types.StringType;
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

	private ArrayList<Configuration> configurations;
	private ArrayList<Device> devices;

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
		setProperlyConfigured(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() {

		configurations = new ArrayList<Configuration>();
		devices = new ArrayList<Device>();

		Socket socket = null;
		BufferedReader reader = null;

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
						if (message.getType() == MessageType.C) {
							configurations.add(Configuration.create(message));
						} else if (message.getType() == MessageType.L) {
							devices.addAll(((L_Message) message).getDevices(configurations));
							logger.info(devices.size() + " devices found.");

							// the L message is the last one, while the reader
							// would
							// hang trying to read a new line and eventually the
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
		logger.debug("Received command from " + itemName);

		// resolve serial number for item
		String serialNumber = null;

		for (MaxCubeBindingProvider provider : providers) {
			serialNumber = provider.getSerialNumber(itemName);

			if (serialNumber != null)
				break;
		}

		if (serialNumber == null)
			return;

		// send command to MAX!Cube LAN Gateway
		Device device = findDevice(serialNumber, devices);

		if (device == null)
			return;

		String rfAddress = device.getRFAddress();

		if (command instanceof DecimalType) {
			DecimalType decimalType = (DecimalType) command;
			S_Command scmd = new S_Command(rfAddress, decimalType.doubleValue());
			String commandString = scmd.getCommandString();

			Socket socket = null;
			try {
				socket = new Socket(ip, port);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.print(commandString);

				socket.close();

			} catch (UnknownHostException e) {
				logger.warn("Cannot establish connection with MAX!cube lan gateway while sending command to '{}'", ip);

			} catch (IOException e) {
				logger.warn("Cannot write data from MAX!cube lan gateway while connecting to '{}'", ip);
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
				throw new ConfigurationException("maxcube:ip", "IP address for MAX!Cube must be set");
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

		}
	}
}