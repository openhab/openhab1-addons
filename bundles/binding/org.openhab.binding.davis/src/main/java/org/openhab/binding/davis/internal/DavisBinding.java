/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.davis.internal;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.davis.DavisBindingProvider;
import org.openhab.binding.davis.datatypes.DavisCommandType;
import org.openhab.binding.davis.datatypes.DavisValueType;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Binding for acquiring data from Davis Weather stations, e.g. Vantage Pro 2
 * 
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public class DavisBinding extends AbstractActiveBinding<DavisBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(DavisBinding.class);

	
	/** 
	 * the refresh interval which is used to poll values from the Davis
	 * weather station (optional, defaults to 10000ms)
	 */
	private long refreshInterval = 10000;

	private static final int BUF_LENGTH = 256;
	
	private String port;
	private SerialPort serialPort;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	
	public DavisBinding() {
	}
		
	
	public void activate() {
	}
	
	public void deactivate() {				
		closePort();
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Davis Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		//logger.debug("execute() method is called!");
		if (isProperlyConfigured()) {

			// get all configured keys from providers, get needed read commands
			// for them, send those read commands
			for (DavisBindingProvider provider : providers) {
				Collection<DavisCommand> commands = DavisValueType.getReadCommandsByKeys(provider.getConfiguredKeys());

				try {
					wakeup();
					for (DavisCommand command : commands) {
						sendCommand(command);
					}
				} catch (IOException e) {
					logger.error("Wakeup failed!");
				}
			}
		}
	}
		
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			// to override the default refresh interval one has to add a 
			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			String newPort = (String) config.get("port"); //$NON-NLS-1$
			if (StringUtils.isNotBlank(newPort) && !newPort.equals(port)) {
				
				try {
					openPort(newPort);
					setProperlyConfigured(true);
				} catch (InitializationException e) {
					logger.error(e.getMessage());
					setProperlyConfigured(false);
				}				
			}
		}
	}	
	
	public void openPort(String portName) throws InitializationException {
		CommPortIdentifier portIdentifier;

		port = portName;
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(port);

			try {
				serialPort = (SerialPort) portIdentifier.open("openhab", 3000);
				serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				inputStream = new DataInputStream(new BufferedInputStream(serialPort.getInputStream()));
				outputStream = serialPort.getOutputStream();

			} catch (PortInUseException e) {
				throw new InitializationException(e);
			} catch (UnsupportedCommOperationException e) {
				throw new InitializationException(e);
			} catch (IOException e) {
				throw new InitializationException(e);
			}

		} catch (NoSuchPortException e) {
			StringBuilder sb = new StringBuilder();
			Enumeration portList = CommPortIdentifier.getPortIdentifiers();
			while (portList.hasMoreElements()) {
				CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
				if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					sb.append(id.getName() + "\n");
				}
			}

			throw new InitializationException("Serial port '" + port
					+ "' could not be found. Available ports are:\n"
					+ sb.toString());
		}
	}
	
	public void closePort() {
		try {
			inputStream.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		serialPort.close();
	}
	
	
	public void sendCommand(DavisCommand command) {
		//logger.debug("sendCommand() method is called!");		
		
		try {

			//command über rs232 schicken (inkl '\n'), wait for ACK
			logger.debug("TX: "+command.getRequestCmd());
			writeString(command.getRequestCmd() + '\n');

			sleep(250);
			DavisCommandType commandType = DavisCommandType.getCommandTypeByCommand(command.getRequestCmd());
			switch (commandType.getResponsetype()) {
				case Constants.RESPONSE_TYPE_NONE:
					break;
				case Constants.RESPONSE_TYPE_ACK:
					int in = inputStream.read();
					if ( in != Constants.ACK) {
			            throw new IOException("Invalid response");
			        }
					break;
				case Constants.RESPONSE_TYPE_OK:
					expectString("\n\rOK\n\r");
					break;
			}
			
			byte[] inputBuf = null;
			switch (commandType.getResponselimitertype()) {
				case Constants.RESPONSE_LIMITER_TYPE_CRLF:
					inputBuf = new BufferedReader(new InputStreamReader(inputStream)).readLine().getBytes();					
					break;
				case Constants.RESPONSE_LIMITER_TYPE_FIXED_SIZE:
					inputBuf = readBytes(commandType.getResponselength());			        
					break;
				case Constants.RESPONSE_LIMITER_TYPE_MULTIPLE_CRLF:
					//TODO add support
					break;
			}			
			
			switch (commandType.getCrcchecktype()) {
				case Constants.CRC_CHECK_TYPE_VAR1:
					if (!CRC16.check(inputBuf, 0, inputBuf.length)) {
						throw new IOException("CRC error");
					}
					break;
				case Constants.CRC_CHECK_TYPE_NONE:
					break;
			}

			//aus response alle werte dekodieren für es ein item gibt -> postUpdate
			Set<DavisValueType> valueTypes = DavisValueType.getValueTypesByCommandType(commandType);
			for (DavisValueType valueType : valueTypes) {				
				//get items and post Updates for all items with key
				for (DavisBindingProvider provider : providers) {
					List<String> itemNames = provider.getItemNamesForKey(valueType.getKey());
					State state = valueType.getDataType().convertToState(inputBuf, valueType);
					for (String itemName : itemNames) {
						eventPublisher.postUpdate(itemName, state);
					}
				}
			}
		} catch (IOException e) {
			//Drop the rest
			try {
				sleep(2000);
				while (inputStream.read() != -1) {
					//Throw away.
				}
				logger.warn("IO Exception in sendCommand() method, dropped remaining data!");						
				writeString("RXTEST\n");
				expectString("\n\rOK\n\r");
			} catch (IOException e1) {
				logger.error("IO Exception in dropping corrupt data!");
				e1.printStackTrace();
			}
		}
	}
	
	protected void writeString(String string) throws IOException {
		outputStream.write(string.getBytes());
		outputStream.flush();
	}
	
	protected void expectString(String string) throws IOException {
		int length = string.length();
		byte[] buf = readBytes(length);
		String s = new String(buf);
		if (!string.equals(s)) {
			throw new IOException("Invalid response: " + escape(s));
		}
	}

	protected byte[] readBytes(int length) throws IOException {
		byte[] buf = new byte[length];
		for (int i = 0; i < length; i++) {
			int c = inputStream.read();
			if (c != -1) {
				buf[i] = (byte) (c & 0xff);
			} else {
				throw new IOException("Unexpected EOF");
			}
		}
		return buf;
	}

	protected boolean wakeup() throws IOException {
		// Send wakeup command.
		boolean awake = false;
		int i = 0;
		while (awake == false && i++ < 3) {
			writeString("\n");
			try {
				expectString("\n\r");
				awake = true;
			} catch (IOException e) {
				// Ignore.
			}
			sleep(i*400);
		}
		return awake;
	}

	protected void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
			// Ignore.
		}
	}
	
	protected String escape(String string) {
		byte[] bytes = string.getBytes();
		return escape(bytes, 0, bytes.length, true);
	}

	protected String escape(byte[] bytes, int offset, int length, boolean printWritable) {
		if (offset > length) {
			throw new IllegalArgumentException("offset " + offset + " is greater than length " + length);
		}
		StringBuilder buf = new StringBuilder();
		for (int i = offset; i < (offset + length); i++) {
			switch (bytes[i]) {
				case '\n':
					buf.append(printWritable ? "\\n" : "<0x0a>");
					break;
				case '\r':
					buf.append(printWritable ? "\\r" : "<0x0d>");
					break;
				case '\t':
					buf.append(printWritable ? "\\t" : "<0x09>");
					break;
				case 0x06:
					buf.append(printWritable ? "<ACK>" : "<0x06>");
					break;
				case 0x18:
					buf.append(printWritable ? "<CAN>" : "<0x18>");
					break;
				case 0x21:
					buf.append(printWritable ? "<NAK>" : "<0x21>");
					break;
				default:
					if (bytes[i] < 0x20 || bytes[i] > 0x7e || !printWritable) {
						String s = Integer.toHexString((int) bytes[i] & 0x000000ff);
						buf.append("<0x");
						if (s.length() == 1) {
							buf.append('0');
						}
						buf.append(s);
						buf.append('>');
					} else {
						buf.append((char) bytes[i]);
					}
					break;
			}
		}
		return buf.toString();
	}
	
}
