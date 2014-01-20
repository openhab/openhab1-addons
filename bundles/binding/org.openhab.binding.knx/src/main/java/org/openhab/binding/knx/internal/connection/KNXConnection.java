/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.internal.connection;

import gnu.io.CommPortIdentifier;
import gnu.io.RXTXVersion;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

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
	
	/** signals that the connection is shut down on purpose */
	public static boolean shutdown = false;

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

	/** time in milliseconds of how long should be paused between two read requests to the bus during initialization. Default value is <code>50</Code> */
	private static long readingPause = 50;
	
	/** timeout in milliseconds to wait for a response from the KNX bus. Default value is <code>10000</code> */
	private static long responseTimeout = 10000;
	
	/** limits the read retries while initialization from the KNX bus. Default value is <code>3</code> */
	private static int readRetriesLimit = 3;

	/** seconds between connect retries when KNX link has been lost, 0 means never retry. Default value is <code>0</code> */
	private static int autoReconnectPeriod = 0;
	
	/** listeners for connection/re-connection events */
	private static Set<KNXConnectionListener> connectionListeners = new HashSet<KNXConnectionListener>();
	
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
		if (pc != null) {
			pc.removeProcessListener(KNXConnection.listener);
			pc.addProcessListener(listener);
		}
		KNXConnection.listener = listener;
	}
	
	public void unsetProcessListener(ProcessListener listener) {
		if (pc != null) {
			pc.removeProcessListener(KNXConnection.listener);
		}
		KNXConnection.listener = null;
	}
	
	public static void addConnectionEstablishedListener(KNXConnectionListener listener) {
		KNXConnection.connectionListeners.add(listener);
	}

	public static void removeConnectionEstablishedListener(KNXConnectionListener listener) {
		KNXConnection.connectionListeners.remove(listener);
	}

	public static synchronized void connect() {
		shutdown = false;
		try {
			if (StringUtils.isNotBlank(ip)) { 
				link = connectByIp(ipConnectionType, localIp, ip, port);
			} else if (StringUtils.isNotBlank(serialPort)) { 
				link = connectBySerial(serialPort);
			} else {
				logger.error("No IP address or serial port could be found in configuration!");
				return;
			}


			NetworkLinkListener linkListener = new NetworkLinkListener() {
				public void linkClosed(CloseEvent e) {
					// if the link is lost, we want to reconnect immediately
					if(!e.isUserRequest() && !shutdown) {
						logger.warn("KNX link has been lost (reason: {} on object {}) - reconnecting...", e.getReason(), e.getSource().toString());
						connect();
					}
					if(!link.isOpen() && !shutdown) {
						logger.error("KNX link has been lost!");
						if(autoReconnectPeriod>0) {
							logger.info("KNX link will be retried in " + autoReconnectPeriod + " seconds");
							final Timer timer = new Timer();
							TimerTask timerTask = new TimerTask() {
								@Override
								public void run() {
									if(shutdown) {
										timer.cancel();
									}
									else {
										logger.info("Trying to reconnect to KNX...");
										connect();
										if(link.isOpen()) {
											timer.cancel();
										}
									}
								}
							};
							timer.schedule(timerTask, autoReconnectPeriod * 1000, autoReconnectPeriod * 1000);
						}
					}
				}
				
				public void indication(FrameEvent e) {}
				
				public void confirmation(FrameEvent e) {}
			};
			
			link.addLinkListener(linkListener);
			
			if(pc!=null) {
				pc.removeProcessListener(listener);
				pc.detach();
			}
			
			pc = new ProcessCommunicatorImpl(link);
			pc.setResponseTimeout((int) responseTimeout/1000);
			
			if(listener!=null) {
				pc.addProcessListener(listener);
			}

			for(KNXConnectionListener listener : KNXConnection.connectionListeners) {
				listener.connectionEstablished();
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
	
	public static synchronized void disconnect() {
		shutdown = true;
		if (pc!=null) {
			KNXNetworkLink link = pc.detach();
			if(listener!=null) {
				pc.removeProcessListener(listener);
				listener = null;
			}
			if (link!=null) {
				logger.info("Closing KNX connection");
				link.close();
			}
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
			throw new KNXException("The serial FT1.2 KNX connection requires the RXTX libraries to be available, but they could not be found!");
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
			
			String autoReconnectPeriodString = (String) config.get("autoReconnectPeriod");
			if (StringUtils.isNotBlank(autoReconnectPeriodString)) {
				int autoReconnectPeriodValue = Integer.parseInt(autoReconnectPeriodString);
				if (autoReconnectPeriodValue >= 0) {
					autoReconnectPeriod = autoReconnectPeriodValue;
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
	
	public static int getAutoReconnectPeriod() {
		return autoReconnectPeriod;
	}
	
	
}
