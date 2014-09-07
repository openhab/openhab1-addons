/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.client;

import java.util.Map;

import org.openhab.binding.homematic.internal.communicator.client.interfaces.RpcClient;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class with common methods for all RpcClients.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public abstract class BaseRpcClient implements RpcClient {
	private final static Logger logger = LoggerFactory.getLogger(BaseRpcClient.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() throws HomematicClientException {
		logger.debug("Starting {}", this.getClass().getSimpleName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ServerId getServerId(HmInterface hmInterface) throws HomematicClientException {
		Map<String, String> deviceDescription = getDeviceDescription(hmInterface, "BidCoS-RF");
		ServerId serverId = new ServerId(deviceDescription.get("TYPE"));
		serverId.setVersion(deviceDescription.get("FIRMWARE"));
		return serverId;
	}

}
