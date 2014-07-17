/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.client;

import java.util.Map;

import org.openhab.binding.homematic.internal.communicator.client.interfaces.RpcClient;
import org.openhab.binding.homematic.internal.config.binding.DatapointConfig;
import org.openhab.binding.homematic.internal.config.binding.VariableConfig;
import org.openhab.binding.homematic.internal.model.HmChannel;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmDevice;
import org.openhab.binding.homematic.internal.model.HmInterface;
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

				for (HmChannel channel : device.getChannels()) {
					for (HmDatapoint dp : channel.getDatapoints()) {
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

		writeField(device, "address", deviceData.get("ADDRESS"), String.class);
		writeField(device, "type", deviceData.get("TYPE"), String.class);
		writeField(device, "hmInterface", HmInterface.HOMEGEAR, HmInterface.class);

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
		writeField(channel, "device", device, HmDevice.class);
		writeField(channel, "number", String.valueOf(channelData.get("INDEX")), String.class);

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
		writeField(dp, "channel", channel, HmChannel.class);
		writeField(dp, "writeable", dpData.get("WRITEABLE"), Boolean.class);

		Object valueList = dpData.get("VALUE_LIST");
		if (valueList != null && valueList instanceof Object[]) {
			Object[] vl = (Object[]) valueList;
			String[] stringArray = new String[vl.length];
			for (int i = 0; i < vl.length; i++) {
				stringArray[i] = vl[i].toString();
			}
			writeField(dp, "valueList", stringArray, String[].class);
		}

		Object value = dpData.get("VALUE");
		if (value instanceof Number) {
			writeField(dp, "minValue", dpData.get("MIN"), value.getClass());
			writeField(dp, "maxValue", dpData.get("MAX"), value.getClass());
		}

		setValueType(dp, value);
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
		setValueType(var, value);
		var.setValue(value);
		return var;
	}

	/**
	 * Sets the valueType of a valueItem.
	 */
	private void setValueType(HmValueItem valueItem, Object value) {
		if (value instanceof Boolean) {
			valueItem.setValueType(2);
		} else if (value instanceof Number) {
			valueItem.setValueType(8);
		} else {
			valueItem.setValueType(20);
		}
	}
}
