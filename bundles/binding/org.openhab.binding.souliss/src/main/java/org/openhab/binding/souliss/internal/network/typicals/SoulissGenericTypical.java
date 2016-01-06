/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

import java.net.DatagramSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openhab.binding.souliss.internal.network.udp.SoulissCommGate;

/**
 * This class implements the base Souliss Typical All other Typicals derive from
 * this class
 * 
 * ...from wiki of Dario De Maio
 * In Souliss the logics that drive your lights, curtains, LED, and
 * others are pre-configured into so called Typicals. A Typical is a
 * logic with a predefined set of inputs and outputs and a know
 * behavior, are used to standardize the user interface and have a
 * configuration-less behavior.
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public abstract class SoulissGenericTypical {
	private int iSlot;
	private int iSoulissNodeID;
	private short sType;
	private float fState;
	private String sName;
	private boolean isUpdated = false;
	private String sNote;
	private static Logger logger = LoggerFactory
			.getLogger(SoulissGenericTypical.class);

	/**
	 * @return the iSlot
	 */
	public int getSlot() {
		return iSlot;
	}

	/**
	 * @param iSlot
	 *            the Slot number to set
	 */
	public void setSlot(int iSlot) {
		this.iSlot = iSlot;
	}

	/**
	 * @param SoulissNode
	 *            the SoulissNodeID to get
	 */
	public int getSoulissNodeID() {
		return iSoulissNodeID;
	}

	/**
	 * @param SoulissNode
	 *            the SoulissNodeID to set
	 */
	public void setSoulissNodeID(int setSoulissNodeID) {
		this.iSoulissNodeID = setSoulissNodeID;
	}

	/**
	 * @return the sType
	 */
	public short getType() {
		return sType;
	}

	/**
	 * @param soulissT
	 *            the typical to set
	 */
	protected void setType(short soulissT) {
		this.sType = soulissT;
	}

	/**
	 * @return the iState
	 */
	public float getState() {
		return fState;
	}

	/**
	 * @param iState
	 *            the state of typical
	 */
	public void setState(float iState) {
		logger.debug("Update State. Name: {}, Typ: 0x{}, Node: {}, Slot: {}. New State: {}", getName(),Integer.toHexString(getType()), getSoulissNodeID(), getSlot(), iState);
		this.fState = iState;
		setUpdatedTrue();
	}

	/**
	 * @return the nodeName
	 */
	public String getName() {
		return sName;
	}

	/**
	 * @param nodeName
	 *            the nodeName to set
	 */
	public void setName(String nodeName) {
		this.sName = nodeName;
	}

	/**
	 * @return isUpdated
	 */
	public boolean isUpdated() {
		return isUpdated;
	}

	/**
	 * Set to FALSE if new values have been read
	 */
	public void resetUpdate() {
		isUpdated = false;
	}

	/**
	 * Set to TRUE if there are a new values inside.
	 * 
	 * @return the isUpdated
	 */
	public void setUpdatedTrue() {
		this.isUpdated = true;
	}

	public String getNote() {
		return sNote;
	}

	/**
	 * Used to store the Openhab type inside typical
	 */
	public void setNote(String sNote) {
		this.sNote = sNote;
	}

	/**
	 * Send command in multicast
	 * 
	 * @param datagramSocket
	 * @param command
	 */
	void commandMulticast(DatagramSocket datagramSocket, short command) {
		logger.debug("Typ: {}, Name: {} - CommandMulticast: {}",getType(), getName(), command);
		SoulissCommGate.sendMULTICASTFORCEFrame(datagramSocket,
				SoulissNetworkParameter.IPAddressOnLAN, getType(), command);
	}

	/**
	 * Request the database structure, aka DBStruct
	 * 
	 * @param datagramSocket
	 */
	public void sendDBStructFrame(DatagramSocket datagramSocket) {
		logger.debug("Typ: {}, Name: {} - sendDBStructFrame", getType(), getName());
		SoulissCommGate.sendDBStructFrame(datagramSocket,
				SoulissNetworkParameter.IPAddressOnLAN);
	}

	/**
	 * Ping the gateway
	 * 
	 * @param datagramSocket
	 * @param putIn_1
	 * @param punIn_2
	 */
	public void ping(DatagramSocket datagramSocket, short putIn_1, short punIn_2) {
		logger.debug("Typ: {}, Name: {} - ping", getType(), getName()); 
		SoulissCommGate.sendPing(datagramSocket,
				SoulissNetworkParameter.IPAddressOnLAN, putIn_1, punIn_2);
	}

	public abstract org.openhab.core.types.State getOHState();
}
