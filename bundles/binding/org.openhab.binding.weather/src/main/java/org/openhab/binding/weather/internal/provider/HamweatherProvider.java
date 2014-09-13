/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.provider;

import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.parser.JsonWeatherParser;

/**
 * Hamweather weather provider.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class HamweatherProvider extends AbstractWeatherProvider {
	private static final String URL = "http://api.aerisapi.com/batch?p=[LATITUDE],[LONGITUDE]&requests=/observations,/forecasts%3Ffilter=day%26to=+5days&client_id=[API_KEY]&client_secret=[API_KEY_2]";

	public HamweatherProvider() {
		super(new JsonWeatherParser());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProviderName getProviderName() {
		return ProviderName.HAMWEATHER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getWeatherUrl() {
		return URL;
	}

}
