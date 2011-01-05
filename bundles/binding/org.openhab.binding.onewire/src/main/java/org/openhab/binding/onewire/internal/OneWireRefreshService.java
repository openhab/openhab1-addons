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

import java.util.Collection;
import java.util.HashSet;

import net.strandbygaard.onewire.device.OwSensor;
import net.strandbygaard.onewire.device.OwSensor.Reading;
import net.strandbygaard.onewire.owclient.DeviceNotFoundException;
import net.strandbygaard.onewire.owclient.OwClient;
import net.strandbygaard.onewire.owclient.UnsupportedDeviceException;

import org.openhab.binding.onewire.OneWireBindingProvider;
import org.openhab.binding.onewire.config.OneWireConfiguration;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.owfs.ownet.OWNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The RefreshService polls all configured OneWireSensors with a configurable 
 * interval and post all values on the internal event bus. The interval is 1 
 * minute by default and can be changed via openhab.cfg. 
 * 
 * @author thomasee
 */
public class OneWireRefreshService extends Thread {

	private static final Logger logger = 
		LoggerFactory.getLogger(OneWireRefreshService.class);
	
	// for interrupting this thread
	private boolean interrupted = false;

	/** to keep track of all binding providers */
	private Collection<OneWireBindingProvider> providers = new HashSet<OneWireBindingProvider>();
	
	private EventPublisher eventPublisher = null;

	private OwClient owc;
	
	
	public void activate() {
		start();
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
			OWNet ownet = new OWNet(ip, port);
			owc = new OwClient(ownet);
			
			logger.info("Established connection to OwServer on IP '{}' Port '{}'.",
					ip, port);
		}
		else {
			logger.warn("Couldn't establish connection to OwServer [IP '{}' Port '{}'].", ip, port);
		}
		
	}
	
	@Override
	public void run() {
		
		while (!interrupted) {
			
			connect(OneWireConfiguration.ip, OneWireConfiguration.port);
			
			if (owc != null) {
				for (OneWireBindingProvider provider : providers) {
					for (String itemName : provider.getItemNames()) {
						
						String sensorId = provider.getSensorId(itemName);
						Reading unitId = provider.getUnitId(itemName);
						
						if (sensorId == null || unitId == null) {
							logger.warn("sensorId or unitId isn't configured properly " +
								"for the given itemName [itemName={}, sensorId={}, unitId={}] => querying bus for values aborted!",
								new Object[]{itemName, sensorId, unitId});
							continue;
						}
						
						OwSensor sensor;
						
						try {
							sensor = owc.find(sensorId);
							double value = sensor.read(unitId);
							
							eventPublisher.postUpdate(itemName, new DecimalType(value));
							
							logger.debug("Found sensor {} with value {}", sensor.getId().toString(), value);
						} 
						catch (UnsupportedDeviceException ude) {
							logger.error("device is unsupported -> we currently support familiy codes 10 and 26", ude);
						}
						catch (DeviceNotFoundException dnfe) {
							logger.error("device not found -> check sensorId in your *.items file", dnfe);
						}
					}
				}
				
			}
			else {
				logger.warn("OneWireClient is null => refresh cycle aborted!");
			}

			pause(OneWireConfiguration.refreshInterval);
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
	
	
}
