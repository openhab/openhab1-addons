/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.knx.internal.connection;

import gnu.io.CommPortIdentifier;
import gnu.io.RXTXVersion;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.CloseEvent;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.knxnetip.KNXnetIPConnection;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkFT12;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.event.NetworkLinkListener;
import tuwien.auto.calimero.link.medium.TPSettings;
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

	private static KNXNetworkLink link;
	
	/** the ip address to use for connecting to the KNX bus */
	private static String ip;
	
	/** the ip connection type for connecting to the KNX bus. Could be either TUNNEL or ROUTING */
	private static int ipConnectionType;

	/** the default multicast ip address (see <a href="http://www.iana.org/assignments/multicast-addresses/multicast-addresses.xml">iana</a> EIBnet/IP)*/
	private static final String DEFAULT_MULTICAST_IP = "224.0.23.12";

	/** KNXnet/IP port number */
	private static int port;
	
	/** local endpoint to specify the multicast interface. no port is used */
	private static String localIp;
	
	/** the serial port to use for connecting to the KNX bus */
	private static String serialPort;

	/** time in milliseconds of how long should be paused between two read requests to the bus during initialization. Defaultvalue is <code>50</Code> */
	private static long readingPause = 50;
	
	/** timeout in milliseconds to wait for a response from the KNX bus. Defaultvalue is <code>10000</code> */
	private static long responseTimeout = 10000;
	
	/** read retry limit while initialization from the KNX bus. Defaultvalue is <code>3</code> */
	private static int readRetriesLimit = 3;
	

	/**
	 * Returns the KNXNetworkLink for talking to the KNX bus.
	 * The link can be null, if it has not (yet) been established successfully.
	 * 
	 * @return the KNX network link
	 */
	public static synchronized ProcessCommunicator getCommunicator() {
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
	
	public static synchronized void connect() {
		
		try {
			
			if (StringUtils.isNotBlank(ip)) { 
				link = connectByIp(ipConnectionType, localIp, ip, port);
			} else if (StringUtils.isNotBlank(serialPort)) { 
				link = connectBySerial(serialPort);
			} else {
				logger.error("No IP address or serial port could be found in configuration!");
				return;
			}
			
			link.addLinkListener(new NetworkLinkListener() {
				public void linkClosed(CloseEvent e) {
					// if the link is lost, we want to reconnect immediately
					if(!e.isUserRequest()) {
						logger.warn("KNX link has been lost (reason: {} on object {}) - reconnecting...", e.getReason(), e.getSource().toString());
						connect();
					}
					if(!link.isOpen()) {
						logger.error("KNX link has been lost!");
					}
				}
				
				public void indication(FrameEvent e) {}
				
				public void confirmation(FrameEvent e) {}
			});
			
			if(pc!=null) {
				pc.removeProcessListener(listener);
				pc.detach();
			}
			
			pc = new ProcessCommunicatorImpl(link);
			pc.setResponseTimeout((int) responseTimeout/1000);
			
			if(listener!=null) {
				pc.addProcessListener(listener);
			}
			
			if (logger.isInfoEnabled()) {
				if (link instanceof KNXNetworkLinkIP) {
					String ipConnectionTypeString = 
						KNXConnection.ipConnectionType == KNXNetworkLinkIP.ROUTER ? "ROUTER" : "TUNNEL";
					logger.info("Established connection to KNX bus on {} in mode {}.", ip + ":" + port, ipConnectionTypeString);
				} else {
					logger.info("Established connection to KNX bus through FT1.2 on serial port {}.", serialPort);
				}
			}
			
		} catch (KNXException e) {
			logger.error("Error connecting to KNX bus: {}", e.getMessage());
		} catch (UnknownHostException e) {
			logger.error("Error connecting to KNX bus: {}", e.getMessage());
		}
	}
	
	private static KNXNetworkLink connectByIp(int ipConnectionType, String localIp, String ip, int port) throws KNXException, UnknownHostException {
		
		InetSocketAddress localEndPoint = null;
		if (StringUtils.isNotBlank(localIp)) {
			localEndPoint = new InetSocketAddress(localIp, 0);
		} else {
			try {
				InetAddress localHost = InetAddress.getLocalHost();
				localEndPoint = new InetSocketAddress(localHost, 0);
			} catch (UnknownHostException uhe) {
				logger.warn("Couldn't find an IP address for this host. Please check the .hosts configuration or use the 'localIp' parameter to configure a valid IP address.");
			}
		}
		
		return new KNXNetworkLinkIP(ipConnectionType, localEndPoint, new InetSocketAddress(ip, port), false, TPSettings.TP1);
	}
	
	private static KNXNetworkLink connectBySerial(String serialPort) throws KNXException {

		try {
			RXTXVersion.getVersion();
			return new KNXNetworkLinkFT12(serialPort, new TPSettings(true));
		} catch(NoClassDefFoundError e) {
			throw new KNXException("The serial FT1.2 KNX connection requires the serial binding to be installed!");
		} catch(KNXException knxe) {
			if(knxe.getMessage().startsWith("can not open serial port")) {
				StringBuilder sb = new StringBuilder("Available ports are:\n");
				Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();
				while (portList.hasMoreElements()) {
					CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
					if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
						sb.append(id.getName() + "\n");
					}
				}
				sb.deleteCharAt(sb.length()-1);
				knxe = new KNXException("Serial port '" + serialPort + "' could not be opened. " + sb.toString());
			}
			throw knxe;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			ip = (String) config.get("ip");

			String connectionTypeString = (String) config.get("type");
			if (StringUtils.isNotBlank(connectionTypeString)) {
				if ("TUNNEL".equals(connectionTypeString)) {
					ipConnectionType = KNXNetworkLinkIP.TUNNEL;
				}
				else if ("ROUTER".equals(connectionTypeString)) {
					ipConnectionType = KNXNetworkLinkIP.ROUTER;
					if (StringUtils.isBlank(ip)) {
						ip = DEFAULT_MULTICAST_IP;
					}
				}
				else {
					throw new ConfigurationException("type", "unknown IP connection type '" + connectionTypeString + "'! Known types are either 'TUNNEL' or 'ROUTER'");
				}
			} else {
				ipConnectionType = KNXNetworkLinkIP.TUNNEL;
			}

			String portConfig = (String) config.get("port");
			if (StringUtils.isNotBlank(portConfig)) {
				port = Integer.parseInt(portConfig);
			} else {
				port = KNXnetIPConnection.IP_PORT;
			}

			localIp = (String) config.get("localIp");

			serialPort = (String) config.get("serialPort");

			String readingPauseString = (String) config.get("pause");
			if (StringUtils.isNotBlank(readingPauseString)) {
				readingPause = Long.parseLong(readingPauseString);
			}
			
			String responseTimeoutString = (String) config.get("timeout");
			if (StringUtils.isNotBlank(responseTimeoutString)) {
				long timeout = Long.parseLong(responseTimeoutString);
				if (timeout > 0) {
					responseTimeout = timeout;
				}
			}
			
			String readRetriesLimitString = (String) config.get("readRetries");
			if (StringUtils.isNotBlank(readRetriesLimitString)) {
				int readRetries = Integer.parseInt(readRetriesLimitString);
				if (readRetries > 0) {
					readRetriesLimit = readRetries;
				}
			}
			
			if(pc==null) connect();
		}
	}

	public static long getReadingPause() {
		return readingPause;
	}
	
	public static int getReadRetriesLimit() {
		return readRetriesLimit;
	}
	
}
