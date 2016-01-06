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
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SocketAndPacket {
	public SocketAndPacket(DatagramSocket socketPar, DatagramPacket packetPar) {
		socket = socketPar;
		packet = packetPar;
	}

	public DatagramSocket socket;
	public DatagramPacket packet;
	public boolean sent;
	public long time=0;
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		//imposta il tempo solo se non è già stato impostato una volta
		if(this.time==0)
		this.time = time;
	}
	public boolean isSent() {
		return sent;
	}
	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
	
}
