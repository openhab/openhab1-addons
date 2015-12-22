/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.simplebinary.internal.SimpleBinaryDeviceState.DeviceStates;
import org.openhab.binding.simplebinary.internal.SimpleBinaryPortState.PortStates;
import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.SimpleBinaryBindingConfig;
import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.SimpleBinaryInfoBindingConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Serial device class
 * 
 * @author Vita Tucek
 * @since 1.8.0
 */
public class SimpleBinaryUART implements SimpleBinaryIDevice, SerialPortEventListener {

	private static final Logger logger = LoggerFactory.getLogger(SimpleBinaryUART.class);

	/** port name ex.: port, port1, ... */
	private String deviceName;
	/** port address ex.: COM1, /dev/ttyS1, ... */
	private String port;
	/** port baud rate */
	private int baud = 9600;
	/** defines maximum resend count */
	private int MAX_RESEND_COUNT = 2;

	private EventPublisher eventPublisher;
	/** item config */
	private Map<String, SimpleBinaryBindingConfig> itemsConfig;
	/** port id */
	private CommPortIdentifier portId;
	/** port instance */
	private SerialPort serialPort;
	/** input data stream */
	private InputStream inputStream;
	/** output data stream */
	private OutputStream outputStream;
	/** buffer for incoming data */
	private SimpleBinaryByteBuffer inBuffer = new SimpleBinaryByteBuffer(256);
	// private ByteBuffer inBuffer = ByteBuffer.allocate(256);
	/** flag that device is connected */
	private boolean connected = false;
	/** queue for commands */
	private Queue<SimpleBinaryItemData> commandQueue = new LinkedList<SimpleBinaryItemData>();
	/** flag waiting */
	private boolean waitingForAnswer = false;
	/** store last sent data */
	private SimpleBinaryItemData lastSentData = null;
	/** counting resend data */
	private int resendCounter = 0;
	/** timer measuring answer timeout */
	private Timer timer = new Timer();
	private TimerTask timeoutTask = null;
	/** Used pool control ex.: OnChange, OnScan */
	private SimpleBinaryPoolControl poolControl;
	/** Flag indicating RTS signal will be handled */
	private boolean forceRTS;
	/** Flag indicating RTS signal will be handled on inverted logic */
	private boolean invertedRTS;
	/** Variable for count minimal time before reset RTS signal */
	private long sentTimeTicks = 0;
	/** State of serial port */
	public SimpleBinaryPortState portState = new SimpleBinaryPortState();
	/** State of connected slave devices */
	public SimpleBinaryDeviceStateCollection devicesStates;

	/**
	 * Constructor
	 * 
	 * @param deviceName
	 * @param port
	 * @param baud
	 * @param simpleBinaryPoolControl
	 * @param forceRTS
	 * @param invertedRTS
	 */
	public SimpleBinaryUART(String deviceName, String port, int baud, SimpleBinaryPoolControl simpleBinaryPoolControl, boolean forceRTS, boolean invertedRTS) {
		this.deviceName = deviceName;
		this.port = port;
		this.baud = baud;
		this.poolControl = simpleBinaryPoolControl;
		this.forceRTS = forceRTS;
		this.invertedRTS = invertedRTS;
	}

	/**
	 * Method to set binding configuration
	 * 
	 * @param eventPublisher
	 * @param itemsConfig
	 * @param itemsInfoConfig
	 */
	public void setBindingData(EventPublisher eventPublisher, Map<String, SimpleBinaryBindingConfig> itemsConfig, Map<String, SimpleBinaryInfoBindingConfig> itemsInfoConfig) {
		this.eventPublisher = eventPublisher;
		this.itemsConfig = itemsConfig;

		this.portState.setBindingData(eventPublisher, itemsInfoConfig, this.deviceName);
		this.devicesStates = new SimpleBinaryDeviceStateCollection(deviceName, itemsInfoConfig, eventPublisher);
	}

	/**
	 * Method to clear inner binding configuration
	 */
	public void unsetBindingData() {
		this.eventPublisher = null;
		this.itemsConfig = null;
		this.devicesStates = null;
	}

	/**
	 * Return port hardware name
	 * 
	 * @return
	 */
	public String getPort() {
		return port;
	}

