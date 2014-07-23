/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.client;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.RemoteControlOptionParser;
import org.openhab.binding.homematic.internal.communicator.client.interfaces.HomematicClient;
import org.openhab.binding.homematic.internal.communicator.client.interfaces.RpcClient;
import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.binding.homematic.internal.model.HmBattery;
import org.openhab.binding.homematic.internal.model.HmBatteryTypeProvider;
import org.openhab.binding.homematic.internal.model.HmChannel;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmDevice;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.openhab.binding.homematic.internal.model.HmRemoteControlOptions;
import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all Homematic clients with common methods.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public abstract class BaseHomematicClient implements HomematicClient {
	private static final Logger logger = LoggerFactory.getLogger(BaseHomematicClient.class);

	protected RpcClient rpcClient;
	protected HomematicContext context = HomematicContext.getInstance();

	public BaseHomematicClient(RpcClient rpcClient) {
		this.rpcClient = rpcClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() throws HomematicClientException {
		rpcClient.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shutdown() throws HomematicClientException {
		rpcClient.shutdown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDatapointValue(HmDatapoint dp, String datapointName, Object value) throws HomematicClientException {
		HmInterface hmInterface = dp.getChannel().getDevice().getHmInterface();
		String address = dp.getChannel().getAddress();
		if (dp.isIntegerValue() && value instanceof Double) {
			value = ((Number) value).intValue();
		}

		rpcClient.setDatapointValue(hmInterface, address, datapointName, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoteControlDisplay(String remoteControlAddress, String text, String options)
			throws HomematicClientException {

		RemoteControlOptionParser rcParameterParser = new RemoteControlOptionParser();
		HmRemoteControlOptions rco = rcParameterParser.parse(remoteControlAddress, options);
		rco.setText(text);

		logger.debug("Sending to remote control {}: {}", remoteControlAddress, rco);

		String address = remoteControlAddress + ":18";
		if (StringUtils.isNotBlank(rco.getText())) {
			rpcClient.setDatapointValue(getDefaultInterface(), address, "TEXT", rco.getText());
		}

		rpcClient.setDatapointValue(getDefaultInterface(), address, "BEEP", rco.getBeep());
		rpcClient.setDatapointValue(getDefaultInterface(), address, "UNIT", rco.getUnit());
		rpcClient.setDatapointValue(getDefaultInterface(), address, "BACKLIGHT", rco.getBacklight());

		for (String symbol : rco.getSymbols()) {
			rpcClient.setDatapointValue(getDefaultInterface(), address, symbol, Boolean.TRUE);
		}

		rpcClient.setDatapointValue(getDefaultInterface(), address, "SUBMIT", Boolean.TRUE);
	}

	/**
	 * Adds the battery info datapoint to the specified device if the device has
	 * batteries.
	 */
	protected void addBatteryInfo(HmDevice device) throws HomematicClientException {
		HmBattery battery = HmBatteryTypeProvider.getBatteryType(device.getType());
		if (battery != null) {
			for (HmChannel channel : device.getChannels()) {
				if ("0".equals(channel.getNumber())) {
					try {
						logger.debug("Adding battery type to device {}: {}", device.getType(), battery.getInfo());
						HmDatapoint dp = new HmDatapoint();
						writeField(dp, "name", "BATTERY_TYPE", String.class);
						writeField(dp, "writeable", Boolean.FALSE, Boolean.class);
						writeField(dp, "valueType", 20, Integer.class);
						dp.setValue(battery.getInfo());
						channel.addDatapoint(dp);
					} catch (IllegalAccessException ex) {
						throw new HomematicClientException(ex.getMessage(), ex);
					}
				}
			}
		}
	}

	protected void writeField(Object target, String fieldName, Object value, Class<?> type)
			throws IllegalAccessException {
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

	/**
	 * Callback interface to iterate through all Homematic valueItems.
	 */
	public interface HmValueItemIteratorCallback {
		/**
		 * Called for every Homematic valueItem after loading from the CCU.
		 */
		public void iterate(HomematicBindingConfig bindingConfig, HmValueItem hmValueItem);
	}

}
