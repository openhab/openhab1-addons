/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.urtsi.internal

import gnu.io.CommPortIdentifier
import gnu.io.PortInUseException
import gnu.io.SerialPort
import gnu.io.SerialPortEvent
import gnu.io.UnsupportedCommOperationException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.List
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.openhab.binding.urtsi.internal.ArrayHelper.*
import static org.openhab.binding.urtsi.internal.UrtsiDevice.*

/**
 * Implementation of the device. This class is responsible for communicating to the hardware which is connected via a serial port.
 * It completely encapsulates the serial communication and just provides a writeString method which returns true, if the message has been transmitted successfully.
 * @author Oliver Libutzki
 * @since 1.3.0
 *
 */
class UrtsiDevice {
	
	static val Logger logger = LoggerFactory::getLogger(typeof(UrtsiDevice))

	static val int baud = 9600
	static val int databits = SerialPort::DATABITS_8
	static val int stopbit = SerialPort::STOPBITS_1
	static val int parity = SerialPort::PARITY_NONE

	// Serial communication fields
	String port

	CommPortIdentifier portId
	SerialPort serialPort
	OutputStream outputStream
	InputStream inputStream

	DedicatedThreadExecutor threadExecutor = new DedicatedThreadExecutor

	public new(String port) {
		this.port = port
	}
	
	/**
	 * Initialize this device and open the serial port
	 * 
	 * @throws InitializationException
	 *             if port can not be opened
	 */
	def void initialize() throws InitializationException {
		// parse ports and if the default port is found, initialized the reader
		var portList = CommPortIdentifier::portIdentifiers
		while (portList.hasMoreElements) {
			val id = portList.nextElement() as CommPortIdentifier
			if (id.portType == CommPortIdentifier::PORT_SERIAL) {
				if (id.name.equals(port)) {
					logger.debug("Serial port '{}' has been found.", port)
					portId = id
				}
			}
		}
		if (portId != null) {
			// initialize serial port
			try {
				serialPort = portId.open("openHAB", 2000) as SerialPort
			} catch (PortInUseException e) {
				throw new InitializationException(e)
			}

			try {
				// set port parameters
				serialPort.setSerialPortParams(baud, databits, stopbit, parity)
			} catch (UnsupportedCommOperationException e) {
				throw new InitializationException(e)
			}

			try {
				inputStream = serialPort.inputStream
			} catch (IOException e) {
				throw new InitializationException(e)
			}
			
			try {
				// get the output stream
				outputStream = serialPort.outputStream
			} catch (IOException e) {
				throw new InitializationException(e)
			}
		} else {
			val sb = new StringBuilder()
			portList = CommPortIdentifier::portIdentifiers
			while (portList.hasMoreElements()) {
				val id = portList.nextElement() as CommPortIdentifier
				if (id.portType == CommPortIdentifier::PORT_SERIAL) {
					sb.append(id.name + "\n");
				}
			}
			throw new InitializationException("Serial port '" + port
					+ "' could not be found. Available ports are:\n"
					+ sb.toString());
		}
	}
	
	
	/**
	 * Sends a string to the serial port of this device.
	 * The execution of the msg is executed in a dedicated Thread, so it's guaranteed that the device doesn't get multiple messages concurrently. 
	 * 
	 * @param msg
	 *            the string to send
	 * @return Returns true, if the message has been transmitted successfully, otherwise false.
	 */
	def boolean writeString(String msg) {
		logger.debug("Writing '{}' to serial port {}", newArrayList( msg, port ))
		val future = 
			threadExecutor.execute( [
				try {
					val List<Boolean> listenerResult = newArrayList
					serialPort.addEventListener[event | 
						switch (event.eventType) {
							case SerialPortEvent::DATA_AVAILABLE: {
								// we get here if data has been received
								val sb = new StringBuilder()
								val readBuffer = getByteArray(20)
								try {
									do {
										// read data from serial device
										while (inputStream.available > 0) {
											val bytes = inputStream.read(readBuffer)
											sb.append(new String(readBuffer, 0, bytes))
										}
										try {
											// add wait states around reading the stream, so that interrupted transmissions are merged
											Thread::sleep(100)
										} catch (InterruptedException e) {
											// ignore interruption
										}
									} while (inputStream.available > 0)
									val result = sb.toString
									if (result == msg) {
										listenerResult.add(true)
									}
										
								} catch (IOException e) {
									logger.debug("Error receiving data on serial port {}: {}", newArrayList( port, e.message ))
								}
							}
						}
							
					]
					serialPort.notifyOnDataAvailable(true)
					outputStream.write(msg.bytes)
					outputStream.flush()
					val timeout = System::currentTimeMillis + 1000
					while (listenerResult.empty && System::currentTimeMillis < timeout) {
						// Waiting for response
						Thread::sleep(100)
					}
					return !listenerResult.empty
				} catch (IOException e) {
					logger.error("Error writing '{}' to serial port {}: {}",
							newArrayList ( msg, port, e.getMessage() ))
				} finally {
					serialPort.removeEventListener()
				}
			])
		future.get
	}
	
	/**
	 * Close this serial device
	 */
	def void close() {
		serialPort.removeEventListener
		IOUtils::closeQuietly(outputStream)
		IOUtils::closeQuietly(inputStream)
		serialPort.close
	}
}