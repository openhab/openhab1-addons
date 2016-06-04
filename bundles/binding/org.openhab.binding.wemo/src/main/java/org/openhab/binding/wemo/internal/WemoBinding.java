/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wemo.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.wemo.WemoBindingProvider;
import org.openhab.binding.wemo.internal.WemoGenericBindingProvider.WemoChannelType;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.net.http.HttpUtil;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This binding allows you to switch your Belkin WeMo devices On or Off, shows
 * energy measurement of Insight Switches and refreshs itemState by polling
 * every 30 Seconds. The Binding does a discovery at startup to find all your
 * WeMo Devices in your installations and stores their UDN and location
 * (IP-Address) in a internal map. If location of a found device changes due to
 * a dhcp lease renewal, rediscovery is started to find the new location.
 *
 * @author Hans-J�rg Merk
 * @since 1.6.0
 */
public class WemoBinding extends AbstractActiveBinding<WemoBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(WemoBinding.class);
	// wemoConfigMap stores the values wemoFriendlyName and their according
	// location (IP-Address:Port) found during
	// discovery.
	protected Map<String, String> wemoConfigMap = new HashMap<String, String>();

	// wemoSubMap stores the event subscription UUIDs for a given itemName
	protected Map<String, String> wemoSubMap = new HashMap<String, String>();

	// wemoReversedSubMap stores the item Names for given UPNP subscription
	// UUIDs
	protected Map<String, String> wemoReversedSubMap = new HashMap<String, String>();

	private static String getInsightParamsXML;
	private static String setRequestXML;
	private EventListener listener = null;

	static {
		try {
			getInsightParamsXML = IOUtils.toString(
					WemoBinding.class.getResourceAsStream("/org/openhab/binding/wemo/internal/GetInsightParams.xml"));
			setRequestXML = IOUtils.toString(
					WemoBinding.class.getResourceAsStream("/org/openhab/binding/wemo/internal/SetRequest.xml"));
		} catch (Exception e) {
			LoggerFactory.getLogger(WemoBinding.class).error("Cannot read XML files!", e);
		}
	}

	/**
	 * the refresh interval which is used to renew the subscriptions to the UPNP
	 * devices
	 */
	private long refreshInterval = 600000;

	/**
	 * the listening port for incoming UPNP notifications
	 */
	private int eventListenPort = 35210;

	public InetAddress address;
	private String localIp;

	@Override
	public void activate() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface ni = interfaces.nextElement();
				if (!ni.isLoopback() && !ni.isPointToPoint() && ni.isUp()) {
					Enumeration<InetAddress> addresses = ni.getInetAddresses();
					while (addresses.hasMoreElements()) {
						localIp = addresses.nextElement().getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			logger.warn("Can't retrieve local IP Address!", e);
		}

		logger.debug("Local IP Found: {}", localIp);

		// Also opens the listening Socket that will be used for UPNP
		// notification events
		startEventListening();

		// Start device discovery, each time the binding starts.
		wemoDiscovery();
	}

	@Override
	public void deactivate() {
		stopEventListening();
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
				logger.debug("Wemo item '{}' subscription will be updated", itemName);

				try {
					if (provider.getUDN(itemName).toLowerCase().contains("insight")) {
						String insightParams = getInsightParams(itemName);

						if (insightParams != null) {

							String[] splitInsightParams = insightParams.split("\\|");

							if (splitInsightParams[0] != null) {

								if (provider.getChannelType(itemName).equals(WemoChannelType.state)) {
									OnOffType binaryState = null;
									binaryState = splitInsightParams[0].equals("0") ? OnOffType.OFF : OnOffType.ON;
									if (binaryState != null) {
										logger.trace("New InsightParam binaryState '{}' for device '{}' received",
												binaryState, itemName);
										eventPublisher.postUpdate(itemName, binaryState);
									}
								}

								if (provider.getChannelType(itemName).equals(WemoChannelType.lastChangedAt)) {
									long lastChangedAt = 0;
									try {
										lastChangedAt = Long.parseLong(splitInsightParams[1]) * 1000; // convert
																										// s
																										// to
																										// ms
																										// s
																										// to
																										// ms
									} catch (NumberFormatException e) {
										logger.error(
												"Unable to parse lastChangedAt value '{}' for device '{}'; expected long",
												splitInsightParams[1], itemName);
									}
									GregorianCalendar cal = new GregorianCalendar();
									cal.setTimeInMillis(lastChangedAt);
									State lastChangedAtState = new DateTimeType(cal);
									if (lastChangedAt != 0) {
										logger.trace("New InsightParam lastChangedAt '{}' for device '{}' received",
												lastChangedAtState, itemName);
										eventPublisher.postUpdate(itemName, lastChangedAtState);
									}
								}

								if (provider.getChannelType(itemName).equals(WemoChannelType.lastOnFor)) {
									State lastOnFor = DecimalType.valueOf(splitInsightParams[2]);
									if (lastOnFor != null) {
										logger.trace("New InsightParam lastOnFor '{}' for device '{}' received",
												lastOnFor, itemName);
										eventPublisher.postUpdate(itemName, lastOnFor);
									}
								}

								if (provider.getChannelType(itemName).equals(WemoChannelType.onToday)) {
									State onToday = DecimalType.valueOf(splitInsightParams[3]);
									if (onToday != null) {
										logger.trace("New InsightParam onToday '{}' for device '{}' received", onToday,
												itemName);
										eventPublisher.postUpdate(itemName, onToday);
									}
								}

								if (provider.getChannelType(itemName).equals(WemoChannelType.onTotal)) {
									State onTotal = DecimalType.valueOf(splitInsightParams[4]);
									if (onTotal != null) {
										logger.trace("New InsightParam onTotal '{}' for device '{}' received", onTotal,
												itemName);
										eventPublisher.postUpdate(itemName, onTotal);
									}
								}

								if (provider.getChannelType(itemName).equals(WemoChannelType.timespan)) {
									State timespan = DecimalType.valueOf(splitInsightParams[5]);
									if (timespan != null) {
										logger.trace("New InsightParam timespan '{}' for device '{}' received",
												timespan, itemName);
										eventPublisher.postUpdate(itemName, timespan);
									}
								}

								if (provider.getChannelType(itemName).equals(WemoChannelType.averagePower)) {
									State averagePower = DecimalType.valueOf(splitInsightParams[6]); // natively
																										// given
																										// in
																										// W
									if (averagePower != null) {
										logger.trace("New InsightParam averagePower '{}' for device '{}' received",
												averagePower, itemName);
										eventPublisher.postUpdate(itemName, averagePower);
									}
								}

								if (provider.getChannelType(itemName).equals(WemoChannelType.currentPower)) {
									BigDecimal currentMW = new BigDecimal(splitInsightParams[7]);
									State currentPower = new DecimalType(
											currentMW.divide(new BigDecimal(1000), RoundingMode.HALF_UP)); // recalculate
									// mW to W
									if (currentPower != null) {
										logger.trace("New InsightParam currentPower '{}' for device '{}' received",
												currentPower, itemName);
										eventPublisher.postUpdate(itemName, currentPower);
									}
								}

								if (provider.getChannelType(itemName).equals(WemoChannelType.energyToday)) {
									BigDecimal energyTodayMWMin = new BigDecimal(splitInsightParams[8]);
									// recalculate mW-mins to Wh
									State energyToday = new DecimalType(
											energyTodayMWMin.divide(new BigDecimal(60000), RoundingMode.HALF_UP));
									if (energyToday != null) {
										logger.trace("New InsightParam energyToday '{}' for device '{}' received",
												energyToday, itemName);
										eventPublisher.postUpdate(itemName, energyToday);
									}
								}

								if (provider.getChannelType(itemName).equals(WemoChannelType.energyTotal)) {
									BigDecimal energyTotalMWMin = new BigDecimal(splitInsightParams[9]);
									// recalculate mW-mins to Wh
									State energyTotal = new DecimalType(
											energyTotalMWMin.divide(new BigDecimal(60000), RoundingMode.HALF_UP));
									if (energyTotal != null) {
										logger.trace("New InsightParam energyTotal '{}' for device '{}' received",
												energyTotal, itemName);
										eventPublisher.postUpdate(itemName, energyTotal);
									}
								}

								if (provider.getChannelType(itemName).equals(WemoChannelType.standbyLimit)) {
									BigDecimal standbyLimitMW = new BigDecimal(splitInsightParams[10]);
									// recalculate mW to W
									State standbyLimit = new DecimalType(
											standbyLimitMW.divide(new BigDecimal(1000), RoundingMode.HALF_UP));
									if (standbyLimit != null) {
										logger.trace("New InsightParam standbyLimit '{}' for device '{}' received",
												standbyLimit, itemName);
										eventPublisher.postUpdate(itemName, standbyLimit);
									}
								}
							}
						}

					} else {
						if (wemoSubMap.get(itemName) != null) {
							// Unsubscribe (we are not making a subscription
							// renewal, because if the device
							// power was lost in between it will ensure that we
							// get a good and valid subscription in any case.
							unsubscribe(itemName);
						}
						// Subscribe
						subscribe(itemName);
					}

				} catch (Exception e) {
					logger.error("Error in execute method: " + e.getMessage(), e);
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
			try {
				String udn = provider.getUDN(itemName);
				logger.trace("item '{}' has UDN '{}'", itemName, udn);
				logger.trace("Command '{}' is about to be sent to item '{}'", command, itemName);
				sendCommand(itemName, command);

			} catch (Exception e) {
				logger.error("Failed to send {} command", command, e);
			}
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
			int TIMEOUT = 1000;

			// Send to 239.255.255.250:1900
			InetSocketAddress dstAddress = new InetSocketAddress(InetAddress.getByName(SSDP_IP), SSDP_PORT);

			// Request-Packet-Constructor
			StringBuffer discoveryMessage = new StringBuffer();
			discoveryMessage.append("M-SEARCH * HTTP/1.1\r\n");
			discoveryMessage.append("HOST: " + SSDP_IP + ":" + SSDP_PORT + "\r\n");
			discoveryMessage.append("MAN: \"ssdp:discover\"\r\n");
			discoveryMessage.append("MX: 5\r\n");
			discoveryMessage.append("ST: urn:Belkin:service:basicevent:1\r\n");
			discoveryMessage.append("\r\n");
			logger.trace("Request: {}", discoveryMessage.toString());
			byte[] discoveryMessageBytes = discoveryMessage.toString().getBytes();
			DatagramPacket discoveryPacket = new DatagramPacket(discoveryMessageBytes, discoveryMessageBytes.length,
					dstAddress);

			// Response-Listener
			MulticastSocket wemoReceiveSocket = null;
			DatagramPacket receivePacket = null;
			try {
				wemoReceiveSocket = new MulticastSocket(SSDP_SEARCH_PORT);
				wemoReceiveSocket.setTimeToLive(10);
				wemoReceiveSocket.setSoTimeout(TIMEOUT);
				logger.debug("Send datagram packet.");
				wemoReceiveSocket.send(discoveryPacket);

				while (true) {
					try {
						logger.debug("Receive SSDP Message.");
						receivePacket = new DatagramPacket(new byte[2048], 2048);
						wemoReceiveSocket.receive(receivePacket);
						final String message = new String(receivePacket.getData());
						if (message.contains("Belkin")) {
							logger.trace("Received message: {}", message);
						}

						new Thread(new Runnable() {
							@Override
							public void run() {
								if (message != null) {
									String location = StringUtils.substringBetween(message, "LOCATION: ", "/setup.xml");
									String udn = StringUtils.substringBetween(message, "USN: uuid:", "::urn:Belkin");
									if (udn != null) {
										logger.trace("Save location '{}' for WeMo device with UDN '{}'", location, udn);
										wemoConfigMap.put(udn, location);
										logger.info("Wemo Device with UDN '{}' discovered", udn);
									}
								}
							}
						}).start();

					} catch (SocketTimeoutException e) {
						logger.debug("Message receive timed out.");
						for (String name : wemoConfigMap.keySet()) {
							logger.trace(name + ":" + wemoConfigMap.get(name));
						}
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
		logger.trace("command '{}' transformed to '{}'", command, onOff);
		String wemoCallResponse = wemoCall(itemName, "urn:Belkin:service:basicevent:1#SetBinaryState",
				setRequestXML.replace("{{state}}", onOff ? "1" : "0"));

		logger.trace("setOn ={}", wemoCallResponse);
	}

	private void unsubscribe(String itemName) {
		final String endpoint = "/upnp/event/basicevent1";

		logger.trace("Unubscribing {}", itemName);
		for (WemoBindingProvider provider : providers) {
			String wemoUDN = provider.getUDN(itemName);
			if (wemoUDN == null) {
				logger.warn("Unknown {} Wemo item!", itemName);
				return;
			}
			String subId = wemoSubMap.get(itemName);
			if (subId == null) {
				logger.trace("Item {} doesn't have subscription!", itemName);
				return;
			}
			logger.trace("Unsubscribing WeMo item '{}' with configuration :", itemName);
			logger.trace("        			UDN = '{}'", provider.getUDN(itemName));
			logger.trace("			ChannelType = '{}'", provider.getChannelType(itemName));

			String wemoLocation = wemoConfigMap.get(wemoUDN);
			if (wemoLocation != null) {
				logger.trace("    Location = '{}'", wemoLocation);

				Properties wemoHeaders = new Properties();
				wemoHeaders.setProperty("SID", "uuid:" + subId);

				String wemoResponse = upnpCall("UNSUBSCRIBE", wemoLocation, endpoint, wemoHeaders);
				if (wemoResponse.contains("HTTP/1.1 200 OK")) {
					wemoSubMap.remove(itemName);
					wemoReversedSubMap.remove(subId);
				}
			} else {
				logger.debug("No Location found for item '{}', start new discovery ", itemName);
				wemoDiscovery();
			}
		}
	}

	private void subscribe(String itemName) {
		try {
			final String endpoint = "/upnp/event/basicevent1";

			logger.trace("Subscribing {}", itemName);
			for (WemoBindingProvider provider : providers) {
				String wemoUDN = provider.getUDN(itemName);
				if (wemoUDN == null) {
					logger.warn("Unknown {} Wemo item!", itemName);
					return;
				}

				String callbackAddr = "http://" + localIp + ":" + eventListenPort + "/";
				logger.trace("Subscribing WeMo item '{}' with configuration :", itemName);
				logger.trace("       			UDN = '{}'", provider.getUDN(itemName));
				logger.trace("			ChannelType = '{}'", provider.getChannelType(itemName));
				logger.debug("	       CallbackAddr = '{}'", callbackAddr);

				String wemoLocation = wemoConfigMap.get(wemoUDN);
				if (wemoLocation != null) {
					logger.trace("    Location = '{}'", wemoLocation);
					Properties wemoHeaders = new Properties();
					wemoHeaders.setProperty("NT", "upnp:event");
					wemoHeaders.setProperty("CALLBACK", "<" + callbackAddr + ">");
					wemoHeaders.setProperty("TIMEOUT", "Second-" + refreshInterval);

					String wemoCallResponse = upnpCall("SUBSCRIBE", wemoLocation, endpoint, wemoHeaders);

					String subId = StringUtils.substringBetween(wemoCallResponse, "uuid:", "\r");
					if (subId != null) {
						logger.debug("Storing subscription id: {} for itemName: {}", subId, itemName);
						wemoSubMap.put(itemName, subId);
						wemoReversedSubMap.put(subId, itemName);
					}
				} else {
					logger.debug("No Location found for item '{}', start new discovery ", itemName);
					wemoDiscovery();
				}
			}
		} catch (Exception e) {
			logger.warn("Exception occured when subscribing " + itemName, e);
		}
	}

	private String upnpCall(String method, String location, String endpoint, Properties headers) {
		try {
			URL url;
			try {
				url = new URL(location);
			} catch (Exception e) {
				logger.warn("Can't parse URL Format for location: {}", location);
				return null;
			}
			headers.setProperty("HOST", url.getHost() + ":" + url.getPort());
			try (Socket s = new Socket(url.getHost(), url.getPort());
					InputStream is = s.getInputStream();
					OutputStream os = s.getOutputStream()) {
				StringBuilder contentSB = new StringBuilder(method).append(" ").append(endpoint)
						.append(" HTTP/1.1\r\n");
				for (Object o : headers.keySet()) {
					String key = o.toString();
					contentSB.append(key).append(": ").append(headers.getProperty(key)).append("\r\n");
				}

				contentSB.append("\r\n");
				byte[] buffer = contentSB.toString().getBytes("UTF-8");
				os.write(buffer);

				StringBuilder data = new StringBuilder();
				buffer = new byte[2048];
				int red;
				while ((red = is.read(buffer)) > -1) {
					data.append(new String(buffer, 0, red));
				}
				String wemoCallResponse = data.toString();
				logger.debug("wemoresp to {}: {}", method, wemoCallResponse);
				return wemoCallResponse;
			} catch (BindException e) {
				logger.warn("Can't bind socket to address for location: " + location, e);
				return null;
			}
		} catch (Exception e) {
			wemoDiscovery();
			throw new RuntimeException("Could not call Wemo, did rediscovery", e);
		}
	}

	private String wemoCall(String itemName, String soapMethod, String content) {
		try {
			for (WemoBindingProvider provider : providers) {

				String soapHeader = "SOAPACTION: \"" + soapMethod + "\"";
				String contentHeader = "Content-Type: text/xml; charset=\"utf-8\"";
				String endpoint = "/upnp/control/basicevent1";

				if (soapMethod.contains("insight")) {
					endpoint = "/upnp/control/insight1";
				}

				String wemoUDN = provider.getUDN(itemName);
				if (wemoUDN == null) {
					return null;
				}
				logger.trace("Calling WeMo item '{}' with configuration :", itemName);
				logger.trace("        UDN = '{}'", provider.getUDN(itemName));
				logger.trace("ChannelType = '{}'", provider.getChannelType(itemName));

				String wemoLocation = wemoConfigMap.get(wemoUDN);
				if (wemoLocation != null) {
					logger.trace("    Location = '{}'", wemoLocation);
					logger.trace("    EndPoint = '{}'", endpoint);

					String wemoURL = wemoLocation + endpoint;

					Properties wemoHeaders = new Properties();
					wemoHeaders.setProperty(soapHeader, contentHeader);

					InputStream wemoContent = new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));

					String wemoCallResponse = HttpUtil.executeUrl("POST", wemoURL, wemoHeaders, wemoContent, "text/xml",
							2000);
					logger.trace("wemoresp: {}", wemoCallResponse);
					return wemoCallResponse;

				} else {
					logger.debug("No Location found for item '{}', start new discovery ", itemName);
					wemoDiscovery();
					String wemoCallResponse = "";
					return wemoCallResponse;
				}
			}
		} catch (Exception e) {
			wemoDiscovery();
			throw new RuntimeException("Could not call Wemo, did rediscovery", e);
		}
		return null;
	}

	private String getInsightParams(String itemName) {
		String insightParamsRequest = null;
		String returnInsightParams = null;

		try {
			insightParamsRequest = wemoCall(itemName, "urn:Belkin:service:insight:1#GetInsightParams",
					getInsightParamsXML);
			if (insightParamsRequest != null) {
				logger.trace("insightParamsRequestResponse :");
				logger.trace("{}", insightParamsRequest);

				returnInsightParams = StringUtils.substringBetween(insightParamsRequest, "<InsightParams>",
						"</InsightParams>");
				logger.debug("New raw InsightParams '{}' for device '{}' received", returnInsightParams, itemName);
				return returnInsightParams;
			}
		} catch (Exception e) {
			logger.error("Failed to get InsightParams for device '{}'", itemName, e);
		}
		return null;
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
			String eventListenPortString = (String) config.get("eventListenPort");
			if (StringUtils.isNotBlank(eventListenPortString)) {
				int oldListenPort = eventListenPort;
				eventListenPort = Integer.parseInt(eventListenPortString);
				if (oldListenPort != eventListenPort) {
					startEventListening();
				}
			}
		}
	}

	private void startEventListening() {
		stopEventListening();

		listener = new EventListener();
		listener.start();
	}

	private void stopEventListening() {
		if (listener != null) {
			listener.shutdown();
			try {
				listener.join();
			} catch (InterruptedException e) {
				logger.trace("Stop Event interrupted!", e);
			}
			listener = null;
		}
	}

	private class EventListener extends Thread {
		private boolean shutdown = false;

		@Override
		public void run() {
			logger.debug("Started Listening for Wemo events...");
			setName("Wemo events listening thread");
			int TIMEOUT = 1000;

			try (ServerSocket notificationSocket = new ServerSocket(eventListenPort)) {
				notificationSocket.setSoTimeout(TIMEOUT);

				while (!shutdown) {
					try {
						final Socket s = notificationSocket.accept();
						new Thread() {

							@Override
							public void run() {
								logger.debug("Received Wemo event, processing...");
								setName("Wemo event handler");
								// We need to set a timeout here, otherwise we
								// need to completely process the HTTP headers,
								// look for the content length, end of header,
								// and count the number of bytes received to
								// determine the end of reception and starts of
								// our send window. Just use timeout :D
								try {
									s.setSoTimeout(100);
								} catch (SocketException se) {
									logger.warn("When setting socket timeout!", se);
								}
								try (OutputStream os = s.getOutputStream(); InputStream is = s.getInputStream()) {
									byte[] buffer = new byte[2048];
									int red;
									StringBuilder msg = new StringBuilder();
									try {
										while ((red = is.read(buffer)) > -1) {
											msg.append(new String(buffer, 0, red));
										}
									} catch (SocketTimeoutException ste) {
										// Normal, see comments above
									}
									String message = msg.toString();
									logger.debug("Full received msg: {}", message);

									String uuid = StringUtils.substringBetween(message, "uuid:", "\r");
									String binaryState = StringUtils.substringBetween(message, "<BinaryState>",
											"</BinaryState>");

									if (binaryState != null && uuid != null) {
										logger.debug("Received binary state changed event for uuid: {}, state {}", uuid,
												binaryState);

										// Find the itemName of that uuid
										String itemName = wemoReversedSubMap.get(uuid);

										if (itemName == null) {
											logger.warn("Wemo Item name not found for UPNP event subscription UUID: {}",
													uuid);
										} else {
											for (WemoBindingProvider provider : providers) {
												if (provider.getUDN(itemName).toLowerCase().contains("motion")) {
													org.openhab.core.types.State newState = binaryState.equals("0")
															? OpenClosedType.OPEN : OpenClosedType.CLOSED;
													eventPublisher.postUpdate(itemName, newState);
												} else {
													org.openhab.core.types.State itemState = binaryState.equals("0")
															? OnOffType.OFF : OnOffType.ON;
													eventPublisher.postUpdate(itemName, itemState);
												}
											}
										}

										os.write(
												("HTTP/1.1 200 OK\r\nContent-Type: text/plain; charset=utf-8\r\nContent-Length: 2\r\nConnection: keep-alive\r\n\r\nOK")
														.getBytes());
										os.flush();
									}
									s.close();
								} catch (Exception e) {
									logger.warn("While processing Wemo event!", e);
								}
							}
						}.start();
					} catch (SocketTimeoutException te) {
						// This is occurring normally, do nothing here, but this
						// is useful to check for shutdown status,
						// and do the shutdown if need be.
					}
				}
				logger.debug("Stopped Listening for Wemo events.");
			} catch (Exception e) {
				logger.warn("Wemo event listening thread crashed!", e);
			}
		}

		public void shutdown() {
			shutdown = true;
		}
	}
}
