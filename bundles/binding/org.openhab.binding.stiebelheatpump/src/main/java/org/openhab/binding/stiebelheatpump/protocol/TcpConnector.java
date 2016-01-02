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
import java.net.Socket;

public class TcpConnector extends SerialConnector {

	private String host;
	private int port;
	private Socket socket;

	public TcpConnector(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	protected void connectPort() throws Exception {
		socket = new Socket(host, port);
		in = socket.getInputStream();
		out = new DataOutputStream(socket.getOutputStream());
	}

	@Override
	protected void disconnectPort() {
		try {
			socket.close();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
