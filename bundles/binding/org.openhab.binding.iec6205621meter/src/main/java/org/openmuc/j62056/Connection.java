/*
 * Copyright 2013-14 Fraunhofer ISE
 *
 * This file is part of j62056.
 * For more information visit http://www.openmuc.org
 *
 * j62056 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * j62056 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with j62056.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.j62056;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Connection {

	private final String serialPortName;
	private SerialPort serialPort;

	private final boolean handleEcho;
	private final int baudRateChangeDelay;
	private int timeout = 5000;

	private DataOutputStream os;
	private DataInputStream is;
	
	private static char[] hexArray = "0123456789ABCDEF".toCharArray();

	private static final byte[] REQUEST_MESSAGE = new byte[] { (byte) 0x2F,
			(byte) 0x3F, (byte) 0x21, (byte) 0x0D, (byte) 0x0A };

	private static final byte[] ACKNOWLEDGE = new byte[] { (byte) 0x06,
			(byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x0D, (byte) 0x0A };

	private static final int INPUT_BUFFER_LENGTH = 1024;
	private final byte[] buffer = new byte[INPUT_BUFFER_LENGTH];

	private static final Charset charset = Charset.forName("US-ASCII");

	private static final int SLEEP_INTERVAL = 100;

	/**
	 * Creates a Connection object. You must call <code>open()</code> before
	 * calling <code>read()</code> in order to read data. The timeout is set by
	 * default to 5s.
	 * 
	 * @param serialPort
	 *            examples for serial port identifiers are on Linux "/dev/ttyS0"
	 *            or "/dev/ttyUSB0" and on Windows "COM1"
	 * @param handleEcho
	 *            tells the connection to throw away echos of outgoing messages.
	 *            Echos are caused by some optical transceivers.
	 * @param baudRateChangeDelay
	 *            tells the connection the time in ms to wait before changing
	 *            the baud rate during message exchange. This parameter can
	 *            usually be set to zero for regular serial ports. If a USB to
	 *            serial converter is used, you might have to use a delay of
	 *            around 250ms because otherwise the baud rate is changed before
	 *            the previous message (i.e. the acknowledgment) has been
	 *            completely sent.
	 */
	public Connection(String serialPort, boolean handleEcho,
			int baudRateChangeDelay) {
		if (serialPort == null) {
			throw new IllegalArgumentException("serialPort may not be NULL");
		}

		serialPortName = serialPort;
		this.handleEcho = handleEcho;
		this.baudRateChangeDelay = baudRateChangeDelay;
	}

	/**
	 * Creates a Connection object. The option handleEcho is set to false and
	 * the baudRateChangeDelay is set to 0.
	 * 
	 * @param serialPort
	 *            examples for serial port identifiers on Linux are "/dev/ttyS0"
	 *            or "/dev/ttyUSB0" and on Windows "COM1"
	 */
	public Connection(String serialPort) {
		this(serialPort, false, 0);
	}

	/**
	 * Sets the maximum time in ms to wait for new data from the remote device.
	 * A timeout of zero is interpreted as an infinite timeout.
	 * 
	 * @param timeout
	 *            the maximum time in ms to wait for new data.
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * Returns the timeout in ms.
	 * 
	 * @return the timeout in ms.
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Opens the serial port associated with this connection.
	 * 
	 * @throws IOException
	 *             if any kind of error occurs opening the serial port.
	 */
	public void open() throws IOException {

		CommPortIdentifier portIdentifier;
		try {
			portIdentifier = CommPortIdentifier
					.getPortIdentifier(serialPortName);
		} catch (NoSuchPortException e) {
			throw new IOException("Serial port with given name does not exist",
					e);
		}

		if (portIdentifier.isCurrentlyOwned()) {
			throw new IOException("Serial port is currently in use.");
		}

		// fixed issue as rxtx library originally used in j62056 does use
		// different version of rxtx
		// com port in their version is using gnu.io.CommPort
		RXTXPort commPort;
		try {
			commPort = portIdentifier.open(this.getClass().getName(), 2000);
		} catch (PortInUseException e) {
			throw new IOException("Serial port is currently in use.", e);
		}

		if (!(commPort instanceof SerialPort)) {
			commPort.close();
			throw new IOException("The specified CommPort is not a serial port");
		}

		serialPort = (SerialPort) commPort;

		try {
			os = new DataOutputStream(serialPort.getOutputStream());
			is = new DataInputStream(serialPort.getInputStream());
		} catch (IOException e) {
			serialPort.close();
			serialPort = null;
			throw new IOException(
					"Error getting input or output or input stream from serial port",
					e);
		}

	}

	/**
	 * Closes the serial port.
	 */
	public void close() {
		if (serialPort == null) {
			return;
		}
		serialPort.close();
		serialPort = null;
	}

	/**
	 * Requests a data message from the remote device using IEC 62056-21 Mode C.
	 * The data message received is parsed and a list of data sets is returned.
	 * 
	 * @return A list of data sets contained in the data message response from
	 *         the remote device. The first data set will contain the
	 *         "identification" of the meter as the id and empty strings for
	 *         value and unit.
	 * @throws IOException
	 *             if any kind of error other than timeout occurs while trying
	 *             to read the remote device. Note that the connection is not
	 *             closed when an IOException is thrown.
	 * @throws TimeoutException
	 *             if no response at all (not even a single byte) was received
	 *             from the meter within the timeout span.
	 */
	public List<DataSet> read() throws IOException, TimeoutException {

		if (serialPort == null) {
			throw new IllegalStateException("Connection is not open.");
		}

		try {
			serialPort.setSerialPortParams(300, SerialPort.DATABITS_7,
					SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
		} catch (UnsupportedCommOperationException e) {
			throw new IOException(
					"Unable to set the given serial comm parameters", e);
		}

		os.write(REQUEST_MESSAGE);
		os.flush();
		int baudRateSetting;
		int baudRate;

		boolean readSuccessful = false;
		int timeval = 0;
		int numBytesReadTotal = 0;

		while (timeout == 0 || timeval < timeout) {
			if (is.available() > 0) {

				int numBytesRead = is.read(buffer, numBytesReadTotal,
						INPUT_BUFFER_LENGTH - numBytesReadTotal);
				numBytesReadTotal += numBytesRead;

				if (numBytesRead > 0) {
					timeval = 0;
				}

				if (((handleEcho && numBytesReadTotal > 11) || (!handleEcho && numBytesReadTotal > 6))
						&& buffer[numBytesReadTotal - 2] == 0x0D
						&& buffer[numBytesReadTotal - 1] == 0x0A) {
					readSuccessful = true;
					break;
				}
			}

			try {
				Thread.sleep(SLEEP_INTERVAL);
			} catch (InterruptedException e) {
			}

			timeval += SLEEP_INTERVAL;
		}

		int offset = 0;
		if (handleEcho) {
			offset = 5;
		}

		if (numBytesReadTotal == offset) {
			throw new TimeoutException();
		}

		if (!readSuccessful) {
			throw new IOException(
					"Timeout while listening for Identification Message");
		}

		// System.out
		// .println("Got the following identification message: " +
		// getByteArrayString(buffer, numBytesReadTotal));

		int startPosition = 0;
		while (startPosition < numBytesReadTotal) {
			if (buffer[startPosition] == (byte) 0x2F) {
				
				if (buffer[startPosition+1] == (byte) 0x00
						| buffer[startPosition+1] == (byte) 0x7F) {
					startPosition++;
				} else {
					break;
				}
				continue;
			} 
			startPosition++;
		}

		byte[] bytes = new byte[numBytesReadTotal - startPosition];
		System.arraycopy(buffer, startPosition, bytes, 0, numBytesReadTotal
				- startPosition);

		baudRateSetting = bytes[offset + 4];
		baudRate = getBaudRate(baudRateSetting);

		String identification = new String(buffer, offset + 5,
				numBytesReadTotal - offset - 7, charset);

		if (baudRate == -1) {
			throw new IOException(
					"Syntax error in identification message received: unknown baud rate received." + bytesToHex(bytes));
		}

		ACKNOWLEDGE[2] = (byte) baudRateSetting;

		os.write(ACKNOWLEDGE);
		os.flush();

		if (handleEcho) {
			readSuccessful = false;
			numBytesReadTotal = 0;
			while (timeout == 0 || timeval < timeout) {
				if (is.available() > 0) {
					int numBytesRead = is.read(buffer, numBytesReadTotal,
							ACKNOWLEDGE.length - numBytesReadTotal);
					numBytesReadTotal += numBytesRead;

					if (numBytesRead > 0) {
						timeval = 0;
					}

					if (numBytesReadTotal == ACKNOWLEDGE.length) {
						readSuccessful = true;
						break;
					}
				}

				try {
					Thread.sleep(SLEEP_INTERVAL);
				} catch (InterruptedException e) {
				}

				timeval += SLEEP_INTERVAL;
			}

			// if (readSuccessful) {
			// System.out.println("Received the following echo of the acknowledge successfully : "
			// + getByteArrayString(buffer, numBytesReadTotal));
			// }
			// else {
			// System.out.println("Receiving full echo failed.");
			// }

		}

		if (baudRateChangeDelay > 0) {
			try {
				Thread.sleep(baudRateChangeDelay);
			} catch (InterruptedException e1) {
			}
		}

		try {
			serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_7,
					SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
		} catch (UnsupportedCommOperationException e) {
			throw new IOException("Serial Port does not support " + baudRate
					+ "bd 7E1");
		}

		readSuccessful = false;
		numBytesReadTotal = 0;

		while (timeout == 0 || timeval < timeout) {
			if (is.available() > 0) {
				int numBytesRead = is.read(buffer, numBytesReadTotal,
						INPUT_BUFFER_LENGTH - numBytesReadTotal);
				numBytesReadTotal += numBytesRead;

				if (numBytesRead > 0) {
					timeval = 0;
				}

				if ((numBytesReadTotal > 4 && buffer[numBytesReadTotal - 3] == 0x21)
						|| (numBytesReadTotal > 7 && buffer[numBytesReadTotal - 5] == 0x21)) {
					readSuccessful = true;
					break;
				}
			}

			try {
				Thread.sleep(SLEEP_INTERVAL);
			} catch (InterruptedException e) {
			}

			timeval += SLEEP_INTERVAL;
		}

		if (!readSuccessful) {
			throw new IOException("Timeout while listening for Data Message");
		}

		// System.out.println("Got the following data message: " +
		// getByteArrayString(buffer, numBytesReadTotal));

		int index;
		int endIndex;
		// if 2nd last character is ETX
		if (buffer[numBytesReadTotal - 2] == 0x03) {
			if (numBytesReadTotal < 8) {
				throw new IOException(
						"Data message does not have minimum length of 8.");
			}
			index = 1;
			endIndex = numBytesReadTotal - 5;
			if (buffer[0] != 0x02) {

				startPosition = 0;
				while (startPosition < numBytesReadTotal) {
					if (buffer[startPosition] == (byte) 0x02) {
						break;
					} else {
						if (startPosition < numBytesReadTotal - 2) {
							startPosition++;
						} else {
							throw new IOException(
									"STX (0x02) character is expected but not received as first byte of data message." + bytesToHex(buffer));
						}
					}
				}
			}
		} else {
			if (numBytesReadTotal < 5) {
				throw new IOException(
						"Data message does not have minimum length of 5.");
			}
			index = 0;
			endIndex = numBytesReadTotal - 3;
		}

		if (buffer[endIndex + 1] != 0x0D) {
			throw new IOException(
					"CR (0x0D) character is expected but not received after data block of data message." + bytesToHex(buffer));
		}

		if (buffer[endIndex + 2] != 0x0A) {
			throw new IOException(
					"LF (0x0A) character is expected but not received after data block of data message."+ bytesToHex(buffer));
		}

		List<DataSet> dataSets = new ArrayList<DataSet>();
		dataSets.add(new DataSet(identification, "", ""));

		while (index != endIndex) {
			String id = null;

			for (int i = index; i < endIndex - 1; i++) {
				if (buffer[i] == 0x28) {
					// found '('
					id = new String(buffer, index, i - index, charset);
					index = i + 1;
					break;
				}
			}
			if (id == null) {
				throw new IOException(
						"'(' (0x28) character is expected but not received inside data block of data message."+ bytesToHex(buffer));
			}

			String value = "";
			String unit = "";
			for (int i = index; i < endIndex; i++) {
				if (buffer[i] == 0x2A) {
					// found '*'
					if (i > index) {
						value = new String(buffer, index, i - index, charset);
					}
					index = i + 1;

					for (int j = index; j < endIndex; j++) {
						if (buffer[j] == 0x29) {
							// found ')'
							unit = new String(buffer, index, j - index, charset);
							index = j + 1;
							break;
						}
					}

					break;
				} else if (buffer[i] == 0x29) {
					// found ')'
					if (i > index) {
						value = new String(buffer, index, i - index, charset);
					}
					index = i + 1;
					break;
				}
			}
			if (buffer[index - 1] != 0x29) {
				throw new IOException(
						"')' (0x29) character is expected but not received inside data block of data message." + bytesToHex(buffer));
			}

			dataSets.add(new DataSet(id, value, unit));

			if (buffer[index] == 0x0d && buffer[index + 1] == 0x0a) {
				index += 2;
			}

		}
		return dataSets;

	}

	/**
	 * Returns the baud rate chosen by the server for this communication
	 * 
	 * @param baudCharacter
	 *            Encoded baud rate (see 6.3.14 item 13c)
	 * @return The chosen baud rate or -1 on error
	 */
	private int getBaudRate(int baudCharacter) {
		int result = -1;
		switch (baudCharacter) {
		case 0x30:
			result = 300;
			break;
		case 0x31:
			result = 600;
			break;
		case 0x32:
			result = 1200;
			break;
		case 0x33:
			result = 2400;
			break;
		case 0x34:
			result = 4800;
			break;
		case 0x35:
			result = 9600;
			break;
		case 0x36:
			result = 19200;
			break;
		}
		return result;
	}
	
	/**
	 * Converts a byte array to good readable string.
	 * 
	 * @param bytes
	 *            to be converted
	 * @return string representing the bytes
	 */
	private String bytesToHex(byte[] bytes) {
		int dwords = bytes.length / 4 + 1;
		char[] hexChars = new char[bytes.length * 3 + dwords * 4];
		int position = 0;
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			if (j % 4 == 0) {
				String str = "(" + String.format("%02d", j) + ")";
				char[] charArray = str.toCharArray();
				for (char character : charArray) {
					hexChars[position] = character;
					position++;
				}
			}
			hexChars[position] = hexArray[v >>> 4];
			position++;
			hexChars[position] = hexArray[v & 0x0F];
			position++;
			hexChars[position] = ' ';
			position++;
		}
		return new String(hexChars);
	}
}
