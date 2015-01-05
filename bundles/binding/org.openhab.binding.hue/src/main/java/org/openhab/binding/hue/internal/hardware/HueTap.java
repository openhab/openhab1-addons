/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hue.internal.hardware;

import org.openhab.binding.hue.internal.SwitchId;
import org.openhab.binding.hue.internal.data.HueSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.*;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * The representation of a physical Hue Tap switch,
 * 
 * @author Roman Hartmann
 * @author Kai Kreuzer
 * @author Jos Schering
 * @author Gernot Eger
 * 
 * @since 1.2.0
 * 
 */
public class HueTap {

	static final Logger logger = LoggerFactory.getLogger(HueTap.class);

	private HueBridge bridge = null;
	private int deviceNumber = 1;
	
	// tap specific
	private SwitchId lastSwitch;
	private DateTime lastPressed;
	
	private Client client;

	/**
	 * Constructor for the HueBulb.
	 * 
	 * @param connectedBridge
	 *            The bridge the bulb is connected to.
	 * @param deviceNumber
	 *            The number under which the bulb is filed in the bridge.
	 */
	public HueTap(HueBridge connectedBridge, int deviceNumber) {
		this.bridge = connectedBridge;
		this.deviceNumber = deviceNumber;
		getStatus(this.bridge.getSettings());

		//TODO: check if needed!!
		this.client = Client.create();
		this.client.setReadTimeout(1000);
		this.client.setConnectTimeout(2000);
	}
	
	/**
	 * Update the internal bulb status according to the Philips hub 
	 * @param HueSettings retrieved from hub
	 */
	public void getStatus(HueSettings settings){
		
//		this.lastSwitch=...;
//		this.lastPressed;

		
		
		//this.isOn = settings.isBulbOn(this.deviceNumber);
	}
	
}
