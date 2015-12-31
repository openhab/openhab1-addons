/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import java.util.Iterator;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.isNumeric;
import org.openhab.binding.novelanheatpump.HeatPumpBindingProvider;
import org.openhab.binding.novelanheatpump.HeatpumpCommandType;
import org.openhab.binding.novelanheatpump.HeatpumpCoolingOperationMode;
import org.openhab.binding.novelanheatpump.HeatpumpOperationMode;
import org.openhab.binding.novelanheatpump.i18n.Messages;
import org.openhab.binding.novelanheatpump.internal.HeatPumpGenericBindingProvider.HeatPumpBindingConfig;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
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
 * @author John Cocula -- made port configurable
 * @since 1.0.0
 */
public class HeatPumpBinding extends AbstractActiveBinding<HeatPumpBindingProvider> implements ManagedService {
	
	private static final Logger logger = LoggerFactory.getLogger(HeatPumpBinding.class);
	private static final SimpleDateFormat sdateformat = new SimpleDateFormat("dd.MM.yy HH:mm"); //$NON-NLS-1$
	private static final int DEFAULT_PORT = 8888;
	private static final long DEFAULT_REFRESH_INTERVAL = 60000L;
	
	/** Parameter code for heating operation mode */
	public static int PARAM_HEATING_OPERATION_MODE = 3;
	/** Parameter code for heating temperature */
	public static int PARAM_HEATING_TEMPERATURE = 1;
	/** Parameter code for warmwater operation mode */
	public static int PARAM_WARMWATER_OPERATION_MODE = 4;
	/** Parameter code for warmwater temperature */
	public static int PARAM_WARMWATER_TEMPERATURE = 2;
	/** Parameter code for cooling operation mode */
	public static int PARAM_COOLING_OPERATION_MODE = 108;
	/** Parameter code for cooling release temperature */
	public static int PARAM_COOLING_RELEASE_TEMP = 110;
	/** Parameter code for target temp MK1 */
	public static int PARAM_COOLING_INLET_TEMP = 132;
	/** Parameter code for start cooling after hours*/
	public static int PARAM_COOLING_START = 850;
	/** Parameter code for stop cooling after hours */
	public static int PARAM_COOLING_STOP = 851;
	
	

	/** Default refresh interval (currently 1 minute) */
	private long refreshInterval = DEFAULT_REFRESH_INTERVAL;

	/* The IP address to connect to */
	protected static String ip;

	/* the port to connect to. */
	protected static int port = DEFAULT_PORT;

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
			boolean properlyConfigured = true;

			long refreshInterval = DEFAULT_REFRESH_INTERVAL;
			int port = DEFAULT_PORT;
			String ip = null;
			try {
				String refreshIntervalString = (String) config.get("refresh"); //$NON-NLS-1$
				refreshInterval = isNotBlank(refreshIntervalString) ?
						Long.parseLong(refreshIntervalString) : DEFAULT_REFRESH_INTERVAL;

				ip = (String) config.get("ip"); //$NON-NLS-1$
				properlyConfigured = properlyConfigured && isNotBlank(ip);

				String portString = (String) config.get("port");  //$NON-NLS-1$
				port = isNumeric(portString) ? Integer.parseInt(portString) : DEFAULT_PORT;
			}
			catch (NumberFormatException ex) {
				properlyConfigured = false;
			}

			if (properlyConfigured) {
				this.refreshInterval = refreshInterval;
				HeatPumpBinding.ip = ip;
				HeatPumpBinding.port = port;
			}
			setProperlyConfigured(properlyConfigured);
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
		HeatpumpConnector connector = new HeatpumpConnector(ip, port);
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


			//read all parameters
			int[] heatpumpParams = connector.getParams();

