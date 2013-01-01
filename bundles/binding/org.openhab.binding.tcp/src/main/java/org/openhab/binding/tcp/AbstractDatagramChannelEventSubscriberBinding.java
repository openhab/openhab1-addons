/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.tcp;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * 
 * This is the base for all "Datagram" connection-less network based connectivity and communication.It
 * requires a ChannelBindingProvider based binding provider. Data is pushed around using the BufferElement class,
 * which is essentially a ByteBuffer with an indicator for blocking/non-blocking (synchronous/asynchronous) communication
 * 
 * Each instance of the derived implementation class will start various threads:
 *  ParserThread : thread to parse incoming data which it takes from a readQueue
 *  ReconnectThread : thread that will monitor network connections, and reconnect them if required
 *  SelectorThread : thread that processes SelectionKeys, e.g. it deals with connecting Channels, reading from them (and storing to 
 *  	to the readQueue), and writing data to them that is polled from the writeQueue
 * 
 * @author Karel Goderis
 * @since 1.1.0
 * 
 */
public abstract class AbstractDatagramChannelEventSubscriberBinding<P extends ChannelBindingProvider>
extends  AbstractChannelEventSubscriberBinding<DatagramChannel,P> {

	@Override
	final public boolean isConnectionOriented(){
		return false;
	}

	@Override
	final protected void connectChannel(DatagramChannel channel, SocketAddress address)
			throws IOException {
		channel.connect(address);	
	}

	@Override
	final protected DatagramChannel openChannel() throws IOException {
		return DatagramChannel.open();	
	}


	@Override
	final protected boolean isConnectedChannel(DatagramChannel channel) {
		return channel.isConnected();
	}

	@Override
	final protected boolean isConnectionPendingChannel(DatagramChannel channel) {
		return false;
	}

	@Override
	final protected boolean finishConnectChannel(DatagramChannel channel)
			throws IOException {
		return true;
	}

	@Override
	final protected int readChannel(DatagramChannel channel, ByteBuffer byteBuffer)
			throws IOException {
		return channel.read(byteBuffer);
	}

	@Override
	final protected int writeChannel(DatagramChannel channel, ByteBuffer byteBuffer)
			throws IOException {
		return channel.write(byteBuffer);
	}

	@Override
	final protected void setKeepAliveChannel(DatagramChannel channel, boolean aBoolean) throws SocketException{
	}


}
