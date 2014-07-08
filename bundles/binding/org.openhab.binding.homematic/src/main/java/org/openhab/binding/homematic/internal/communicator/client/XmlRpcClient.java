/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.client;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.homematic.internal.common.HomematicConfig;
import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.CcuClient;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnection;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnectionRF;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnectionWired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client implementation for sending messages via XML-RPC to the CCU. It's a glue
 * class for the XML-RPC framework developed by Mathias Ewald and Thomas Letsch.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class XmlRpcClient implements CcuClient {
	private final static Logger logger = LoggerFactory.getLogger(XmlRpcClient.class);
	private HomematicConfig config = HomematicContext.getInstance().getConfig();

	private Map<HmInterface, XmlRpcConnection> xmlRpcConnections = new HashMap<HmInterface, XmlRpcConnection>(2);

	public XmlRpcClient() {
		logger.info("Starting {}", this.getClass().getSimpleName());
		xmlRpcConnections.put(HmInterface.RF, new XmlRpcConnectionRF(config.getHost()));
		xmlRpcConnections.put(HmInterface.WIRED, new XmlRpcConnectionWired(config.getHost()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(HmInterface hmInterface) throws CcuClientException {
		xmlRpcConnections.get(hmInterface).init(config.getXmlRpcCallbackUrl(), hmInterface.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void release(HmInterface hmInterface) throws CcuClientException {
		xmlRpcConnections.get(hmInterface).init(config.getXmlRpcCallbackUrl(), "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDatapointValue(HmDatapoint dp, String datapointName, Object value) throws CcuClientException {
		HmInterface hmInterface = dp.getChannel().getDevice().getHmInterface();
		String address = dp.getChannel().getAddress();
		if (dp.isIntegerValue() && value instanceof Double) {
			value = ((Number) value).intValue();
		}
		try {
			xmlRpcConnections.get(hmInterface).setValue(address, datapointName, value);
		} catch (Exception ex) {
			throw new CcuClientException(ex.getMessage(), ex);
		}
	}

}
