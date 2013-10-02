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
package org.openhab.binding.fs20.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.fs20.FS20BindingConfig;
import org.openhab.binding.fs20.FS20BindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akuz.cul.CULCommunicationException;
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
 * @since 1.3.0
 */
public class FS20Binding extends AbstractActiveBinding<FS20BindingProvider>
		implements ManagedService, CULListener {

	private static final Logger logger = LoggerFactory
			.getLogger(FS20Binding.class);

	private final static String KEY_DEVICE_NAME = "device";

	private String deviceName;

	private CULHandler cul;

	/**
	 * the refresh interval which is used to poll values from the FS20 server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public FS20Binding() {
	}

	public void activate() {
		if (StringUtils.isEmpty(deviceName)) {
			logger.error("Device name not configured, can't start the FS20 binding");
			return;
		}
		logger.debug("Activating FS20 binding");
		getCULHandle();
	}

	private void setNewDeviceName(String deviceName) {
		if (cul != null) {
			CULManager.close(cul);
		}
		this.deviceName = deviceName;
		getCULHandle();
	}

	private void getCULHandle() {
		try {
			logger.debug("Opening CUL device on " + deviceName);
			cul = CULManager.getOpenCULHandler(deviceName, CULMode.SLOW_RF);
			cul.registerListener(this);
		} catch (CULDeviceException e) {
			logger.error("Can't open cul device", e);
			cul = null;
		}
	}

	public void deactivate() {
		logger.debug("Deactivating FS20 binding");
		cul.unregisterListener(this);
		CULManager.close(cul);
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
		return "FS20 Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		FS20BindingConfig bindingConfig = null;
		for (FS20BindingProvider provider : super.providers) {
			bindingConfig = provider.getConfigForItemName(itemName);
			if (bindingConfig != null) {
				break;
			}
		}
		if (bindingConfig != null) {
			logger.debug("Received command " + command.toString()
					+ " for item " + itemName);
			try {
				FS20Command fs20Command = FS20CommandHelper
						.convertHABCommandToFS20Command(command);
				cul.send("F" + bindingConfig.getAddress()
						+ fs20Command.getHexValue());
			} catch (CULCommunicationException e) {
				logger.error("An exception occured while sending a command", e);
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		logger.debug("Received new config");
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			String deviceName = (String) config.get(KEY_DEVICE_NAME);
			if (StringUtils.isEmpty(deviceName)) {
				logger.error("No device name configured");
				setProperlyConfigured(false);
				throw new ConfigurationException(KEY_DEVICE_NAME,
						"The device name can't be empty");
			} else {
				setNewDeviceName(deviceName);
			}

			setProperlyConfigured(true);
			// read further config parameters here ...

		}
	}

	@Override
	public void dataReceived(String data) {
		// It is possible that we see here messages of other protocols
		if (data.startsWith("F")) {
			logger.debug("Received FS20 message: " + data);
			handleReceivedMessage(data);
		}

	}

	private void handleReceivedMessage(String message) {
		String houseCode = (message.substring(1, 5));
		String address = (message.substring(5, 7));
		String command = message.substring(7, 9);
		String fullAddress = houseCode + address;
		FS20BindingConfig config = null;
		for (FS20BindingProvider provider : providers) {
			config = provider.getConfigForAddress(fullAddress);
			if (config != null) {
				break;
			}
		}
		if (config != null) {
			FS20Command fs20Command = FS20Command.getFromHexValue(command);
			logger.debug("Received command " + fs20Command.toString()
					+ " for device " + config.getAddress());
			eventPublisher.postUpdate(config.getItem().getName(),
					FS20CommandHelper.getStateFromFS20Command(fs20Command));
		} else {
			logger.debug("Received message for unknown device " + fullAddress);
		}
	}

	@Override
	public void error(Exception e) {
		logger.error("Error while communicating with CUL", e);

	}

}