			handleEventType(new DecimalType((double) heatpumpParams[PARAM_HEATING_TEMPERATURE] / 10.), HeatpumpCommandType.TYPE_HEATING_TEMPERATURE);
			handleEventType(new DecimalType(heatpumpParams[PARAM_HEATING_OPERATION_MODE]), HeatpumpCommandType.TYPE_HEATING_OPERATION_MODE);
			handleEventType(new DecimalType((double) heatpumpParams[PARAM_WARMWATER_TEMPERATURE] / 10.), HeatpumpCommandType.TYPE_WARMWATER_TEMPERATURE);
			handleEventType(new DecimalType(heatpumpParams[PARAM_WARMWATER_OPERATION_MODE]), HeatpumpCommandType.TYPE_WARMWATER_OPERATION_MODE);
			handleEventType(new DecimalType(heatpumpParams[PARAM_COOLING_OPERATION_MODE]), HeatpumpCommandType.TYPE_COOLING_OPERATION_MODE);
			handleEventType(new DecimalType(heatpumpParams[PARAM_COOLING_RELEASE_TEMP] / 10.), HeatpumpCommandType.TYPE_COOLING_RELEASE_TEMPERATURE);
			handleEventType(new DecimalType(heatpumpParams[PARAM_COOLING_INLET_TEMP] / 10.), HeatpumpCommandType.TYPE_COOLING_INLET_TEMP);
			handleEventType(new DecimalType(heatpumpParams[PARAM_COOLING_START] / 10.), HeatpumpCommandType.TYPE_COOLING_START_AFTER_HOURS);
			handleEventType(new DecimalType(heatpumpParams[PARAM_COOLING_STOP] / 10.), HeatpumpCommandType.TYPE_COOLING_STOP_AFTER_HOURS);
			

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
			logger.info("found new value for reverse engineering !!!! No idea what the heatpump will do in state {}.",heatpumpValues[119]); //$NON-NLS-1$
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
			logger.info("found new value for reverse engineering !!!! No idea what the heatpump will do in state {}.",heatpumpValues[117]); //$NON-NLS-1$
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
	
