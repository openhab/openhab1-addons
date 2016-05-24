/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.megadevice.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.megadevice.MegaDeviceBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Petr Shatsillo
 * @since 0.0.1
 */
public class MegaDeviceBinding extends AbstractActiveBinding<MegaDeviceBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(MegaDeviceBinding.class);
	private static EventPublisher ep;

	private boolean isSetPublisher;
	private boolean firstRun = true;

	private Map<String, Long> lastPollTime = new HashMap<String, Long>();

	private static Collection<MegaDeviceBindingProvider> megaproviders = new CopyOnWriteArraySet<MegaDeviceBindingProvider>();
	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	private BundleContext bundleContext;
	// private EventPublisher eventPublisher = null;

	/**
	 * the refresh interval which is used to poll values from the MegaDevice
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 1000;

	/**
	 * Called by the SCR to activate the component with its configuration read
	 * from CAS
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the
	 *            ConfigAdmin service
	 */

	private long delay = 1000;

	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;

		// the configuration is guaranteed not to be null, because the component
		// definition has the
		// configuration-policy set to require. If set to 'optional' then the
		// configuration may be null

		// to override the default refresh interval one has to add a
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			// refreshInterval = Long.parseLong(refreshIntervalString);
		}

		String portNumber = (String) configuration.get("httpserverport");
		int portnumber = 0;
		if (StringUtils.isNotBlank(portNumber)) {
			portnumber = Integer.parseInt(portNumber);
		}

		String Delay = (String) configuration.get("delay");
		if (StringUtils.isNotBlank(Delay)) {
			// delay = Long.parseLong(Delay);
		}
		setProperlyConfigured(true);
		if (portnumber > 0) {
			MegadeviceHttpServer.setPort(portnumber);
		}
		new MegadeviceHttpServer().start();

	}

	/**
	 * Called by the SCR when the configuration of a binding has been changed
	 * through the ConfigAdmin service.
	 * 
	 * @param configuration
	 *            Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		// update the internal configuration accordingly
	}

	/**
	 * Called by the SCR to deactivate the component when either the
	 * configuration is removed or mandatory references are no longer satisfied
	 * or the component has simply been stopped.
	 * 
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;

		MegadeviceHttpServer.setRunningState(false);

		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "MegaDevice Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {

		if (!isSetPublisher) {
			setEP();
		}
		setProviders();
		if (firstRun) {
			ScanPorts();
			firstRun = false;
		}
		ScanPortsScheduler();
	}

	private void ScanPortsScheduler() {

		// logger.debug("ScanPortsScheduler start...");

		String Result = "";
		for (MegaDeviceBindingProvider provider : providers) {

			for (String itemName : provider.getItemNames()) {

				int refreshInterval = provider.getPollInterval(itemName);

				// logger.debug("refreshInterval = " + refreshInterval);

				Long lastUpdateTimeStamp = lastPollTime.get(itemName);
				if (lastUpdateTimeStamp == null) {
					lastUpdateTimeStamp = 0L;
				}

				long diff = System.currentTimeMillis() - lastUpdateTimeStamp;
				boolean needsUpdate = diff >= (refreshInterval * 1000);

				if (refreshInterval == 0) {
					needsUpdate = false;
				}

				if (needsUpdate) {

					try {
						if (provider.getItemType(itemName).toString().contains("NumberItem")) {
							if (provider.getPORT(itemName).toString().contains("tget")) {
								Result = "http://" + provider.getIP(itemName) + "/" + provider.password(itemName)
										+ "/?tget=1";
							} else if ((provider.getPORT(itemName).toString().contains("t"))
									|| (provider.getPORT(itemName).toString().contains("h"))) {
								String[] PortParse = provider.getPORT(itemName).toString().split("[,]");

								Result = "http://" + provider.getIP(itemName) + "/" + provider.password(itemName)
										+ "/?pt=" + PortParse[0] + "&cmd=get";
							} else {

								Result = "http://" + provider.getIP(itemName) + "/" + provider.password(itemName)
										+ "/?pt=" + provider.getPORT(itemName) + "&cmd=get";
							}
						}else if (provider.getItemType(itemName).toString().contains("StringItem")) {
							if(provider.getPORT(itemName).toString().contains("r")){
								String[] PortParse = provider.getPORT(itemName).toString().split("[,]");

								Result = "http://" + provider.getIP(itemName) + "/" + provider.password(itemName)
										+ "/?pt=" + PortParse[0] + "&cmd=get";
							}
							
						}
						else {
							Result = "http://" + provider.getIP(itemName) + "/" + provider.password(itemName) + "/?pt="
									+ provider.getPORT(itemName) + "&cmd=get";
						}

						URL obj = new URL(Result);
						HttpURLConnection con = (HttpURLConnection) obj.openConnection();
						logger.debug(Result);

						// optional default is GET
						con.setRequestMethod("GET");
						con.setConnectTimeout(500);
						con.setReadTimeout(500);

						// add request header
						con.setRequestProperty("User-Agent", "Mozilla/5.0");

						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String inputLine;
						StringBuilder response = new StringBuilder();

						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						in.close();
						logger.debug("input string->" + response.toString());
						if (response.toString().contains("ON")) {
							ep.postUpdate(itemName, OnOffType.ON);
						} else if (response.toString().contains("OFF")) {
							ep.postUpdate(itemName, OnOffType.OFF);
						} else {
							if (provider.getItemType(itemName).toString().contains("DimmerItem")) {
								int percent = (int) Math.round(Integer.parseInt(response.toString()) / 2.55);
								ep.postUpdate(itemName, PercentType.valueOf(Integer.toString(percent)));
								logger.debug(itemName + " " + percent);
							} else if (provider.getItemType(itemName).toString().contains("NumberItem")) {

								if (provider.getPORT(itemName).toString().contains("dht")) {
									String[] PortParse = provider.getPORT(itemName).toString().split("[,]");
									for (int ind = 0; ind < PortParse.length; ind++) {
										logger.debug(PortParse[ind]);
									}
									if (PortParse[2].contains("t")) {

										String[] ResponseParse = response.toString().split("[:/]");
										for (int ind = 0; ind < ResponseParse.length; ind++) {
											logger.debug(ind + ": " + ResponseParse[ind]);
										}
										if (ResponseParse.length > 2) {
											if (ResponseParse[0].contains("temp")) {
												ep.postUpdate(itemName, DecimalType.valueOf(ResponseParse[1]));
											}
										} else {
											ep.postUpdate(itemName, DecimalType.valueOf(ResponseParse[0]));
										}

									} else if (PortParse[2].contains("h")) {
										String[] ResponseParse = response.toString().split("[:/]");

										for (int ind = 0; ind < ResponseParse.length; ind++) {
											logger.debug(ind + ": " + ResponseParse[ind]);
										}
										if (ResponseParse.length > 2) {
											if (ResponseParse[2].contains("hum")) {
												ep.postUpdate(itemName, DecimalType.valueOf(ResponseParse[3]));
											}
										} else {
											ep.postUpdate(itemName, DecimalType.valueOf(ResponseParse[1]));
										}
									}  else if (PortParse[2].contains("r")) {
										String[] ResponseParse = response.toString().split("[:/]");
										for (int ind = 0; ind < ResponseParse.length; ind++) {
											logger.debug(ind + ": " + ResponseParse[ind]);
										}
										if (ResponseParse.length > 2) {			
												ep.postUpdate(itemName, StringType.valueOf(ResponseParse[1] + "/" + ResponseParse[3]));
										} else {
											ep.postUpdate(itemName, StringType.valueOf(ResponseParse[0] + "/" + ResponseParse[1]));
										}
										
									}
								} else if (provider.getPORT(itemName).toString().contains("1w")) {
									String[] ResponseParse = response.toString().split("[:]");
									if (ResponseParse.length > 1) {
										logger.debug(ResponseParse[1]);
										ep.postUpdate(itemName, DecimalType.valueOf(ResponseParse[1]));
									} else {
										ep.postUpdate(itemName, DecimalType.valueOf(response.toString()));
									}
								} else if (provider.getPORT(itemName).toString().contains("tget")) {

									ep.postUpdate(itemName, DecimalType.valueOf(response.toString()));
								} else {
									ep.postUpdate(itemName, DecimalType.valueOf(response.toString()));
								}
							} else if (provider.getItemType(itemName).toString().contains("StringItem")){
								if (provider.getPORT(itemName).toString().contains("dht")) {
									String[] PortParse = provider.getPORT(itemName).toString().split("[,]");
									for (int ind = 0; ind < PortParse.length; ind++) {
										logger.debug(PortParse[ind]);
									}
									if (PortParse[2].contains("r")) {
										String[] ResponseParse = response.toString().split("[:/]");
										for (int ind = 0; ind < ResponseParse.length; ind++) {
											logger.debug(ind + ": " + ResponseParse[ind]);
										}
										if (ResponseParse.length > 2) {			
												ep.postUpdate(itemName, StringType.valueOf(ResponseParse[1] + "/" + ResponseParse[3]));
										} else {
											ep.postUpdate(itemName, StringType.valueOf(ResponseParse[0] + "/" + ResponseParse[1]));
										}
									}
								}
							}
						}
					} catch (IOException e) {
						logger.debug("Connect to megadevice " + provider.getIP(itemName) + " error: "
								+ e.getLocalizedMessage());
					}

					lastPollTime.put(itemName, System.currentTimeMillis());
				}
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

		SendCommand(itemName, command.toString());
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {

		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);

	}

	public void setEP() {
		this.ep = eventPublisher;

	}

	public void setProviders() {
		this.megaproviders = providers;

	}

	private void SendCommand(String itemName, String newState) {
		int state = 0;
		HttpURLConnection con;
		for (MegaDeviceBindingProvider provider : providers) {
			logger.debug("SendCommand exec");
			for (String itemname : provider.getItemNames()) {
				// logger.debug(itemname +" has type "+
				// provider.getItemType(itemName).toString());
				if ((itemname.equals(itemName)) && (provider.getItemType(itemname).toString().contains("SwitchItem"))) {
					if (newState.equals("ON")) {
						state = 1;
					} else if (newState.equals("OFF")) {
						state = 0;
					}

					URL MegaURL;
					String Result = "http://" + provider.getIP(itemName) + "/" + provider.password(itemName) + "/?cmd="
							+ provider.getPORT(itemName) + ":" + state;
					logger.info("Switch: " + Result);
					try {
						MegaURL = new URL(Result);
						con = (HttpURLConnection) MegaURL.openConnection();
						// optional default is GET

						con.setRequestMethod("GET");
						con.setConnectTimeout(500);
						con.setReadTimeout(500);

						// add request header
						con.setRequestProperty("User-Agent", "Mozilla/5.0");
						if (con.getResponseCode() == 200)
							logger.debug("OK");
						con.disconnect();
					} catch (MalformedURLException e) {
						logger.debug("1" + e);
						e.printStackTrace();
					} catch (ProtocolException e) {
						logger.debug("2" + e);
						e.printStackTrace();
					} catch (IOException e) {
						logger.debug(e.getLocalizedMessage());
						e.printStackTrace();
					}
				} else if ((itemname.equals(itemName))
						&& (provider.getItemType(itemname).toString().contains("DimmerItem"))) {
					int result = (int) Math.round(Integer.parseInt(newState) * 2.55);
					logger.debug("Dimmer value-> " + result);
					URL MegaURL;
					String Result = "http://" + provider.getIP(itemName) + "/" + provider.password(itemName) + "/?cmd="
							+ provider.getPORT(itemName) + ":" + result;
					logger.info("dimmer:", Result);

					try {
						MegaURL = new URL(Result);
						con = (HttpURLConnection) MegaURL.openConnection();
						// optional default is GET
						con.setRequestMethod("GET");
						con.setConnectTimeout(500);
						con.setReadTimeout(500);
						// add request header
						con.setRequestProperty("User-Agent", "Mozilla/5.0");
						if (con.getResponseCode() == 200)
							logger.debug("OK");
						con.disconnect();
					} catch (MalformedURLException e) {
						logger.error("1" + e);
						e.printStackTrace();
					} catch (ProtocolException e) {
						logger.error("2" + e);
						e.printStackTrace();
					} catch (IOException e) {
						logger.error(e.getLocalizedMessage());
						e.printStackTrace();
					}
				} else {
					// logger.error(itemname +"cannot determine type: " +
					// provider.getItemType(itemname).toString());

				}
			}
		}
	}

	public void ScanPorts() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String Result = "";
		for (MegaDeviceBindingProvider provider : providers) {

			for (String itemName : provider.getItemNames()) {
				try {
					if (provider.getItemType(itemName).toString().contains("NumberItem")) {
						if (provider.getPORT(itemName).toString().contains("tget")) {
							Result = "http://" + provider.getIP(itemName) + "/" + provider.password(itemName)
									+ "/?tget=1";
						} else if ((provider.getPORT(itemName).toString().contains("t"))
								|| (provider.getPORT(itemName).toString().contains("h"))) {
							String[] PortParse = provider.getPORT(itemName).toString().split("[,]");

							Result = "http://" + provider.getIP(itemName) + "/" + provider.password(itemName) + "/?pt="
									+ PortParse[0] + "&cmd=get";
						} else {

							Result = "http://" + provider.getIP(itemName) + "/" + provider.password(itemName) + "/?pt="
									+ provider.getPORT(itemName) + "&cmd=get";
						}
					} else {
						Result = "http://" + provider.getIP(itemName) + "/" + provider.password(itemName) + "/?pt="
								+ provider.getPORT(itemName) + "&cmd=get";
					}

					URL obj = new URL(Result);
					HttpURLConnection con = (HttpURLConnection) obj.openConnection();
					logger.debug(Result);

					logger.debug("Sleeping...");
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					logger.debug("Waking up...");
					// optional default is GET
					con.setRequestMethod("GET");
					con.setConnectTimeout(500);
					con.setReadTimeout(500);

					// add request header
					con.setRequestProperty("User-Agent", "Mozilla/5.0");

					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuilder response = new StringBuilder();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();
					logger.debug("input string->" + response.toString());
					if (response.toString().contains("ON")) {
						ep.postUpdate(itemName, OnOffType.ON);
					} else if (response.toString().contains("OFF")) {
						ep.postUpdate(itemName, OnOffType.OFF);
					} else {
						if (provider.getItemType(itemName).toString().contains("DimmerItem")) {
							int percent = (int) Math.round(Integer.parseInt(response.toString()) / 2.55);
							ep.postUpdate(itemName, PercentType.valueOf(Integer.toString(percent)));
							logger.debug(itemName + " " + percent);
						} else if (provider.getItemType(itemName).toString().contains("NumberItem")) {

							if (provider.getPORT(itemName).toString().contains("dht")) {
								String[] PortParse = provider.getPORT(itemName).toString().split("[,]");
								for (int ind = 0; ind < PortParse.length; ind++) {
									logger.debug(PortParse[ind]);
								}
								if (PortParse[2].contains("t")) {

									String[] ResponseParse = response.toString().split("[:/]");
									for (int ind = 0; ind < ResponseParse.length; ind++) {
										logger.debug(ind + ": " + ResponseParse[ind]);
									}
									if (ResponseParse.length > 2) {
										if (ResponseParse[0].contains("temp")) {
											ep.postUpdate(itemName, DecimalType.valueOf(ResponseParse[1]));
										}
									} else {
										ep.postUpdate(itemName, DecimalType.valueOf(ResponseParse[0]));
									}

								} else if (PortParse[2].contains("h")) {
									String[] ResponseParse = response.toString().split("[:/]");

									for (int ind = 0; ind < ResponseParse.length; ind++) {
										logger.debug(ind + ": " + ResponseParse[ind]);
									}
									if (ResponseParse.length > 2) {
										if (ResponseParse[2].contains("hum")) {
											ep.postUpdate(itemName, DecimalType.valueOf(ResponseParse[3]));
										}
									} else {
										ep.postUpdate(itemName, DecimalType.valueOf(ResponseParse[1]));
									}
								}
							} else if (provider.getPORT(itemName).toString().contains("1w")) {
								String[] ResponseParse = response.toString().split("[:]");
								if (ResponseParse.length > 1) {
									logger.debug(ResponseParse[1]);
									ep.postUpdate(itemName, DecimalType.valueOf(ResponseParse[1]));
								} else {
									ep.postUpdate(itemName, DecimalType.valueOf(response.toString()));
								}
							} else if (provider.getPORT(itemName).toString().contains("tget")) {

								ep.postUpdate(itemName, DecimalType.valueOf(response.toString()));
							} else {
								ep.postUpdate(itemName, DecimalType.valueOf(response.toString()));
							}
						}
					}
				} catch (IOException e) {
					logger.debug(
							"Connect to megadevice " + provider.getIP(itemName) + " error: " + e.getLocalizedMessage());
				}
			}

		}

	}

	public static void updateValues(String hostAddress, String[] getCommands, OnOffType onoff) {
		if (hostAddress.equals("0:0:0:0:0:0:0:1"))
			hostAddress = "localhost";
		logger.debug("action at address ->> " + hostAddress + " On_OFF: " + onoff);
		for (MegaDeviceBindingProvider provider : megaproviders) {
			for (String itemName : provider.getItemNames()) {
				if (provider.getItemType(itemName).toString().contains("SwitchItem")) {
					if (provider.getIP(itemName).equals(hostAddress)
							&& provider.getPORT(itemName).equals(getCommands[2])) {
						logger.debug(" itemName: " + provider.getItemType(itemName));
						ep.postUpdate(itemName, onoff);
					}
				} else if (provider.getItemType(itemName).toString().contains("NumberItem")) {
					if (provider.getIP(itemName).equals(hostAddress) && provider.getPORT(itemName).equals("at")) {
						logger.debug(" itemName: " + provider.getItemType(itemName));
						ep.postUpdate(itemName, DecimalType.valueOf(getCommands[2]));
					} else if (provider.getIP(itemName).equals(hostAddress) && getCommands[1].equals("st")
							&& provider.getPORT(itemName).equals("st")) {
						ep.postUpdate(itemName, DecimalType.valueOf(getCommands[2]));
					}
				} else if (provider.getItemType(itemName).toString().contains("StringItem")) {
					if (provider.getIP(itemName).equals(hostAddress)
							&& provider.getPORT(itemName).equals(getCommands[2]) && getCommands[3].equals("ib")) {
						ep.postUpdate(itemName, StringType.valueOf(getCommands[4]));
					}
				}
			}
		}
	}

}
