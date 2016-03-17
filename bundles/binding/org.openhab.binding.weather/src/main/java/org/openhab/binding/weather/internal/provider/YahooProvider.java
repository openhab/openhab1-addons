/**
 * Copyright (c) 2010-2016, openHAB.org and others.
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
 * Yahoo weather provider.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class YahooProvider extends AbstractWeatherProvider {
    // SELECT * FROM weather.forecast WHERE woeid='[WOEID]'
    private static final String URL = "http://query.yahooapis.com/v1/public/yql?format=json&q=SELECT%20*%20FROM%20weather.forecast%20WHERE%20u%3D'c'%20AND%20woeid%20%3D%20'[WOEID]'";

    public YahooProvider() {
        super(new JsonWeatherParser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderName getProviderName() {
        return ProviderName.YAHOO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getWeatherUrl() {
        return URL;
    }

}
