/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.cups.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.WhichJobsEnum;
import org.openhab.binding.cups.CupsBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The RefreshService polls all configured CupsSensors with a configurable
 * interval and post all values on the internal event bus. The interval is 1
 * minute by default and can be changed via openhab.cfg.
 * 
 * @author Tobias Bräutigam
 * @since 1.1.0
 */
public class CupsBinding extends AbstractActiveBinding<CupsBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(CupsBinding.class);
	
	private static final Pattern IP_PATTERN = Pattern.compile("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}");

	private CupsClient client;

	/** the ip address to use for connecting to the Cups server */
	private String host = "localhost";
	private String ip;

	/** the port to use for connecting to the Cups server (optional, defaults to 631) */
	private int port = 631;

	/**
	 * the refresh interval which is used to poll values from the Cups server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	@Override
	protected String getName() {
		return "Cups Refresh Service";
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * Create a new {@link CupsClient} with the given <code>ip</code> and
	 * <code>port</code>
	 * 
	 * @param host
	 * @param port
	 */
	private void connect(String host, int port) {
		if (host != null && StringUtils.isNotBlank(host) && port > 0) {
			try {
				client = new CupsClient(host,port);
				logger.debug("Connection to CupsServer {} established",host);
			} catch (Exception e) {
				logger.error("Couldn't connect to CupsServer [Host '" + host + "' Port '" + port + "']: ", e.getLocalizedMessage());
			}
		} else {
			logger.warn("Couldn't connect to CupsServer because of missing connection parameters [Host '{}' Port '{}'].", host, port);
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void execute() {
		if (client != null) {
			for (CupsBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					
					String printerName = provider.getPrinterName(itemName);
					WhichJobsEnum whichJobs = provider.getWhichJobs(itemName);

					if (printerName == null) {
						logger.warn("printerName isn't configured properly "
							+ "for the given itemName [itemName={}, printerName={}] => querying bus for values aborted!",
							new Object[] { itemName, printerName });
						continue;
					}

					State value = UnDefType.UNDEF;

					try {
						URL printerUrl=null;
						CupsPrinter printer=null;
						try {
							printerUrl = new URL(printerName);
						}
						catch (MalformedURLException e) {
							try {
								printerUrl = new URL("http://"+ip+":"+port+"/printers/"+printerName);
							}
							catch (MalformedURLException e1) {
								logger.error("wrong printer address {}",printerName);
							}
						}
						if (printerUrl==null) {
							// try to find printer by name
							for (CupsPrinter pr : client.getPrinters()) {
								if (pr.getName().equalsIgnoreCase(printerName)) {
									printer = pr;
									break;
								}
							}
						}
						else {
							printer = client.getPrinter(printerUrl);
						}
						if (printer!=null) {
							value = new DecimalType(client.getJobs(printer, whichJobs, "", false).size());
							logger.debug("Found printer {}#{} with value {}", new Object[] {printerUrl, whichJobs, value});
						} else {
							logger.info("there is no printer for path {}",
									printerUrl);
						}						
					} catch (IOException ioe) {
						logger.error(
								"couldn't establish network connection while reading '"	+ printerName + "'", ioe);
					} catch (Exception e) {
						logger.error(
								"couldn't get printer '"	+ printerName + "' from cups server", e);
					} finally {
						eventPublisher.postUpdate(itemName, value);
					}
				}
			}
		} else {
			logger.warn("CupsClient is null => refresh cycle aborted!");
		}
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {

		if (config != null) {
			host = (String) config.get("host");
			Matcher matcher = IP_PATTERN.matcher(host);
			if (!matcher.matches()) {
				try {
					InetAddress address = InetAddress.getByName(host);
					ip = address.getHostAddress();
				} catch (UnknownHostException e) {
					throw new ConfigurationException("host", "unknown host '" + host + "'!");
				}
			} else {
				// host should contain an IP address
				ip = host;
			}

			String portString = (String) config.get("port");
			if (StringUtils.isNotBlank(portString)) {
				port = Integer.parseInt(portString);
			}

			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			// there is a valid Cups-configuration, so connect to the Cups
			// server ...
			connect(ip, port);

			setProperlyConfigured(true);
		}

	}

}
