/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.logic.data;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 	This class represents an IP connection. Therefore it has information about the address, 
 *  as well as a buffer which holds the input / output.
 * 
 * @author Patrik Pastuschek
 * @since  1.7.0
 * 
 */
public class LCNChannel {
	
	/**The remote address of the channel.*/
	public InetSocketAddress remote;
	/**The buffer that holds input / output.*/
	public ByteBuffer buffer;
	/**Is true if the channel is currently reconnecting.*/
	public boolean isReconnecting;
	/**The actual NIO channel.*/
	public SocketChannel channel;

	/**
	 * Simple constructor for a LCNChannel.
	 * @param remote The remote address.
	 * @param buffer The underlying ByteBuffer.
	 * @param isReconnecting a flag to show that the underlying channel is reconnecting.
	 * @param channel The actual SocketChannel.
	 */
	public LCNChannel(InetSocketAddress remote, ByteBuffer buffer, boolean isReconnecting, SocketChannel channel) {
		
		this.remote = remote;
		this.buffer = buffer;
		this.isReconnecting = isReconnecting;
		this.channel = channel;

	}

}