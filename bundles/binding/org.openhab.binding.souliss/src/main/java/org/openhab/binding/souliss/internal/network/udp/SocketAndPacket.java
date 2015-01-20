/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Data Structure for class SendDispatcherThread
 * @author Antonino-Fazio
 */
public class SocketAndPacket {
	public SocketAndPacket(DatagramSocket socketPar, DatagramPacket packetPar) {
		socket=socketPar;
		packet=packetPar;
	}
	public DatagramSocket socket;
	public DatagramPacket packet;
}
