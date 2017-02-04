/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
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
 * MeteoBlue weather provider.
 *
 * @author Gerhard Riegler
 * @since 1.9.0
 */
public class MeteoBlueProvider extends AbstractWeatherProvider {
    private static final String URL = "http://my.meteoblue.com/dataApi/dispatch.pl?apikey=[API_KEY]&type=json_7day_3h_firstday&lat=[LATITUDE]&lon=[LONGITUDE]&temperature=C&windspeed=ms-1&winddirection=degree&precipitationamount=mm&format=json";

    public MeteoBlueProvider() {
        super(new JsonWeatherParser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderName getProviderName() {
        return ProviderName.METEOBLUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getWeatherUrl() {
        return URL;
    }

}
