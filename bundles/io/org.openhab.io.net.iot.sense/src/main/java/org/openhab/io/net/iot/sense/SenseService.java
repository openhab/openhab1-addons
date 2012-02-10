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
package org.openhab.io.net.iot.sense;

import java.util.Dictionary;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.io.net.http.HttpUtil;
import org.openhab.io.net.iot.IoTService;
import org.openhab.io.net.iot.sense.internal.SenseEventBean;
import org.openhab.io.net.iot.sense.internal.SenseEventTransformer;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONSerializer;


/**
 * This is the implementation of the Open.Sen.se {@link IoTService}. To learn
 * more about Open.Sen.se please visit their <a href="http://open.sen.se/">website</a>.
 * 
 * @author Juanker Atina
 * @author Thomas.Eichstaedt-Engelen
 */
public class SenseService implements IoTService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(SenseService.class);
	
	private static String apiKey;
	private static String url;
	
	private final static String DEFAULT_EVENT_URL = "http://api.sen.se/events/?sense_key=";

	private boolean initialized = false;
	

	/**
	 * @{inheritDoc}
	 */
	public String getName() {
		return "sen.se";
	}

	/**
	 * @{inheritDoc}
	 */
	public void send(String feedId, String value) {
		if (initialized) {
			JSONSerializer serializer = 
				new JSONSerializer().transform(new SenseEventTransformer(), SenseEventBean.class);
			String res = serializer.serialize(new SenseEventBean(feedId, value));
			
			String serviceUrl = SenseService.url + SenseService.apiKey;
			String responseBody = HttpUtil.executeUrl(
				"POST", serviceUrl, IOUtils.toInputStream(res), "application/json", 5000);
			logger.debug("Sent event to Sen.se with response message: " + responseBody);
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config!=null) {

			SenseService.url = (String) config.get("url");
			if (StringUtils.isBlank(SenseService.url)) {
				SenseService.url = DEFAULT_EVENT_URL;
			}
			
			SenseService.apiKey = (String) config.get("apikey");
			if (StringUtils.isBlank(SenseService.apiKey)) {
				logger.warn("The Open.Sen.se API-Key is missing - please configure at openhab.cfg");
				return;
			}
			
			initialized = true;
		}
	}
	
}
