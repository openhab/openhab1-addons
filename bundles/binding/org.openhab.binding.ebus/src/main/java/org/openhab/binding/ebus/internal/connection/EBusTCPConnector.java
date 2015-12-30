/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the TCP/IP implementation of the eBus connector. It only handles
 * TCP/IP specific connection/disconnection. All logic is handled by
 * abstract class AbstractEBusConnector.
 * 
 * @author Christian Sowada
 * @since 1.7.0
 */
public class EBusTCPConnector extends AbstractEBusWriteConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(EBusTCPConnector.class);
	
	/** The tcp socket */
	private Socket socket;

	/** The tcp hostname */
	private String hostname;

	/** The tcp port */
	private int port;

	/** output stream for eBus communication*/
	private OutputStream outputStream;

	/** input stream for eBus communication*/
	private InputStream inputStream;

	/**
	 * Constructor
	 * @param hostname
	 * @param port
	 */
	public EBusTCPConnector(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.connection.AbstractEBusConnector#connect()
	 */
	@Override
	protected boolean connect() throws IOException  {
		
		
		
		try {
			socket = new Socket(hostname, port);
			socket.setSoTimeout(20000);
			socket.setKeepAlive(true);
			
			socket.setTcpNoDelay(true);
			socket.setTrafficClass((byte)0x10);
			
			// Useful? We try it
//			socket.setReceiveBufferSize(1);
			socket.setSendBufferSize(1);
			
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			
			return super.connect();
			
		} catch (ConnectException e) {
			logger.error(e.toString());

			
		} catch (Exception e) {
			logger.error(e.toString(), e);
			
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.connection.AbstractEBusConnector#disconnect()
	 */
	@Override
	protected boolean disconnect() throws IOException  {
		
		if(outputStream != null)
			outputStream.flush();
		
		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(outputStream);
		
		if(socket != null) {
			socket.close();
			socket = null;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.internal.connection.AbstractEBusConnector#readByte()
	 */
	@Override
	protected int readByte(boolean lowLatency) throws IOException {
		return inputStream.read();
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.internal.connection.AbstractEBusWriteConnector#writeByte(int)
	 */
	@Override
	protected void writeByte(int b) throws IOException {
		outputStream.write(b);
		outputStream.flush();
	}

	@Override
	protected boolean isReceiveBufferEmpty() throws IOException {
		return inputStream.available() == 0;
	}

	@Override
	protected boolean isConnected() {
		return inputStream != null;
	}

	@Override
	protected void resetInputBuffer() throws IOException {
		int available = inputStream.available();
		if(available > 0) logger.debug("InputBuffer is not empty before sending: {} bytes waiting !", available);
		inputStream.skip(available);
	}

	@Override
	protected InputStream getInputStream() {
		return inputStream;
	}
}
