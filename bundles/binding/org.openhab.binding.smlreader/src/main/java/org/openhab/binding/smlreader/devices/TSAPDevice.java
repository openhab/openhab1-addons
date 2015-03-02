/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.devices;

import java.io.IOException;
import java.net.InetAddress;

import javax.net.ssl.SSLSocketFactory;

import org.openmuc.jsml.structures.SML_File;
import org.openmuc.jsml.tl.SML_TConnection;
import org.openmuc.jsml.tl.SML_TSAP;
import org.openmuc.jsml.tl.XTrustProvider;

/**
 * Represents a configured - SML capable device with a TSAP connection.
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
final public class TSAPDevice extends SmlDeviceBase {

	SML_TSAP sml_tSAP;
	SML_TConnection receiver;
	String host;
	int port;
	boolean useSSL;

	/**
	* Constructor
	*/
	public TSAPDevice(String id, String host, int port, boolean useSSL){
		super(id);
		this.host = host;
		this.port = port;
		this.useSSL = useSSL;
		
		if (useSSL) {
			XTrustProvider.install();
			SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			sml_tSAP = new SML_TSAP(sslSocketFactory);
		}
		else {
			sml_tSAP = new SML_TSAP();
		}
	}

	/**
	* Open TSAP connection.
	*/
	private void openTSAPConnection() {
		try {
			receiver = sml_tSAP.connectTo(InetAddress.getByName(this.getHost()), this.getPort(), 0);
		} catch (IOException e) {
			logger.error("Can't open the TSAP connection of " + this.toString() + " due to exception!");
			logger.error(e.getMessage());
		}
	}
	
	/**
	* Disconnect TSAP connection.
	*/
	private void closeTSAPConnection() {
		receiver.disconnect();
	}

	/**
	* Gets the configured host name of this device.
	* @returns the configured hostname.
	*/
	private String getHost() {
		return this.host;
	}
	
	/**
	* Gets the configured port number of this device.
	* @returns the configured port number.
	*/
	private int getPort() {
		return this.port;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public String toString(){
		return "Device-Id: '" + this.getDeviceId() + "Host: '" + this.getHost() + "' Port: '" + this.getPort() + "'";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected boolean openConnection() {
		boolean connectionEstablished = true;

		openTSAPConnection();
		
		return connectionEstablished;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void closeConnection() {
		closeTSAPConnection();
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected SML_File getSmlFile() {
		SML_File smlFile = null;
		
		if(receiver != null){
			try {
				smlFile = receiver.receive();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.error("Can't receive SML informations due to missing initialization of the TSAP interface of : " + this.toString() + "!");
		}
		
		return smlFile;
	}
}
