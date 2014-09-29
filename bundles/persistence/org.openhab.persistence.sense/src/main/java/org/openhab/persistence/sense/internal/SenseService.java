/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.sense.internal;

import java.util.Dictionary;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.io.net.http.HttpUtil;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONSerializer;


/**
 * This is the implementation of the Open.Sen.se {@link PersistenceService}. To learn
 * more about Open.Sen.se please visit their <a href="http://open.sen.se/">website</a>.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class SenseService implements PersistenceService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(SenseService.class);
	
	private String apiKey;
	private String url;
	
	private final static String DEFAULT_EVENT_URL = "http://api.sen.se/events/?sense_key=";

	private boolean initialized = false;
	
	/**
	 * @{inheritDoc}
	 */
	public String getName() {
		return "sense";
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item, String alias) {
		if (initialized) {
			JSONSerializer serializer = 
				new JSONSerializer().transform(new SenseEventTransformer(), SenseEventBean.class);
			String serializedBean = serializer.serialize(new SenseEventBean(alias, item.getState().toString()));
			
			String serviceUrl = url + apiKey;
			String response = HttpUtil.executeUrl(
				"POST", serviceUrl, IOUtils.toInputStream(serializedBean), "application/json", 5000);
			logger.debug("Stored item '{}' as '{}' in Sen.se and received response: {} ", new String[] { item.getName(), alias, response });
		}
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item) {
		throw new UnsupportedOperationException("The sense service requires aliases for persistence configurations that should match the feed id");
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
				throw new ConfigurationException("sense:apikey", "The Open.Sen.se API-Key is missing - please configure it in openhab.cfg");
			}
			
			initialized = true;
		}
	}
	
}
