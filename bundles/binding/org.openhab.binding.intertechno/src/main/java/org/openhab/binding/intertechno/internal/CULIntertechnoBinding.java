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
package org.openhab.binding.intertechno.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.intertechno.CULIntertechnoBindingProvider;
import org.openhab.binding.intertechno.IntertechnoBindingConfig;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.CULHandler;
import org.openhab.io.transport.cul.CULManager;
import org.openhab.io.transport.cul.CULMode;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the communcation with Intertechno devices via CUL devices.
 * Currently it is only possible to send commands.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULIntertechnoBinding extends
		AbstractActiveBinding<CULIntertechnoBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(CULIntertechnoBinding.class);

	/**
	 * Which CUL device should we use, i.e. serial:/dev/tyyACM0
	 */
	private final static String KEY_DEVICE_NAME = "device";

	/**
	 * How often should the command be repeated? See <a
	 * href="http://culfw.de/commandref.html">Culfw Command Ref</a> for more
	 * details.
	 */
	private final static String KEY_REPITIONS = "repetitions";
	/**
	 * How long should one pulse be? See <a
	 * href="http://culfw.de/commandref.html">Culfw Command Ref</a> for more
	 * details.
	 */
	private final static String KEY_WAVE_LENGTH = "wavelength";

	private CULHandler cul;

	private String deviceName;
	private int repititions = 6;
	private int wavelength = 420;

	/**
	 * the refresh interval which is used to poll values from the CULIntertechno
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public CULIntertechnoBinding() {
	}

	public void activate() {
		bindCULHanlder();
	}

	public void deactivate() {
		if (cul != null) {
			CULManager.close(cul);
		}
	}

	private void setNewDeviceName(String newDeviceName) {
		if (!StringUtils.isEmpty(newDeviceName)
				&& !newDeviceName.equals(deviceName)) {
			if (cul != null) {
				CULManager.close(cul);
			}
			deviceName = newDeviceName;
			bindCULHanlder();
		}
	}

	private void bindCULHanlder() {
		if (!StringUtils.isEmpty(deviceName)) {
			try {
				cul = CULManager.getOpenCULHandler(deviceName, CULMode.SLOW_RF);
				cul.send("it" + wavelength);
				cul.send("isr" + repititions);
			} catch (CULDeviceException e) {
				logger.error("Can't open CUL", e);
			} catch (CULCommunicationException e) {
				logger.error("Can't set intertechno parameters", e);
			}
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
		return "CULIntertechno Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		IntertechnoBindingConfig config = null;
		for (CULIntertechnoBindingProvider provider : providers) {
			config = provider.getConfigForItemName(itemName);
			if (config != null) {
				break;
			}
		}
		if (config != null && command instanceof OnOffType) {
			OnOffType type = (OnOffType) command;
			String commandValue = null;
			switch (type) {
			case ON:
				commandValue = config.getCommandValueON();
				break;
			case OFF:
				commandValue = config.getCommandValueOFF();
				break;
			}
			if (commandValue != null) {
				try {
					cul.send("is" + config.getAddress() + commandValue);
				} catch (CULCommunicationException e) {
					logger.error("Can't write to CUL", e);
				}
			} else {
				logger.error("Can't determine value to send for command "
						+ command.toString());
			}
		}
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

			Integer repititions = parseOptionalNumericParameter(KEY_REPITIONS,
					config);
			if (repititions != null) {
				this.repititions = repititions.intValue();
			}

			Integer wavelength = parseOptionalNumericParameter(KEY_WAVE_LENGTH,
					config);
			if (wavelength != null) {
				this.wavelength = wavelength.intValue();
			}

			// Parse device last, so we already know all relevant parameter
			String deviceName = (String) config.get(KEY_DEVICE_NAME);
			if (StringUtils.isEmpty(deviceName)) {
				setProperlyConfigured(false);
				throw new ConfigurationException(KEY_DEVICE_NAME,
						"The device mustn't be empty");
			} else {
				setNewDeviceName(deviceName);
			}

			setProperlyConfigured(true);
		}
	}

	private Integer parseOptionalNumericParameter(String key,
			Dictionary<String, ?> config) throws ConfigurationException {
		String valueString = (String) config.get(key);
		int value = 0;
		if (!StringUtils.isEmpty(valueString)) {
			try {
				value = Integer.parseInt(valueString);
				return value;
			} catch (NumberFormatException e) {
				setProperlyConfigured(false);
				throw new ConfigurationException(key, "Can't parse number");
			}
		}
		return null;
	}

}