	/**
	 * Check if port is opened
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Open serial port
	 * 
	 * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#open()
	 */
	public Boolean open() {
		logger.debug("Port {} - Opening", this.port);

		portState.setState(PortStates.CLOSED);
		// clear device states
		devicesStates.clear();
		// set initial state for configured devices
		devicesStates.setStateToAllConfiguredDevices(this.deviceName, DeviceStates.UNKNOWN);
		// reset connected state
		connected = false;
		setWaitingForAnswer(false);

		try {
			// get port ID
			portId = CommPortIdentifier.getPortIdentifier(this.port);
		} catch (NoSuchPortException ex) {
			portState.setState(PortStates.NOT_EXIST);

			logger.warn("Port {} not found", this.port);
			logger.warn("Available ports: " + getCommPortListString());

			return false;
		}

		if (portId != null) {
			// initialize serial port
			try {
				serialPort = (SerialPort) portId.open("openHAB", 2000);
			} catch (PortInUseException e) {
				portState.setState(PortStates.NOT_AVAILABLE);

				logger.error("Port {} is in use", this.port);

				this.close();
				return false;
			}

			try {
				inputStream = serialPort.getInputStream();
			} catch (IOException e) {
				logger.error("Port {} exception: {}", this.port, e.getMessage());

				this.close();
				return false;
			}

			try {
				// get the output stream
				outputStream = serialPort.getOutputStream();
			} catch (IOException e) {
				logger.error("Port {} exception:{}", this.port, e.getMessage());

				this.close();
				return false;
			}

			try {
				serialPort.addEventListener(this);
			} catch (TooManyListenersException e) {
				logger.error("Port {} exception:{}", this.port, e.getMessage());

				this.close();
				return false;
			}

			// activate the DATA_AVAILABLE notifier
			serialPort.notifyOnDataAvailable(true);
			if (this.forceRTS) {
				// OUTPUT_BUFFER_EMPTY
				serialPort.notifyOnOutputEmpty(true);
			}

			try {
				// set port parameters
				serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
				// serialPort.setRTS(false);
			} catch (UnsupportedCommOperationException e) {
				logger.error("Port {} exception: {}", this.port, e.getMessage());

				this.close();
				return false;
			}
		}

		logger.debug("Port {} - opened", this.port);

		portState.setState(PortStates.LISTENING);
		connected = true;
		return true;
	}

	/**
	 * Return list of available port
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getCommPortListString() {
		StringBuilder sb = new StringBuilder();
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
			if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				sb.append(id.getName() + "\n");
			}
		}

		return sb.toString();
	}

	/**
	 * Close serial port
	 * 
	 * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#close()
	 */
	public void close() {
		serialPort.removeEventListener();
		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(outputStream);
		serialPort.close();

		portState.setState(PortStates.CLOSED);
		connected = false;
	}

	/**
	 * Reconnect device
	 */
	private void reconnect() {
		logger.info("Port {}: Trying to reconnect", this.port);

		close();
		open();
	}

	/**
	 * Send command into device channel
	 * 
	 * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#sendData(java.lang.String,
	 *      org.openhab.core.types.Command,
	 *      org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.SimpleBinaryBindingConfig)
	 */
	public void sendData(String itemName, Command command, SimpleBinaryBindingConfig config) {
		// compile data
		// byte[] data = SimpleBinaryProtocol.compileDataFrame(itemName, command, config);
		SimpleBinaryItem data = SimpleBinaryProtocol.compileDataFrame(itemName, command, config);

		sendData(data);
	}

	/**
	 * Add compiled data item to sending queue
	 * 
	 * @param data
	 */
	public void sendData(SimpleBinaryItem data) {
		if (data != null) {
			logger.debug("Port {}: Adding command into queue", this.port);
			commandQueue.add(data);

			processCommandQueue();
		} else
			logger.warn("Port {}: Nothing to send. Empty data", this.port);
	}

	/**
	 * Prepare request to check if device with specific address has new data
	 * 
	 * @param deviceAddress
	 */
	private void sendNewDataCheck(int deviceAddress) {
		SimpleBinaryItemData data = SimpleBinaryProtocol.compileNewDataFrame(deviceAddress);

		commandQueue.add(data);

		processCommandQueue();
	}

	/**
	 * Prepare request for read data of specific item
	 * 
	 * @param itemConfig
	 */
	private void sendReadData(SimpleBinaryBindingConfig itemConfig) {
		SimpleBinaryItemData data = SimpleBinaryProtocol.compileReadDataFrame(itemConfig);

		// check if packet already exist
		if (!dataInQueue(data))
			commandQueue.add(data);

		processCommandQueue();
	}

