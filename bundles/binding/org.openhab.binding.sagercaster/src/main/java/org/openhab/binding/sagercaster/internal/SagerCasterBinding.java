/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sagercaster.internal;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Dictionary;

import org.joda.time.DateTime;
import org.joda.time.base.AbstractInstant;
import org.openhab.binding.sagercaster.SagerCasterBindingConfig;
import org.openhab.binding.sagercaster.SagerCasterBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.extensions.PersistenceExtensions;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sager Weather Caster Binding implementation
 * This binding monitor some input items evolutions and changes (sea level pressure,
 * wind bearing, cloud level, raining status, wind speed) in order to trigger
 * calculation of the Sager Caster Algorithm to generate weather forecasts.
 * 
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public class SagerCasterBinding extends AbstractBinding<SagerCasterBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(SagerCasterBinding.class);
	private static SagerWeatherCaster sagerWeatherCaster = new SagerWeatherCaster();

	static final String LATITUDE = "latitude";
	static final String PERSISTENCE = "persistence";
	
	private static String persistenceService = null; 
	

	/**
	 * Called by the SCR to deactivate the component when either the
	 * configuration is removed or mandatory references are no longer satisfied
	 * or the component has simply been stopped.
	 * 
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {
		sagerWeatherCaster = null;
	}

	private void postNewForecast(SagerCasterBindingProvider provider) {
		for (final String itemName : provider.getItemNamesBy(CommandType.FORECAST)) {
			eventPublisher.postUpdate(itemName, new StringType(
					sagerWeatherCaster.getForecast()));
		}
		for (final String itemName : provider.getItemNamesBy(CommandType.VELOCITY)) {
			eventPublisher.postUpdate(itemName, new StringType(
					sagerWeatherCaster.getWindVelocity()));
		}
		for (final String itemName : provider.getItemNamesBy(CommandType.WINDFROM)) {
			eventPublisher.postUpdate(itemName, new DecimalType(
					sagerWeatherCaster.getWindDirection()));
		}
		for (final String itemName : provider.getItemNamesBy(CommandType.WINDTO)) {
			eventPublisher.postUpdate(itemName, new DecimalType(
					sagerWeatherCaster.getWindDirection2()));
		}
	}
	
	protected HistoricItem getPreviousValue(Item theItem) {
		DateTime now = new DateTime();
		AbstractInstant earlier = now.minusHours(6);
		if (persistenceService == null) {
			return PersistenceExtensions.historicState(theItem, earlier);
		} else {
			return PersistenceExtensions.historicState(theItem, earlier, persistenceService);			
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
		for (SagerCasterBindingProvider provider : providers) {

			SagerCasterBindingConfig config = provider.getConfig(itemName);

			switch (config.commandType) {
			case CLOUDLEVEL: {
				logger.debug("Updated cloudlevel, updating forecast");
				DecimalType newValue = (DecimalType) command;
				sagerWeatherCaster.setCloudLevel(newValue.doubleValue());
				postNewForecast(provider);
				break;
			}
			case RAINING: {
				logger.debug("Updated rain status, updating forecast");
				OnOffType newOnOffValue = (OnOffType) command;
				sagerWeatherCaster.setRaining(newOnOffValue.equals(OnOffType.ON));
				postNewForecast(provider);
				break;
			}
			case SEALEVELPRESSURE: {
				logger.debug("Updated sea-level pressure, updating forecast");
				DecimalType newValue = (DecimalType) command;
				HistoricItem historicItem = getPreviousValue(config.item);
				if (historicItem != null) {
					DecimalType previousValue = (DecimalType) historicItem
							.getState();
					sagerWeatherCaster.setPressure(newValue.doubleValue(),
							previousValue.doubleValue());
					State pressTrend = new DecimalType(
							sagerWeatherCaster.getPressureTrend());
					for (final String pressureItems : provider
							.getItemNamesBy(CommandType.PRESSURETREND)) {
						eventPublisher.postUpdate(pressureItems, pressTrend);
					}
					postNewForecast(provider);
				} else
					logger.warn("Not enough historic data to study pressure evolution, wait a bit ...");
				break;
			}
			case WINDBEARING: { // Bearing have changed, we can compute a
								// compass orientation
				logger.debug("Updated wind direction, updating forecast");
				DecimalType newValue = (DecimalType) command;
				HistoricItem historicItem = getPreviousValue(config.item);
				if (historicItem != null) {
					DecimalType previousValue = (DecimalType) historicItem
							.getState();
					sagerWeatherCaster.setBearing(newValue.intValue(),
							previousValue.intValue());
					State compassState = new StringType(
							sagerWeatherCaster.getCompass());
					for (final String compassItems : provider
							.getItemNamesBy(CommandType.COMPASS)) {
						eventPublisher.postUpdate(compassItems, compassState);
					}
					State windTrend = new DecimalType(
							sagerWeatherCaster.getWindEvolution());
					for (final String windTrendItems : provider
							.getItemNamesBy(CommandType.WINDTREND)) {
						eventPublisher.postUpdate(windTrendItems, windTrend);
					}
					postNewForecast(provider);
				} else
					logger.warn("Not enough historic data to study wind bearing evolution, wait a bit ...");
				break;
			}
			case WINDSPEED: {
				logger.debug("Updated wind speed, updating forecast");
				DecimalType newValue = (DecimalType) command;
				sagerWeatherCaster.setBeaufort(newValue.intValue());
				postNewForecast(provider);
				break;
			}
			}
		}
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		String latitude = (String) properties.get(LATITUDE);
		sagerWeatherCaster.setLatitude(Double.parseDouble(latitude));
		
		String persistence = (String) properties.get(PERSISTENCE);
		if (!isBlank(persistence)) {
			persistenceService = persistence;
		};
	}

}
