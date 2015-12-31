/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.connection;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ebus.EBusBindingProvider;
import org.openhab.binding.ebus.internal.configuration.TelegramConfiguration;
import org.openhab.binding.ebus.internal.configuration.TelegramValue;
import org.openhab.binding.ebus.internal.parser.EBusConfigurationProvider;
import org.openhab.binding.ebus.internal.utils.EBusCodecUtils;
import org.openhab.binding.ebus.internal.utils.EBusUtils;
import org.openhab.binding.ebus.internal.utils.NumberUtils;
import org.openhab.binding.ebus.internal.utils.StateUtils;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christian Sowada
 * @since 1.7.0
 */
public class EBusCommandProcessor implements BindingChangeListener {

	private static final Logger logger = LoggerFactory
			.getLogger(EBusCommandProcessor.class);

	private Map<String, ScheduledFuture<?>> futureMap = new HashMap<String, ScheduledFuture<?>>();

	private ScheduledExecutorService scheduler;

	private AbstractEBusWriteConnector connector;

	private EBusConfigurationProvider configurationProvider;

	/**
	 * @param connector
	 */
	public void setConnector(AbstractEBusWriteConnector connector) {
		this.connector = connector;
	}

	/**
	 * 
	 */
	public void deactivate() {
		if(scheduler != null) {
			scheduler.shutdown();
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.binding.BindingChangeListener#allBindingsChanged(org.openhab.core.binding.BindingProvider)
	 */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		logger.debug("Remove all polling items for this provider from scheduler ...");
		for (String itemName : provider.getItemNames()) {
			if(futureMap.containsKey(itemName)) {
				futureMap.get(itemName).cancel(true);
			}
		}

		for (String itemName : provider.getItemNames()) {
			bindingChanged(provider, itemName);
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.binding.BindingChangeListener#bindingChanged(org.openhab.core.binding.BindingProvider, java.lang.String)
	 */
	@Override
	public void bindingChanged(BindingProvider provider, final String itemName) {

		logger.debug("Binding changed for item {}", itemName);

		final EBusBindingProvider eBusProvider = (EBusBindingProvider)provider;
		int refreshRate = eBusProvider.getRefreshRate(itemName);

		if(refreshRate > 0) {

			final Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						byte[] data = composeSendData(
								eBusProvider, itemName, null);

						if(data != null && data.length > 0) {
							if(connector == null) {
								logger.warn("eBus connector not ready, can't send data yet!");
							} else {
								connector.addToSendQueue(data);
							}
						} else {
							if(configurationProvider != null && !configurationProvider.isEmpty()) {
								logger.warn("No data to send for item {}! Check your item configuration.", itemName);
							}
						}
					} catch (Exception e) {
						logger.error("Error while running runnable ...", e);
					}
				}
			};

			if(futureMap.containsKey(itemName)) {
				logger.debug("Stopped old polling item {} ...", itemName);
				futureMap.remove(itemName).cancel(true);
			}

			if(scheduler == null) {
				scheduler = Executors.newScheduledThreadPool(2);
			}

			logger.debug("Add polling item {} with refresh rate {} to scheduler ...",
					itemName, refreshRate);

			// do not start all pollings at the same time
			int randomInitDelay = (int) (Math.random() * (30 - 4) + 4);
			futureMap.put(itemName, scheduler.scheduleWithFixedDelay(r, randomInitDelay, 
					refreshRate, TimeUnit.SECONDS));

		} else if(futureMap.containsKey(itemName)) {
			logger.debug("Remove scheduled refresh for item {}", itemName);
			futureMap.get(itemName).cancel(true);
			futureMap.remove(itemName);
		}
	}

	/**
	 * @param binding
	 */
	public void setConfigurationProvider(EBusConfigurationProvider configurationProvider) {
		this.configurationProvider = configurationProvider;
	}

