/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.provider;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.weather.internal.common.LocationConfig;
import org.openhab.binding.weather.internal.common.ProviderConfig;
import org.openhab.binding.weather.internal.common.WeatherConfig;
import org.openhab.binding.weather.internal.common.WeatherContext;
import org.openhab.binding.weather.internal.model.Forecast;
import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.model.Weather;
import org.openhab.binding.weather.internal.parser.WeatherParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common base class for all weather providers. Retrieves, parses and returns
 * weather data.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public abstract class AbstractWeatherProvider implements WeatherProvider {
	private static final Logger logger = LoggerFactory.getLogger(AbstractWeatherProvider.class);
	private static HttpClient httpClient = null;

	private WeatherConfig config = WeatherContext.getInstance().getConfig();
	private WeatherParser parser;

	static {
		httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		HttpClientParams params = httpClient.getParams();
		params.setConnectionManagerTimeout(5000);
		params.setSoTimeout(30000);
		params.setContentCharset("UTF-8");
	}

	public AbstractWeatherProvider(WeatherParser parser) {
		this.parser = parser;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Weather getWeather(LocationConfig locationConfig) {
		Weather weather = new Weather(getProviderName());
		executeRequest(weather, prepareUrl(getWeatherUrl(), locationConfig), locationConfig);

		String forecastUrl = getForecastUrl();
		if (forecastUrl != null && !weather.hasError()) {
			executeRequest(weather, prepareUrl(forecastUrl, locationConfig), locationConfig);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("{}[{}]: {}", getProviderName(), locationConfig.getLocationId(), weather.toString());
			for (Weather fc : weather.getForecast()) {
				logger.debug("{}[{}]: {}", getProviderName(), locationConfig.getLocationId(), fc.toString());
			}
		}

		return weather;
	}

	/**
	 * Prepares the provider URL by setting the config.
	 */
	private String prepareUrl(String url, LocationConfig locationConfig) {
		ProviderConfig providerConfig = config.getProviderConfig(getProviderName());
		if (providerConfig != null) {
			url = StringUtils.replace(url, "[API_KEY]", providerConfig.getApiKey());
			url = StringUtils.replace(url, "[API_KEY_2]", providerConfig.getApiKey2());
		}
		url = StringUtils.replace(url, "[LATITUDE]", locationConfig.getLatitude().toString());
		url = StringUtils.replace(url, "[LONGITUDE]", locationConfig.getLongitude().toString());
		url = StringUtils.replace(url, "[LANGUAGE]", locationConfig.getLanguage());
		return url;
	}

	/**
	 * Executes the http request and parses the returned stream.
	 */
	private void executeRequest(Weather weather, String url, LocationConfig locationConfig) {
		GetMethod get = null;
		try {
			logger.trace("{}[{}]: request : {}", getProviderName(), locationConfig.getLocationId(), url);

			get = new GetMethod(url);
			httpClient.executeMethod(get);

			InputStream is = null;
			if (logger.isTraceEnabled()) {
				String response = get.getResponseBodyAsString(100000);
				response = StringUtils.remove(response, "\n");
				response = StringUtils.trim(response);
				logger.trace("{}[{}]: response: {}", getProviderName(), locationConfig.getLocationId(), response);
				is = new ByteArrayInputStream(response.getBytes(get.getResponseCharSet()));
			} else {
				is = get.getResponseBodyAsStream();
			}

			if (get.getStatusCode() == HttpStatus.SC_OK) {
				parser.parseInto(is, weather);
			}
			// special handling because of bad OpenWeatherMap json structure
			if (weather.getProvider() == ProviderName.OPENWEATHERMAP && weather.getResponseCode() != null
					&& weather.getResponseCode() == 200) {
				weather.setError(null);
			}

			if (!weather.hasError() && get.getStatusCode() != HttpStatus.SC_OK) {
				weather.setError(get.getStatusLine().toString());
			}

			if (weather.hasError()) {
				logger.error("{}[{}]: Can't retreive weather data: {}", getProviderName(),
						locationConfig.getLocationId(), weather.getError());
			} else {
				setLastUpdate(weather);
			}

		} catch (Exception ex) {
			logger.error(getProviderName() + ": " + ex.getMessage(), ex);
			weather.setError(ex.getClass().getSimpleName() + ": " + ex.getMessage());
		} finally {
			if (get != null) {
				get.releaseConnection();
			}
		}
	}

	/**
	 * Sets the current timestamp in every weather object.
	 */
	private void setLastUpdate(Weather weather) {
		Calendar cal = Calendar.getInstance();
		weather.getCondition().setLastUpdate(cal);
		for (Forecast forecast : weather.getForecast()) {
			forecast.getCondition().setLastUpdate(cal);
		}
	}

	/**
	 * Returns the provider weather url.
	 */
	protected abstract String getWeatherUrl();

	/**
	 * Returns the provider forecast url, some providers needs a second http
	 * request.
	 */
	protected String getForecastUrl() {
		return null;
	}

}