	@Override
	protected void internalReceiveCommand(String itemName, Command command){
		HeatPumpGenericBindingProvider provider = findFirstProvider();
		if(provider != null){
			HeatPumpBindingConfig bindingConfig = provider.getHeatPumpBindingConfig(itemName);
			HeatpumpCommandType commandType =bindingConfig.getType();
			switch(commandType){
				case TYPE_HEATING_OPERATION_MODE:
					if(command instanceof DecimalType){
						int value = ((DecimalType)command).intValue();
						HeatpumpOperationMode mode = HeatpumpOperationMode.fromValue(value);
						if(mode != null){
							if(sendParamToHeatpump(PARAM_HEATING_OPERATION_MODE, mode.getValue())){
								logger.info("Heatpump heating operation mode set to {}.", mode.name());
							}
							
						}else{
							logger.warn("Headpump heating operation mode with value {} is unknown.", value);
						}
					}else{
						logger.warn("Headpump heating operation mode item {} must be from type:{}." , itemName, DecimalType.class.getSimpleName());						
					}
					break;
				case TYPE_HEATING_TEMPERATURE:
					if(command instanceof DecimalType){
						float temperature = ((DecimalType)command).floatValue();
						int value = (int)(temperature * 10.);
						if(sendParamToHeatpump(PARAM_HEATING_TEMPERATURE, value)){
							logger.info("Heatpump heating temeprature set to {}.", temperature);							
						}
					}else{
						logger.warn("Headpump heating temperature item {} must be from type:{}.",itemName, DecimalType.class.getSimpleName());						
					}
					break;
				case TYPE_WARMWATER_OPERATION_MODE:
					if(command instanceof DecimalType){
						int value = ((DecimalType)command).intValue();
						HeatpumpOperationMode mode = HeatpumpOperationMode.fromValue(value);
						if(mode != null){
							if(sendParamToHeatpump(PARAM_WARMWATER_OPERATION_MODE, mode.getValue())){
								logger.info("Heatpump warmwater operation mode set to: {}. ", mode.name());
							}
							
						}else{
							logger.warn("Headpump warmwater operation mode with value {} is unknown.", value);
						}
					}else{
						logger.warn("Headpump warmwater operation mode item {} must be from type: {}.", itemName, DecimalType.class.getSimpleName());						
					}
					break;
				case TYPE_WARMWATER_TEMPERATURE:
					if(command instanceof DecimalType){
						float temperature = ((DecimalType)command).floatValue();
						int value = (int)(temperature * 10.);
						if(sendParamToHeatpump(PARAM_WARMWATER_TEMPERATURE, value)){
							logger.info("Heatpump warmwater temeprature set to {}.", temperature);							
						}
					}else{
						logger.warn("Headpump warmwater temperature item {} must be from type: {}.", itemName, DecimalType.class.getSimpleName());						
					}
					break;
				case TYPE_COOLING_OPERATION_MODE:
					if(command instanceof DecimalType){
						int value = ((DecimalType)command).intValue();
						HeatpumpCoolingOperationMode mode = HeatpumpCoolingOperationMode.fromValue(value);
						if(mode != null){
							if(sendParamToHeatpump(PARAM_COOLING_OPERATION_MODE, mode.getValue())){
								logger.info("Heatpump cooling operation mode set to {}.", mode.name());
							}
							
						}else{
							logger.warn("Headpump cooling operation mode with value {} is unknown.", value);
						}
					}else{
						logger.warn("Headpump cooling operation mode item {} must be from type: {}.", itemName, DecimalType.class.getSimpleName());						
					}
					break;
				case TYPE_COOLING_RELEASE_TEMPERATURE:
					if(command instanceof DecimalType){
						float temperature = ((DecimalType)command).floatValue();
						int value = (int)(temperature * 10.);
						if(sendParamToHeatpump(PARAM_COOLING_RELEASE_TEMP, value)){
							logger.info("Heatpump cooling release temeprature set to {}.", temperature);							
						}
					}else{
						logger.warn("Headpump cooling release temperature item {} must be from type: {}.", itemName, DecimalType.class.getSimpleName());						
					}
					break;
				case TYPE_COOLING_INLET_TEMP:
					if(command instanceof DecimalType){
						float temperature = ((DecimalType)command).floatValue();
						int value = (int)(temperature * 10.);
						if(sendParamToHeatpump(PARAM_COOLING_INLET_TEMP, value)){
							logger.info("Heatpump cooling target temp MK1 set to {}.", temperature);							
						}
					}else{
						logger.warn("Headpump cooling target temp MK1 item {} must be from type: {}.", itemName, DecimalType.class.getSimpleName());						
					}
					break;
				case TYPE_COOLING_START_AFTER_HOURS:
					if(command instanceof DecimalType){
						float hours = ((DecimalType)command).floatValue();
						int value = (int)(hours * 10.);
						if(sendParamToHeatpump(PARAM_COOLING_START, value)){
							logger.info("Heatpump cooling start after hours set to {}.", hours);							
						}
					}else{
						logger.warn("Headpump cooling start after hours item {} must be from type: {}.", itemName, DecimalType.class.getSimpleName());						
					}
					break;
				case TYPE_COOLING_STOP_AFTER_HOURS:
					if(command instanceof DecimalType){
						float hours = ((DecimalType)command).floatValue();
						int value = (int)(hours * 10.);
						if(sendParamToHeatpump(PARAM_COOLING_STOP, value)){
							logger.info("Heatpump cooling stop after hours set to {}.", hours);							
						}
					}else{
						logger.warn("Headpump cooling stop after hours item {} must be from type: {}.", itemName, DecimalType.class.getSimpleName());						
					}
					break;
					
				default:
			}
		}
		
	}

	/**
	 * Set a parameter on the Novela heatpump.
	 * 
	 * @param param
	 * @param value
	 */
	private boolean sendParamToHeatpump(int param, int value) {
		HeatpumpConnector connector = new HeatpumpConnector(ip, port);
		try {
			connector.connect();
			return connector.setParam(param, value);
		} catch (UnknownHostException e) {
			logger.warn("the given hostname '{}' of the Novela heatpump is unknown", ip);
			return false;
		} catch (IOException e) {
			logger.warn("couldn't establish network connection [host '{}']", ip);
			return false;
		} finally {
			if (connector != null) {
				connector.disconnect();
			}
		}
	}

	/**
	 * Finds the binding provider.
	 * @return
	 */
	private HeatPumpGenericBindingProvider findFirstProvider() {
		Iterator<HeatPumpBindingProvider> it = providers.iterator();
		while(it.hasNext()){
			HeatPumpBindingProvider provider = it.next();
			if(provider instanceof HeatPumpGenericBindingProvider){
				return (HeatPumpGenericBindingProvider)provider; 
			}
		}
		return null;
	}	

}
