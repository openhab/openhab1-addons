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
 * Wunderground weather provider.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WundergroundProvider extends AbstractWeatherProvider {
	private static final String URL = "http://api.wunderground.com/api/[API_KEY]/conditions/forecast10day/lang:[LANGUAGE]/q/[LATITUDE],[LONGITUDE].json";

	public WundergroundProvider() {
		super(new JsonWeatherParser());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProviderName getProviderName() {
		return ProviderName.WUNDERGROUND;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getWeatherUrl() {
		return URL;
	}

}
