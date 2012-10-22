/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.onewire.OneWireBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.owfs.jowfsclient.Enums.OwBusReturn;
import org.owfs.jowfsclient.Enums.OwDeviceDisplayFormat;
import org.owfs.jowfsclient.Enums.OwPersistence;
import org.owfs.jowfsclient.Enums.OwTemperatureScale;
import org.owfs.jowfsclient.OwfsClientFactory;
import org.owfs.jowfsclient.OwfsException;
import org.owfs.jowfsclient.internal.OwfsClientImpl;
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

	private boolean isProperlyConfigured = false;

	private OwfsClientImpl owc;

	/** the ip address to use for connecting to the OneWire server */
	private String ip = null;

	/** the port to use for connecting to the OneWire server (optional, defaults to 4304) */
	private int port = 4304;

	/**
	 * the refresh interval which is used to poll values from the OneWire server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	/** the retry count in case no valid value was returned upon read (optional, defaults to 3) */
	private int retry = 3;
	
	/** defines which temperature scale owserver should return temperatures in (optional, defaults to CELSIUS) */
	private OwTemperatureScale tempScale = OwTemperatureScale.OWNET_TS_CELSIUS;
	
	
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
			owc = (OwfsClientImpl) OwfsClientFactory.newOwfsClient(ip, port, false);

			/* Configure client */
			owc.setDeviceDisplayFormat(OwDeviceDisplayFormat.OWNET_DDF_F_DOT_I);
			owc.setBusReturn(OwBusReturn.OWNET_BUSRETURN_ON);
			owc.setPersistence(OwPersistence.OWNET_PERSISTENCE_ON);
			owc.setTemperatureScale(tempScale);
			owc.setTimeout(5000);

			try {
				boolean isConnected = owc.connect();
				if (isConnected) {
					logger.info("Established connection to OwServer on IP '{}' Port '{}'.",	ip, port);
				} else {
					logger.warn("Establishing connection to OwServer [IP '{}' Port '{}'] timed out.", ip, port);
				}
			} catch (IOException ioe) {
				logger.error("Couldn't connect to OwServer [IP '" + ip + "' Port '" + port + "']: ", ioe.getLocalizedMessage());
			}
		} else {
			logger.warn("Couldn't connect to OwServer because of missing connection parameters [IP '{}' Port '{}'].", ip, port);
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean isProperlyConfigured() {
		return isProperlyConfigured;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void execute() {
		if (owc != null) {
			for (OneWireBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {

					String sensorId = provider.getSensorId(itemName);
					String unitId = provider.getUnitId(itemName);

					if (sensorId == null || unitId == null) {
						logger.warn("sensorId or unitId isn't configured properly "
							+ "for the given itemName [itemName={}, sensorId={}, unitId={}] => querying bus for values aborted!",
							new Object[] { itemName, sensorId, unitId });
						continue;
					}

					State value = UnDefType.UNDEF;

					try {
						if (owc.exists("/" + sensorId)) {
							int attempt = 1;
							while (value == UnDefType.UNDEF && attempt <= retry) {
								String valueString = owc.read(sensorId + "/" + unitId);
								logger.debug("{}: Read value '{}' from {}/{}, attempt={}",
									new Object[] { itemName, valueString, sensorId, unitId, attempt });
								if (valueString != null) {
									value = new DecimalType(Double.valueOf(valueString));
								} 
								attempt++;
							}
						} else {
							logger.info("there is no sensor for path {}",
									sensorId);
						}

						logger.debug("Found sensor {} with value {}", sensorId, value);
					} catch (OwfsException oe) {
						logger.warn("couldn't read from path {}", sensorId);
						if (logger.isDebugEnabled()) {
							logger.debug("reading from path " + sensorId + " throws exception", oe);
						}
					} catch (IOException ioe) {
						logger.error(
								"couldn't establish network connection while reading '"	+ sensorId + "'", ioe);
					} finally {
						eventPublisher.postUpdate(itemName, value);
					}
				}
			}
		} else {
			logger.warn("OneWireClient is null => refresh cycle aborted!");
		}
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {

		if (config != null) {
			ip = (String) config.get("ip");

			String portString = (String) config.get("port");
			if (StringUtils.isNotBlank(portString)) {
				port = Integer.parseInt(portString);
			}

			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			String retryString = (String) config.get("retry");
			if (StringUtils.isNotBlank(retryString)) {
				retry = Integer.parseInt(retryString);
			}
			
			String tempScaleString = (String) config.get("tempscale");
			if (StringUtils.isNotBlank(tempScaleString)) {
				try {
					tempScale = OwTemperatureScale.valueOf("OWNET_TS_" + tempScaleString);
				} catch (IllegalArgumentException iae) {
					throw new ConfigurationException(
							"onewire:tempscale","Unknown temperature scale '"
							+ tempScaleString + "'. Valid values are CELSIUS, FAHRENHEIT, KELVIN or RANKIN.");
				}
			}

			// there is a valid onewire-configuration, so connect to the onewire
			// server ...
			connect(ip, port);

			isProperlyConfigured = true;
			start();
		}

	}

}
