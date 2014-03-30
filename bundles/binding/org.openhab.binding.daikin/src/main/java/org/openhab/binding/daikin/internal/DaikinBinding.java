/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.daikin.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import org.openhab.binding.daikin.DaikinBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An active binding which requests the state of a Daikin heat pump via the
 * KKRP01A online controller and can sends commands as well 
 * 
 *  - Commands supported:
 *  	- Power		
 *  	- Mode 		AUTO, COOL, DRY, HEAT, ONLYFUN, NONE
 *  	- Temp 		between 10 - 32
 *  	- Fan 		F1, F2, F3, F4, F5, FA (auto)
 *  	- Swing		UD (up/down), OFF (off)
 * 
 * @author Ben Jones
 * @since 1.5.0
 */
public class DaikinBinding extends AbstractActiveBinding<DaikinBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(DaikinBinding.class);
	
	private static final String CONFIG_KEY_REFRESH = "refresh";
	private static final String CONFIG_KEY_HOST = "host";
	private static final String CONFIG_KEY_USERNAME = "username";
	private static final String CONFIG_KEY_PASSWORD = "password";

	private Long refreshInterval = 60000L;
	private Map<String, DaikinHost> hosts = new HashMap<String, DaikinHost>();
	
	// the temp values come back with European formatting - i.e. 23,5
    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("de"));
	
	public DaikinBinding() {
	}
	
	/**
     * @{inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
    	return refreshInterval;
    }
    
    @Override
    protected String getName() {
    	return "Daikin Refresh Service";
    }
    
    @Override
    public void activate() {
    	super.activate();
    }
    
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		// update the internal state for the associated host and send the
		// new state (all values) to the controller to update
		for (DaikinBindingProvider provider : providers) {
			if (!provider.providesBindingFor(itemName))
				continue;
			
			DaikinBindingConfig bindingConfig = provider.getBindingConfig(itemName);
			if (!hosts.containsKey(bindingConfig.getId()))
				continue;
			
			DaikinHost host = hosts.get(bindingConfig.getId());
			DaikinCommandType commandType = bindingConfig.getCommandType();
			
			if (!commandType.isExecutable()) {
				logger.warn("Attempting to send a command to '{}' which is not executable ({}). Ignoring.", itemName, commandType);
				continue;
			}
			
			host.setState(commandType, command);
			updateState(host);
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void execute() {
		// refresh the state for each host and then check for any item
		// bindings that are associated with this host, and update
		for (DaikinHost host : hosts.values()) {
			refreshState(host);

			for (DaikinBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					DaikinBindingConfig bindingConfig = provider.getBindingConfig(itemName);
					if (!bindingConfig.getId().equals(host.getId()))
						continue;
					
					DaikinCommandType commandType = bindingConfig.getCommandType();
					eventPublisher.postUpdate(itemName, host.getState(commandType));
				}
			}
		}
	}
	
	private void refreshState(DaikinHost host) {
		String url = String.format("http://%s/param.csv", host.getHost());
		CloseableHttpClient httpClient = HttpClients.custom().build();

		// make a GET request to the Daikin controller for the current state
        List<String> results = new ArrayList<String>();
        try {
            HttpGet httpGet = new HttpGet( url );
            CloseableHttpResponse httpResponse = httpClient.execute( httpGet );
            try {
	            InputStream content = httpResponse.getEntity().getContent();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	
	            String line;
	            while ((line = reader.readLine()) != null) {
	                if (line != null && line.length() > 0)
	                	results.add(line.substring(0, line.length() - 1));
	            }
            } finally {
            	httpResponse.close();
            }
        } catch (ClientProtocolException e) {
            logger.error("Client protocol error attempting to request current state", e);
            return;
        } catch (IOException e) {
            logger.error("IO error attempting to request current state", e);
            return;
        }

        // check the response was OK
        if (!results.get(0).equals("OK")) {
        	logger.error("Bad connection state received: {}", results.get(0));
        	return;
        }

        // parse the state values from the response and update our host
        host.setPower(results.get(1).equals("ON"));
        host.setMode(results.get(2));
		host.setTemp(parseDecimal(results.get(3), host.getTemp()));
        host.setFan(results.get(4));
        host.setSwing(results.get(5));
        host.setTempIn(parseDecimal(results.get(6), BigDecimal.ZERO));
        host.setTimer(results.get(7));
        host.setTempOut(parseDecimal(results.get(14), BigDecimal.ZERO));
        host.setHumidityIn(parseDecimal(results.get(15), BigDecimal.ZERO));
	}
	
	private void updateState(DaikinHost host) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("wON", host.getPower() ? "On" : "Off"));
        nameValuePairs.add(new BasicNameValuePair("wMODE", toSentenceCase(host.getMode())));
        nameValuePairs.add(new BasicNameValuePair("wTEMP", host.getTemp().setScale(0).toPlainString() + "C"));
        nameValuePairs.add(new BasicNameValuePair("wFUN", toSentenceCase(host.getFan())));
        nameValuePairs.add(new BasicNameValuePair("wSWNG", toSentenceCase(host.getSwing())));
        nameValuePairs.add(new BasicNameValuePair("wSETd1", "Set"));
		
		String url = String.format("http://%s", host.getHost());
		CloseableHttpClient httpClient = HttpClients.custom().build();

		// TODO: can't figure out how to authenticate this HTTP POST request
		// TODO: have to configure the controller with NO AUTHENTICATION for this to work
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity( new UrlEncodedFormEntity(nameValuePairs));
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            try {
            	if (httpResponse.getStatusLine().getStatusCode() != 200)
            	{
            		logger.warn("Invalid response received from Daikin controller '{}': {}:{}", 
            				host.getHost(), 
            				httpResponse.getStatusLine().getStatusCode(), 
            				httpResponse.getStatusLine().getReasonPhrase());
            		return;
            	}
            } finally {
            	httpResponse.close();
            }
        } catch (ClientProtocolException e) {
            logger.error("Client protocol error attempting to send command", e);
            return;
        } catch (IOException e) {
            logger.error("IO error attempting to send command", e);
            return;
        }
	}

	private BigDecimal parseDecimal(String value, BigDecimal valueIfNone) {
		if (value.equals("NONE"))
			return valueIfNone;
		try {
			return new BigDecimal(numberFormat.parse(value).doubleValue());
		} catch (java.text.ParseException e) {
			logger.error("Failed to parse number: {}", value);
			return BigDecimal.ZERO;
		}
	}
	
	private String toSentenceCase(String value) {
		if (StringUtils.isEmpty(value))
			return value;
		
		return StringUtils.capitalize(value.toLowerCase());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			Enumeration keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = (String) config.get(key);
			
				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				if (key.equals(CONFIG_KEY_REFRESH)) {
					refreshInterval = Long.parseLong(value); 
					continue;
				}				
								
				String[] keyParts = key.split("\\.");
				String hostId = keyParts[0];
				String configKey = keyParts[1];
				
				if (!hosts.containsKey(hostId)) {
					hosts.put(hostId, new DaikinHost(hostId));
				}
				
				DaikinHost host = hosts.get(hostId);
				
				if (configKey.equals(CONFIG_KEY_HOST)) {
					host.setHost(value);
				} else if (configKey.equals(CONFIG_KEY_USERNAME)) {
					host.setUsername(value);
				} else if (configKey.equals(CONFIG_KEY_PASSWORD)) {
					host.setPassword(value);
				} else {
					throw new ConfigurationException(key, "Unrecognised configuration parameter: " + configKey);
				}
			}
			
			// start the refresh thread
			setProperlyConfigured(true);
		}
	}	
}
