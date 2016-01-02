/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * connector for serial port communication.
 * 
 * @author Evert van Es (originaly copied from)
 * @author Peter Kreutzer
 */
public abstract class SerialConnector implements ProtocolConnector {

	protected static final Logger logger = LoggerFactory
			.getLogger(SerialConnector.class);

	InputStream in = null;
	DataOutputStream out = null;
	ByteStreamPipe byteStreamPipe = null;

	private CircularByteBuffer buffer;

	@Override
	public void connect() {
		try {
			connectPort();
			out.flush();
			buffer = new CircularByteBuffer(Byte.MAX_VALUE * Byte.MAX_VALUE + 2
					* Byte.MAX_VALUE);
			byteStreamPipe = new ByteStreamPipe(in, buffer);
		} catch (Exception e) {
			throw new RuntimeException("Could not init serial connection" , e);
		}
	}
	
	@Override
	public void disconnect() {
		logger.debug("Interrupt reading thread");
		byteStreamPipe.close();

		logger.debug("Closing serial connection");
		try {
			out.close();
			disconnectPort();
			buffer.stop();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		} catch (IOException e) {
			logger.warn("Could not fully shut down heat pump driver", e);
		}

		logger.debug("Disconnected");
	}

	protected abstract void connectPort() throws Exception;
	
	protected abstract void disconnectPort();
	
	@Override
	public byte get() throws StiebelHeatPumpException {
		return buffer.get();
	}

	@Override
	public short getShort() throws StiebelHeatPumpException {
		return buffer.getShort();
	}

	@Override
	public void get(byte[] data) throws StiebelHeatPumpException {
		buffer.get(data);
	}

	@Override
	public void mark() {
		buffer.mark();
	}

	@Override
	public void reset() {
		buffer.reset();
	}

	@Override
	public void write(byte[] data) {
		try {
			logger.debug("Send request message : {}",
					DataParser.bytesToHex(data));
			out.write(data);
			out.flush();

		} catch (IOException e) {
			throw new RuntimeException("Could not write", e);
		}
	}

	@Override
	public void write(byte data) {
		try {
			logger.debug(String.format("Send %02X", data));
			out.write(data);
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException("Could not write", e);
		}
	}

}
