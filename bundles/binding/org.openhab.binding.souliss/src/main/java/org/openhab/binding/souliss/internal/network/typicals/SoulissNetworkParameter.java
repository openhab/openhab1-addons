/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contain parameter of Souliss Network. 
 * Those are loaded at startup from SoulissBinding.updated(), from file openhab.cfg 
 * and used by SoulissBinding.execute(), SoulissCommGate.send(), UDPServerThread, decodeDBStructRequest.decodeMacaco
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissNetworkParameter {
	public static String IPAddressOnLAN = "";
	public static int nodes;
	public static int maxnodes;
	public static int maxTypicalXnode;
	public static int maxrequests;
	public static int MaCacoIN_s;
	public static int MaCacoTYP_s;
	public static int MaCacoOUT_s;
	static Properties prop = new Properties();
	public static int presetTime = 1000;
	public static int REFRESH_DBSTRUCT_TIME = presetTime;
	public static int REFRESH_SUBSCRIPTION_TIME = presetTime;
	public static int REFRESH_HEALTY_TIME = presetTime;
	public static int REFRESH_MONITOR_TIME = presetTime;
	public static int SEND_DELAY = presetTime;
	public static int SEND_MIN_DELAY = presetTime;
	public static long SECURE_SEND_TIMEOUT_TO_REQUEUE = presetTime;
	public static long SECURE_SEND_TIMEOUT_TO_REMOVE_PACKET = presetTime;
	private static Logger logger = LoggerFactory
			.getLogger(SoulissNetworkParameter.class);
	public static int NodeIndex = 70;
	public static int UserIndex = 133;
	public static Integer serverPort;
	public static DatagramSocket datagramsocket;


	/**
	 * @return sPar value format to string 0x+sPar
	 */
	public static String getPropTypicalBytes(String sPar) {
		return (String) prop.get("0x" + sPar);
	}

	/**
	 * Load in the memory the contents of InputStream is
	 * 
	 * @param is
	 */
	public static void load(InputStream is) {
		try {
			prop.load(is);
			logger.trace("ok");
			is.close();
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}
}
