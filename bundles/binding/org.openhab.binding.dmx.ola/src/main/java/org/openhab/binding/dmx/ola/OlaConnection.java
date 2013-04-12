/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
