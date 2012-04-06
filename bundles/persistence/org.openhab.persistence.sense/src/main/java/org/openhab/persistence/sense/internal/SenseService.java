/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
				logger.warn("The Open.Sen.se API-Key is missing - please configure it in openhab.cfg");
				return;
			}
			
			initialized = true;
		}
	}
	
}
