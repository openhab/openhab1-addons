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
import tuwien.auto.calimero.link.NetworkLinkListener;
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

	private static final Logger sLogger = LoggerFactory.getLogger(KNXConnection.class);

	private static ProcessCommunicator sPC = null;
	
	private static ProcessListener sProcessCommunicationListener = null;

	private static KNXNetworkLink sLink;
	
	/** signals that the connection is shut down on purpose */
	public static boolean sShutdown = false;

	/** the ip address to use for connecting to the KNX bus */
	private static String sIp;
	
	/** the ip connection type for connecting to the KNX bus. Could be either TUNNEL or ROUTING */
	private static int sIpConnectionType;
	
	/** the default multicast ip address (see <a href="http://www.iana.org/assignments/multicast-addresses/multicast-addresses.xml">iana</a> EIBnet/IP)*/
	private static final String DEFAULT_MULTICAST_IP = "224.0.23.12";

	/** KNXnet/IP port number */
	private static int sPort;
	
	/** local endpoint to specify the multicast interface. no port is used */
	private static String sLocalIp;
	
	/** the serial port to use for connecting to the KNX bus */
	private static String sSerialPort;

	/** time in milliseconds of how long should be paused between two read requests to the bus during initialization. Default value is <code>50</Code> */
	private static long sReadingPause = 50;
	
	/** timeout in milliseconds to wait for a response from the KNX bus. Default value is <code>10000</code> */
	private static long sResponseTimeout = 10000;
	
	/** limits the read retries while initialization from the KNX bus. Default value is <code>3</code> */
	private static int sReadRetriesLimit = 3;

	/** seconds between connect retries when KNX link has been lost, 0 means never retry. Default value is <code>0</code> */
	private static int sAutoReconnectPeriod = 0;
	
	/** listeners for connection/re-connection events */
	private static Set<KNXConnectionListener> sConnectionListeners = new HashSet<KNXConnectionListener>();
	
	/**
	 * Returns the KNXNetworkLink for talking to the KNX bus.
	 * The link can be null, if it has not (yet) been established successfully.
	 * 
	 * @return the KNX network link
	 */
	public static synchronized ProcessCommunicator getCommunicator() {
		if(sLink!=null && !sLink.isOpen()) {
			connect();
		}
		return sPC;
	}

	public void setProcessListener(ProcessListener listener) {
		if (sPC != null) {
			sPC.removeProcessListener(KNXConnection.sProcessCommunicationListener);
			sPC.addProcessListener(listener);
		}
		KNXConnection.sProcessCommunicationListener = listener;
	}
	
	public void unsetProcessListener(ProcessListener listener) {
		if (sPC != null) {
			sPC.removeProcessListener(KNXConnection.sProcessCommunicationListener);
		}
		KNXConnection.sProcessCommunicationListener = null;
	}
	
	public static void addConnectionEstablishedListener(KNXConnectionListener listener) {
		KNXConnection.sConnectionListeners.add(listener);
	}

	public static void removeConnectionEstablishedListener(KNXConnectionListener listener) {
		KNXConnection.sConnectionListeners.remove(listener);
	}

	/**
	 * Tries to connect either by IP or serial bus, depending on supplied config data.
	 * @return true if connection was established, false otherwise
	 */
	public static synchronized boolean connect() {
		boolean successRetVal=false;
		
		sShutdown = false;
		try {
			if (StringUtils.isNotBlank(sIp)) { 
				sLink = connectByIp(sIpConnectionType, sLocalIp, sIp, sPort);
			} else if (StringUtils.isNotBlank(sSerialPort)) { 
				sLink = connectBySerial(sSerialPort);
			} else {
				sLogger.error("No IP address or serial port could be found in configuration!");
				return false;
			}


			NetworkLinkListener linkListener = new NetworkLinkListener() {
				public void linkClosed(CloseEvent e) {
					// if the link is lost, we want to reconnect immediately
					if(!(CloseEvent.USER_REQUEST == e.getInitiator()) && !sShutdown) {
						sLogger.warn("KNX link has been lost (reason: {} on object {}) - reconnecting...", e.getReason(), e.getSource().toString());
						connect();
					}
					if(!sLink.isOpen() && !sShutdown) {
						sLogger.error("KNX link has been lost!");
						if(sAutoReconnectPeriod>0) {
							sLogger.info("KNX link will be retried in " + sAutoReconnectPeriod + " seconds");
							final Timer timer = new Timer();
							TimerTask timerTask = new TimerTask() {
								@Override
								public void run() {
									if(sShutdown) {
										timer.cancel();
									}
									else {
										sLogger.info("Trying to reconnect to KNX...");
										connect();
										if(sLink.isOpen()) {
											timer.cancel();
										}
									}
								}
							};
							timer.schedule(timerTask, sAutoReconnectPeriod * 1000, sAutoReconnectPeriod * 1000);
						}
					}
				}
				
				public void indication(FrameEvent e) {}
				
				public void confirmation(FrameEvent e) {}
			};
			
			sLink.addLinkListener(linkListener);
			
			if(sPC!=null) {
				sPC.removeProcessListener(sProcessCommunicationListener);
				sPC.detach();
			}
			
			sPC = new ProcessCommunicatorImpl(sLink);
			sPC.setResponseTimeout((int) sResponseTimeout/1000);
			
			if(sProcessCommunicationListener!=null) {
				sPC.addProcessListener(sProcessCommunicationListener);
			}

			for(KNXConnectionListener listener : KNXConnection.sConnectionListeners) {
				listener.connectionEstablished();
			}

			successRetVal=true;

			if (sLogger.isInfoEnabled()) {
				if (sLink instanceof KNXNetworkLinkIP) {
					String ipConnectionTypeString = 
						KNXConnection.sIpConnectionType == KNXNetworkLinkIP.ROUTING ? "ROUTER" : "TUNNEL";
					sLogger.info("Established connection to KNX bus on {} in mode {}.", sIp + ":" + sPort, ipConnectionTypeString);
				} else {
					sLogger.info("Established connection to KNX bus through FT1.2 on serial port {}.", sSerialPort);
				}
			}
			
		} catch (KNXException e) {
			sLogger.error("Error connecting to KNX bus: {}", e.getMessage());
		} catch (UnknownHostException e) {
			sLogger.error("Error connecting to KNX bus (unknown host): {}", e.getMessage());
		} catch (InterruptedException e) {
			sLogger.error("Error connecting to KNX bus (interrupted): {}", e.getMessage());
		}
		return successRetVal;
	}
	
	public static synchronized void disconnect() {
		sShutdown = true;
		if (sPC!=null) {
			KNXNetworkLink link = sPC.detach();
			if(sProcessCommunicationListener!=null) {
				sPC.removeProcessListener(sProcessCommunicationListener);
				sProcessCommunicationListener = null;
			}
			if (link!=null) {
				sLogger.info("Closing KNX connection");
				link.close();
			}
		}
	}

	private static KNXNetworkLink connectByIp(int ipConnectionType, String localIp, String ip, int port) throws KNXException, UnknownHostException, InterruptedException {
		
		InetSocketAddress localEndPoint = null;
		if (StringUtils.isNotBlank(localIp)) {
			localEndPoint = new InetSocketAddress(localIp, 0);
		} else {
			try {
				InetAddress localHost = InetAddress.getLocalHost();
				localEndPoint = new InetSocketAddress(localHost, 0);
			} catch (UnknownHostException uhe) {
				sLogger.warn("Couldn't find an IP address for this host. Please check the .hosts configuration or use the 'localIp' parameter to configure a valid IP address.");
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
	
	/* (non-Javadoc)
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			sLogger.debug("KNXBinding configuration present. Setting up KNX bus connection.");
			sIp = (String) config.get("ip");

			String connectionTypeString = (String) config.get("type");
			if (StringUtils.isNotBlank(connectionTypeString)) {
				if ("TUNNEL".equals(connectionTypeString)) {
					sIpConnectionType = KNXNetworkLinkIP.TUNNELING;
				}
				else if ("ROUTER".equals(connectionTypeString)) {
					sIpConnectionType = KNXNetworkLinkIP.ROUTING;
					if (StringUtils.isBlank(sIp)) {
						sIp = DEFAULT_MULTICAST_IP;
					}
				}
				else {
					throw new ConfigurationException("type", "unknown IP connection type '" + connectionTypeString + "'! Known types are either 'TUNNEL' or 'ROUTER'");
				}
			} else {
				sIpConnectionType = KNXNetworkLinkIP.TUNNELING;
			}

			String portConfig = (String) config.get("port");
			if (StringUtils.isNotBlank(portConfig)) {
				sPort = Integer.parseInt(portConfig);
			} else {
				sPort = KNXnetIPConnection.DEFAULT_PORT;
			}

			sLocalIp = (String) config.get("localIp");

			sSerialPort = (String) config.get("serialPort");

			String readingPauseString = (String) config.get("pause");
			if (StringUtils.isNotBlank(readingPauseString)) {
				sReadingPause = Long.parseLong(readingPauseString);
			}
			
			String responseTimeoutString = (String) config.get("timeout");
			if (StringUtils.isNotBlank(responseTimeoutString)) {
				long timeout = Long.parseLong(responseTimeoutString);
				if (timeout > 0) {
					sResponseTimeout = timeout;
				}
			}
			
			String readRetriesLimitString = (String) config.get("readRetries");
			if (StringUtils.isNotBlank(readRetriesLimitString)) {
				int readRetries = Integer.parseInt(readRetriesLimitString);
				if (readRetries > 0) {
					sReadRetriesLimit = readRetries;
				}
			}
			
			String autoReconnectPeriodString = (String) config.get("autoReconnectPeriod");
			if (StringUtils.isNotBlank(autoReconnectPeriodString)) {
				int autoReconnectPeriodValue = Integer.parseInt(autoReconnectPeriodString);
				if (autoReconnectPeriodValue >= 0) {
					sAutoReconnectPeriod = autoReconnectPeriodValue;
				}
			}

			
			if(sPC==null) {
				if (!connect()) {
					sLogger.warn("Inital connection to KNX bus failed!");
					if(sAutoReconnectPeriod>0) {
						sLogger.info("KNX link will be retried in {} seconds", sAutoReconnectPeriod);
						final Timer timer = new Timer();
						TimerTask timerTask = new TimerTask() {
							@Override
							public void run() {
								if(sShutdown) {
									timer.cancel();
								}
								else {
									sLogger.info("Trying to reconnect to KNX...");
									connect();
									if( sLink!=null && sLink.isOpen()) {
										sLogger.info("Connected to KNX");
										timer.cancel();
									}
									else {
										sLogger.info("KNX link will be retried in {} seconds", sAutoReconnectPeriod);
									}
								}
							}
						};
						timer.schedule(timerTask, sAutoReconnectPeriod * 1000, sAutoReconnectPeriod * 1000);
					}
				}
			}
		}
		else {
			sLogger.info("KNXBinding configuration is not present. Please check your configuration file or if not needed remove the KNX addon.");
		}
	}

	public static long getReadingPause() {
		return sReadingPause;
	}
	
	public static int getReadRetriesLimit() {
		return sReadRetriesLimit;
	}
	
	public static int getAutoReconnectPeriod() {
		return sAutoReconnectPeriod;
	}
	
	
}
