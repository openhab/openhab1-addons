/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.octoller.devicecom;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.openhab.binding.octoller.internal.OctollerBindingConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

/**
 * openHAB binding for octoller (www.octoller.com) For use with octoller-Gateway
 * 
 * @author JPlenert
 * @since 1.8.0
 */
public class Connection {

	Socket socket;
	InetAddress address;
	OutputStream outStream;
	InputStream inStream;

	public Connection(String nameOrAddress) throws IOException {
		address = InetAddress.getByName(nameOrAddress);
		socket = new Socket(address, 2300);
		outStream = socket.getOutputStream();
		inStream = socket.getInputStream();
	}

	public String doCommand(String command) throws IOException {
		ByteBuffer b = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
		b.putInt(command.length());
		outStream.write(b.array(), 0, 4); // length
		outStream.write(command.getBytes()); // data

		byte[] buffer = new byte[4];
		inStream.read(buffer, 0, 4);
		b = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
		int len = b.getInt();
		buffer = new byte[len];
		inStream.read(buffer, 0, len);
		return new String(buffer, "UTF-8");
	}

	public void close() throws IOException {
		socket.close();
	}

	public void processResultToPublisher(EventPublisher eventPublisher,
			String itemName, String result) {
		if (result.equals("Up"))
			eventPublisher.postUpdate(itemName, (State) new PercentType(0));
		else if (result.equals("Down"))
			eventPublisher.postUpdate(itemName, (State) new PercentType(100));
		else if (result.equals("Unknown"))
			eventPublisher.postUpdate(itemName, (State) new PercentType(50));
		else if (result.equals("On"))
			eventPublisher.postUpdate(itemName, (State) OnOffType.ON);
		else if (result.equals("Off"))
			eventPublisher.postUpdate(itemName, (State) OnOffType.OFF);
	}

	public String buildCommandString(OctollerBindingConfig config,
			String commandType, String command) {
		StringBuilder sb = new StringBuilder();
		sb.append("DeviceHost=");
		sb.append(config.DeviceHost);
		sb.append("|");
		sb.append("CommandType=");
		sb.append(commandType);
		sb.append("|");
		sb.append("Command=");
		sb.append(command);
		sb.append("|");

		if (config.BlockID != 0) {
			sb.append("BlockID=");
			sb.append(config.BlockID);
			sb.append("|");
		}

		if (config.BlockName != null) {
			sb.append("BlockName=");
			sb.append(config.BlockName);
			sb.append("|");
		}

		return sb.toString();
	}

}