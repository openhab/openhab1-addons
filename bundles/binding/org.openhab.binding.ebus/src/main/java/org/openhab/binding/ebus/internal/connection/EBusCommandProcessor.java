/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.connection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ebus.EBusBindingProvider;
import org.openhab.binding.ebus.internal.parser.EBusConfigurationProvider;
import org.openhab.binding.ebus.internal.utils.EBusUtils;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingProvider;
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
	private AbstractEBusConnector connector;

	private EBusConfigurationProvider configurationProvider;

//	private EBusBinding binding;
	


	/**
	 * @param connector
	 */
	public void setConnector(AbstractEBusConnector connector) {
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
					byte[] data = composeSendData(
							eBusProvider, itemName, null);
					
					if(data != null && data.length > 0) {
						if(connector == null) {
							logger.warn("eBus connector not ready, can't send data yet!");
						} else {
							connector.send(data);
						}
					} else {
						logger.warn("No data to send for item {}! Check your item configuration.", itemName);
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
	 * @param provider
	 * @param itemName
	 * @param type
	 * @return
	 */
	public byte[] composeSendData(EBusBindingProvider provider, String itemName, String type) {

		if(configurationProvider == null || configurationProvider.isEmpty()) {
			logger.debug("eBus configuration provider not ready, can't get send data yet.");
			return null;
		}
		
		byte[] data = null;
		
		String cmd = provider.getCommand(itemName);
		String cmdClass = provider.getCommandClass(itemName);
		Map<String, Object> command2 = null;

		command2 = configurationProvider.getCommandById(cmd, cmdClass);
		if(command2 != null) {

			byte[] b = EBusUtils.toByteArray((String) command2.get("data"));
			byte[] b2 = EBusUtils.toByteArray((String) command2.get("command"));
			
			Byte dst = provider.getTelegramDestination(itemName);
			Byte src = provider.getTelegramSource(itemName);

			if(dst == null) {
				throw new RuntimeException("no destination!");
			}

			if(src == null) {
				src = connector.getSenderId();
			}

			byte[] buffer = new byte[b.length+6];
			buffer[0] = src;
			buffer[1] = dst;
			buffer[4] = (byte) b.length;
			System.arraycopy(b2, 0, buffer, 2, b2.length);
			System.arraycopy(b, 0, buffer, 5, b.length);

			data = buffer;
		}
		
		// first try, data-ON, data-OFF, etc.
		if(data == null && StringUtils.isNotEmpty(type)) {
			data = provider.getTelegramData(itemName, type);
		}

		if(data == null) {
			// ok, try data param
			data = provider.getTelegramData(itemName);
		}
		
		return data;
	}
}
