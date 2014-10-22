/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gc100ir.lib;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that stores information about the devices attached to the GC-100.
 * 
 * @author Parikshit Thakur & Team
 * @since 1.6.0
 */
public class GC100IRDevice {

	int connector;
	int module;
	String connectorType;
	String deviceType;
	String configName;
	String ipAddress;
	TCPIPSocket socket;
	Map<String , String> discoveryMessageMap = new HashMap<String, String>();
	GC100IRControlPoint gc100ControlPoint;
	
	/**
	 * Constructor.
	 * Initializes an object of GC100IRControlPoint to local variable.
	 * @param gc100ControlPoint an object of GC100IRControlPoint
	 * @param configURL a String value of ipAddress
	 * @param module an integer value of module
	 * @param connector an integer value of connector
	 * @param connectorType an integer value of connectorType
	 */
	GC100IRDevice(GC100IRControlPoint gc100ControlPoint, String configURL, int module, int connector, String connectorType){
		
		this.gc100ControlPoint = gc100ControlPoint;
		this.module = module;
		this.connector = connector;
		this.connectorType = connectorType;		
		this.ipAddress = configURL.substring(configURL.lastIndexOf("/")+1);
	}
	
	/**
	 * Get InetAddress of the device.
	 * 
	 * @return an InetAddress representing IPAddress of the device.
	 */
	public InetAddress getInetAddress() throws UnknownHostException{
		
		return InetAddress.getByName(ipAddress); 
		
	}
	
	/**
	 * Get the String value of IP Address.
	 * 
	 * @return a String value specifies IP Address
	 */
	public String getIPAddressString() {
		
		return ipAddress; 	
	}
	
	/**
	 * Get the connector number to which device is attached.
	 * 
	 * @return an integer value of connector
	 */
	public int getConnector() {
		
		return connector;
		
	}
	
	/**
	 * Get the module number to which device is attached.
	 * 
	 * @return an integer value of module
	 */
	public int getModule() {
		
		return module;
		
	}
	
	/**
	 * Get the connectorType of the device.
	 * 
	 * @return a String value of connectorType
	 */
	public String getConnectorType() {
		
		return connectorType;
		
	}
	
	/**
	 * Set the TCPIPSocket.
	 * 
	 * @param socket an object of TCPIPSocket
	 */
	void setTCPIPSocket(TCPIPSocket socket){
		this.socket = socket;
		
	}
	
	/**
	 * Get the TCPIPSocket.
	 * 
	 * @return an object of TCPIPSocket
	 */
	TCPIPSocket getTCPIPSocket(){
		
		return socket;
		
	}
	
}
