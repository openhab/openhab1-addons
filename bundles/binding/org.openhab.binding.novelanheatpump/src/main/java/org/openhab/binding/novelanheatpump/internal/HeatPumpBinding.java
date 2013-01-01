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
package org.openhab.binding.novelanheatpump.internal;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.novelanheatpump.HeatPumpBindingProvider;
import org.openhab.binding.novelanheatpump.HeatpumpCommandType;
import org.openhab.binding.novelanheatpump.i18n.Messages;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Heatpump binding connects to a Novelan (Siemens) Heatpump with the 
 * {@link HeatpumpConnector} and read the internal state array every minute.
 * With the state array each binding will be updated.
 * 
 * @author Jan-Philipp Bolle
 * @since 1.0.0
 */
public class HeatPumpBinding extends AbstractActiveBinding<HeatPumpBindingProvider> implements ManagedService {
	
	private static final Logger logger = LoggerFactory.getLogger(HeatPumpBinding.class);
	private static final SimpleDateFormat sdateformat = new SimpleDateFormat("dd.MM.yy HH:mm"); //$NON-NLS-1$

	/** Default refresh interval (currently 1 minute) */
	private long refreshInterval = 60000L;

	private boolean isProperlyConfigured = false;
	protected static ItemRegistry itemRegistry;

	/* The IP address to connect to */
	protected static String ip;
	

	public void deactivate() {
	}

	public void activate() {
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		HeatPumpBinding.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		HeatPumpBinding.itemRegistry = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			String ip = (String) config.get("ip"); //$NON-NLS-1$
			if (StringUtils.isNotBlank(ip) && !ip.equals(HeatPumpBinding.ip)) {
				// only do something if the ip has changed
				HeatPumpBinding.ip = ip;

				String refreshIntervalString = (String) config.get("refresh");
				if (StringUtils.isNotBlank(refreshIntervalString)) {
					refreshInterval = Long.parseLong(refreshIntervalString);
				}

				isProperlyConfigured = true;
				start();
			}
		}

	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public boolean isProperlyConfigured() {
		return isProperlyConfigured;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void execute() {
		if (!bindingsExist()) {
			logger.debug("There is no existing Heatpump binding configuration => refresh cycle aborted!");
			return;
		}
		HeatpumpConnector connector = new HeatpumpConnector(ip);
		try {
			connector.connect();
			// read all available values
			int[] heatpumpValues = connector.getValues();

			// all temperatures are 0.2 degree Celsius exact
			// but use int to save values
			// example 124 is 12.4 degree Celsius
			handleEventType(new DecimalType(((double) heatpumpValues[15]) / 10), HeatpumpCommandType.TYPE_TEMPERATURE_OUTSIDE);
			handleEventType(new DecimalType(((double) heatpumpValues[10] / 10)), HeatpumpCommandType.TYPE_TEMPERATURE_SUPPLAY);
			handleEventType(new DecimalType(((double) heatpumpValues[11] / 10)), HeatpumpCommandType.TYPE_TEMPERATURE_RETURN);
			handleEventType(new DecimalType(((double) heatpumpValues[12] / 10)), HeatpumpCommandType.TYPE_TEMPERATURE_REFERENCE_RETURN);
			handleEventType(new DecimalType(((double) heatpumpValues[18] / 10)), HeatpumpCommandType.TYPE_TEMPERATURE_SERVICEWATER_REFERENCE);
			handleEventType(new DecimalType(((double) heatpumpValues[17] / 10)), HeatpumpCommandType.TYPE_TEMPERATURE_SERVICEWATER);
			handleEventType(new DecimalType(((double) heatpumpValues[26] / 10)), HeatpumpCommandType.TYPE_HEATPUMP_SOLAR_COLLECTOR);
			handleEventType(new DecimalType(((double) heatpumpValues[27] / 10)), HeatpumpCommandType.TYPE_HEATPUMP_SOLAR_STORAGE);
			handleEventType(new DecimalType(((double) heatpumpValues[27] / 10)), HeatpumpCommandType.TYPE_HEATPUMP_SOLAR_STORAGE);
			String heatpumpState = getStateString(heatpumpValues) + ": " + getStateTime(heatpumpValues); //$NON-NLS-1$
			handleEventType(new StringType(heatpumpState), HeatpumpCommandType.TYPE_HEATPUMP_STATE);

		} catch (UnknownHostException e) {
			logger.warn("the given hostname '{}' of the Novela heatpump is unknown", ip);
		} catch (IOException e) {
			logger.warn("couldn't establish network connection [host '{}']", ip);
		} finally {
			if (connector != null) {
				connector.disconnect();
			}
		}

	}

	private void handleEventType(org.openhab.core.types.State state, HeatpumpCommandType heatpumpCommandType) {
		if (itemRegistry != null) {
			for (HeatPumpBindingProvider provider : providers) {
				for (String itemName : provider.getItemNamesForType(heatpumpCommandType)) {
					eventPublisher.postUpdate(itemName, state);
				}
			}
		}
	}

	/**
	 * generate a readable string containing the time since the heatpump is in
	 * the state.
	 * 
	 * @param heatpumpValues
	 *            the internal state array of the heatpump
	 * @return a human readable time string
	 */
	private String getStateTime(int[] heatpumpValues) {
		String returnValue = ""; //$NON-NLS-1$
		// for a long time create a date
		if (heatpumpValues[118] == 2) {
			long value = heatpumpValues[95];
			if (value < 0)
				value = 0;
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(value * 1000L);
			returnValue += sdateformat.format(cal.getTime());
		} else {
			// for a shorter time use the counted time (HH:mm:ss)
			int value = heatpumpValues[120];
			returnValue += String.format("%02d:", new Object[] { Integer.valueOf(value / 3600) }); //$NON-NLS-1$
			value %= 3600;
			returnValue += String.format("%02d:", new Object[] { Integer.valueOf(value / 60) }); //$NON-NLS-1$
			value %= 60;
			returnValue += String.format("%02d", new Object[] { Integer.valueOf(value) }); //$NON-NLS-1$
		}
		return returnValue;
	}

	/**
	 * generate a readable state string from internal heatpump values.
	 * 
	 * @param heatpumpValues
	 *            the internal state array of the heatpump
	 * @return a human readable string, the result displays what the heatpump is
	 *         doing
	 */
	private String getStateString(int[] heatpumpValues) {
		String returnValue = ""; //$NON-NLS-1$
		switch (heatpumpValues[117]) {
		case -1:
			returnValue = Messages.HeatPumpBinding_ERROR;
			break;
		case 0:
			returnValue = Messages.HeatPumpBinding_RUNNING;
			break;
		case 1:
			returnValue = Messages.HeatPumpBinding_STOPPED;
			break;
		case 2:
			returnValue = Messages.HeatPumpBinding_APPEAR;
			break;
		case 3:
			logger.info("found new value for reverse engineering !!!! No idea what the heatpump will do in state 3."); //$NON-NLS-1$
			break;
		case 4:
			logger.info("found new value for reverse engineering !!!! No idea what the heatpump will do in state 4."); //$NON-NLS-1$
			break;
		case 5:
			returnValue = Messages.HeatPumpBinding_DEFROSTING;

		}
		return returnValue;
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Heatpump Refresh Service";
	}

}
