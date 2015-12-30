/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import java.util.Dictionary;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.maxcube.MaxCubeBindingProvider;
import org.openhab.binding.maxcube.internal.exceptions.IncompleteMessageException;
import org.openhab.binding.maxcube.internal.exceptions.IncorrectMultilineIndexException;
import org.openhab.binding.maxcube.internal.exceptions.MessageIsWaitingException;
import org.openhab.binding.maxcube.internal.exceptions.NoMessageAvailableException;
import org.openhab.binding.maxcube.internal.exceptions.UnprocessableMessageException;
import org.openhab.binding.maxcube.internal.exceptions.UnsupportedMessageTypeException;
import org.openhab.binding.maxcube.internal.message.C_Message;
import org.openhab.binding.maxcube.internal.message.Configuration;
import org.openhab.binding.maxcube.internal.message.Device;
import org.openhab.binding.maxcube.internal.message.DeviceInformation;
import org.openhab.binding.maxcube.internal.message.HeatingThermostat;
import org.openhab.binding.maxcube.internal.message.L_Message;
import org.openhab.binding.maxcube.internal.message.M_Message;
import org.openhab.binding.maxcube.internal.message.Message;
import org.openhab.binding.maxcube.internal.message.MessageProcessor;
import org.openhab.binding.maxcube.internal.message.MessageType;
import org.openhab.binding.maxcube.internal.message.S_Command;
import org.openhab.binding.maxcube.internal.message.S_Message;
import org.openhab.binding.maxcube.internal.message.ShutterContact;
import org.openhab.binding.maxcube.internal.message.ThermostatModeType;
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

	/**
	 * Duty cycle of the cube
	 */
	private int dutyCycle = 0;

	/**
	 * The available memory slots of the cube
	 */
	private int freeMemorySlots;

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
	private List<Configuration> configurations = new ArrayList<Configuration>();
	private List<Device> devices = new ArrayList<Device>();
	
	/**
	 * connection socket and reader/writer for execute method
	 */
	private Socket socket = null;
	private BufferedReader reader = null;
	private OutputStreamWriter writer = null;
	
	
	/**
	 * Processor that handles lines received from MAX!Cube
	 */
	MessageProcessor messageProcessor = new MessageProcessor();
	
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
		socketClose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void execute() {
		if (ip == null) {
			logger.debug("Update prior to completion of interface IP configuration");
			return;
		}
		try {
			String raw = null;
			if(maxRequestsPerConnection > 0 && requestCount >= maxRequestsPerConnection) {
				logger.debug("maxRequestsPerConnection reached, reconnecting.");
				socket.close();
				this.socketConnect();
			}
			if(socket == null) {
				this.socketConnect();
			}
			else {
			
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
				
				Message message = null;
				try {
					this.messageProcessor.addReceivedLine(raw);
					if (this.messageProcessor.isMessageAvailable()) {
						message = this.messageProcessor.pull();
					} else {
						continue;
					}
					
					message.debug(logger);

					if (message != null) {
						message.debug(logger);
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
							sMessageProcessing((S_Message)message);
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
				} catch (IncorrectMultilineIndexException ex) {
					logger.info("Incorrect MAX!Cube multiline message detected. Stopping processing and continue with next Line.");
					this.messageProcessor.reset();
				} catch (NoMessageAvailableException ex) {
					logger.info("Could not process MAX!Cube message. Stopping processing and continue with next Line.");
					this.messageProcessor.reset();
				} catch (IncompleteMessageException ex) {
					logger.info("Error while parsing MAX!Cube multiline message. Stopping processing, and continue with next Line.");
					this.messageProcessor.reset();
				} catch (UnprocessableMessageException ex) {
					logger.info("Error while parsing MAX!Cube message. Stopping processing, and continue with next Line.");
					this.messageProcessor.reset();
				} catch (UnsupportedMessageTypeException ex) {
					logger.info("Unsupported MAX!Cube message detected. Ignoring and continue with next Line.");
					this.messageProcessor.reset();
				} catch (MessageIsWaitingException ex) {
					logger.info("There was and unhandled message waiting. Ignoring and continue with next Line.");
					this.messageProcessor.reset();
				} catch (Exception e) {
					logger.info("Failed to process message received by MAX! protocol.");
					logger.debug(Utils.getStackTrace(e));
					this.messageProcessor.reset();
				}
			}
			if(!exclusive) {
				socketClose();
			}

			for (MaxCubeBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					String serialNumber = provider.getSerialNumber(itemName);

					Device device = findDevice(serialNumber, devices);

					if (device == null) {
						logger.info("Cannot find MAX!cube device with serial number '{}'", serialNumber);
						logAvailableMaxDevices();
						continue;
					}
					// all devices have a battery state, so this is type-independent
					if (provider.getBindingType(itemName) == BindingType.BATTERY) {
						if (device.battery().isChargeUpdated()){
							eventPublisher.postUpdate(itemName, device.battery().getCharge());
						}
					} else if (provider.getBindingType(itemName) == BindingType.CONNECTION_ERROR){
						if (device.isErrorUpdated()){
							OnOffType connectionError = device.isError() ? OnOffType.ON : OnOffType.OFF;
							eventPublisher.postUpdate(itemName, connectionError);
						}
					} else {
						switch (device.getType()) {
						case HeatingThermostatPlus:
						case HeatingThermostat:
							if (provider.getBindingType(itemName) == BindingType.VALVE
									&& ((HeatingThermostat) device).isValvePositionUpdated()) {
								eventPublisher.postUpdate(itemName, ((HeatingThermostat) device).getValvePosition());
								break;
							}
							// omitted break, fall through
						case WallMountedThermostat: // and also HeatingThermostat
							if (provider.getBindingType(itemName) == BindingType.MODE
									&& ((HeatingThermostat) device).isModeUpdated()) {
								eventPublisher.postUpdate(itemName, ((HeatingThermostat) device).getModeString());
							} else if (provider.getBindingType(itemName) == BindingType.ACTUAL
									&& ((HeatingThermostat) device).isTemperatureActualUpdated()) {
								eventPublisher
										.postUpdate(itemName, ((HeatingThermostat) device).getTemperatureActual());
							} else if (((HeatingThermostat) device).isTemperatureSetpointUpdated()
									&& provider.getBindingType(itemName) == null) {
								eventPublisher.postUpdate(itemName,
										((HeatingThermostat) device).getTemperatureSetpoint());
							}
							break;
						case ShutterContact:
							if (((ShutterContact) device).isShutterStateUpdated()) {
								eventPublisher.postUpdate(itemName, ((ShutterContact) device).getShutterState());
							}
							break;
						default:
							// no further devices supported yet
						}
					}
				}
			}
		} catch (UnknownHostException e) {
			logger.info("Host error occurred while connecting to MAX! Cube lan gateway '{}': {}", ip, e.getMessage());
			socketClose();
		} catch (IOException e) {
			logger.info("IO error occurred while connecting to MAX! Cube lan gateway '{}': {}", ip, e.getMessage());
			socketClose(); //reconnect on next execution
		} catch (Exception e) {
			logger.info("Error occurred while connecting to MAX! Cube lan gateway '{}': {}", ip, e.getMessage());
			logger.info(Utils.getStackTrace(e));
			socketClose(); //reconnect on next execution
		}
	}
	
	private void logAvailableMaxDevices() {
		if (logger.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Available MAX! devices are:");
			for (Device d : devices) {
				sb.append("\n\t");
				sb.append(d.getSerialNumber());
			}
			logger.debug(sb.toString());
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

			if (StringUtils.isBlank(serialNumber)) {
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

				S_Command cmd = new S_Command(rfAddress, device.getRoomId(), ((HeatingThermostat) device).getMode(), decimalType.doubleValue());
				commandString = cmd.getCommandString();
			} else if (command instanceof StringType) {
				String commandContent = command.toString().trim().toUpperCase();
				S_Command cmd = null;
				ThermostatModeType commandThermoType = null;
				if (commandContent.contentEquals(ThermostatModeType.AUTOMATIC.toString())) {
					commandThermoType = ThermostatModeType.AUTOMATIC;
					cmd = new S_Command(rfAddress, device.getRoomId(), commandThermoType);
				} else if (commandContent.contentEquals(ThermostatModeType.BOOST.toString())) {
					commandThermoType = ThermostatModeType.BOOST;
					Double setTemp = Double.parseDouble( ((HeatingThermostat) device).getTemperatureSetpoint().toString());
					cmd = new S_Command(rfAddress, device.getRoomId(), commandThermoType, setTemp);
				} else if (commandContent.contentEquals(ThermostatModeType.MANUAL.toString())) {
					commandThermoType = ThermostatModeType.MANUAL;
					Double setTemp = Double.parseDouble( ((HeatingThermostat) device).getTemperatureSetpoint().toString());
					cmd = new S_Command(rfAddress, device.getRoomId(), commandThermoType, setTemp);
					logger.debug("updates to MANUAL mode with temperature '{}'", setTemp);
				} else {
					logger.debug("Only updates to AUTOMATIC, MANUAL & BOOST supported, received value ;'{}'", commandContent);
					continue;
				}
				commandString = cmd.getCommandString();
			}

			if (commandString != null) {

				try {
					if(socket == null) {
						this.socketConnect();
					}
					writer.write(commandString);
					logger.debug(commandString);
					writer.flush();
					
					Message message = null;
					String raw = reader.readLine();
                    try {
                        while (!this.messageProcessor.isMessageAvailable()) {
                        	this.messageProcessor.addReceivedLine(raw);
                            raw = reader.readLine();
                        }

                        message = this.messageProcessor.pull();
                    } catch (Exception e) {
                        logger.info("Error while handling response from MAX! Cube lan gateway!");
                        logger.debug(Utils.getStackTrace(e));
                        this.messageProcessor.reset();
                    }
					
					if (message !=null) {
						if (message.getType() == MessageType.S) {
							sMessageProcessing((S_Message)message);
						}}
					if(!exclusive) {
						socket.close();
						socket = null;
					}
				} catch (UnknownHostException e) {
					logger.info("Host error occurred while connecting to MAX! Cube lan gateway '{}': {}", ip, e.getMessage());
					socketClose();
				} catch (IOException e) {
					logger.info("IO error occurred while writing to MAX! Cube lan gateway '{}': {}", ip, e.getMessage());
					socketClose(); //reconnect on next execution
				} catch (Exception e) {
					logger.info("Error occurred while writing to MAX! Cube lan gateway '{}': {}", ip, e.getMessage());
					logger.info(Utils.getStackTrace(e));
					socketClose(); //reconnect on next execution
				}
				logger.debug("Command Sent to {}", ip);
			} else {
				logger.debug("Null Command not sent to {}", ip);
			}
		}
	}

	/**
	 * Processes the S message and updates Duty Cycle & Free Memory Slots
	 * @param S_Message message
	 */
	private void sMessageProcessing(S_Message message) {
		dutyCycle =  message.getDutyCycle();
		freeMemorySlots = message.getFreeMemorySlots();
		if (message.isCommandDiscarded()) {
			logger.info("Last Send Command discarded. Duty Cycle: {}, Free Memory Slots: {}",dutyCycle,freeMemorySlots);
		} else
			logger.debug("S message. Duty Cycle: {}, Free Memory Slots: {}",dutyCycle,freeMemorySlots);
	}
	
	private boolean socketConnect() throws UnknownHostException, IOException {
		socket = new Socket(ip, port);
		socket.setSoTimeout(2000);
		logger.debug("open new connection... to " + ip + " port " + port);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new OutputStreamWriter(socket.getOutputStream());
		requestCount = 0;
		return true;
	}

	private void socketClose() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// Ignore
			}
			socket = null;
		}
	}

	private Device findDevice(String serialNumber, List<Device> devices) {
		for (Device device : devices) {
			if (device.getSerialNumber().toUpperCase().equals(serialNumber)) {
				return device;
			}
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
