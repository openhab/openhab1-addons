/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wemo.internal;


import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.wemo.WemoBindingProvider;
import org.openhab.binding.wemo.internal.WemoGenericBindingProvider.WemoChannelType;
import org.apache.commons.lang.StringUtils;
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
 * This binding allows you to switch your Belkin WeMo devices On or Off, shows energy measurement of Insight Switches
 * and refreshs itemState by polling every 30 Seconds.
 * The Binding does a discovery at startup to find all your WeMo Devices in your installations and stores their
 * UDN and location (IP-Address) in a internal map.
 * If location of a found device changes due to a dhcp lease renewal, rediscovery is started to find the new location.
 * 
 * @author Hans-Jörg Merk
 * @since 1.6.0
 */
public class WemoBinding extends AbstractActiveBinding<WemoBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(WemoBinding.class);

	// wemoConfigMap stores the values wemoFriendlyName and their according location (IP-Address:Port) found during discovery.
	protected Map<String, String> wemoConfigMap = new HashMap<String, String>();
	
	
    private static String getInsightParamsXML;
    private static String getRequestXML;
    private static String setRequestXML;

    static {
        try {
            getInsightParamsXML = IOUtils.toString(WemoBinding.class
                    .getResourceAsStream("/org/openhab/binding/wemo/internal/GetInsightParams.xml"));
            getRequestXML = IOUtils.toString(WemoBinding.class
                    .getResourceAsStream("/org/openhab/binding/wemo/internal/GetRequest.xml"));
            setRequestXML = IOUtils.toString(WemoBinding.class
                    .getResourceAsStream("/org/openhab/binding/wemo/internal/SetRequest.xml"));
        } catch (Exception e) {
            LoggerFactory.getLogger(WemoBinding.class).error("Cannot read XML files!", e);
        }
    }
	/** 
	 * the refresh interval which is used to poll values from the WeMo-Devices
	 */
	private long refreshInterval = 60000;

	public InetAddress address;
	
	public void activate() {
		//Start device discovery, each time the binding starts.
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
				logger.debug("Wemo item '{}' state will be updated", itemName);

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
                                        eventPublisher.postUpdate(itemName,  binaryState);
                                    }
                            	}
                            	
                            	if (provider.getChannelType(itemName).equals(WemoChannelType.lastChangedAt)) {
                            		long lastChangedAt = 0;
                            		try {
                            			lastChangedAt = Long.parseLong(splitInsightParams[1]) * 1000; // convert s to ms
                            		} catch (NumberFormatException e) {
                            			logger.error("Unable to parse lastChangedAt value '{}' for device '{}'; expected long", 
                            					splitInsightParams[1], itemName);
                            		}
                                    GregorianCalendar cal = new GregorianCalendar();
                                    cal.setTimeInMillis(lastChangedAt);
                                    State lastChangedAtState = new DateTimeType(cal);
                                    if (lastChangedAt != 0) {	
                                        logger.trace("New InsightParam lastChangedAt '{}' for device '{}' received",
                                        		lastChangedAtState, itemName);
                                        eventPublisher.postUpdate(itemName,  lastChangedAtState);
                                    }
                            	}

                            	if (provider.getChannelType(itemName).equals(WemoChannelType.lastOnFor)) {
                                    State lastOnFor = DecimalType.valueOf(splitInsightParams[2]);
                                    if (lastOnFor != null) {	
                                        logger.trace("New InsightParam lastOnFor '{}' for device '{}' received",
                                        		lastOnFor, itemName);
                                        eventPublisher.postUpdate(itemName,  lastOnFor);
                                    }
                            	}
 
                            	if (provider.getChannelType(itemName).equals(WemoChannelType.onToday)) {
                                    State onToday = DecimalType.valueOf(splitInsightParams[3]);
                                    if (onToday != null) {	
                                        logger.trace("New InsightParam onToday '{}' for device '{}' received",
                                        		onToday, itemName);
                                        eventPublisher.postUpdate(itemName,  onToday);
                                    }
                            	}

                            	if (provider.getChannelType(itemName).equals(WemoChannelType.onTotal)) {
                                    State onTotal = DecimalType.valueOf(splitInsightParams[4]);
                                    if (onTotal != null) {	
                                        logger.trace("New InsightParam onTotal '{}' for device '{}' received",
                                        		onTotal, itemName);
                                        eventPublisher.postUpdate(itemName,  onTotal);
                                    }
                            	}
                            	
                            	if (provider.getChannelType(itemName).equals(WemoChannelType.timespan)) {
                                    State timespan = DecimalType.valueOf(splitInsightParams[5]);
                                    if (timespan != null) {	
                                        logger.trace("New InsightParam timespan '{}' for device '{}' received",
                                        		timespan, itemName);
                                        eventPublisher.postUpdate(itemName,  timespan);
                                    }
                            	}

                            	
                            	if (provider.getChannelType(itemName).equals(WemoChannelType.averagePower)) {
                                    State averagePower = DecimalType.valueOf(splitInsightParams[6]); // natively given in W
                                    if (averagePower != null) {	
                                        logger.trace("New InsightParam averagePower '{}' for device '{}' received",
                                        		averagePower, itemName);
                                        eventPublisher.postUpdate(itemName,  averagePower);
                                    }
                            	}


                            	if (provider.getChannelType(itemName).equals(WemoChannelType.currentPower)) {
                                    BigDecimal currentMW = new BigDecimal(splitInsightParams[7]);
                                    State currentPower = new DecimalType(currentMW.divide(new BigDecimal(1000), RoundingMode.HALF_UP)); // recalculate
                                                                                                                  // mW to W
                                    if (currentPower != null) {	
                                        logger.trace("New InsightParam currentPower '{}' for device '{}' received",
                                        		currentPower, itemName);
                                        eventPublisher.postUpdate(itemName,  currentPower);
                                    }
                            	}
                            	
                            	if (provider.getChannelType(itemName).equals(WemoChannelType.energyToday)) {
                                    BigDecimal energyTodayMWMin = new BigDecimal(splitInsightParams[8]);
                                    // recalculate mW-mins to Wh
                                    State energyToday = new DecimalType(energyTodayMWMin.divide(new BigDecimal(60000), RoundingMode.HALF_UP)); 
                                    if (energyToday != null) {	
                                        logger.trace("New InsightParam energyToday '{}' for device '{}' received",
                                        		energyToday, itemName);
                                        eventPublisher.postUpdate(itemName,  energyToday);
                                    }
                            	}
                            	
                            	if (provider.getChannelType(itemName).equals(WemoChannelType.energyTotal)) {
                                    BigDecimal energyTotalMWMin = new BigDecimal(splitInsightParams[9]);
                                    // recalculate mW-mins to Wh
                                    State energyTotal = new DecimalType(energyTotalMWMin.divide(new BigDecimal(60000), RoundingMode.HALF_UP));                                   
                                    if (energyTotal != null) {	
                                        logger.trace("New InsightParam energyTotal '{}' for device '{}' received",
                                        		energyTotal, itemName);
                                        eventPublisher.postUpdate(itemName,  energyTotal);
                                    }
                            	}
                            	
                            	if (provider.getChannelType(itemName).equals(WemoChannelType.standbyLimit)) {
                                    BigDecimal standbyLimitMW = new BigDecimal(splitInsightParams[10]);
                                    // recalculate mW to W
                                    State standbyLimit = new DecimalType(standbyLimitMW.divide(new BigDecimal(1000), RoundingMode.HALF_UP)); 
                                    if (standbyLimit != null) {  
                                        logger.trace("New InsightParam standbyLimit '{}' for device '{}' received",
                                                standbyLimit, itemName);
                                        eventPublisher.postUpdate(itemName,  standbyLimit);
                                    }
                                }
                            }
                        }

					} else {
                        String state = getWemoState(itemName);
                        
                        if (state != null) {
                        	if (provider.getUDN(itemName).toLowerCase().contains("motion")) {
                        		State newState = state.equals("0") ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
            					eventPublisher.postUpdate(itemName,  newState);
                        	} else {
            					State itemState = state.equals("0") ? OnOffType.OFF : OnOffType.ON;
            					eventPublisher.postUpdate(itemName,  itemState);                        		
                        	}
                        }
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
			    logger.trace("item '{}' has UDN '{}'",itemName, udn);
				logger.trace("Command '{}' is about to be send to item '{}'",command, itemName );
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

			// Send from localhost:1901
			InetAddress localhost = InetAddress.getLocalHost();
			InetSocketAddress srcAddress = new InetSocketAddress(localhost,	SSDP_SEARCH_PORT);
			
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
			DatagramPacket discoveryPacket = new DatagramPacket(
					discoveryMessageBytes, discoveryMessageBytes.length, dstAddress);

			// Send multi-cast packet
			MulticastSocket multicast = null;
			try {
				multicast = new MulticastSocket(null);
				multicast.bind(srcAddress);
				logger.trace("Source-Address = '{}'", srcAddress);
				multicast.setTimeToLive(5);
				logger.trace("Send multicast request.");
				multicast.send(discoveryPacket);
			} finally {
				logger.trace("Multicast ends. Close connection.");
				multicast.disconnect();
				multicast.close();
			}

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
						for (String name: wemoConfigMap.keySet()) {
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
				logger.trace("Calling WeMo item '{}' with configuration :", itemName );
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
					
					String wemoCallResponse = HttpUtil.executeUrl("POST", wemoURL, wemoHeaders, wemoContent, "text/xml", 2000);
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
		
    private String getWemoState(String itemName) {
        String stateRequest = null;
        String returnState = null;

        try {
            stateRequest = wemoCall(itemName, "urn:Belkin:service:basicevent:1#GetBinaryState", getRequestXML);
            if (stateRequest != null) {
                returnState = StringUtils.substringBetween(stateRequest, "<BinaryState>", "</BinaryState>");

                logger.debug("New binary state '{}' for item '{}' received", returnState, itemName);
            }
        } catch (Exception e) {
            logger.error("Failed to get binary state for item '{}'", itemName, e);
        }

        if (returnState != null) {
             return returnState;
        } else {
            return null;
        }
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

		}
	}

	
}
