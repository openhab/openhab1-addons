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
package org.openhab.binding.http.internal;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.openhab.binding.http.internal.HttpGenericBindingProvider.CHANGED_COMMAND_KEY;

import java.util.Calendar;

import org.openhab.binding.http.HttpBindingProvider;
import org.openhab.core.events.AbstractEventSubscriberBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.io.net.http.HttpUtil;
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
	protected void internalReceiveUpdate(String itemName, State newState) {
		formatAndExecute(itemName, CHANGED_COMMAND_KEY, newState);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		formatAndExecute(itemName, command, command);
	}
	
	/**
	 * Finds the corresponding binding provider, replaces formatting markers
	 * in the url (@see java.util.Formatter for further information) and executes
	 * the formatted url. 
	 * 
	 * @param itemName the item context
	 * @param command the executed command or one of the virtual commands 
	 * (see {@link HttpGenericBindingProvider})
	 * @param value the value to be used by the String.format method
	 */
	private void formatAndExecute(String itemName, Command command, Type value) {
		HttpBindingProvider provider = 
			findFirstMatchingBindingProvider(itemName, command);
		
		if (provider == null) {
			logger.trace("doesn't find matching binding provider [itemName={}, command={}]", itemName, command);
			return;
		}
		
		String httpMethod =	provider.getHttpMethod(itemName, command);
		String url = provider.getUrl(itemName, command);
		url = String.format(url, Calendar.getInstance().getTime(), value);
		
		if (isNotBlank(httpMethod) && isNotBlank(url)) {
			HttpUtil.executeUrl(httpMethod, url, provider.getHttpHeaders(itemName, command), null, null, SO_TIMEOUT);
		}
	}
	
	/**
	 * Find the first matching {@link HttpBindingProvider} according to 
	 * <code>itemName</code> and <code>command</code>. 
	 * 
	 * @param itemName
	 * @param command
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
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
