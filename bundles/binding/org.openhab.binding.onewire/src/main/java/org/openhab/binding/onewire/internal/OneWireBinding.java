/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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
import java.util.Dictionary;

import org.openhab.binding.onewire.OneWireBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.owfs.jowfsclient.Enums.OwBusReturn;
import org.owfs.jowfsclient.Enums.OwDeviceDisplayFormat;
import org.owfs.jowfsclient.Enums.OwPersistence;
import org.owfs.jowfsclient.Enums.OwTemperatureScale;
import org.owfs.jowfsclient.OwfsClient;
import org.owfs.jowfsclient.OwfsClientFactory;
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
public class OneWireBinding extends AbstractActiveBinding<OneWireBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(OneWireBinding.class);

	private OwfsClient owc;
	
	/** the ip address to use for connecting to the OneWire server*/
	private String ip = null;
	
	/** the port to use for connecting to the OneWire server (defaults to 4304) */
	private int port = 4304;
	
	/** the refresh interval which is used to poll values from the OneWire server (defaults to 60000ms) */
	private long refreshInterval = 60000;
	
	
	@Override
	protected String getName() {
		return "OneWire Refresh Service";
	}
	
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
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
	public void execute() {

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

	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
		
		if (config != null) {
			ip = (String) config.get("ip");
			
			String portString = (String) config.get("port");
			if (portString != null && !portString.isEmpty()) {
				port = Integer.parseInt(portString);
			}			
			
			String refreshIntervalString = (String) config.get("refresh");
			if (refreshIntervalString != null && !refreshIntervalString.isEmpty()) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			// there is a valid onewire-configuration, so connect to the onewire
			// server ...
			connect(ip, port);

			// and start this refresh-Thread
			start();
		}

	}

	
}
