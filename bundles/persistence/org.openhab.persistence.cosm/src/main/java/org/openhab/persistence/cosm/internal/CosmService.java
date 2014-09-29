/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.cosm.internal;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONSerializer;


/**
 * This is the implementation of the Cosm {@link PersistenceService}. To learn
 * more about Cosm please visit their <a href="http://cosm.com/">website</a>.
 * 
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class CosmService implements PersistenceService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(CosmService.class);
	
	private String apiKey;
	private String url;
	
	private final static String DEFAULT_EVENT_URL = "http://api.cosm.com/v2/feeds/";
	
	private boolean initialized = false;
	
	
	/**
	 * @{inheritDoc}
	 */
	public String getName() {
		return "cosm";
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item, String alias) {
		if (initialized) {
			try { 
				String serviceUrl = url + alias;
	            URL url = new URL(serviceUrl); 
	            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection(); 
	            httpCon.setDoOutput(true); 
	            httpCon.setRequestMethod("PUT"); 
	            httpCon.setRequestProperty("Content-type", "application/json"); 
	            httpCon.setRequestProperty("X-ApiKey",apiKey); 
	            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream()); 

				JSONSerializer serializer = 
					new JSONSerializer().transform(new CosmEventTransformer(), CosmEventBean.class);
				String serializedBean = serializer.serialize(new CosmEventBean(alias, item.getState().toString()));

	            out.write(serializedBean); 
	            out.flush(); 
				logger.debug("Stored item '{}' as '{}' in Cosm and received response: {} ", new String[] { item.getName(), alias, httpCon.getResponseMessage() });
	            out.close(); 
	        } catch (Exception e) { 
	            logger.warn("Connection error"); 
	        } 		
		}
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item) {
		throw new UnsupportedOperationException("The cosm service requires aliases for persistence configurations that should match the id within the feed");
	}
	

	/**
	 * @{inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config!=null) {

			url = (String) config.get("url");
			if (StringUtils.isBlank(url)) {
				url = DEFAULT_EVENT_URL;
			}
			
			apiKey = (String) config.get("apikey");
			if (StringUtils.isBlank(apiKey)) {
				throw new ConfigurationException("cosm:apikey", "The Cosm API-Key is missing - please configure it in openhab.cfg");
			}
			
			initialized = true;
		}
	}
	
}
