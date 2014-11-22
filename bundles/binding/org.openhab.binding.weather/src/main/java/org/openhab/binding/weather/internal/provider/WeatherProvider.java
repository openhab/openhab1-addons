/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.provider;

import org.openhab.binding.weather.internal.common.LocationConfig;
import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.model.Weather;

/**
 * Weather provider definition.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public interface WeatherProvider {

	/**
	 * Returns the weather data for the specified location config.
	 */
	public Weather getWeather(LocationConfig locationConfig);

	/**
	 * Returns the provider name.
	 */
	public ProviderName getProviderName();

}
