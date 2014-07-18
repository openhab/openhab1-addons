/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.openhab.binding.homematic.internal.model.HmInterface;

/**
 * This class adds Wired specific methods of the CCU interface. It connects to
 * port 2000 of the given address.
 * 
 * @see XmlRpcConnection
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class XmlRpcConnectionWired extends XmlRpcConnection {

	private final String address;
	private XmlRpcClient xmlRpcClient;

	public XmlRpcConnectionWired(String address) {
		this.address = address;
		xmlRpcClient = createXmlRpcClient();
	}

	@Override
	protected XmlRpcClient getXmlRpcClient() {
		return xmlRpcClient;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public Integer getPort() {
		return HmInterface.WIRED.getPort();
	}

}
