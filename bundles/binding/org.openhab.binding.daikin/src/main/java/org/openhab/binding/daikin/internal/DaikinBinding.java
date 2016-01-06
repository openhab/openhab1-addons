/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

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
 *  	- Mode 		Auto, Cool, Dry, Heat, OnlyFun, Night
 *  	- Temp 		between 10C - 32C
 *  	- Fan 		Fun1, Fun2, Fun3, Fun4, Fun5 (speeds), FAuto (auto)
 *  	- Swing		Ud (up/down), Off
 * 
 * @author Ben Jones
 * @since 1.5.0
 */
public class DaikinBinding extends AbstractActiveBinding<DaikinBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(DaikinBinding.class);
	private static HttpClient httpClient = null;
	
	static {
		httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		HttpClientParams params = httpClient.getParams();
		params.setConnectionManagerTimeout(5000);
		params.setSoTimeout(30000);
		params.setContentCharset("UTF-8");	
	}

	private static final String CONFIG_KEY_REFRESH = "refresh";
	private static final String CONFIG_KEY_HOST = "host";
	private static final String CONFIG_KEY_USERNAME = "username";
	private static final String CONFIG_KEY_PASSWORD = "password";

	// the temp values come back with European formatting - i.e. 23,5
    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("de"));
	
	private Long refreshInterval = 60000L;
	private Map<String, DaikinHost> hosts = new HashMap<String, DaikinHost>();
	
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
		// make a GET request to the Daikin controller for the current state
        List<String> results = new ArrayList<String>();
		GetMethod httpGet = null;
        try {
			String url = String.format("http://%s/param.csv", host.getHost());
            httpGet = new GetMethod(url);
			
			httpClient.executeMethod(httpGet);
			
			if (httpGet.getStatusCode() != HttpStatus.SC_OK) {
				logger.warn("Invalid response received from Daikin controller '{}': {}", 
						host.getHost(), 
						httpGet.getStatusCode());
				return;
			}
			
			InputStream content = httpGet.getResponseBodyAsStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));

			String line;
			while ((line = reader.readLine()) != null) {
				if (line != null && line.length() > 0)
					results.add(line.substring(0, line.length() - 1));
			}
        } catch (Exception e) {
            logger.error("Error attempting to request current state", e);
            return;
        } finally {
			if (httpGet != null)
				httpGet.releaseConnection();
		}

        // check the response was OK
        if (!results.get(0).equals("OK")) {
        	logger.error("Bad connection state received: {}", results.get(0));
        	return;
        }

        // parse the state values from the response and update our host
        // NOTE: we don't update some of our internal state if OFF otherwise
        //       values get cleared and when we switch on we reset state 
        host.setPower(results.get(1).equals("ON"));
        if (host.getPower()) {
	        host.setMode(parseMode(results.get(2)));
	        host.setFan(parseFan(results.get(4)));
	        
			// when setting to Dry mode the temp comes back as 50 - don't want to store this
			BigDecimal temp = parseDecimal(results.get(3));
	        if (!host.getMode().equals(DaikinMode.Dry) || temp.compareTo(new BigDecimal(32)) <= 0)
	        	host.setTemp(temp);
        }
        host.setSwing(parseSwing(results.get(5)));
        
        // read-only state
        host.setTempIn(parseDecimal(results.get(6)));
        host.setTimer(parseTimer(results.get(7)));
        host.setTempOut(parseDecimal(results.get(14)));
        host.setHumidityIn(parseDecimal(results.get(15)));
	}
	
	private void updateState(DaikinHost host) {		
		// TODO: can't figure out how to authenticate this HTTP POST request
		// TODO: have to configure the controller with NO AUTHENTICATION for this to work
		// TODO: maybe something like this...https://github.com/jim-easterbrook/pywws/commit/a537fab5061b8967270f972636017cd84a63065f
		PostMethod httpPost = null;
        try {
			String url = String.format("http://%s", host.getHost());
            httpPost = new PostMethod(url);
			
			httpPost.addParameter("wON", host.getPower() ? "On" : "Off");
			httpPost.addParameter("wMODE", host.getMode().getCommand());
			httpPost.addParameter("wTEMP", host.getTemp().setScale(0).toPlainString() + "C");
			httpPost.addParameter("wFUN", host.getFan().getCommand());
			httpPost.addParameter("wSWNG", host.getSwing().getCommand());
			httpPost.addParameter("wSETd1", "Set");

            httpClient.executeMethod(httpPost);
			
			if (httpPost.getStatusCode() != HttpStatus.SC_OK) {
				logger.warn("Invalid response received from Daikin controller '{}': {}", 
						host.getHost(), 
						httpPost.getStatusCode());
				return;
			}
        } catch (Exception e) {
            logger.error("Error attempting to send command", e);
            return;
        } finally {
			if (httpPost != null)
				httpPost.releaseConnection();
		}
	}

	private DaikinMode parseMode(String value) {
        if (value.equals("AUTO"))    return DaikinMode.Auto;
        if (value.equals("DRY"))     return DaikinMode.Dry;
        if (value.equals("COOL"))    return DaikinMode.Cool;
        if (value.equals("HEAT"))    return DaikinMode.Heat;
        if (value.equals("ONLYFUN")) return DaikinMode.OnlyFun;
        if (value.equals("NIGHT"))   return DaikinMode.Night;
        
        return DaikinMode.None;
	}
	
    private DaikinFan parseFan(String value) {
        if (value.equals("FA")) return DaikinFan.Auto;
        if (value.equals("F1")) return DaikinFan.F1;
        if (value.equals("F2")) return DaikinFan.F2;
        if (value.equals("F3")) return DaikinFan.F3;
        if (value.equals("F4")) return DaikinFan.F4;
        if (value.equals("F5")) return DaikinFan.F5;
        
        return DaikinFan.None;
    }
    
    private DaikinSwing parseSwing(String value) {
        if (value.equals("UD"))  return DaikinSwing.UpDown;
        if (value.equals("OFF")) return DaikinSwing.Off;
        
        return DaikinSwing.None;
    }
    
    private DaikinTimer parseTimer(String value) {
        if (value.equals("OFF/OFF")) return DaikinTimer.OffOff;
        if (value.equals("ON/OFF"))  return DaikinTimer.OnOff;
        if (value.equals("OFF/ON"))  return DaikinTimer.OffOn;
        if (value.equals("ON/ON"))   return DaikinTimer.OnOn;
        
        return DaikinTimer.None;
    }

    private BigDecimal parseDecimal(String value) {
		if (value.equals("NONE"))
			return BigDecimal.ZERO;
		try {
			return new BigDecimal(numberFormat.parse(value).doubleValue());
		} catch (java.text.ParseException e) {
			logger.error("Failed to parse number: {}", value);
			return BigDecimal.ZERO;
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
