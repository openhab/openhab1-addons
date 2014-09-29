/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.ola;

import java.util.List;

import ola.OlaClient;
import ola.proto.Ola.DeviceInfo;
import ola.proto.Ola.DeviceInfoReply;
import ola.proto.Ola.PatchAction;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.dmx.DmxConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DMX Connection Implementation using OLA as the DMX target. This class wraps
 * the OLA Client. It defaults to universe 0 and autobinds to all devices.
 */
public class OlaConnection implements DmxConnection {

	private static final Logger logger = 
		LoggerFactory.getLogger(OlaConnection.class);
	
	private final static String DEFAULT_HOST = "localhost";
	
	private final static int DEFAULT_PORT = 9010;

	private OlaClient client;
	

	@Override
	public void open(String connectionString) throws Exception {
		String host = DEFAULT_HOST;
		int port = DEFAULT_PORT;
		
		if (StringUtils.isNotBlank(connectionString)) {
			String[] connectionStringElements = connectionString.split(":");
			if (connectionStringElements.length == 1) {
				host = connectionStringElements[0];
			} else if (connectionStringElements.length == 2) {
				host = connectionStringElements[0];
				port = Integer.valueOf(connectionStringElements[1]).intValue();
			}
		}
			
		client = new OlaClient();
		client.connect(host, port);
		autoBind();
	}

	@Override
	public void close() {
		if (client != null) {
			client.close();
		}
	}

	@Override
	public boolean isClosed() {
		if (client == null) {
			return true;
		}
		return client.isClosed();
	}
	
	@Override
	public void sendDmx(byte[] arg0) throws Exception {
		client.streamDmx(0, arg0);
	}

	/**
	 * Auto bind to all available devices unless autobind is disabled.
	 */
	private void autoBind() {

		String autoBind = System.getProperty("net.opendmx.ola.autobind");
		if (autoBind == null || !autoBind.equalsIgnoreCase("false")) {
			// bind default universe to all devices
			logger.info("Starting autobind for universe 0. To disable autobind use -Dnet.opendmx.ola.autobind=false");
			DeviceInfoReply devInfoReply = client.getDeviceInfo();
			if (devInfoReply == null) {
				logger.error("Could not retrieve device list. Binding to OLA aborted.");
				return;
			}
			List<DeviceInfo> devList = devInfoReply.getDeviceList();
			for (DeviceInfo devInfo : devList) {
				if (client.patchPort(devInfo.getDeviceAlias(), 0,
						PatchAction.PATCH, 0)) {
					logger.info("Linked universe 0 to device "
							+ devInfo.getDeviceName());
				} else {
					logger.info("Could not link universe 0 to device "
							+ devInfo.getDeviceName());
				}
			}
		}
	}
}
