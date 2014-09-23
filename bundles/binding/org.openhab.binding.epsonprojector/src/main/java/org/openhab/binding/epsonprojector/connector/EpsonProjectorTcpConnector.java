/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.epsonprojector.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.epsonprojector.internal.EpsonProjectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for TCP communication.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class EpsonProjectorTcpConnector implements EpsonProjectorConnector {

	private static final Logger logger = 
			LoggerFactory.getLogger(EpsonProjectorTcpConnector.class);

	String ip = null;
	int port = 10000;
	Socket socket = null;
	InputStream in = null;
	OutputStream out = null;

	public EpsonProjectorTcpConnector(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	/**
	 * {@inheritDoc}
	 */
	public void connect() throws EpsonProjectorException {
		logger.debug("Open connection to address'{}:{}'", ip, port);
		
		 try {
			socket = new Socket(ip, port);
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (Exception e) {
			throw new EpsonProjectorException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void disconnect() throws EpsonProjectorException {
		if (out != null) {
			logger.debug("Close tcp out stream");
			IOUtils.closeQuietly(out);
		}
		if (in != null) {
			logger.debug("Close tcp in stream");
			IOUtils.closeQuietly(in);
		}
		if (socket != null) {
			logger.debug("Closing socket");
			try {
				socket.close();
			} catch (IOException e) {
				logger.warn("Error occured when closing tcp socket", e);
			}
		}

		socket = null;
		out = null;
		in = null;

		logger.debug("Closed");
	}

	/**
	 * {@inheritDoc}
	 */
	public String sendMessage(String data, int timeout) throws EpsonProjectorException {
		
		try {
			// flush input stream
			if (in.markSupported()) {
				in.reset();
			} else {

				while (in.available() > 0) {

					int availableBytes = in.available();

					if (availableBytes > 0) {

						byte[] tmpData = new byte[availableBytes];
						in.read(tmpData, 0, availableBytes);
					}
				}
			}

			out.write(data.getBytes());
			out.write("\r\n".getBytes());
			out.flush();

			String resp = "";

			long startTime = System.currentTimeMillis();
			long elapsedTime = 0;

			while (elapsedTime < timeout) {
				int availableBytes = in.available();
				if (availableBytes > 0) {
					byte[] tmpData = new byte[availableBytes];
					int readBytes = in.read(tmpData, 0, availableBytes);
					resp = resp.concat(new String(tmpData, 0, readBytes));
					
					if (resp.contains(":")) {
						return resp;
					}
				} else {
					Thread.sleep(100);
				}

				elapsedTime = Math.abs((new Date()).getTime() - startTime);
			}

		} catch (Exception e) {
			throw new EpsonProjectorException(e);
		}

		return null;
	}
}
