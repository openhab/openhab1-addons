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

package org.openhab.binding.onewire.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;

import org.openhab.binding.onewire.OneWireBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsClientFactory;
import org.owfs.jowfsclient.Enums.OwBusReturn;
import org.owfs.jowfsclient.Enums.OwDeviceDisplayFormat;
import org.owfs.jowfsclient.Enums.OwPersistence;
import org.owfs.jowfsclient.Enums.OwTemperatureScale;
import org.owfs.jowfsclient.OwfsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The RefreshService polls all configured OneWireSensors with a configurable 
 * interval and post all values on the internal event bus. The interval is 1 
 * minute by default and can be changed via openhab.cfg. 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public class OneWireRefreshService extends Thread implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(OneWireRefreshService.class);
	
	// for interrupting this thread
	private boolean interrupted = false;

	/** to keep track of all binding providers */
	private Collection<OneWireBindingProvider> providers = new HashSet<OneWireBindingProvider>();
	
	private EventPublisher eventPublisher = null;

	private OwfsClient owc;
	
	/** the ip address to use for connecting to the OneWire server*/
	private static String ip = null;
	
	/** the port to use for connecting to the OneWire server (defaults to 4304) */
	private static int port = 4304;
	
	/** the refresh interval which is used to poll values from the OneWire server (defaults to 60000ms) */
	private static long refreshInterval = 60000;
	
	
	public void activate() {
	}
	
	public void deactivate() {
		setInterrupted(true);
	}
	
	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}
	
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}	
	
	public void addBindingProvider(OneWireBindingProvider provider) {
		this.providers.add(provider);
	}

	public void removeBindingProvider(OneWireBindingProvider provider) {
		this.providers.remove(provider);
	}	
	
		
	/**
	 * Create a new {@link OwClient} with the given <code>ip</code> and 
	 * <code>port</code>
	 * 
	 * @param ip
	 * @param port
	 */
	private void connect(String ip, int port) {
		
		if (ip != null && port > 0) {
			
			owc = OwfsClientFactory.newOwfsClient(ip, port, false);

			/* Configure client */
			owc.setDeviceDisplayFormat(OwDeviceDisplayFormat.OWNET_DDF_F_DOT_I);
			owc.setBusReturn(OwBusReturn.OWNET_BUSRETURN_ON);
			owc.setPersistence(OwPersistence.OWNET_PERSISTENCE_ON);
			owc.setTemperatureScale(OwTemperatureScale.OWNET_TS_CELSIUS);
			
			logger.info("Established connection to OwServer on IP '{}' Port '{}'.",
					ip, port);
		}
		else {
			logger.warn("Couldn't establish connection to OwServer [IP '{}' Port '{}'].", ip, port);
		}
		
	}
	
	@Override
	public void run() {
		
		logger.info("refresh thread started [refresh interval {}ms]", OneWireRefreshService.refreshInterval);
		
		while (!interrupted) {
			if (owc != null) {
				
				for (OneWireBindingProvider provider : providers) {
					for (String itemName : provider.getItemNames()) {
						
						String sensorId = provider.getSensorId(itemName);
						String unitId = provider.getUnitId(itemName);
						
						if (sensorId == null || unitId == null) {
							logger.warn("sensorId or unitId isn't configured properly " +
								"for the given itemName [itemName={}, sensorId={}, unitId={}] => querying bus for values aborted!",
								new Object[]{itemName, sensorId, unitId});
							continue;
						}
						
						double value = 0; 
						
						try {
							
							if (owc.exists("/" + sensorId)) {
								String valueString = owc.read("/" + sensorId + "/" + unitId);
								if (valueString != null) {
									value = Double.valueOf(valueString);
								}
								
								// we don't want to stress the very slow 1-wire bus
								pause(250);
							}
							else {
								logger.info("there is no sensor for path {}", sensorId);
							}
							
							
							eventPublisher.postUpdate(itemName, new DecimalType(value));
							
							logger.debug("Found sensor {} with value {}", sensorId, value);
						} 
						catch (OwfsException oe) {
							logger.error("communication error with owserver while reading '" + sensorId + "'", oe);
						}
						catch (IOException ioe) {
							logger.error("couldn't establish network connection while reading '" + sensorId + "'", ioe);
						}
					}
				}
			}
			else {
				logger.warn("OneWireClient is null => refresh cycle aborted!");
			}

			// sleep for a while ...
			pause(OneWireRefreshService.refreshInterval);
		}
	}

	/**
	 * Pause sensor polling for the given <code>refreshInterval</code>. Possible
	 * {@link InterruptedException} is logged with no further action.
	 *  
	 * @param refreshInterval 
	 */
	private void pause(long refreshInterval) {
		
		try {
			Thread.sleep(refreshInterval);
		}
		catch (InterruptedException e) {
			logger.error("pausing OneWireRefresh-Thread throws exception", e);
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
		
		if (config != null) {
			OneWireRefreshService.ip = (String) config.get("ip");
			
			String portString = (String) config.get("port");
			if (portString != null && !portString.isEmpty()) {
				OneWireRefreshService.port = Integer.parseInt(portString);
			}			
			
			String refreshIntervalString = (String) config.get("refresh");
			if (refreshIntervalString != null && !refreshIntervalString.isEmpty()) {
				OneWireRefreshService.refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			// there is a valid onewire-configuration, so connect to the onewire
			// server ...
			connect(OneWireRefreshService.ip, OneWireRefreshService.port);

			// and start this refresh-Thread
			start();
		}

	}

	
}
