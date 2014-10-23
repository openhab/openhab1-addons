/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.openhab.binding.homematic.internal.communicator.client.interfaces.RpcClient;
import org.openhab.binding.homematic.internal.config.binding.DatapointConfig;
import org.openhab.binding.homematic.internal.config.binding.VariableConfig;
import org.openhab.binding.homematic.internal.model.HmChannel;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmDevice;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.openhab.binding.homematic.internal.model.HmRssiInfo;
import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.binding.homematic.internal.model.HmVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HomematicClient implementation for a Homegear (https://www.homegear.eu)
 * server.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class HomegearClient extends BaseHomematicClient {
	private static final Logger logger = LoggerFactory.getLogger(HomegearClient.class);

	private boolean started;

	public HomegearClient(RpcClient rpcClient) {
		super(rpcClient);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HmInterface getDefaultInterface() {
		return HmInterface.HOMEGEAR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() throws HomematicClientException {
		logger.info("Starting {}", HomegearClient.class.getSimpleName());
		super.start();
		started = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shutdown() throws HomematicClientException {
		super.shutdown();
		started = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerCallback() throws HomematicClientException {
		rpcClient.init(getDefaultInterface());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void releaseCallback() throws HomematicClientException {
		rpcClient.release(getDefaultInterface());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void iterateAllDatapoints(HmValueItemIteratorCallback callback) throws HomematicClientException {
		Object[] result = rpcClient.getAllValues(getDefaultInterface());

		try {
			for (int i = 0; i < result.length; i++) {
				@SuppressWarnings("unchecked")
				Map<String, ?> entryMap = (Map<String, ?>) result[i];
				HmDevice device = parseDevice(entryMap);
				addBatteryInfo(device);
				logger.trace("{}", device);

				for (HmChannel channel : device.getChannels()) {
					for (HmDatapoint dp : channel.getDatapoints()) {
						logger.trace("  {}", dp.toDumpString());
						DatapointConfig bindingConfig = new DatapointConfig(device.getAddress(), channel.getNumber(),
								dp.getName());
						callback.iterate(bindingConfig, dp);
					}
				}
			}
		} catch (Exception ex) {
			throw new HomematicClientException(ex.getMessage(), ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeProgram(String programName) throws HomematicClientException {
		logger.debug("Executing script on Homegear: {}", programName);

		rpcClient.executeProgram(getDefaultInterface(), programName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void iterateAllVariables(HmValueItemIteratorCallback callback) throws HomematicClientException {
		Map<String, ?> result = rpcClient.getAllSystemVariables(getDefaultInterface());
		for (String variableName : result.keySet()) {
			HmVariable variable = createVariable(variableName, result.get(variableName));
			VariableConfig bindingConfig = new VariableConfig(variable.getName());
			callback.iterate(bindingConfig, variable);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVariable(HmValueItem hmValueItem, Object value) throws HomematicClientException {
		rpcClient.setSystemVariable(getDefaultInterface(), hmValueItem.getName(), value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, HmRssiInfo> getRssiInfo() throws HomematicClientException {
		logger.info("Reloading RSSI infos not necessary for Homegear, values are always up to date");
		return new HashMap<String, HmRssiInfo>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStarted() {
		return started;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supportsVariables() {
		return true;
	}

	/**
	 * Parses the device informations into the binding model.
	 */
	@SuppressWarnings("unchecked")
	private HmDevice parseDevice(Map<String, ?> deviceData) throws IllegalAccessException {
		HmDevice device = new HmDevice();

		FieldUtils.writeField(device, "address", deviceData.get("ADDRESS"), true);
		FieldUtils.writeField(device, "type", deviceData.get("TYPE"), true);
		FieldUtils.writeField(device, "hmInterface", HmInterface.HOMEGEAR, true);

		Object[] channelList = (Object[]) deviceData.get("CHANNELS");
		for (int i = 0; i < channelList.length; i++) {
			Map<String, ?> channelData = (Map<String, ?>) channelList[i];
			device.addChannel(parseChannel(device, channelData));
		}

		return device;
	}

	/**
	 * Parses the channel informations into the binding model.
	 */
	@SuppressWarnings("unchecked")
	private HmChannel parseChannel(HmDevice device, Map<String, ?> channelData) throws IllegalAccessException {
		HmChannel channel = new HmChannel();
		FieldUtils.writeField(channel, "device", device, true);
		FieldUtils.writeField(channel, "number", String.valueOf(channelData.get("INDEX")), true);

		Map<String, ?> paramsList = (Map<String, ?>) channelData.get("PARAMSET");
		for (String name : paramsList.keySet()) {
			channel.addDatapoint(parseDatapoint(channel, name, (Map<String, ?>) paramsList.get(name)));
		}

		return channel;
	}

	/**
	 * Parses the datapoint informations into the binding model.
	 */
	private HmDatapoint parseDatapoint(HmChannel channel, String name, Map<String, ?> dpData)
			throws IllegalAccessException {
		HmDatapoint dp = new HmDatapoint();
		dp.setName(name);
		FieldUtils.writeField(dp, "channel", channel, true);
		FieldUtils.writeField(dp, "writeable", dpData.get("WRITEABLE"), true);

		Object valueList = dpData.get("VALUE_LIST");
		if (valueList != null && valueList instanceof Object[]) {
			Object[] vl = (Object[]) valueList;
			String[] stringArray = new String[vl.length];
			for (int i = 0; i < vl.length; i++) {
				stringArray[i] = vl[i].toString();
			}
			FieldUtils.writeField(dp, "valueList", stringArray, true);
		}

		Object value = dpData.get("VALUE");

		String type = (String) dpData.get("TYPE");
		boolean isString = StringUtils.equals("STRING", type);
		if (isString && value != null && !(value instanceof String)) {
			value = ObjectUtils.toString(value);
		}
		setValueType(dp, type, value);

		if (dp.isNumberValueType()) {
			FieldUtils.writeField(dp, "minValue", dpData.get("MIN"), true);
			FieldUtils.writeField(dp, "maxValue", dpData.get("MAX"), true);
		}

		dp.setValue(value);
		return dp;
	}

	/**
	 * Creates a writeable HmVariable object.
	 */
	private HmVariable createVariable(String name, Object value) {
		HmVariable var = new HmVariable();
		var.setName(name);
		var.setWriteable(true);
		var.setValue(guessType(value));
		var.setValue(value);
		return var;
	}

	/**
	 * Guesses the value type.
	 */
	private int guessType(Object value) {
		if (value == null) {
			return 20;
		} else if (value instanceof Boolean) {
			return 2;
		} else if (value instanceof Integer || value instanceof Long) {
			return 8;
		} else if (value instanceof Number) {
			return 4;
		} else {
			return 20;
		}
	}

	/**
	 * Sets the valueType of a valueItem.
	 */
	private void setValueType(HmValueItem valueItem, String type, Object value) {
		if ("BOOL".equals(type) || "ACTION".equals(type)) {
			valueItem.setValueType(2);
		} else if ("INTEGER".equals(type) || "ENUM".equals(type)) {
			valueItem.setValueType(8);
		} else if ("FLOAT".equals(type)) {
			valueItem.setValueType(4);
		} else if ("STRING".equals(type)) {
			valueItem.setValueType(20);
		} else {
			logger.warn("Unknown value type '{}', guessing type!", type);
			valueItem.setValueType(guessType(value));
		}
	}

}
