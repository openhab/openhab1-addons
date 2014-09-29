/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.comfoair.handling;

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
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.comfoair.internal.InitializationException;
import org.openhab.core.library.types.DecimalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * With this connector the hole serial communication is handled.
 * 
 * @author Holger Hees
 * @since 1.3.0
 */
public class ComfoAirConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(ComfoAirConnector.class);

	private static byte[] START = { (byte) 0x07, (byte) 0xf0 };
	private static byte[] END = { (byte) 0x07, (byte) 0x0f };
	private static byte[] ACK = { (byte) 0x07, (byte) 0xf3 };

	private boolean isSuspended = true;

	private String port;
	private SerialPort serialPort;
	private InputStream inputStream;
	private OutputStream outputStream;

	/**
	 * Open and initialize a serial port.
	 * 
	 * @param portName
	 *            e.g. /dev/ttyS0
	 * @param listener
	 *            the listener which is informed after a successful response
	 *            read
	 * @throws InitializationException
	 */
	public void open(String portName) throws InitializationException {
		logger.debug("Open ComfoAir connection");

		port = portName;
		CommPortIdentifier portIdentifier;

		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(port);

			try {
				serialPort = (SerialPort) portIdentifier.open("openhab", 3000);
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				inputStream = new DataInputStream(new BufferedInputStream(
						serialPort.getInputStream()));
				outputStream = serialPort.getOutputStream();

				ComfoAirCommand command = ComfoAirCommandType.getChangeCommand(
						ComfoAirCommandType.ACTIVATE.key, new DecimalType(1));
				sendCommand(command);
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
				CommPortIdentifier id = (CommPortIdentifier) portList
						.nextElement();
				if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					sb.append(id.getName() + "\n");
				}
			}

			throw new InitializationException("Serial port '" + port
					+ "' could not be found. Available ports are:\n"
					+ sb.toString());
		}
	}

	/**
	 * Close the serial port.
	 */
	public void close() {
		logger.debug("Close ComfoAir connection");

		ComfoAirCommand command = ComfoAirCommandType.getChangeCommand(
				ComfoAirCommandType.ACTIVATE.key, new DecimalType(0));
		sendCommand(command);

		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(outputStream);
		serialPort.close();
	}

	/**
	 * Prepare a command for sending using the serial port.
	 * 
	 * @param command
	 * @return reply byte values
	 */
	public synchronized int[] sendCommand(ComfoAirCommand command) {

		int requestCmd = command.getRequestCmd();
		int retry = 0;

		// Switch support for app or ccease control
		if (requestCmd == 0x9b) {
			isSuspended = !isSuspended;
		} else if (requestCmd == 0x9c) {
			return new int[] { isSuspended ? 0x00 : 0x03 };
		} else if (isSuspended) {
			logger.debug("Ignore cmd. Service is currently suspended");
			return null;
		}

		do {
			int[] requestData = command.getRequestData();

			// Fake read request for ccease properties
			if (requestData == null && requestCmd == 0x37) {
				requestData = new int[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
						0x00 };
			}

			byte[] requestBlock = calculateRequest(requestCmd, requestData);
			if (!send(requestBlock)) {
				return null;
			}

			byte[] responseBlock = new byte[0];

			try {

				// 31 is max. response length
				byte[] readBuffer = new byte[31];

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
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// ignore interruption
					}

				} while (inputStream.available() > 0);

				// check for ACK
				if (responseBlock.length >= 2
						&& responseBlock[0] == (byte) 0x07
						&& responseBlock[1] == (byte) 0xf3) {
					if (command.getReplyCmd() == null) {
						// confirm additional data with an ACK
						if (responseBlock.length > 2) {
							send(ACK);
						}
						return null;
					}

					// check for start and end sequence and if the response cmd
					// matches
					// 11 is the minimum response length with one data byte
					if (responseBlock.length >= 11
							&& responseBlock[2] == (byte) 0x07
							&& responseBlock[3] == (byte) 0xf0
							&& responseBlock[responseBlock.length - 2] == (byte) 0x07
							&& responseBlock[responseBlock.length - 1] == (byte) 0x0f
							&& (responseBlock[5] & 0xff) == command
									.getReplyCmd()) {

						logger.debug("receive RAW DATA: "
								+ dumpData(responseBlock));

						byte[] cleanedBlock = cleanupBlock(responseBlock);

						int dataSize = cleanedBlock[2];

						// the cleanedBlock size should equal dataSize + 2 cmd
						// bytes and + 1 checksum byte
						if (dataSize + 3 == cleanedBlock.length - 1) {

							byte checksum = cleanedBlock[dataSize + 3];
							int[] replyData = new int[dataSize];
							for (int i = 0; i < dataSize; i++) {
								replyData[i] = cleanedBlock[i + 3] & 0xff;
							}

							byte[] _block = new byte[3 + replyData.length];
							System.arraycopy(cleanedBlock, 0, _block, 0,
									_block.length);

							// validate calculated checksum against submitted
							// checksum
							if (calculateChecksum(_block) == checksum) {

								logger.debug(String.format("receive CMD: %02x",
										command.getReplyCmd())
										+ " DATA: "
										+ dumpData(replyData));

								send(ACK);

								return replyData;
							}

							logger.warn("Unable to handle data. Checksum verification failed");
						} else {
							logger.warn("Unable to handle data. Data size not valid");
						}

						logger.warn(String.format("skip CMD: %02x",
								command.getReplyCmd())
								+ " DATA: " + dumpData(cleanedBlock));
					}
				}

			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}

			try {

				Thread.sleep(1000);
				logger.warn("Retry cmd. Last call was not successful."
						+ " Request: "
						+ dumpData(requestBlock)
						+ " Response: "
						+ (responseBlock.length > 0 ? dumpData(responseBlock)
								: "null"));

			} catch (InterruptedException e) {
				// ignore interruption
			}

		} while (retry++ < 5);

		if (retry == 5) {
			logger.error("Unable to send command. " + retry
					+ " retries failed.");
		}

		return null;
	}

	/**
	 * Generate the byte sequence for sending to ComfoAir (incl. START & END
	 * sequence and checksum).
	 * 
	 * @param command
	 * @param data
	 * @return response byte value block with cmd, data and checksum
	 */
	private byte[] calculateRequest(int command, int[] data) {

		// generate the command block (cmd and request data)
		int length = data == null ? 0 : data.length;

		byte[] block = new byte[4 + length];

		block[0] = 0x00;
		block[1] = (byte) command;
		block[2] = (byte) length;
		if (data != null) {
			for (int i = 0; i < data.length; i++) {
				block[i + 3] = (byte) data[i];
			}
		}

		// calculate checksum for command block
		byte checksum = calculateChecksum(block);
		block[block.length - 1] = checksum;

		// escape the command block with checksum included
		block = escapeBlock(block);

		byte[] request = new byte[4 + block.length];

		request[0] = START[0];
		request[1] = START[1];
		System.arraycopy(block, 0, request, 2, block.length);
		request[request.length - 2] = END[0];
		request[request.length - 1] = END[1];

		return request;
	}

	/**
	 * Calculates a checksum for a command block (cmd, data and checksum).
	 * 
	 * @param block
	 * @return checksum byte value
	 */
	private byte calculateChecksum(byte[] block) {

		int datasum = 0;
		for (int i = 0; i < block.length; i++) {
			datasum += (int) block[i];
		}
		datasum += 173;

		String hexString = Integer.toHexString(datasum);
		if (hexString.length() > 2) {
			hexString = hexString.substring(hexString.length() - 2);
		}

		return (byte) Integer.parseInt(hexString, 16);
	}

	/**
	 * Cleanup a commandblock from quoted 0x07 characters.
	 * 
	 * @param processBuffer
	 * @return the 0x07 cleaned byte values
	 */
	private byte[] cleanupBlock(byte[] processBuffer) {

		int pos = 0;
		byte[] cleanedBuffer = new byte[50];

		for (int i = 4; i < processBuffer.length - 2; i++) {
			if ((byte) 0x07 == processBuffer[i]) {
				i++;
			}

			cleanedBuffer[pos] = processBuffer[i];
			pos++;
		}

		byte[] _block = new byte[pos];
		System.arraycopy(cleanedBuffer, 0, _block, 0, _block.length);

		return _block;
	}

	/**
	 * Escape special 0x07 character.
	 * 
	 * @param cleanedBuffer
	 * @return escaped byte value array
	 */
	private byte[] escapeBlock(byte[] cleanedBuffer) {

		int pos = 0;

		byte[] processBuffer = new byte[50];

		for (int i = 0; i < cleanedBuffer.length; i++) {
			if ((byte) 0x07 == cleanedBuffer[i]) {
				processBuffer[pos] = (byte) 0x07;
				pos++;
			}

			processBuffer[pos] = cleanedBuffer[i];
			pos++;
		}

		byte[] _block = new byte[pos];
		System.arraycopy(processBuffer, 0, _block, 0, _block.length);

		return _block;
	}

	/**
	 * Send the byte values.
	 * 
	 * @param request
	 * @return successful flag
	 */
	private boolean send(byte[] request) {
		logger.debug("send DATA: " + dumpData(request));

		try {
			outputStream.write(request);
			outputStream.flush();

			return true;

		} catch (IOException e) {
			logger.error("Error writing to serial port {}: {}", port,
					e.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * Is used to debug byte values.
	 * 
	 * @param data
	 * @return
	 */
	public static String dumpData(int[] data) {

		StringBuffer sb = new StringBuffer();
		for (int ch : data) {
			sb.append(String.format(" %02x", ch));
		}
		return sb.toString();
	}

	private String dumpData(byte[] data) {

		StringBuffer sb = new StringBuffer();
		for (byte ch : data) {
			sb.append(String.format(" %02x", ch));
		}
		return sb.toString();
	}
}
