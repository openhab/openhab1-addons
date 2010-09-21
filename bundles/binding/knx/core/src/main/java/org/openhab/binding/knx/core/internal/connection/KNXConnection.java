/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.knx.core.internal.connection;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.CloseEvent;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.event.NetworkLinkListener;
import tuwien.auto.calimero.link.medium.KNXMediumSettings;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;
import tuwien.auto.calimero.process.ProcessListener;

/**
 * This class establishes the connection to the KNX bus.
 * It uses the ConfigAdmin service to retrieve the relevant configuration data.
 * 
 * @author Kai Kreuzer
 *
 */
public class KNXConnection implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(KNXConnection.class);
	
	private static ProcessCommunicator pc = null;
	
	private static ProcessListener listener = null;

	private static KNXNetworkLinkIP link;
	
	/** the ip address to use for connecting to the KNX bus */
	private static String ip;
	
	/** time in milliseconds of how long should be paused between two read requests to the bus during initialization */
	private static long readingPause;

	/**
	 * Returns the KNXNetworkLink for talking to the KNX bus.
	 * The link can be null, if it has not (yet) been established successfully.
	 * 
	 * @return the KNX network link
	 */
	public static ProcessCommunicator getCommunicator() {
		if(link!=null && !link.isOpen()) connect();
		return pc;
	}

	public void setProcessListener(ProcessListener listener) {
		if(pc!=null) {
			pc.removeProcessListener(KNXConnection.listener);
			pc.addProcessListener(listener);
		}
		KNXConnection.listener = listener;
	}
	
	public void unsetProcessListener(ProcessListener listener) {
		if(pc!=null) {
			pc.removeProcessListener(KNXConnection.listener);
		}
		KNXConnection.listener = null;
	}
	
	public static void connect() {
		if (ip != null && !ip.isEmpty()) {
			try {
				link = new KNXNetworkLinkIP(ip,
						new KNXMediumSettings(null) {
							public short getMedium() {
								return KNXMediumSettings.MEDIUM_TP1;
							}
						});
				link.addLinkListener(new NetworkLinkListener() {
					public void linkClosed(CloseEvent e) {
						// if the link is lost, we want to reconnect immediately
						if(!e.isUserRequest()) connect();
					}
					
					public void indication(FrameEvent e) {}
					
					public void confirmation(FrameEvent e) {}
				});
				pc = new ProcessCommunicatorImpl(link);
				pc.setResponseTimeout(10);
				if(listener!=null) {
					pc.addProcessListener(listener);
				}
				logger.info("Established connection to KNX bus on IP {}.", ip);
			} catch (KNXException e) {
				logger.error("Error connecting to KNX bus", e);
			}
		} else {
			logger.error("No IP address could be found in configuration!");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			ip = (String) config.get("ip");
			readingPause = Long.parseLong((String) config.get("pause"));
			connect();
		} else {
			pc = null;
		}
	}

	public static long getReadingPause() {
		return readingPause;
	}
}
