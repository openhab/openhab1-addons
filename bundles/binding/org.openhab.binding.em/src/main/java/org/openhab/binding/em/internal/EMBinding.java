/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.em.internal;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.em.EMBindingProvider;
import org.openhab.binding.em.internal.EMBindingConfig.Datapoint;
import org.openhab.binding.em.internal.EMBindingConfig.EMType;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akuz.cul.CULDeviceException;
import de.akuz.cul.CULHandler;
import de.akuz.cul.CULListener;
import de.akuz.cul.CULManager;
import de.akuz.cul.CULMode;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class EMBinding extends AbstractActiveBinding<EMBindingProvider>
		implements ManagedService, CULListener {

	private static final Logger logger = LoggerFactory
			.getLogger(EMBinding.class);

	private final static String CONFIG_KEY_DEVICE_NAME = "device";
	/**
	 * the refresh interval which is used to poll values from the EM server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	private String deviceName;

	private CULHandler cul;

	private Map<String, Integer> counterMap = new HashMap<String, Integer>();

	public EMBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again

	}

	private void closeCUL() {
		cul.unregisterListener(this);
		CULManager.close(cul);
	}

	private void setNewDeviceName(String deviceName) {
		if (deviceName != null && this.deviceName != null
				&& this.deviceName.equals(deviceName)) {
			return;
		}
		if (this.deviceName != null && cul != null) {
			closeCUL();
		}
		try {
			cul = CULManager.getOpenCULHandler(deviceName, CULMode.SLOW_RF);
			cul.registerListener(this);
			this.deviceName = deviceName;
		} catch (CULDeviceException e) {
			logger.error("Can't get CULHandler", e);
		}
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
		return "EM Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// Nothing to do
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			String deviceName = (String) config.get(CONFIG_KEY_DEVICE_NAME);
			if (!StringUtils.isEmpty(deviceName)) {
				setNewDeviceName(deviceName);
			} else {
				setProperlyConfigured(false);
				throw new ConfigurationException(CONFIG_KEY_DEVICE_NAME,
						"Device name not configured");
			}
			setProperlyConfigured(true);
		}
	}

	@Override
	public void dataReceived(String data) {
		if (!StringUtils.isEmpty(data) && data.startsWith("E")) {
			parseDataLine(data);
		}

	}

	private void parseDataLine(String data) {
		String typeString = data.substring(1, 3);
		String address = data.substring(3, 5);
		String counterString = data.substring(5, 7);
		if (!checkNewMessage(address, counterString)) {
			logger.warn("Received message from " + address + " more than once");
			return;
		}

		String cumulatedValueString = data.substring(7, 11);
		// Next two not set for type 2;
		String lastValueString = null;
		String topValueString = null;
		if (data.length() > 11) {
			lastValueString = data.substring(11, 15);
			topValueString = data.substring(15, 19);
		}
		EMBindingConfig emConfig = findConfig(typeString, address,
				Datapoint.CUMULATED_VALUE);
		if (emConfig != null) {
			updateItem(emConfig, cumulatedValueString);
		}
		if (!StringUtils.isEmpty(lastValueString)) {
			emConfig = findConfig(typeString, address, Datapoint.LAST_VALUE);
			if (emConfig != null) {
				updateItem(emConfig, lastValueString);
			}
		}
		if (!StringUtils.isEmpty(topValueString)) {
			emConfig = findConfig(typeString, address, Datapoint.TOP_VALUE);
			if (emConfig != null) {
				updateItem(emConfig, topValueString);
			}
		}
	}

	private void updateItem(EMBindingConfig config, String valueString) {
		int value = Integer.parseInt(valueString, 16);
		DecimalType status = new DecimalType(value);
		eventPublisher.postUpdate(config.getItem().getName(), status);
	}

	private boolean checkNewMessage(String address, String counterString) {
		Integer lastCounter = counterMap.get(address);
		if (lastCounter == null) {
			lastCounter = -1;
		}
		int counter = Integer.parseInt(counterString, 16);
		if (counter > lastCounter) {
			return true;
		}
		return false;
	}

	private EMBindingConfig findConfig(String typeString, String address,
			Datapoint datapoint) {
		EMBindingConfig emConfig = null;
		EMType type = EMType.getFromTypeValue(typeString);
		for (EMBindingProvider provider : this.providers) {
			emConfig = provider.getConfigByTypeAndAddressAndDatapoint(type,
					address, Datapoint.CUMULATED_VALUE);
			if (emConfig != null) {
				return emConfig;
			}
		}
		return null;
	}

	@Override
	public void error(Exception e) {
		logger.error("Exception instead of new data from CUL", e);

	}

}
