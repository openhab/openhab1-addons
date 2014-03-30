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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.openhab.binding.daikin.DaikinBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An active binding which requests a given URL frequently.
 * 
 * @author Ben Jones
 * @since 1.5.0
 */
public class DaikinBinding extends AbstractActiveBinding<DaikinBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(DaikinBinding.class);
	
	private static final String CONFIG_KEY_REFRESH = "refresh";
	private static final String CONFIG_KEY_HOST = "host";

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
	protected void internalReceiveUpdate(String itemName, State newState) {
		
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void execute() {
		for (DaikinHost host : hosts.values()) {
			// get the current state for this host
			Map<DaikinCommandType, State> state = getState(host);

			// check each item binding for this host and update any item state
			for (DaikinBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					DaikinBindingConfig bindingConfig = provider.getBindingConfig(itemName);
					if (!bindingConfig.getId().equals(host.getId()))
						continue;
					
					DaikinCommandType commandType = bindingConfig.getCommandType();
					if (!state.containsKey(commandType))
						continue;
					
					State value = state.get(commandType);
					eventPublisher.postUpdate(itemName, value);
				}
			}
		}
	}

	private Map<DaikinCommandType, State> getState(DaikinHost host) {
		String url = String.format("http://%s/param.csv", host.getHost());
		CloseableHttpClient httpClient = HttpClients.custom().build();

        Map<DaikinCommandType, State> state = new HashMap<DaikinCommandType, State>();
        try {
            HttpGet httpGet = new HttpGet( url );
            CloseableHttpResponse httpResponse = httpClient.execute( httpGet );
            try {
	            InputStream content = httpResponse.getEntity().getContent();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	
	            List<String> results = new ArrayList<String>();
	            String line;
	            while ((line = reader.readLine()) != null) {
	                if (line == null || line.length() == 0)
	                    continue;
	                results.add(line.substring(0, line.length() - 1));
	            }
	            
	            if (!results.get(0).equals("OK")) {
	            	logger.error("Bad connection state received: {}", results.get(0));
	            } else {
	                state.put(DaikinCommandType.POWER, results.get(1).equals("ON") ? OnOffType.ON : OnOffType.OFF);
	                state.put(DaikinCommandType.MODE, new StringType(results.get(2)));
	                state.put(DaikinCommandType.TEMP, parseNumber(results.get(3)));
	                state.put(DaikinCommandType.FAN, new StringType(results.get(4)));
	                state.put(DaikinCommandType.SWING, new StringType(results.get(5)));
	                state.put(DaikinCommandType.TEMPIN, parseNumber(results.get(6)));
	                state.put(DaikinCommandType.TIMER, new StringType(results.get(7)));
	                state.put(DaikinCommandType.TEMPOUT, parseNumber(results.get(14)));
	                state.put(DaikinCommandType.HUMIDITYIN, parseNumber(results.get(15)));
	            }
            } finally {
            	httpResponse.close();
            }
        } catch (ClientProtocolException e) {
            logger.error("Client protocol error attempting to request current state", e);
        } catch (IOException e) {
            logger.error("IO error attempting to request current state", e);
        }

        return state;
	}
	
	private boolean setState(DaikinHost host, List<NameValuePair> nameValuePairs) {
		String url = String.format("http://%s", host.getHost());
		CloseableHttpClient httpClient = HttpClients.custom().build();

        boolean success = false;
        try {
            HttpPost httpPost = new HttpPost( url );
            httpPost.setEntity( new UrlEncodedFormEntity( nameValuePairs ) );
            CloseableHttpResponse httpResponse = httpClient.execute( httpPost );
            try {
            	success = httpResponse.getStatusLine().getStatusCode() == 200;
            } finally {
            	httpResponse.close();
            }
        } catch (ClientProtocolException e) {
            logger.error("Client protocol error attempting to send command", e);
        } catch (IOException e) {
            logger.error("IO error attempting to send command", e);
        }

        return success;
	}

	private DecimalType parseNumber(String value) {
		try {
			return new DecimalType(numberFormat.parse(value).doubleValue());
		} catch (java.text.ParseException e) {
			logger.error("Failed to parse number: {}", value);
			return new DecimalType(0);
		}
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
				} else {
					throw new ConfigurationException(key, "Unrecognised configuration parameter: " + configKey);
				}
			}
			
			// start the refresh thread
			setProperlyConfigured(true);
		}
	}	
}
