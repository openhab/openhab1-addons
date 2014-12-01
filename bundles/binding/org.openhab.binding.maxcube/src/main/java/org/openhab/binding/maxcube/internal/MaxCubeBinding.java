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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import org.openhab.binding.maxcube.internal.message.S_Message;
import org.openhab.binding.maxcube.internal.message.ShutterContact;
import org.openhab.binding.maxcube.internal.message.ThermostatModeType;
import org.openhab.binding.maxcube.internal.message.WallMountedThermostat;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
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
 * Note that the MAX Cube has a lock out that only allows a maximum of 36s of
 * transmissions (1%) in total in 1 hour. This means that if too many S messages
 * are sent then the cube no longer sends the data out.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @author Bernd Michael Helm (bernd.helm at helmundwalter.de)
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
	
	/** If set to true, the binding will leave the connection to the cube
	 * open and just request new informations.
	 * This allows much higher poll rates and causes less load than the
	 * non-exclusive polling but has the drawback that no other apps
	 * (i.E. original software) can use the cube while this binding is
	 * running.
	 */
	private static boolean exclusive = false;
	
	/**
	 * in exclusive mode, how many requests are allowed until connection is closed and reopened
	 */
	private static int maxRequestsPerConnection = 1000;
	
	private int requestCount = 0;

	/** MaxCube's default off temperature */
	private static final DecimalType DEFAULT_OFF_TEMPERATURE = new DecimalType(4.5);

	/** MaxCubes default on temperature */
	private static final DecimalType DEFAULT_ON_TEMPERATURE = new DecimalType(30.5);
	
	/**
	 * Configuration and device lists, kept during the overall lifetime of the
	 * binding
	 */
	private ArrayList<Configuration> configurations = new ArrayList<Configuration>();
	private ArrayList<Device> devices = new ArrayList<Device>();
	
	/**
	 * connection socket and reader/writer for execute method
	 */
	private Socket socket = null;
	private BufferedReader reader = null;
	private OutputStreamWriter writer = null;
	
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
	
	@Override
	public void deactivate() {
		if(socket!=null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			socket = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() {
		if (ip == null) {
			logger.debug("Update prior to completion of interface IP configuration");
			return;
		}
		try {
			String raw = null;
			if(socket == null) {
				this.socketConnect();
			}if(maxRequestsPerConnection > 0 && requestCount >= maxRequestsPerConnection) {
				logger.debug("maxRequestsPerConnection reached, reconnecting.");
				socket.close();
				this.socketConnect();
				requestCount = 0;
			}else {
			
				/* if the connection is already open (this happens in exclusive mode), just send a "l:\r\n" to get the latest live informations
				 * note that "L:\r\n" or "l:\n" would not work.
				 */
				logger.debug("Sending state request #"+this.requestCount+" to Maxcube");
				writer.write("l:"+'\r'+'\n');
				writer.flush();
				requestCount++;
			}
			
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
						} else if (message.getType() == MessageType.S) {
							/** TODO: Implement handling of S: messages for proper command SET confirmation*/
							cont=false;
						} else if (message.getType() == MessageType.L) {
							((L_Message) message).updateDevices(devices, configurations);
							
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
			if(!exclusive) {
				socket.close();
				socket = null;
			}

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
					//all devices have a battery state, so this is type-independent
					if (provider.getBindingType(itemName) == BindingType.BATTERY && device.isBatteryLowUpdated()) {
						eventPublisher.postUpdate(itemName, device.getBatteryLow());
					} 
					
					switch (device.getType()) {
						case HeatingThermostatPlus:
						case HeatingThermostat:
							if (provider.getBindingType(itemName) == BindingType.VALVE
									&& ((HeatingThermostat) device).isValvePositionUpdated()) {
								eventPublisher.postUpdate(itemName, ((HeatingThermostat) device).getValvePosition());
							}
							//omitted break, fall through
						case WallMountedThermostat: // and also HeatingThermostat
							if (provider.getBindingType(itemName) == BindingType.MODE
									&& ((HeatingThermostat) device).isModeUpdated()) {
								eventPublisher.postUpdate(itemName, ((HeatingThermostat) device).getModeString());
							} else if (provider.getBindingType(itemName) == BindingType.ACTUAL
									&& ((HeatingThermostat) device).isTemperatureActualUpdated()) {
								eventPublisher.postUpdate(itemName, ((HeatingThermostat) device).getTemperatureActual());
							} else if (((HeatingThermostat) device).isTemperatureSetpointUpdated()){
								eventPublisher.postUpdate(itemName, ((HeatingThermostat) device).getTemperatureSetpoint());
							}
							break;
						case ShutterContact:
							if(((ShutterContact) device).isShutterStateUpdated()){
								eventPublisher.postUpdate(itemName, ((ShutterContact) device).getShutterState());
							}
							break;
						default:
							// no further devices supported yet
					}
				}
			}
		} catch (UnknownHostException e) {
			logger.info("Cannot establish connection with MAX!Cube lan gateway while connecting to '{}'", ip);
			logger.debug(Utils.getStackTrace(e));
			socket = null;
		} catch (IOException e) {
			logger.info("Cannot read data from MAX!Cube lan gateway while connecting to '{}'", ip);
			logger.debug(Utils.getStackTrace(e));
			socket = null; //reconnect on next execution
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

			if (serialNumber.equals(null)) {
				continue;
			}

			// send command to MAX!Cube LAN Gateway
			Device device = findDevice(serialNumber, devices);

			if (device == null) {
				logger.debug("Cannot send command to device with serial number {}, device not listed.", serialNumber);
				continue;
			}

			String rfAddress = device.getRFAddress();
			String commandString = null;

			if (command instanceof DecimalType || command instanceof OnOffType) {
				DecimalType decimalType = DEFAULT_OFF_TEMPERATURE;
				if (command instanceof DecimalType) {
					decimalType = (DecimalType) command;
				} else if (command instanceof OnOffType) {
					decimalType = OnOffType.ON.equals(command) ? DEFAULT_ON_TEMPERATURE : DEFAULT_OFF_TEMPERATURE;
				}

				S_Command cmd = new S_Command(rfAddress, device.getRoomId(), decimalType.doubleValue());
				commandString = cmd.getCommandString();
			} else if (command instanceof StringType) {
				String commandContent = command.toString().trim().toUpperCase();
				ThermostatModeType commandThermoType = null;
				if (commandContent.contentEquals(ThermostatModeType.AUTOMATIC.toString())) {
					commandThermoType = ThermostatModeType.AUTOMATIC;
				} else if (commandContent.contentEquals(ThermostatModeType.BOOST.toString())) {
					commandThermoType = ThermostatModeType.BOOST;
				} else {
					logger.debug("Only updates to AUTOMATIC & BOOST supported, received value ;'{}'", commandContent);
					continue;
				}

				S_Command cmd = new S_Command(rfAddress, device.getRoomId(), commandThermoType);
				commandString = cmd.getCommandString();
			}

			if (commandString != null) {

				try {
					if(socket == null) {
						this.socketConnect();
					}
					/*DataOutputStream stream = new DataOutputStream(socket.getOutputStream());

					byte[] b = commandString.getBytes();
					stream.write(b);*/
					writer.write(commandString);
					logger.debug(commandString);
					writer.flush();
					if(!exclusive) {
						socket.close();
						socket = null;
					}
				} catch (UnknownHostException e) {
					logger.warn("Cannot establish connection with MAX!cube lan gateway while sending command to '{}'", ip);
					logger.debug(Utils.getStackTrace(e));
				} catch (IOException e) {
					logger.warn("Cannot write data from MAX!Cube lan gateway while connecting to '{}'", ip);
					logger.debug(Utils.getStackTrace(e));
				}
				logger.debug("Command Sent to {}", ip);
			} else {
				logger.debug("Null Command not sent to {}", ip);
			}
		}
	}
	
	private boolean socketConnect() throws UnknownHostException, IOException {
		socket = new Socket(ip, port);
		logger.debug("open new connection... to "+ip+" port "+port);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new OutputStreamWriter(socket.getOutputStream());
		return true;
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
		} else if (raw.startsWith("S:")) {
			return new S_Message(raw);
		} else {
			logger.debug("Unknown message block: '{}'",raw);
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
			
			String exclusiveString = (String) config.get("exclusive");
			if (StringUtils.isNotBlank(exclusiveString)) {
				exclusive = Boolean.parseBoolean(exclusiveString);
			}
			
			String maxRequestsPerConnectionString = (String) config.get("maxRequestsPerConnection");
			if (maxRequestsPerConnectionString != null && !maxRequestsPerConnectionString.isEmpty()) {
				maxRequestsPerConnection = Integer.parseInt(maxRequestsPerConnectionString);
			}
		} else {
			ip = discoveryGatewayIp();
		}
		
		setProperlyConfigured(ip != null);
	}

	/**
	 * Discovers the MAX!CUbe LAN Gateway IP address.
	 * 
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
