/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.artnet;

import java.net.InetAddress;

import org.openhab.binding.dmx.DmxConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import artnet4j.ArtNet;
import artnet4j.ArtNetException;
import artnet4j.ArtNetNode;
import artnet4j.packets.ArtDmxPacket;

/**
 * DMX Connection Implementation using ArtNet as the DMX target.
 * 
 * @author Rainer Ostendorf
 * @since 1.4.0
 */
public class ArtnetConnection implements DmxConnection {

	private static final Logger logger = LoggerFactory
			.getLogger(ArtnetConnection.class);

	/** sequence ID, used for enumerating the artnet packets send */
	private int sequenceID;

	/** the Artnet4J server */
	private ArtNet artnet = new ArtNet();

	/** flag to indicate if the connection was open()ed */
	private Boolean isConnectionClosed = true;

	/** list of our receivers, filled in open() */
	private List<ArtNetNode> receiverNodes = new ArrayList<ArtNetNode>();

	/**
	 * Gets called when the DMX connection is opened The connection String holds
	 * the information from openhab.cfg setting for example:
	 * dmx:connection,dmx:connection=192.168.2.151,192.168.2.201 will send the
	 * DMX data to the both artnet receivers listed {@inheritDoc}
	 * 
	 * @see org.openhab.binding.dmx.DmxConnection#open(java.lang.String)
	 */
	@Override
	public void open(String connectionString) throws Exception {

		logger.debug("artnet-config: " + connectionString);

		// extract IPs from string <IP>,<IP>,.. and add them to list
		for (String configString : connectionString.split(",")) {
			logger.debug("adding artnet receiver " + configString);
			ArtNetNode node = new ArtNetNode();
			node.setIPAddress(InetAddress.getByName(configString));
			receiverNodes.add(node);
		}

		// start the artnet server
		try {
			artnet.start();

			// indicate that the connection was opened
			isConnectionClosed = false;

		} catch (ArtNetException e) {
			logger.error(e.fillInStackTrace().toString());
		}

	}

	@Override
	public void close() {
		receiverNodes.clear(); // remove all receivers from list
		artnet.stop(); // stop the artnet server
		isConnectionClosed = true; // indicate that connection is closed
	}

	@Override
	public boolean isClosed() {
		return isConnectionClosed;
	}

	/**
	 * This function gets called each time DMX data to be submitted it iterates
	 * through the list of receivers, and sends out the data to them. when no
	 * receiver was specified, the data is broadcasted
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.openhab.binding.dmx.DmxConnection#sendDmx(byte[])
	 */
	@Override
	public void sendDmx(byte[] buffer) throws Exception {

		if (!isConnectionClosed) {

			ArtDmxPacket dmx = new ArtDmxPacket();

			// Default to universe 0. May need to be changed later to support
			// multiple universes.
			dmx.setUniverse(0, 0);
			dmx.setSequenceID(sequenceID % 255);
			dmx.setDMX(buffer, buffer.length);

			if (!receiverNodes.isEmpty()) {

				for (ArtNetNode receiver : receiverNodes) {
					logger.trace("Sending " + buffer.length + " Bytes to "
							+ receiver.getIPAddress().toString());
					artnet.unicastPacket(dmx, receiver);
				}

			} else {
				artnet.broadcastPacket(dmx);
			}
			sequenceID++;
		}
	}
}
