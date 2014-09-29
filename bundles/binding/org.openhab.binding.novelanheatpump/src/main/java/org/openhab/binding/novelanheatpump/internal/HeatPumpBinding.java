/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	/* The IP address to connect to */
	protected static String ip;
	

	public void deactivate() {
	}

	public void activate() {
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

				setProperlyConfigured(true);
			}
		}
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
			handleEventType(new DecimalType((double) heatpumpValues[10] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_SUPPLAY);
			handleEventType(new DecimalType((double) heatpumpValues[11] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_RETURN);
			handleEventType(new DecimalType((double) heatpumpValues[12] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_REFERENCE_RETURN);
			handleEventType(new DecimalType((double) heatpumpValues[13] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_OUT_EXTERNAL);
			handleEventType(new DecimalType((double) heatpumpValues[14] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_HOT_GAS);
			handleEventType(new DecimalType((double) heatpumpValues[15] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_OUTSIDE);
			handleEventType(new DecimalType((double) heatpumpValues[16] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_OUTSIDE_AVG);
			handleEventType(new DecimalType((double) heatpumpValues[17] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_SERVICEWATER);
			handleEventType(new DecimalType((double) heatpumpValues[18] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_SERVICEWATER_REFERENCE);
			handleEventType(new DecimalType((double) heatpumpValues[19] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_PROBE_IN);
			handleEventType(new DecimalType((double) heatpumpValues[20] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_PROBE_OUT);
			handleEventType(new DecimalType((double) heatpumpValues[21] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_MK1);
			handleEventType(new DecimalType((double) heatpumpValues[22] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_MK1_REFERENCE);
			handleEventType(new DecimalType((double) heatpumpValues[24] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_MK2);
			handleEventType(new DecimalType((double) heatpumpValues[25] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_MK2_REFERENCE);
			handleEventType(new DecimalType((double) heatpumpValues[26] / 10), HeatpumpCommandType.TYPE_HEATPUMP_SOLAR_COLLECTOR);
			handleEventType(new DecimalType((double) heatpumpValues[27] / 10), HeatpumpCommandType.TYPE_HEATPUMP_SOLAR_STORAGE);
			handleEventType(new DecimalType((double) heatpumpValues[28] / 10), HeatpumpCommandType.TYPE_TEMPERATURE_EXTERNAL_SOURCE);
			handleEventType(new StringType(formatHours( heatpumpValues[56])), HeatpumpCommandType.TYPE_HOURS_COMPRESSOR1);
			handleEventType(new DecimalType((double) heatpumpValues[57] ), HeatpumpCommandType.TYPE_STARTS_COMPRESSOR1);
			handleEventType(new StringType(formatHours( heatpumpValues[58])), HeatpumpCommandType.TYPE_HOURS_COMPRESSOR2);
			handleEventType(new DecimalType((double) heatpumpValues[59] ), HeatpumpCommandType.TYPE_STARTS_COMPRESSOR2);
			handleEventType(new StringType(formatHours(heatpumpValues[60])), HeatpumpCommandType.TYPE_HOURS_ZWE1);
			handleEventType(new StringType(formatHours(heatpumpValues[61])), HeatpumpCommandType.TYPE_HOURS_ZWE2);
			handleEventType(new StringType(formatHours(heatpumpValues[62])), HeatpumpCommandType.TYPE_HOURS_ZWE3);
			handleEventType(new StringType(formatHours(heatpumpValues[63])), HeatpumpCommandType.TYPE_HOURS_HETPUMP);
			handleEventType(new StringType(formatHours(heatpumpValues[64])), HeatpumpCommandType.TYPE_HOURS_HEATING);
			handleEventType(new StringType(formatHours(heatpumpValues[65])), HeatpumpCommandType.TYPE_HOURS_WARMWATER);
			handleEventType(new StringType(formatHours(heatpumpValues[65])), HeatpumpCommandType.TYPE_HOURS_COOLING);
			handleEventType(new DecimalType((double) heatpumpValues[151] / 10), HeatpumpCommandType.TYPE_THERMALENERGY_HEATING);
			handleEventType(new DecimalType((double) heatpumpValues[152] / 10), HeatpumpCommandType.TYPE_THERMALENERGY_WARMWATER);
			handleEventType(new DecimalType((double) heatpumpValues[153] / 10), HeatpumpCommandType.TYPE_THERMALENERGY_POOL);
			handleEventType(new DecimalType((double) heatpumpValues[154] / 10), HeatpumpCommandType.TYPE_THERMALENERGY_TOTAL);
			handleEventType(new DecimalType((double) heatpumpValues[155]), HeatpumpCommandType.TYPE_MASSFLOW);
			
			String heatpumpState = getStateString(heatpumpValues) + ": " + getStateTime(heatpumpValues); //$NON-NLS-1$
			handleEventType(new StringType(heatpumpState), HeatpumpCommandType.TYPE_HEATPUMP_STATE);
			String heatpumpExtendedState = getExtendeStateString(heatpumpValues) + ": " + formatHours(heatpumpValues[120]); //$NON-NLS-1$
			handleEventType(new StringType(heatpumpExtendedState), HeatpumpCommandType.TYPE_HEATPUMP_EXTENDED_STATE);

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
		for (HeatPumpBindingProvider provider : providers) {
			for (String itemName : provider.getItemNamesForType(heatpumpCommandType)) {
				eventPublisher.postUpdate(itemName, state);
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
			returnValue = formatHours(value);
		}
		return returnValue;
	}

	private String formatHours(int value) {
		String returnValue = "";
		returnValue += String.format("%02d:", new Object[] { Integer.valueOf(value / 3600) }); //$NON-NLS-1$
		value %= 3600;
		returnValue += String.format("%02d:", new Object[] { Integer.valueOf(value / 60) }); //$NON-NLS-1$
		value %= 60;
		returnValue += String.format("%02d", new Object[] { Integer.valueOf(value) }); //$NON-NLS-1$
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
	private String getExtendeStateString(int[] heatpumpValues) {
		String returnValue = ""; //$NON-NLS-1$
		switch (heatpumpValues[119]) {
		case -1:
			returnValue = Messages.HeatPumpBinding_ERROR;
			break;
		case 0:
			returnValue = Messages.HeatPumpBinding_HEATING;
			break;
		case 1:
			returnValue = Messages.HeatPumpBinding_STANDBY;
			break;
		case 2:
			returnValue = Messages.HeatPumpBinding_SWITCH_ON_DELAY;
			break;
		case 3:
			returnValue = Messages.HeatPumpBinding_SWITCHING_CYCLE_BLOCKING;
			break;
		case 4:
			returnValue = Messages.HeatPumpBinding_PROVIDER_LOCK_TIME;
			break;
		case 5:
			returnValue = Messages.HeatPumpBinding_SERVICE_WATER;
			break;
		case 6:
			returnValue = Messages.HeatPumpBinding_SCREED_HEAT_UP;
			break;
		case 7:
			returnValue = Messages.HeatPumpBinding_DEFROSTING;
			break;
		case 8:
			returnValue = Messages.HeatPumpBinding_PUMP_FLOW;
			break;
		case 9:
			returnValue = Messages.HeatPumpBinding_DISINFECTION;
			break;
		case 10:
			returnValue = Messages.HeatPumpBinding_COOLING;
			break;
		case 12:
			returnValue = Messages.HeatPumpBinding_POOL_WATER;
			break;
		case 13:
			returnValue = Messages.HeatPumpBinding_HEATING_EXT;
			break;
		case 14:
			returnValue = Messages.HeatPumpBinding_SERVICE_WATER_EXT;
			break;
		case 16:
			returnValue = Messages.HeatPumpBinding_FLOW_MONITORING;
			break;
		case 17:
			returnValue = Messages.HeatPumpBinding_ZWE_OPERATION;
			break;
		default:
			logger.info("found new value for reverse engineering !!!! No idea what the heatpump will do in state "+heatpumpValues[119]); //$NON-NLS-1$
			returnValue = Messages.HeatPumpBinding_UNKNOWN;

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
		case 5:
			returnValue = Messages.HeatPumpBinding_DEFROSTING;
			break;
		default:
			logger.info("found new value for reverse engineering !!!! No idea what the heatpump will do in state "+heatpumpValues[117]); //$NON-NLS-1$
			returnValue = Messages.HeatPumpBinding_UNKNOWN;

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
