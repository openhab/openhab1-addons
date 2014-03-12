/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rme.internal;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.IllegalClassException;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.rme.RMEBindingProvider;
import org.openhab.binding.rme.RMEValueSelector;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * @author Karel Goderis
 * @author Pauli Anttila
 * @since 1.1.0
 * 
 */
public class RMEBinding extends AbstractActiveBinding<RMEBindingProvider>
implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(RMEBinding.class);

	private Map<String, SerialDevice> serialDevices = new HashMap<String, SerialDevice>();

	/** stores information about the which items are associated to which port. The map has this content structure: itemname -> port */ 
	private Map<String, String> itemMap = new HashMap<String, String>();

	/** stores information about the context of items. The map has this content structure: context -> Set of itemNames */ 
	private Map<String, Set<String>> contextMap = new HashMap<String, Set<String>>();

	/** the refresh interval which is used to check for changes in the binding configurations */
	private static long refreshInterval = 5000;

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;

		for(SerialDevice serialDevice : serialDevices.values()) {
			serialDevice.setEventPublisher(eventPublisher);
		}
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;

		for(SerialDevice serialDevice : serialDevices.values()) {
			serialDevice.setEventPublisher(null);
		}
	}

	public void activate() {
		// Nothing to do here. We start the binding when the first item bindigconfig is processed
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {



	}



	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {}

		setProperlyConfigured(true);

	}

	@Override
	protected void execute() {
		if(isProperlyConfigured()) {
			for(RMEBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {

					String serialPort = ((RMEBindingProvider)provider).getSerialPort(itemName);

					SerialDevice serialDevice = serialDevices.get(serialPort);
					if (serialDevice == null) {
						serialDevice = new SerialDevice(serialPort);
						serialDevice.setEventPublisher(eventPublisher);
						try {
							serialDevice.initialize();
						} catch (InitializationException e) {
							logger.error("Could not open serial port " + serialPort + ": "
									+ e.getMessage());

						} catch (Throwable e) {
							logger.error("Could not open serial port " + serialPort + ": "
									+ e.getMessage());
						}
						itemMap.put(itemName, serialPort);
						serialDevices.put(serialPort, serialDevice);
					}

					Set<String> itemNames = contextMap.get(serialPort);
					if (itemNames == null) {
						itemNames = new HashSet<String>();
						contextMap.put(serialPort, itemNames);
					}
					itemNames.add(itemName);
				}

				//			} else {
				//				// the Item is removed from the provider
				//
				//				// remove serial device is no item left for serial device
				//				// we remove all information in the serial devices
				//				SerialDevice serialDevice = serialDevices.get(itemMap.get(itemName));
				//				itemMap.remove(itemName);
				//				if(serialDevice==null) {
				//					return;
				//				}
				//
				//				// if there is no binding left, dispose this device
				//				Set<String> itemNames = contextMap.get(serialDevice.getPort());
				//				if(itemNames != null && itemNames.size()==0) {
				//					contextMap.remove(serialDevice.getPort());
				//					serialDevice.close();
				//					serialDevices.remove(serialDevice.getPort());
				//				}
				//
				//			}		
			}
		}
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Sonos Refresh Service";
	}

	protected class SerialDevice implements SerialPortEventListener {

		private String port;
		private int baud = 2400;
		private String previousLine=null;
		private HashMap<RMEValueSelector,String> cachedValues = new HashMap<RMEValueSelector,String>();

		private EventPublisher eventPublisher;

		private CommPortIdentifier portId;
		private SerialPort serialPort;

		private InputStream inputStream;

		private OutputStream outputStream;

		public SerialDevice(String port) {
			this.port = port;
		}

		public SerialDevice(String port, int baud) {
			this.port = port;
			this.baud = baud;
		}

		public void setEventPublisher(EventPublisher eventPublisher) {
			this.eventPublisher = eventPublisher;
		}

		public void unsetEventPublisher(EventPublisher eventPublisher) {
			this.eventPublisher = null;
		}

		public String getPort() {
			return port;
		}

		/**
		 * Initialize this device and open the serial port
		 * 
		 * @throws InitializationException if port can not be opened
		 */
		@SuppressWarnings("rawtypes")
		public void initialize() throws InitializationException {
			// parse ports and if the default port is found, initialized the reader
			Enumeration portList = CommPortIdentifier.getPortIdentifiers();
			while (portList.hasMoreElements()) {
				CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
				if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					if (id.getName().equals(port)) {
						logger.debug("Serial port '{}' has been found.", port);
						portId = id;
					}
				}
			}
			if (portId != null) {
				// initialize serial port
				try {
					serialPort = (SerialPort) portId.open("openHAB", 2000);
				} catch (PortInUseException e) {
					throw new InitializationException(e);
				}

				try {
					inputStream = serialPort.getInputStream();
				} catch (IOException e) {
					throw new InitializationException(e);
				}

				try {
					serialPort.addEventListener(this);
				} catch (TooManyListenersException e) {
					throw new InitializationException(e);
				}

				// activate the DATA_AVAILABLE notifier
				serialPort.notifyOnDataAvailable(true);

				try {
					// set port parameters
					serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
				} catch (UnsupportedCommOperationException e) {
					throw new InitializationException(e);
				}

				try {
					// get the output stream
					outputStream = serialPort.getOutputStream();
				} catch (IOException e) {
					throw new InitializationException(e);
				}
			} else {
				StringBuilder sb = new StringBuilder();
				portList = CommPortIdentifier.getPortIdentifiers();
				while (portList.hasMoreElements()) {
					CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
					if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
						sb.append(id.getName() + "\n");
					}
				}
				throw new InitializationException("Serial port '" + port + "' could not be found. Available ports are:\n" + sb.toString());
			}
		}

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
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;
			case SerialPortEvent.DATA_AVAILABLE:

				try {

					BufferedReader br = new BufferedReader(new InputStreamReader(inputStream), 32*1024*1024);
					while (br.ready()) {
						String line = br.readLine();
						line = StringUtils.chomp(line);

						// little hack to overcome Locale limits of the RME Rain Manager
						line = line.replace(",",".");
						line = line.trim();

						if(previousLine==null) {
							previousLine = line;
						}

						if(!previousLine.equals(line)) {
							processData(line);
							previousLine=line;
						}
					}

				} catch (IOException e) {
					logger.debug("Error receiving data on serial port {}: {}", new Object[] { port, e.getMessage() });
				}
				break;
			}
		}

		private void processData(String data) {

			if(data != null) {

				Pattern RESPONSE_PATTERN = Pattern.compile("(.*);(0|1);(0|1);(0|1);(0|1);(0|1);(0|1);(0|1);(0|1);(0|1)");

				Matcher matcher = RESPONSE_PATTERN.matcher(data);
				if(matcher.matches()) {

					for (RMEBindingProvider provider : providers) {
						for (String itemName : provider.getItemNames()) {
							String serialPort = provider.getSerialPort(itemName);
							RMEValueSelector selector = provider.getValueSelector(itemName);

							if (port.equals(serialPort)) {
								if(cachedValues.get(selector)==null || !cachedValues.get(selector).equals(matcher.group(selector.getFieldIndex()))) {
									cachedValues.put(selector, matcher.group(selector.getFieldIndex()));
									State value;
									try {
										if(matcher.group(selector.getFieldIndex()).equals("0")) {
											value = createStateForType(selector,"OFF");
										} else 	if(matcher.group(selector.getFieldIndex()).equals("1")) {
											value = createStateForType(selector,"ON");
										} else {
											value = createStateForType(selector,matcher.group(selector.getFieldIndex()));
										}
									} catch (BindingConfigParseException e) {
										logger.error("An exception occured while converting {} to a valide state : {}",matcher.group(selector.getFieldIndex()),e.getMessage());
										return;
									}

									eventPublisher.postUpdate(itemName, value);
								}
							}
						}
					}
				}
			}
		}


		@SuppressWarnings("unchecked")
		private State createStateForType(RMEValueSelector selector, String value) throws BindingConfigParseException {

			Class<? extends Type> typeClass  = selector.getTypeClass();
			List<Class<? extends State>> stateTypeList = new ArrayList<Class<? extends State>>();

			stateTypeList.add((Class<? extends State>) typeClass);

			State state = TypeParser.parseState(stateTypeList, value);

			return state;	
		}

		/**
		 * Sends a string to the serial port of this device
		 * 
		 * @param msg the string to send
		 */
		public void writeString(String msg) {
			logger.debug("Writing '{}' to serial port {}", new String[] { msg, port });
			try {
				// write string to serial port
				outputStream.write(msg.getBytes());
				outputStream.flush();
			} catch (IOException e) {
				logger.error("Error writing '{}' to serial port {}: {}", new String[] { msg, port, e.getMessage() });
			}
		}

		/**
		 * Close this serial device
		 */
		public void close() {
			serialPort.removeEventListener();
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
			serialPort.close();
		}

	}


}
