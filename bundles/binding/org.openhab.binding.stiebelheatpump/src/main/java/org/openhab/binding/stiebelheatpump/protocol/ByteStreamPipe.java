/**
 * Copyright 2014 
 * This file is part of stiebel heat pump reader.
 * It is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * It is  is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with the project. 
 * If not, see http://www.gnu.org/licenses/.
 */
package org.openhab.binding.stiebelheatpump.protocol;

import java.io.IOException;
import java.io.InputStream;

import org.openhab.binding.stiebelheatpump.protocol.CircularByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ByteStreamPipe class that runs the read thread to read bytes from the heat
 * pump connector
 * 
 * @author Peter Kreutzer
 */
public class ByteStreamPipe implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(ByteStreamPipe.class);

	private boolean running = true;
	private InputStream in = null;
	private CircularByteBuffer buffer;
	private Thread taskThread;

	public ByteStreamPipe(InputStream in, CircularByteBuffer buffer) {
		this.in = in;
		this.buffer = buffer;
	}

	public void startTask() {
		taskThread = new Thread(this);
		taskThread.setName("StiebelConnectorThread");
		taskThread.start();
	}

	public void stopTask() {
		taskThread.interrupt();
		try {
			in.close();
		} catch (IOException e) {
			logger.error("Error while closing COM port.", e);
		}
		try {
			taskThread.join();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		while (!Thread.interrupted())
			try {
				if (in.available() > 0) {
					byte readByte = (byte) in.read();
					logger.debug(String.format("Received %02X", readByte));
					buffer.put(readByte);
				}
			} catch (Exception e) {
				logger.error("Error while reading from COM port. Stopping.", e);
				throw new RuntimeException(e);
			}
	}

	public void stop() {
		running = false;
		try {
			in.close();
		} catch (IOException e) {
			logger.error("Error while closing COM port.", e);
		}
	}
}
