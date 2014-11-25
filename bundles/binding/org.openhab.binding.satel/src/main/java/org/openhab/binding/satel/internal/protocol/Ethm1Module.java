/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO document me!
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class Ethm1Module extends SatelModule {
	private static final Logger logger = LoggerFactory.getLogger(Ethm1Module.class);

	private String host;
	private int port;
	private String encryptionKey;
	private Socket socket;

	public Ethm1Module(String host, int port, int timeout, String encryptionKey) {
		super(timeout);
		
		this.host = host;
		this.port = port;
		this.encryptionKey = encryptionKey;
		this.socket = null;
	}

	@Override
	protected CommunicationChannel connect() {
		logger.info("Connecting to ETHM-1 module at {}:{}", this.host, this.port);
		
		try {
			if (StringUtils.isNotBlank(this.encryptionKey)) {
				// TODO implement encryption
				logger.error("ETHM-1 encryption not yet implemented.");
				return null;
			}
			this.socket = new Socket();
			this.socket.connect(new InetSocketAddress(this.host, this.port), this.getTimeout());
			
			return new CommunicationChannel() {
				@Override
				public InputStream getInputStream() throws IOException {
					return Ethm1Module.this.socket.getInputStream();
				}
				@Override
				public OutputStream getOutputStream() throws IOException {
					return Ethm1Module.this.socket.getOutputStream();
				}
				@Override
				public void disconnect() {
					logger.info("Closing connection to ETHM-1 module");
					try {
						if (Ethm1Module.this.socket != null) {
							Ethm1Module.this.socket.close();
							Ethm1Module.this.socket = null;
						}
					} catch (IOException e) {
						logger.error("IO error occurred during closing socket", e);
					}
				}
			};
		} catch (IOException e) {
			logger.error("IO error occurred during connecting socket", e);
		}
		
		return null;
	}
}
