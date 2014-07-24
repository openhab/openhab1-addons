package org.openhab.binding.homematic.internal.communicator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.client.HomematicClientException;
import org.openhab.binding.homematic.internal.config.binding.DatapointConfig;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmRemoteControlOptions;
import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class parses the options from the Homematic action.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */

public class RemoteControlOptionParser {
	private static final Logger logger = LoggerFactory.getLogger(RemoteControlOptionParser.class);

	private HomematicContext context = HomematicContext.getInstance();

	private String remoteControlAddress;

	/**
	 * Extracts the possible options from the remote control and parses the
	 * specified.
	 */
	public HmRemoteControlOptions parse(String remoteControlAddress, String options) throws HomematicClientException {
		this.remoteControlAddress = remoteControlAddress;
		String[] parms = StringUtils.split(StringUtils.remove(options, ' '), ",");

		String[] symbols = getSymbols();
		String[] beepValueList = getValueItems("BEEP");
		String[] backlightValueList = getValueItems("BACKLIGHT");
		String[] unitValueList = getValueItems("UNIT");

		if (logger.isDebugEnabled()) {
			logger.debug("Remote control {} supports these beep parameter: {}", remoteControlAddress, beepValueList);
			logger.debug("Remote control {} supports these backlight parameter: {}", remoteControlAddress,
					backlightValueList);
			logger.debug("Remote control {} supports these unit parameter: {}", remoteControlAddress, unitValueList);
			logger.debug("Remote control {} supports these symbols: {}", remoteControlAddress, symbols);
		}

		HmRemoteControlOptions rcd = new HmRemoteControlOptions();

		if (parms != null) {
			for (String parameter : parms) {
				logger.debug("Parsing remote control parameter {}", parameter);
				rcd.setBeep(getIntParameter(beepValueList, rcd.getBeep(), parameter, "BEEP"));
				rcd.setBacklight(getIntParameter(backlightValueList, rcd.getBacklight(), parameter, "BACKLIGHT"));
				rcd.setUnit(getIntParameter(unitValueList, rcd.getUnit(), parameter, "UNIT"));

				if (ArrayUtils.contains(symbols, parameter)) {
					logger.debug("Symbol {} found for remote control {}", parameter, remoteControlAddress);
					rcd.addSymbol(parameter);
				}
			}
		}
		return rcd;
	}

	/**
	 * Returns the first found parameter index of the valueList.
	 */
	private int getIntParameter(String[] valueList, int currentValue, String parameter, String parameterName) {
		int idx = ArrayUtils.indexOf(valueList, parameter);
		if (idx != -1) {
			if (currentValue == 0) {
				logger.debug("{} Parameter {} found at index {} for remote control {}", parameterName, parameter,
						idx + 1, remoteControlAddress);
				return idx + 1;
			} else {
				logger.warn("{} Parameter already set for remote control {}, ignoring {}!", parameterName,
						remoteControlAddress, parameter);
				return currentValue;
			}
		} else {
			return currentValue;
		}
	}

	/**
	 * Returns all possible value items from the remote control.
	 */
	private String[] getValueItems(String parameterName) {
		DatapointConfig dpConfig = new DatapointConfig(remoteControlAddress, "18", parameterName);
		HmValueItem hmValueItem = context.getStateHolder().getState(dpConfig);
		if (hmValueItem != null) {
			String[] valueList = (String[]) ArrayUtils.remove(hmValueItem.getValueList(), 0);
			int onIdx = ArrayUtils.indexOf(valueList, "ON");
			if (onIdx != -1) {
				valueList[onIdx] = parameterName + "_ON";
			}
			return valueList;
		}
		return new String[0];
	}

	/**
	 * Returns all possible symbols from the remote control.
	 */
	private String[] getSymbols() throws HomematicClientException {
		DatapointConfig dpConfig = new DatapointConfig(remoteControlAddress, "18", "SUBMIT");
		HmDatapoint rcDatapoint = (HmDatapoint) context.getStateHolder().getState(dpConfig);
		if (rcDatapoint == null) {
			throw new HomematicClientException("Address " + remoteControlAddress
					+ " is not a Homematic remote control with a display");
		}

		List<String> symbols = new ArrayList<String>();
		for (HmDatapoint datapoint : rcDatapoint.getChannel().getDatapoints()) {
			if (datapoint.isWriteable() && datapoint.getValueType() == 2 && !"SUBMIT".equals(datapoint.getName())) {
				symbols.add(datapoint.getName());
			}
		}
		return symbols.toArray(new String[0]);
	}

}