	/**
	 * Check if data packet is not already in queue
	 * 
	 * @param item
	 * @return
	 */
	private boolean dataInQueue(SimpleBinaryItemData item) {
		if (commandQueue.isEmpty())
			return false;

		for (SimpleBinaryItemData qitem : commandQueue) {
			if (qitem.getData().length != item.getData().length)
				break;

			for (int i = 0; i < qitem.getData().length; i++) {
				if (qitem.getData()[i] != item.getData()[i])
					break;

				if (i == qitem.getData().length - 1)
					return true;
			}
		}

		return false;
	}

	/**
	 * Check if queue is not empty and send data to device
	 * 
	 */
	private void processCommandQueue() {
		logger.debug("Port {} - Processing commandQueue - length {}", this.port, commandQueue.size());

		if (commandQueue.isEmpty())
			return;

		if (!waitingForAnswer) {
			resendCounter = 0;
			sendDataOut(commandQueue.remove());
		} else
			logger.debug("Port {} - Processing commandQueue - waiting", this.port);
	}

	/**
	 * Write data into device stream
	 * 
	 * @param data
	 *            Item data with compiled packet
	 */
	private void sendDataOut(SimpleBinaryItemData data) {
		logger.debug("Port {} - Sending data to device {} with length {} bytes", this.port, data.getDeviceId(), data.getData().length);
		logger.debug("Port {} - data: {}", this.port, SimpleBinaryProtocol.arrayToString(data.getData(), data.getData().length));

		try {
			// set RTS
			if (this.forceRTS) {
				logger.debug("Port {} - RTS set", this.port);

				// calc minumum time for send + currentTime + 0ms
				sentTimeTicks = System.currentTimeMillis() + (((data.getData().length * 8) + 4) * 1000 / this.baud);

				serialPort.setRTS(invertedRTS ? false : true);
			}

			// write string to serial port
			outputStream.write(data.getData());
			outputStream.flush();

			setWaitingForAnswer(true);

			setLastSentData(data);
		} catch (IOException e) {
			logger.error("Port {} - Error while writing", this.port);

			reconnect();
		}
	}

	/**
	 * Set / reset waiting task for answer for slave device
	 * 
	 * @param state
	 */
	private void setWaitingForAnswer(boolean state) {
		waitingForAnswer = state;

		if (state) {
			if (timeoutTask != null)
				timeoutTask.cancel();

			timeoutTask = new TimerTask() {
				@Override
				public void run() {
					timeoutTask = null;
					dataTimeouted();
					setWaitingForAnswer(false);
				}
			};

			timer.schedule(timeoutTask, 2000);
		} else {
			if (timeoutTask != null) {
				timeoutTask.cancel();
				timeoutTask = null;
			}
		}
	}

	/**
	 * Method processed after waiting for answer is timeouted
	 */
	private void dataTimeouted() {
		logger.warn("Port {} - Receiving data timeouted", this.port);

		int address = this.getLastSentData().getDeviceId();

		devicesStates.setDeviceState(this.deviceName, address, DeviceStates.NOT_RESPONDING);

		logger.debug("Port {} - Input buffer cleared", this.port);

		inBuffer.clear();
	}

