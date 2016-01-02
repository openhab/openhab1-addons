/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.protocol;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ByteStreamPipe class that runs the read thread to read bytes from the heat
 * pump connector
 * 
 * @author Peter Kreutzer
 */
public class ByteStreamPipe implements Runnable, Closeable {

	private static final Logger logger = LoggerFactory
			.getLogger(ByteStreamPipe.class);

	private boolean running = true;
	private InputStream in = null;
	private CircularByteBuffer buffer;
	private Thread taskThread;

	public ByteStreamPipe(InputStream in, CircularByteBuffer buffer) {
		this.in = in;
		this.buffer = buffer;
		running = true;
		taskThread = new Thread(this);
		taskThread.setName("StiebelConnectorThread");
		taskThread.start();
	}

	@Override
	public void run() {
		while (running) {
			try {
				if (in.available() > 0) {
					byte readByte = (byte) in.read();
					logger.debug(String.format("Received %02X", readByte));
					buffer.put(readByte);
				} else {
					Thread.sleep(200);
				}
			} catch (Exception e) {
				logger.error("Error while reading from COM port. Stopping.", e);
				throw new RuntimeException(e);
			}
		}
		try {
			in.close();
		} catch (IOException e) {
			logger.warn("Error closing stream", e);
		}
	}

	@Override
	public void close() {
		running = false;
		try {
			taskThread.join();
		} catch (InterruptedException e) {
			// Ignore
		}
	}

}