	/**
	 * @param commandId
	 * @param commandClass
	 * @param dst
	 * @param src
	 * @param values
	 * @return
	 */
	private byte[] composeEBusTelegram(String commandId,	Byte dst, Byte src, Map<String, Object> values) {

		if(configurationProvider == null || configurationProvider.isEmpty()) {
			logger.debug("eBUS configuration provider not ready, can't get send data yet.");
			return null;
		}

		byte[] buffer = null;

		TelegramConfiguration commandCfg = configurationProvider.getCommandById(commandId);
		if(commandCfg != null) {

			if(dst == null && StringUtils.isNotEmpty(commandCfg.getDst())) {
				dst = EBusUtils.toByte((String) commandCfg.getDst());
			}

			if(dst == null) {
				logger.error("Unable to send command, destination adress is missing. Set \"dst\" in item.cfg ...");
				return null;
			}

			byte[] bytesData = EBusUtils.toByteArray((String) commandCfg.getData());
			byte[] bytesCmd = EBusUtils.toByteArray((String) commandCfg.getCommand());

			buffer = new byte[bytesData.length+6];
			buffer[0] = src;
			buffer[1] = dst;
			buffer[4] = (byte) bytesData.length;
			System.arraycopy(bytesCmd, 0, buffer, 2, bytesCmd.length);

			if(values == null || values.isEmpty()) {
				logger.trace("No setter-values for eBUS telegram, used default data ...");
				System.arraycopy(bytesData, 0, buffer, 5, bytesData.length);
				return buffer;
			}

			Map<String, TelegramValue> valuesConfig = commandCfg.getValues();

			if(valuesConfig == null || valuesConfig.isEmpty()) {
				logger.warn("No values configurated in json cfg ...");
				return null;
			}

			for (Entry<String, Object> entry : values.entrySet()) {

				TelegramValue valueEntry = valuesConfig.get(entry.getKey());

				if(valueEntry == null) {
					logger.warn("Unable to set value key \"{}\" in command \"{}.{}\", can't compose telegram ...", 
							entry.getKey(), commandId);
					return null;
				}

				String type = valueEntry.getType();
				int pos = valueEntry.getPos()-1;

				BigDecimal value = NumberUtils.toBigDecimal(entry.getValue());

				if(valueEntry.getMax() != null && value.compareTo(valueEntry.getMax()) == 1) {
					throw new RuntimeException("Value larger than allowed!");
				}

				if(valueEntry.getMin() != null && value.compareTo(valueEntry.getMin()) == -1) {
					throw new RuntimeException("Value smaller than allowed!");
				}

				if(value != null && valueEntry.getFactor() != null) {
					value = value.divide(valueEntry.getFactor());
				}

				byte[] encode = EBusCodecUtils.encode(type, value);

				if(encode.length == 0) {
					logger.warn("eBUS codec encoder returns empty buffer ...");
					return null;
				}

				// add computed single value to data buffer
				System.arraycopy(encode, 0, bytesData, pos-5, encode.length);
			}

			for (Entry<String, TelegramValue> value : valuesConfig.entrySet()) {

				// check if the special value type for kromschöder/wolf crc is availabel
				if(StringUtils.equals(value.getValue().getType(), "crc-kw")) {

					byte b = 0;
					int pos = (Integer)value.getValue().getPos()-6;

					for (int i = 0; i < bytesData.length; i++) {
						// exclude crc pos
						if(i != pos) {
							b = EBusUtils.crc8(bytesData[i], b, (byte)0x5C);
						}
					}

					// set crc to specified position
					bytesData[pos] = b;
				}
			}

			bytesData = EBusUtils.encodeEBusData(bytesData);
			System.arraycopy(bytesData, 0, buffer, 5, bytesData.length);

			return buffer;
		}

		return null;
	}

	/**
	 * @param provider
	 * @param itemName
	 * @param type
	 * @return
	 */
	public byte[] composeSendData(EBusBindingProvider provider, String itemName, Command command) {

		if(configurationProvider == null || configurationProvider.isEmpty()) {
			logger.debug("eBus configuration provider not ready, can't get send data yet.");
			return null;
		}

		byte[] data = null;

		String cmd = provider.getCommand(itemName);
		Byte dst = provider.getTelegramDestination(itemName);
		Byte src = provider.getTelegramSource(itemName);

		HashMap<String, Object> values = null;

		
		if(StringUtils.isEmpty(cmd)) {
			// use id instead
			String id = provider.getId(itemName);
			if(!StringUtils.isEmpty(id)) {
				String[] split = StringUtils.split(id, ".");
				
				if(split.length > 1) {
					cmd = split[0] + "." + split[1];
				}
			}
		}
		
		if(src == null) {
			src = connector.getSenderId();
		}

		if(command == null) {
			// polling
			data = composeEBusTelegram(cmd, dst, src, values);

		} else {
			String setValue = provider.getSet(itemName);
			int index = StringUtils.lastIndexOf(setValue, ".");
			String cmdId = StringUtils.left(setValue, index);
			String valueName = StringUtils.substring(setValue, index+1);

			// try to convert command to a supported value
			Object value = StateUtils.convertFromState(command);
			if(value != null) {
				values = new HashMap<String, Object>();
				values.put(valueName, value);
			}

			data = composeEBusTelegram(cmdId, dst, src, values);
		}

		// first try, data-ON, data-OFF, etc.
		if(data == null) {
			String type = command != null ? command.toString().toLowerCase() : null;
			if(StringUtils.isNotEmpty(type)) {
				data = provider.getTelegramData(itemName, type);
			}
		}

		if(data == null) {
			// ok, try data param
			data = provider.getTelegramData(itemName);
		}

		return data;
	}
}
