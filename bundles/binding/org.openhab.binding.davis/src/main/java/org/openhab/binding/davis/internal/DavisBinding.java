/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
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
		logger.trace("execute() method is called!");
		
		try {
			openPort();
				
			// get all configured keys from providers, get needed read commands
			// for them, send those read commands
			for (DavisBindingProvider provider : providers) {
				Collection<DavisCommand> commands = DavisValueType.getReadCommandsByKeys(provider.getConfiguredKeys());
		
				for (DavisCommand command : commands) {					
					if (wakeup()) {					
						sendCommand(command);
					} else {
						logger.warn("Wakeup failed, trying reset sequence!");
						resetAfterError();
					}
				}
			}
			
			closePort();
			
		} catch (InitializationException e) {
			logger.error(e.getMessage());					
		}
				
		logger.trace("execute() method is finished!");
	}
		
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.trace("update() method is called!");
		if (config != null) {
			// to override the default refresh interval one has to add a 
			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			String newPort = (String) config.get("port"); //$NON-NLS-1$
			if (StringUtils.isNotBlank(newPort) && !newPort.equals(port)) {				
				port = newPort;
				setProperlyConfigured(true);				
			}
		}
	}	
	
	public void openPort() throws InitializationException {
		CommPortIdentifier portIdentifier;
		
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(port);

			try {
				serialPort = (SerialPort) portIdentifier.open("openhab", 3000);
				serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				//serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
				serialPort.enableReceiveTimeout(100);
				serialPort.enableReceiveThreshold(1);				
				inputStream = new DataInputStream(new BufferedInputStream(serialPort.getInputStream()));				
				outputStream = serialPort.getOutputStream();
				logger.debug("port opened: "+port);
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
		logger.debug("port closed: "+port);
	}
	
	protected void resetAfterError() {
		//Drop the rest
		try {
			logger.warn("error, dropping remaining data!");
			readResponse();
			wakeup();
			writeString("RXTEST\n");
			byte[] buf = readResponse();
			expectString(buf,"\n\rOK\n\r");
		} catch (IOException e1) {
			logger.warn("IO Exception reset after Error: "+e1);			
			closePort(); 
			try {
				openPort();
			} catch (InitializationException e) {
				logger.error("reopening port failed!");
			}
		}
	}
	
	public byte[] readResponse() throws IOException {
		byte[] responseBlock = new byte[0];
		byte[] readBuffer = new byte[BUF_LENGTH];

		do {
			while (inputStream.available() > 0) {

				int bytes = inputStream.read(readBuffer);

				// merge bytes
				byte[] mergedBytes = new byte[responseBlock.length
						+ bytes];
				System.arraycopy(responseBlock, 0, mergedBytes, 0,
						responseBlock.length);
				System.arraycopy(readBuffer, 0, mergedBytes,
						responseBlock.length, bytes);

				responseBlock = mergedBytes;
			}
			try {
				// add wait states around reading the stream, so that
				// interrupted transmissions are merged
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// ignore interruption
			}

		} while (inputStream.available() > 0);
		String string = new String(responseBlock);
		logger.debug("RX: "+escape(string));
		return responseBlock;
	}
	
	
	public void sendCommand(DavisCommand command) {
		byte[] responseBlock;
		int offset = 0;
		
		logger.debug("sendCommand() method is called!");		
		DavisCommandType commandType = DavisCommandType.getCommandTypeByCommand(command.getRequestCmd());
		
		try {

			//command über rs232 schicken (inkl '\n'), wait for ACK			
			writeString(command.getRequestCmd() + '\n');

			responseBlock = readResponse();

			
			switch (commandType.getResponsetype()) {
				case Constants.RESPONSE_TYPE_NONE:
					break;
				case Constants.RESPONSE_TYPE_ACK:	
					byte[] resp = new byte[1];
					resp[0] = Constants.ACK;
					expectString(responseBlock,new String(resp));					
					offset = 1;
					break;
				case Constants.RESPONSE_TYPE_OK:
					expectString(responseBlock,"\n\rOK\n\r");
					offset = 6;
					break;
			}
			
			switch (commandType.getResponselimitertype()) {
				case Constants.RESPONSE_LIMITER_TYPE_CRLF:
					if (responseBlock[responseBlock.length-2]!='\n' || responseBlock[responseBlock.length-1]!='\r') {
			            throw new IOException("expected CRLF at end of response missing");
					}
					break;
				case Constants.RESPONSE_LIMITER_TYPE_FIXED_SIZE:
					if (responseBlock.length-offset!=commandType.getResponselength()) {
			            throw new IOException("expected length of response: "+commandType.getResponselength() + ", but got: "+ (responseBlock.length-offset));
					}					        
					break;
				case Constants.RESPONSE_LIMITER_TYPE_MULTIPLE_CRLF:
					//TODO add support
					break;
			}			
			
			switch (commandType.getCrcchecktype()) {
				case Constants.CRC_CHECK_TYPE_VAR1:
					if (!CRC16.check(responseBlock, offset, responseBlock.length-offset)) {
						throw new IOException("CRC error");
					}
					break;
				case Constants.CRC_CHECK_TYPE_NONE:
					break;
			}
			
			try {
				int responseLength = responseBlock.length-offset-(commandType.getCrcchecktype()==Constants.CRC_CHECK_TYPE_VAR1?2:0);
				byte[] inputBuf = new byte[responseLength];
				System.arraycopy(responseBlock, offset, inputBuf, 0,responseLength);
	
				String string = new String(inputBuf);
				logger.debug("parsing: "+escape(string));
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
			} catch (ArrayIndexOutOfBoundsException aie) {
				logger.warn(aie.getMessage());
			}
		} catch (IOException e) {
			logger.warn(e.getMessage());
			resetAfterError();
		}
	}
	
	protected void writeString(String string) throws IOException {
		logger.debug("TX: "+escape(string));
		outputStream.write(string.getBytes());
		outputStream.flush();
	}
	
	protected void expectString(byte[] buffer,String string) throws IOException {
		String s = new String(buffer);
		if (buffer.length<string.length()) {
			throw new IOException("unexpected response too short: " + escape(s)+ ", expected: "+escape(string));
		}
		if (!string.equals(s.substring(0, string.length()))) {
			throw new IOException("unexpected response mismatch: " + escape(s.substring(0, string.length()))+ ", expected: "+escape(string));
		}
	}

	protected boolean wakeup() {
		// Send wakeup command.
		boolean awake = false;
		int i = 0;
		while (awake == false && i++ < 3) {
			try {
				logger.debug("sending wakeup sequence");
				writeString("\n");	
				sleep(100);
				logger.debug("waiting for wakeup response");
				byte[] buf = readResponse();
				expectString(buf,"\n\r");				
				awake = true;
				return awake;
			} catch (IOException e) {
				logger.warn("wakeup failed, retry");
			}
			sleep(1200);
		}
		return awake;
	}

	protected void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
			logger.debug("sleep interrupted");
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
