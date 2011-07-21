/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.http.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.http.HttpBindingProvider;
import org.openhab.core.events.AbstractEventSubscriberBinding;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A passive binding to send out HTTP-Requests according to a given command. It
 * could be used to control devices with a HTTP Interface (e.g. Streaming-Radios) 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public class HttpOutBinding extends AbstractEventSubscriberBinding<HttpBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(HttpOutBinding.class);

	/** the default socket timeout when requesting an url */
	private static final int SO_TIMEOUT = 5000;

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void processCommand(String itemName, Command command) {
		
		HttpBindingProvider provider = 
			findFirstMatchingBindingProvider(itemName, command);
		
		if (provider == null) {
			logger.warn("doesn't find matching binding provider [itemName={}, command={}]", itemName, command);
			return;
		}
		
		String httpMethod =	provider.getHttpMethod(itemName, command);
		String url = provider.getUrl(itemName, command);
		
		if (StringUtils.isNotBlank(httpMethod) && StringUtils.isNotBlank(url)) {
			HttpUtil.executeUrl(httpMethod, url, SO_TIMEOUT);
		}
	}
	
	/**
	 * Find the first matching {@link ExecBindingProvider} according to 
	 * <code>itemName</code> and <code>command</code>. 
	 * 
	 * @param itemName
	 * @param command
	 * 
	 * @return the mathing binding provder or <code>null</code> if no binding
	 * provider could be found
	 */
	private HttpBindingProvider findFirstMatchingBindingProvider(String itemName, Command command) {
		
		HttpBindingProvider firstMatchingProvider = null;
		
		for (HttpBindingProvider provider : this.providers) {
			
			String url = provider.getUrl(itemName, command);
			
			if (url != null) {
				firstMatchingProvider = provider;
				break;
			}
		}
		
		return firstMatchingProvider;
	}
	

}
