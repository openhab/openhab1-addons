/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jeelinkec3k.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.jeelinkec3k.JeeLinkEC3KBindingConfig;
import org.openhab.binding.jeelinkec3k.JeeLinkEC3KBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author magcode
 * @since 1.0.0
 */
public class JeeLinkEC3KBinding extends
		AbstractActiveBinding<JeeLinkEC3KBindingProvider> implements
		ManagedService {
	private final static String KEY_DEVICE_NAME = "device";
	private final static String KEY_PRICE = "pricekwh";
	private double price;
	private EC3KSerialHandler serialHandler;
	private static final Logger logger = LoggerFactory
			.getLogger(JeeLinkEC3KBinding.class);

	/**
	 * how often items need to be updated. Messages from EC3K may be received
	 * more often. Default 1 minute
	 */
	private long refreshInterval = 60000;

	public JeeLinkEC3KBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
		serialHandler.closeHardware();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "JeeLink EC3K Receiver Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		if (itemName != null && command instanceof OnOffType) {
			OnOffType type = (OnOffType) command;
			for (JeeLinkEC3KBindingProvider provider : providers) {
				JeeLinkEC3KBindingConfig ecBindingCfg = provider
						.getConfigForItemName(itemName);
				ecBindingCfg.setRealTime(type == OnOffType.ON);
			}
			logger.info("internalreceivecommand");
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.info("internalReceiveUpdate");
	}

	/**
	 * @{inheritDoc
	 */

	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		logger.debug("Received new config");
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			String deviceName = (String) config.get(KEY_DEVICE_NAME);
			if (StringUtils.isEmpty(deviceName)) {
				logger.error("No device name configured");
				setProperlyConfigured(false);
				throw new ConfigurationException(KEY_DEVICE_NAME,
						"The device name can't be empty");
			} else {
				try {
					serialHandler = new EC3KSerialHandler(deviceName, this);
					serialHandler.openHardware();
					setProperlyConfigured(true);
				} catch (Exception e) {
					logger.error("Failed to open hardware", e);
				}

			}
			String priceString = (String) config.get(KEY_PRICE);
			price = Double.parseDouble(priceString);

		}
	}

	public void dataReceived(String data) {

		if (data.startsWith("OK")) {
			logger.debug("Received EC3K message: " + data);
			EC3KData e3Data = new EC3KData(data);

			for (JeeLinkEC3KBindingProvider provider : providers) {
				JeeLinkEC3KBindingConfig ecBindingCfg = provider
						.getConfigForAddress(e3Data.getId());
				if (ecBindingCfg != null && needsUpate(ecBindingCfg)) {

					logger.debug("currentWatt: " + e3Data.getCurrentWatt());
					logger.debug("maxWatt: " + e3Data.getMaxWatt());
					logger.debug("consumptionTotal: "
							+ e3Data.getConsumptionTotal());
					logger.debug("secondsOn: " + e3Data.getSecondsOn());
					logger.debug("hoursOn: " + e3Data.getSecondsOn() / 60 / 60);
					logger.debug("secondsTotal: " + e3Data.getSecondsTotal());
					logger.debug("hoursTotal: " + e3Data.getSecondsTotal() / 60
							/ 60);

					State state = new DecimalType(e3Data.getCurrentWatt());
					eventPublisher.postUpdate(ecBindingCfg.getCurrentWattItem()
							.getName(), state);

					// max
					state = new DecimalType(e3Data.getMaxWatt());
					eventPublisher.postUpdate(ecBindingCfg.getMaxWattItem()
							.getName(), state);

					// total
					state = new DecimalType(e3Data.getConsumptionTotal());
					ecBindingCfg.setTotalConsumption(e3Data
							.getConsumptionTotal());
					eventPublisher.postUpdate(ecBindingCfg
							.getTotalConsumptionItem().getName(), state);

					// last updated
					state = new DateTimeType();
					eventPublisher.postUpdate(ecBindingCfg.getLastUpdatedItem()
							.getName(), state);

					// timestamp
					ecBindingCfg.setTimestamp(System.currentTimeMillis());

					// consumption today, will be updated during "setTimestamp"
					state = new DecimalType(ecBindingCfg.getConsumptionToday());
					eventPublisher.postUpdate(ecBindingCfg
							.getConsumptionTodayItem().getName(), state);

					// price today
					state = new DecimalType(ecBindingCfg.getConsumptionToday()
							* price / 1000);
					eventPublisher.postUpdate(ecBindingCfg.getPriceToday()
							.getName(), state);

				} else {
					logger.debug("Suppressing this message");
				}
			}

		}

	}

	private boolean needsUpate(JeeLinkEC3KBindingConfig config) {
		boolean realTime = config.isRealTime();
		boolean itsTime = System.currentTimeMillis() > config.getTimestamp()
				+ refreshInterval;
		return realTime || itsTime;
	}
}
