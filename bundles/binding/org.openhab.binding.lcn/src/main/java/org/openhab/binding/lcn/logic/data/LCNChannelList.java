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
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * This class represents a list of LCNChannels.
 * These LCNChannels can be searched for, by certain criteria.
 * 
 * @author Patrik Pastuschek
 * @since 1.7.0
 * 
 */
public class LCNChannelList extends ArrayList<LCNChannel> {

	/**Generated serial.*/
	private static final long serialVersionUID = -33341381860036659L;

	/**
	 * Find a LCNChannel by InetSocketAddress.
	 * 
	 * @param address The InetSocketAddress to search by.
	 * @return A matching channel, null if no matching channel could be found.
	 */
	public LCNChannel get(InetSocketAddress address) {
		synchronized(this) {
			LCNChannel result = null;
			for (LCNChannel chan : this) {
				if (null != chan && null != address && address.equals(chan.remote)) {
					result = chan;
				}
			}
			return result;
		}
	}

	/**
	 * Find a LCNChannel by SocketChannel.
	 * 
	 * @param channel The SocketChannel to search by.
	 * @return A matching channel, null if no matching channel could be found.
	 */
	public LCNChannel get(SocketChannel channel) {
		synchronized(this) {
			LCNChannel result = null;
			for (LCNChannel chan : this) {
				if(channel.equals(chan.channel)) {
					result =  chan;
				}
			}
			return result;
		}
	}

	/**
	 * Find all LCNChannels that are equal to the SocketChannel.
	 * 
	 * @param channel The SocketChannel to search by.
	 * @return All matching channels, null if no matching channels were found.
	 */
	public ArrayList<LCNChannel> getAll(SocketChannel channel) {
		synchronized(this) {
			ArrayList<LCNChannel> result = new ArrayList<LCNChannel>();
			for (LCNChannel chan : this) {
				if (channel.equals(chan.channel)) {
					result.add(chan);
				}
			}
			return result;
		}
	}
	

	/**
	 * Sets the 'reconnect' flag for all LCNChannels inside this list.
	 * 
	 * @param channel The SocketChannel base, for which all channels are to be set as reconnecting.
	 * @param reconnecting
	 */
	public void setAllReconnecting(SocketChannel channel, boolean reconnecting) {
		synchronized(this) {
			for (LCNChannel chan : this) {
				if (channel.equals(chan)) {
					chan.isReconnecting = reconnecting;
				}
			}
		}		
	}
	
	/**
	 * Sets the 'reconnect' flag for all LCNChannels inside this list.
	 * 
	 * @param channel The LcnChannel base, for which all channels are to be set as reconnecting.
	 * @param reconnecting
	 */
	public void setAllReconnecting(LCNChannel channel, boolean reconnecting) {
		synchronized(this) {
			for (LCNChannel chan : this) {
				if (channel.equals(chan)) {
					chan.isReconnecting = reconnecting;
				}
			}
		}		
	}
	
	/**
	 * Replaces the actual SocketChannel for all LCNChannels that target the given address.
	 * 
	 * @param address The InetSocketAddress target.
	 * @param channel The SocketChannel which is to replace the old SocketChannel.
	 */
	public void replace (InetSocketAddress address, SocketChannel channel) {
		synchronized(this) {
			for (LCNChannel chan : this) {
				if (!channel.equals(chan) && address.equals(chan.remote)) {
					chan.channel = channel;
				}
			}
	
		}

	}

}
