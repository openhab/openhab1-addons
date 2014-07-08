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

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.openhab.binding.homematic.internal.communicator.client.CcuClient.HmValueItemIteratorCallback;
import org.openhab.binding.homematic.internal.communicator.client.interfaces.HomematicClient;
import org.openhab.binding.homematic.internal.communicator.client.interfaces.RpcClient;
import org.openhab.binding.homematic.internal.config.binding.DatapointConfig;
import org.openhab.binding.homematic.internal.model.HmChannel;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmDevice;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HomematicClient implementation for a Homegear (https://www.homegear.eu)
 * server.
 * 
 * @author Gerhard Riegler
 * @since 1.5.1
 */
public class HomegearClient implements HomematicClient {
	private static final Logger logger = LoggerFactory.getLogger(HomegearClient.class);

	private RpcClient rpcClient;
	private boolean started;

	public HomegearClient(RpcClient rpcClient) {
		this.rpcClient = rpcClient;
	}

	protected HmInterface getInterface() {
		return HmInterface.HOMEGEAR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() throws HomematicClientException {
		logger.info("Starting {}", HomegearClient.class.getSimpleName());
		rpcClient.start();
		started = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shutdown() throws HomematicClientException {
		rpcClient.shutdown();
		started = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerCallback() throws HomematicClientException {
		rpcClient.init(getInterface());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void releaseCallback() throws HomematicClientException {
		rpcClient.release(getInterface());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDatapointValue(HmDatapoint dp, String datapointName, Object value) throws HomematicClientException {
		rpcClient.setDatapointValue(dp, datapointName, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void iterateAllDatapoints(HmValueItemIteratorCallback callback) throws HomematicClientException {
		Object[] result = rpcClient.getAllValues(HmInterface.HOMEGEAR);

		try {
			for (int i = 0; i < result.length; i++) {
				@SuppressWarnings("unchecked")
				Map<String, ?> entryMap = (Map<String, ?>) result[i];
				HmDevice device = parseDevice(entryMap);

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
		rpcClient.executeProgram(getInterface(), programName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void iterateAllVariables(HmValueItemIteratorCallback callback) throws HomematicClientException {
		logger.warn("iterateAllVariables not supported on interface " + getInterface().toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVariable(HmValueItem hmValueItem, Object value) throws HomematicClientException {
		logger.warn("setVariable not supported on interface " + getInterface().toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRemoteControlDisplay(String remoteControlAddress, String text, String options)
			throws HomematicClientException {
		logger.warn("setRemoteControlDisplay not supported on interface " + getInterface().toString());
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
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supportsRemoteControls() {
		return false;
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
		writeField(dp, "name", name, String.class);
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
			writeDatapointFieldIfNotEmpty(dpData.get("MIN"), "minValue", dp);
			writeDatapointFieldIfNotEmpty(dpData.get("MAX"), "maxValue", dp);
		}

		if (value instanceof Boolean) {
			writeField(dp, "valueType", 2, Integer.class);
		} else if (value instanceof Number) {
			writeField(dp, "valueType", 8, Integer.class);
		} else {
			writeField(dp, "valueType", 20, Integer.class);
		}

		dp.setValue(value);
		return dp;
	}

	private void writeDatapointFieldIfNotEmpty(Object value, String fieldName, HmDatapoint dp)
			throws IllegalAccessException {
		if (StringUtils.isBlank(ObjectUtils.toString(value))) {
			logger.warn("{} for datapoint {} for device {} is empty!", fieldName, dp.getName(), dp.getChannel()
					.getDevice().getAddress());
		} else {
			writeField(dp, fieldName, value, value.getClass());
		}
	}

	private void writeField(Object target, String fieldName, Object value, Class<?> type) throws IllegalAccessException {
		if (value == null) {
			throw new IllegalArgumentException("Field " + fieldName + " is required for target "
					+ target.getClass().getSimpleName());
		}

		if (type.getName().equals(value.getClass().getName())) {
			FieldUtils.writeField(target, fieldName, value, true);
		} else {
			throw new IllegalArgumentException("Value '" + value + "' (" + value.getClass().getName()
					+ ") is not from type (" + type.getName() + ") in fieldName '" + fieldName + "' of class '"
					+ target.getClass().getName() + "'");
		}
	}

}