	/**
	 * Serial events
	 * 
	 * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
	 */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
			break;
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			// reset RTS
			if (this.forceRTS && (serialPort.isRTS() && !invertedRTS || !serialPort.isRTS() && invertedRTS)) {

				// comply minimum sending time. Added because on ubuntu14.04 event OUTPUT_BUFFER_EMPTY is called
				// periodically even before some data are send.
				if (System.currentTimeMillis() > sentTimeTicks) {
					serialPort.setRTS(invertedRTS ? true : false);

					logger.debug("Port {} - RTS reset", this.port);
				}
			}
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			try {
				while (inputStream.available() > 0) {
					if (!readIncomingData())
						break;
				}

				// logger.debug("Port {} - Received data - buffer length {} bytes", port, inBuffer.position());

				// check data
				if (itemsConfig != null) {
					// check minimum length
					while (inBuffer.position() > 3) {
						// flip buffer
						inBuffer.flip();

						logger.debug("Port {} - Reading input buffer, lenght={} bytes", port, inBuffer.limit());

						try {
							int first = inBuffer.get();
							inBuffer.rewind();

							// check expected address
							if (lastSentData.getDeviceId() != first) {
								throw new WrongAddressException(lastSentData.getDeviceId(), first);
							}

							// decompile income message
							SimpleBinaryMessage itemData = SimpleBinaryProtocol.decompileData(inBuffer, itemsConfig, deviceName);

							if (itemData != null) {
								setWaitingForAnswer(false);

								if (itemData instanceof SimpleBinaryItem) {
									logger.trace("Port {} - Incoming data", port);

									resendCounter = 0;

									// set state
									devicesStates.setDeviceState(this.deviceName, ((SimpleBinaryItem) itemData).getDeviceId(), DeviceStates.CONNECTED);

									State state = ((SimpleBinaryItem) itemData).getState();

									if (state == null) {
										logger.warn("Port {} - Incoming data - Unknown item state", port);
									} else {
										logger.debug("Port {} - Incoming data - item:{}/state:{}", port, ((SimpleBinaryItem) itemData).name, state);

										if (eventPublisher != null)
											eventPublisher.postUpdate(((SimpleBinaryItem) itemData).name, state);
									}

									// if data income on request "check new data" send it again for new check
									if (getLastSentData().getMessageType() == SimpleBinaryMessageType.CHECKNEWDATA) {
										inBuffer.compact();
										logger.debug("Port {} - Repeat CHECKNEWDATA command", port);
										this.resendData();
										break;
									}
								} else if (itemData instanceof SimpleBinaryMessage) {
									logger.debug("Port {} - Incoming control message", port);

									// address
									int itemAddress = lastSentData.itemAddress;

									// set state
									devicesStates.setDeviceState(this.deviceName, ((SimpleBinaryMessage) itemData).getDeviceId(), DeviceStates.CONNECTED);

									if (itemData.getMessageType() == SimpleBinaryMessageType.OK) {
										logger.debug("Port {} - Device {} for item {} report data OK", port, itemData.getDeviceId(), itemAddress);

										resendCounter = 0;

									} else if (itemData.getMessageType() == SimpleBinaryMessageType.RESEND) {
										logger.info("Port {} - Device {} for item {} request resend data", port, itemData.getDeviceId(), itemAddress);

										if (resendCounter < MAX_RESEND_COUNT) {
											inBuffer.compact();
											this.resendData();
											resendCounter++;
											break;
										} else {
											logger.warn("Port {} - Max resend attempts reached.", port);
											// set state
											devicesStates.setDeviceState(this.deviceName, ((SimpleBinaryMessage) itemData).getDeviceId(), DeviceStates.RESPONSE_ERROR);

											resendCounter = 0;
										}
									} else if (itemData.getMessageType() == SimpleBinaryMessageType.NODATA) {
										logger.debug("Port {} - Device {} answer no new data", port, itemData.getDeviceId());

										resendCounter = 0;

									} else if (itemData.getMessageType() == SimpleBinaryMessageType.UNKNOWN_DATA) {
										logger.warn("Port {} - Device {} report unknown data", port, itemData.getDeviceId());
										logger.debug("Port {} - Last sent data:", port);
										logger.debug(lastSentData.getData().toString());

										// set state
										devicesStates.setDeviceState(this.deviceName, ((SimpleBinaryMessage) itemData).getDeviceId(), DeviceStates.DATA_ERROR);

										resendCounter = 0;

									} else if (itemData.getMessageType() == SimpleBinaryMessageType.UNKNOWN_ADDRESS) {
										logger.warn("Port {} - Device {} for item {} report unknown address", port, itemData.getDeviceId(), itemAddress);
										logger.debug("Port {} - Last sent data:", port);
										logger.debug(lastSentData.getData().toString());

										// set state
										devicesStates.setDeviceState(this.deviceName, ((SimpleBinaryMessage) itemData).getDeviceId(), DeviceStates.DATA_ERROR);

										resendCounter = 0;

									} else if (itemData.getMessageType() == SimpleBinaryMessageType.SAVING_ERROR) {
										logger.warn("Port {} - Device {} for item {} report saving data error", port, itemData.getDeviceId(), itemAddress);
										logger.debug("Port {} - Last sent data:", port);
										logger.debug(lastSentData.getData().toString());

										// set state
										devicesStates.setDeviceState(this.deviceName, ((SimpleBinaryMessage) itemData).getDeviceId(), DeviceStates.DATA_ERROR);

										resendCounter = 0;

									} else {
										resendCounter = 0;
										logger.warn("Port {} - Device {} - Unsupported message type received: " + itemData.getMessageType().toString(), port,
												itemData.getDeviceId(), port);

										// set state
										devicesStates.setDeviceState(this.deviceName, ((SimpleBinaryMessage) itemData).getDeviceId(), DeviceStates.DATA_ERROR);
									}
								}

								// compact buffer
								inBuffer.compact();
								processCommandQueue();

							} else {
								inBuffer.rewind();
								// compact buffer
								inBuffer.compact();

								break;
							}
						} catch (BufferUnderflowException ex) {
							logger.warn("Port {} - Buffer underflow while reading: {}", this.port, ex.getMessage());
							// print details
							printCommunicationInfo();

							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw);
							ex.printStackTrace(pw);
							logger.warn("Underflow stacktrace: " + sw.toString());

							logger.warn("Buffer mode {}, remaining={} bytes, limit={} bytes", inBuffer.getMode().toString(), inBuffer.remaining(), inBuffer.limit());

							try {
								// inBuffer.clear();

								// rewind buffer
								inBuffer.rewind();
								logger.warn("Buffer mode {}, remaining after rewind {} bytes", inBuffer.getMode().toString(), inBuffer.remaining());
								// compact buffer
								inBuffer.compact();
							} catch (ModeChangeException exep) {
								logger.error(exep.getMessage());
							}

							break;

						} catch (NoValidCRCException ex) {
							logger.error("Port {} - Invalid CRC while reading: {}", this.port, ex.getMessage());
							// print details
							printCommunicationInfo();
							// compact buffer
							inBuffer.compact();

							if (resendCounter < MAX_RESEND_COUNT) {
								this.resendData();
								resendCounter++;
								break;
							} else {
								setWaitingForAnswer(false);
								processCommandQueue();

								// set state
								devicesStates.setDeviceState(this.deviceName, lastSentData.getDeviceId(), DeviceStates.RESPONSE_ERROR);
							}
						} catch (NoValidItemInConfig ex) {
							logger.error("Port {} - Item not found in items config: {}", this.port, ex.getMessage());
							// print details
							printCommunicationInfo();
							// compact buffer
							inBuffer.compact();

							setWaitingForAnswer(false);
							processCommandQueue();

							// set state
							devicesStates.setDeviceState(this.deviceName, lastSentData.getDeviceId(), DeviceStates.DATA_ERROR);
						} catch (WrongAddressException ex) {
							logger.error("Port {} - Address not valid: {}", this.port, ex.getMessage());
							// print details
							printCommunicationInfo();

							setWaitingForAnswer(false);

							// only 4 bytes (minus 1 after delete first byte) is not enough to have complete message
							if (inBuffer.limit() < 5) {
								// clear buffer
								inBuffer.clear();

								logger.warn("Port {} - Address not valid: input buffer cleared", this.port);
								processCommandQueue();

								// set state
								devicesStates.setDeviceState(this.deviceName, lastSentData.getDeviceId(), DeviceStates.DATA_ERROR);
							} else {
								logger.warn("Port {} - Address not valid: {} bytes in buffer. Triyng to find correct message. ", this.port, inBuffer.limit());
								// delete first byte
								inBuffer.rewind();
								inBuffer.get();
								inBuffer.compact();
							}

						} catch (UnknownMessageException ex) {
							logger.error("Port {} - Income unknown message: {}", this.port, ex.getMessage());
							// print details
							printCommunicationInfo();

							setWaitingForAnswer(false);

							// inBuffer.rewind();

							// only 4 bytes (minus 1 after delete first byte) is not enough to have complete message
							if (inBuffer.limit() < 5) {
								// clear buffer
								inBuffer.clear();

								logger.warn("Port {} - Income unknown message: input buffer cleared", this.port);
								processCommandQueue();

								// set state
								devicesStates.setDeviceState(this.deviceName, lastSentData.getDeviceId(), DeviceStates.DATA_ERROR);
							} else {
								logger.warn("Port {} - Income unknown message : {} bytes in buffer. Triyng to find correct message. ", this.port, inBuffer.limit());
								// delete first byte
								inBuffer.rewind();
								inBuffer.get();
								inBuffer.compact();
							}

						} catch (NotImplementedException ex) {
							logger.warn("Port {} - Message not implemented: {}", this.port, ex.getMessage());
							// print details
							printCommunicationInfo();
							inBuffer.clear();

							setWaitingForAnswer(false);
							processCommandQueue();

							// set state
							devicesStates.setDeviceState(this.deviceName, lastSentData.getDeviceId(), DeviceStates.DATA_ERROR);
						} catch (ModeChangeException ex) {
							logger.error("Port {} - Bad operation: {}", this.port, ex.getMessage());

							inBuffer.initialize();

							// print details
							printCommunicationInfo();

						} catch (Exception ex) {
							logger.error("Port {} - Reading incoming data error: {}", this.port, ex.getMessage());
							// print details
							printCommunicationInfo();
							inBuffer.clear();
							processCommandQueue();
						}

						// check for new data
						while (inputStream.available() > 0) {
							logger.debug("Port {} - another new data - {}bytes", this.port, inputStream.available());
							if (!readIncomingData())
								break;
						}
					}
				}
			} catch (IOException e) {
				logger.error("Port {} - Error receiving data: {}", port, e.getMessage());
			} catch (Exception ex) {
				logger.error("Port {} - Exception receiving data: {}", port, ex.toString());
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				logger.error(sw.toString());
			}
			break;
		}
	}

	/**
	 * Print communication information
	 * 
	 * @throws ModeChangeException
	 */
	private void printCommunicationInfo() throws ModeChangeException {
		// content of input buffer
		int position = inBuffer.position();
		inBuffer.rewind();
		byte[] data = new byte[inBuffer.limit()];
		inBuffer.get(data);
		inBuffer.position(position);
		logger.info("Port {} - Data in input buffer: {}", port, SimpleBinaryProtocol.arrayToString(data, data.length));

		// last data out
		logger.info("Port {} - Last sent data: {}", port, SimpleBinaryProtocol.arrayToString(lastSentData.getData(), lastSentData.getData().length));
	}

	/**
	 * Read data from serial port buffer
	 * 
	 * @throws IOException
	 * @throws ModeChangeException
	 */
	private boolean readIncomingData() throws IOException, ModeChangeException {

		byte[] readBuffer = new byte[32];
		int bytes = inputStream.read(readBuffer);

		logger.debug("Port {} - Received incoming data length {} bytes", port, bytes);
		logger.debug("Port {} - data: {}", port, SimpleBinaryProtocol.arrayToString(readBuffer, bytes));

		if (bytes + inBuffer.position() >= inBuffer.capacity()) {
			logger.error("Port {} - Buffer overrun", port);
			return false;
		} else {
			inBuffer.put(readBuffer, 0, bytes);
		}

		return true;
	}

	/**
	 * Provide resending last sent message
	 * 
	 * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#resendData()
	 */
	public void resendData() {

		// TODO: minimum time for data line stabilizing

		if (getLastSentData() != null) {
			logger.debug("Port {} - Resend data", port);

			// resendCounter++;
			sendDataOut(getLastSentData());

			logger.debug("Port {} - resendCounter {}", port, resendCounter);
		}
	}

	/**
	 * Return last sent message
	 * 
	 * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#getLastSentData()
	 */
	public SimpleBinaryItemData getLastSentData() {
		return lastSentData;
	}

	/**
	 * Set last sent data
	 * 
	 * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#setLastSentData(org.openhab.binding.simplebinary.internal.SimpleBinaryItemData)
	 */
	public void setLastSentData(SimpleBinaryItemData data) {
		lastSentData = data;
	}

	/**
	 * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#checkNewData()
	 */
	public void checkNewData() {

		if (isConnected()) {
			logger.debug("Port {} - checkNewData() in mode {} is called", port, poolControl);

			if (poolControl == SimpleBinaryPoolControl.ONSCAN) {
				for (Map.Entry<String, SimpleBinaryBindingConfig> item : itemsConfig.entrySet()) {
					if (item.getValue().device.equals(this.deviceName)) {
						logger.debug("Port {} - checkNewData() item={} direction={} ", port, item.getValue().item.getName(), item.getValue().direction);
						// input direction only
						if (item.getValue().direction < 2)
							this.sendReadData(item.getValue());
					}
				}
			} else {
				List<Integer> deviceItems = new ArrayList<Integer>();

				for (Map.Entry<String, SimpleBinaryBindingConfig> item : itemsConfig.entrySet()) {
					if (item.getValue().device.equals(this.deviceName)) {
						if (!deviceItems.contains(item.getValue().busAddress))
							deviceItems.add(item.getValue().busAddress);
					}
				}

				for (Integer integer : deviceItems) {
					this.sendNewDataCheck(integer);
				}

				deviceItems = null;
			}
		}
	}
}
