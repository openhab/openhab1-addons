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
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnection;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnectionCuxd;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnectionRF;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnectionWired;

/**
 * Client implementation for sending messages via XML-RPC to the CCU. It's a
 * glue class for the XML-RPC framework developed by Mathias Ewald and Thomas
 * Letsch.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class XmlRpcClient extends BaseRpcClient {
	private HomematicConfig config = HomematicContext.getInstance().getConfig();

	private Map<HmInterface, XmlRpcConnection> xmlRpcConnections = new HashMap<HmInterface, XmlRpcConnection>(2);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shutdown() throws HomematicClientException {
		xmlRpcConnections.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(HmInterface hmInterface) throws HomematicClientException {
		if (getConnection(hmInterface).init(config.getXmlRpcCallbackUrl(), hmInterface.toString()) == false) {
			xmlRpcConnections.put(hmInterface, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void release(HmInterface hmInterface) throws HomematicClientException {
		getConnection(hmInterface).init(config.getXmlRpcCallbackUrl(), "");
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getAllValues(HmInterface hmInterface) throws HomematicClientException {
		Object result = getConnection(hmInterface).getAllValues();
		if (result instanceof Object[]) {
			return (Object[]) result;
		}

		throw new HomematicClientException("getAllValues returns unknown result type: "
				+ (result == null ? "null" : result.getClass()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ?> getAllSystemVariables(HmInterface hmInterface) throws HomematicClientException {
		Object result = getConnection(hmInterface).getAllSystemVariables();
		if (result instanceof Map) {
			return (Map<String, ?>) result;
		}

		throw new HomematicClientException("getAllSystemVariables returns unknown result type: "
				+ (result == null ? "null" : result.getClass()));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getDeviceDescription(HmInterface hmInterface, String address)
			throws HomematicClientException {
		Object result = getConnection(hmInterface).getDeviceDescription(address);
		if (result instanceof Map) {
			return (Map<String, String>) result;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeProgram(HmInterface hmInterface, String programName) throws HomematicClientException {
		getConnection(hmInterface).executeProgram(programName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDatapointValue(HmInterface hmInterface, String address, String datapointName, Object value)
			throws HomematicClientException {
		try {
			getConnection(hmInterface).setValue(address, datapointName, value);
		} catch (Exception ex) {
			throw new HomematicClientException(ex.getMessage(), ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSystemVariable(HmInterface hmInterface, String name, Object value) throws HomematicClientException {
		try {
			getConnection(hmInterface).setSystemVariable(name, value);
		} catch (Exception ex) {
			throw new HomematicClientException(ex.getMessage(), ex);
		}
	}

	private XmlRpcConnection getConnection(HmInterface hmInterface) {
		if (xmlRpcConnections.get(hmInterface) == null) {
			XmlRpcConnection xmlRpcCon = null;
			if (HmInterface.WIRED == hmInterface) {
				xmlRpcCon = new XmlRpcConnectionWired(config.getHost());
			} else if (HmInterface.CUXD == hmInterface) {
				xmlRpcCon = new XmlRpcConnectionCuxd(config.getHost());
			} else {
				xmlRpcCon = new XmlRpcConnectionRF(config.getHost());
			}
			xmlRpcConnections.put(hmInterface, xmlRpcCon);
		}
		return xmlRpcConnections.get(hmInterface);
	}

}
