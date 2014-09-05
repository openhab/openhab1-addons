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

import org.apache.commons.lang.StringUtils;
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
		ServerId serverId = null;
		try {
			serverId = new ServerId(getVersion(hmInterface));
		} catch (Exception ex) {
			// ignore, the getVersion method may not be implemented (CCU1)
		}

		if (serverId == null || !serverId.isHomegear()) {
			Map<String, String> deviceDescription = getDeviceDescription(hmInterface, "BidCoS-RF");
			String firmwareVersion = null;
			if (deviceDescription != null) {
				firmwareVersion = deviceDescription.get("FIRMWARE");
			}
			serverId = new ServerId(StringUtils.defaultIfBlank(firmwareVersion, "unknown"));
		}

		return serverId;
	}

}
