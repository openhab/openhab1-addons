/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wemo.internal;


import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import org.openhab.binding.wemo.WemoBindingProvider;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.net.http.HttpUtil;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This binding allows you to switch your Belkin Wemo Switches on or Off
 * and refreshs itemState by polling every 60 Seconds.
 * The Binding does a discovery at startup to find all your Wemo switches in your installations and stores their
 * friendlyNames and location (IP-Address) in a internal map.
 * If location of a found device changes due to a dhcp lease renewal, rediscovery is started to find new location.
 * 
 * @author Hans-Jörg Merk
 * @since 1.6.0
 */
public class WemoBinding extends AbstractActiveBinding<WemoBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(WemoBinding.class);

	// wemoConfigMap stores the values wemoFriendlyName and their according location (IP-Address:Port) found during discovery.
		protected Map<String, String> wemoConfigMap = new HashMap<String, String>();
	
	
	/** 
	 * the refresh interval which is used to poll values from the Wemo-Devices
	 */
	private long refreshInterval = 60000;

	public InetAddress address;
	
	public void activate() {
		//Start device discovery, each time the binding start.
		wemoDiscovery();
	}
	
	public void deactivate() {
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Wemo Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		logger.debug("execute() method is called!");
		
		for (WemoBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				logger.debug("Wemo switch '{}' state will be updated", itemName);

				try {
					String resp = wemoCall(itemName,
							"urn:Belkin:service:basicevent:1#GetBinaryState",
							IOUtils.toString(getClass().getResourceAsStream(
									"GetRequest.xml")));

					String state = resp.replaceAll(
							"[\\d\\D]*<BinaryState>(.*)</BinaryState>[\\d\\D]*", "$1");

					State itemState = state.equals("0") ? OnOffType.OFF : OnOffType.ON;
					eventPublisher.postUpdate(itemName,  itemState);

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand() is called!");

		for (WemoBindingProvider provider : providers) {
			String switchFriendlyName = provider.getWemoFriendlyName(itemName);
		    logger.debug("item '{}' is configured as '{}'",itemName, switchFriendlyName);
			}
		try {
			logger.debug("Command '{}' is about to be send to item '{}'",command, itemName );
			sendCommand(itemName, command);
			
		} catch (Exception e) {
			logger.error("Failed to send {} command", command, e);
		}
	}			


	public void wemoDiscovery() {
		logger.debug("wemoDiscovery() is called!");
		try {
			final int SSDP_PORT = 1900;
			final int SSDP_SEARCH_PORT = 1901;

			// Broadcast address
			final String SSDP_IP = "239.255.255.250";

			// Connection timeout
			int TIMEOUT = 5000;

			// Send from localhost:1901
			InetAddress localhost = InetAddress.getLocalHost();
			InetSocketAddress srcAddress = new InetSocketAddress(localhost,	SSDP_SEARCH_PORT);
			
			// Send to 239.255.255.250:1900
			InetSocketAddress dstAddress = new InetSocketAddress(InetAddress.getByName(SSDP_IP), SSDP_PORT);

			// Request-Packet-Constructor
			StringBuffer discoveryMessage = new StringBuffer();
			discoveryMessage.append("M-SEARCH * HTTP/1.1\r\n");
			discoveryMessage.append("HOST: " + SSDP_IP + ":" + SSDP_PORT + "\r\n");
			discoveryMessage.append("ST: urn:Belkin:device:\r\n");
			discoveryMessage.append("MAN: \"ssdp:discover\"\r\n");
			discoveryMessage.append("MX: 5\r\n");
			discoveryMessage.append("\r\n");
		    logger.debug("Request: {}", discoveryMessage.toString());
			byte[] discoveryMessageBytes = discoveryMessage.toString().getBytes();
			DatagramPacket discoveryPacket = new DatagramPacket(
					discoveryMessageBytes, discoveryMessageBytes.length, dstAddress);

			// Send multi-cast packet
			MulticastSocket multicast = null;
			try {
				multicast = new MulticastSocket(null);
				multicast.bind(srcAddress);
				logger.debug("Source-Address = '{}'", srcAddress);
				multicast.setTimeToLive(4);
				logger.debug("Send multicast request.");
				multicast.send(discoveryPacket);
			} finally {
				logger.debug("Multicast ends. Close connection.");
				multicast.disconnect();
				multicast.close();
			}

			// Response-Listener
			DatagramSocket wemoReceiveSocket = null;
			DatagramPacket receivePacket = null;
			try {
				wemoReceiveSocket = new DatagramSocket(SSDP_SEARCH_PORT);
				wemoReceiveSocket.setSoTimeout(TIMEOUT);
				logger.debug("Send datagram packet.");
				wemoReceiveSocket.send(discoveryPacket);

				while (true) {
					try {
						logger.debug("Receive SSDP Message.");
						receivePacket = new DatagramPacket(new byte[1536], 1536);
						wemoReceiveSocket.receive(receivePacket);
						final String message = new String(receivePacket.getData());
						logger.debug("Recieved message: {}", message);
				
						new Thread(new Runnable() {
							@Override
							public void run() {
								String location = StringUtils.substringBetween(message, "LOCATION: ", "/setup.xml");
								if (location != null) {
								
									logger.debug("Wemo device found at URL '{}'", location);
								
									try {
										int timeout = 5000;
										String friendlyNameResponse = HttpUtil.executeUrl("GET", location+"/setup.xml", timeout);
										String friendlyName = StringUtils.substringBetween(friendlyNameResponse, "<friendlyName>", "</friendlyName>");
										logger.info("Wemo device '{}' found at '{}'", friendlyName, location);
										wemoConfigMap.put(friendlyName, location);
									
										} catch (Exception te) {
											logger.error("Could not find wemo friendlyName ", te);
										}
								}
							}
						}).start();

					} catch (SocketTimeoutException e) {
						logger.debug("Message receive timed out.");
						break;
					}
				}
			} finally {
				if (wemoReceiveSocket != null) {
					wemoReceiveSocket.disconnect();
					wemoReceiveSocket.close();
				}
			}
			
		} catch (Exception e) {
			logger.error("Could not start wemo device discovery", e);
		}
		
	}
	public void sendCommand(String itemName, Command command) throws IOException {
		
		boolean onOff = OnOffType.ON.equals(command);
		logger.debug("command '{}' transformed to '{}'", command, onOff); 
		String wemoCallResponse = wemoCall(itemName,
				"urn:Belkin:service:basicevent:1#SetBinaryState",
				IOUtils.toString(
						getClass().getResourceAsStream("SetRequest.xml"))
						.replace("{{state}}", onOff ? "1" : "0"));

		logger.debug("setOn ={}", wemoCallResponse);
	}

	private String wemoCall(String itemName, String soapMethod, String content) {
		try {
			
			String endpoint = "/upnp/control/basicevent1";
			String switchFriendlyName = null;
			
			for (WemoBindingProvider provider : providers) {
				switchFriendlyName = provider.getWemoFriendlyName(itemName);
				}

			String wemoLocation = wemoConfigMap.get(switchFriendlyName);
			if (wemoLocation != null) {
				logger.debug("item '{}' is located at '{}'", itemName, wemoLocation);
				URL url = new URL(wemoLocation + endpoint);
				Socket wemoSocket = new Socket(InetAddress.getByName(url.getHost()), url.getPort());
				try {
					OutputStream wemoOutputStream = wemoSocket.getOutputStream();
					StringBuffer wemoStringBuffer = new StringBuffer();
					wemoStringBuffer.append("POST " + url + " HTTP/1.1\r\n");
					wemoStringBuffer.append("Content-Type: text/xml; charset=utf-8\r\n");
					wemoStringBuffer.append("Content-Length: " + content.getBytes().length + "\r\n");
					wemoStringBuffer.append("SOAPACTION: \"" + soapMethod + "\"\r\n");
					wemoStringBuffer.append("\r\n");
					wemoOutputStream.write(wemoStringBuffer.toString().getBytes());
					wemoOutputStream.write(content.getBytes());
					wemoOutputStream.flush();
					String wemoCallResponse = IOUtils.toString(wemoSocket.getInputStream());
					return wemoCallResponse;
				} finally {
					wemoSocket.close();
					}
			} else {
				logger.debug("No Location found for item '{}', start new discovery ", itemName);
				wemoDiscovery();
				String wemoCallResponse = "";
				return wemoCallResponse;
			}
		} catch (Exception e) {
			wemoDiscovery();
			throw new RuntimeException("Could not call Wemo, did rediscovery", e);
		}
	}
		
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		setProperlyConfigured(true);		
		if (config != null) {
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

		}
	}

}
