/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.http.HttpBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.TypeParser;
import org.openhab.io.net.http.HttpUtil;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * An active binding which requests a given URL frequently.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @since 0.6.0
 */
public class HttpBinding extends AbstractActiveBinding<HttpBindingProvider> implements ManagedService {

	static final Logger logger = LoggerFactory.getLogger(HttpBinding.class);
	
	/** the timeout to use for connecting to a given host (defaults to 5000 milliseconds) */
	private int timeout = 5000;

	/** the interval to find new refresh candidates (defaults to 1000 milliseconds)*/ 
	private int granularity = 1000;
	
	private Map<String, Long> lastUpdateMap = new HashMap<String, Long>();
	
	/** RegEx to extract a parse a function String <code>'(.*?)\((.*)\)'</code> */
	private static final Pattern EXTRACT_FUNCTION_PATTERN = Pattern.compile("(.*?)\\((.*)\\)");

	// TODO: TEE: remove dependency to ItemRegistry since bindings should be stateless
	private ItemRegistry itemRegistry;
	
	
	public HttpBinding() {
		
	}
	
	
	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}
	
	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}
	
    /**
     * @{inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
    	return granularity;
    }
    
    @Override
    protected String getName() {
    	return "HTTP Refresh Service";
    }
    
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
	 * @{inheritDoc}
	 */
	@Override
	public boolean isProperlyConfigured() {
		return true;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void execute() {
		
		for (HttpBindingProvider provider : providers) {
			for (String itemName : provider.getInBindingItemNames()) {
				
				String url = provider.getUrl(itemName);
				url = String.format(url, Calendar.getInstance().getTime());

				Properties headers = provider.getHttpHeaders(itemName);
				int refreshInterval = provider.getRefreshInterval(itemName);
				String transformation = provider.getTransformation(itemName);
				
				Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
				if (lastUpdateTimeStamp == null) {
					lastUpdateTimeStamp = 0L;
				}
				
				long age = System.currentTimeMillis() - lastUpdateTimeStamp;
				boolean needsUpdate = age >= refreshInterval;
				
				if (needsUpdate) {
					
					logger.debug("item '{}' is about to be refreshed now", itemName);
					
					String response = HttpUtil.executeUrl("GET", url, headers, null, null, timeout);
					
					if(response==null) {
						logger.error("No response received from '{}'", url);
					} else {
						String transformedResponse;
						
						try {
							String[] parts = splitTransformationConfig(transformation);
							String transformationType = parts[0];
							String transformationFunction = parts[1];
							
							TransformationService transformationService = 
								TransformationHelper.getTransformationService(HttpActivator.getContext(), transformationType);
							if (transformationService != null) {
								transformedResponse = transformationService.transform(transformationFunction, response);
							} else {
								transformedResponse = response;
								logger.warn("couldn't transform response because transformationService of type '{}' is unavailable", transformationType);
							}
						}
						catch (TransformationException te) {
							logger.error("transformation throws exception [transformation="
									+ transformation + ", response=" + response + "]", te);
							
							// in case of an error we return the response without any
							// transformation
							transformedResponse = response;
						}
						
						logger.debug("transformed response is '{}'", transformedResponse);
						
						Item item = getItemFromItemName(itemName);
						State state = createState(item, transformedResponse);
						
						if (state != null) {
							eventPublisher.postUpdate(itemName, state);
						}
					}
					
					lastUpdateMap.put(itemName, System.currentTimeMillis());
				}					
			}
		}
	}
	
	/**
	 * Splits a transformation configuration string into its two parts - the
	 * transformation type and the function/pattern to apply.
	 * 
	 * @param transformation the string to split
	 * @return a string array with exactly two entries for the type and the function
	 */
	protected String[] splitTransformationConfig(String transformation) {
		Matcher matcher = EXTRACT_FUNCTION_PATTERN.matcher(transformation);
		
		if (!matcher.matches()) {
			throw new IllegalArgumentException("given transformation function '" + transformation + "' does not follow the expected pattern '<function>(<pattern>)'");
		}
		matcher.reset();
		
		matcher.find();			
		String type = matcher.group(1);
		String pattern = matcher.group(2);
	
		return new String[] { type, pattern };
	}

	/**
	 * Returns the {@link Item} for the given <code>itemName</code> or 
	 * <code>null</code> if there is no or to many corresponding Items
	 * 
	 * @param itemName
	 * 
	 * @return the {@link Item} for the given <code>itemName</code> or 
	 * <code>null</code> if there is no or to many corresponding Items
	 */
	private Item getItemFromItemName(String itemName) {
		try {
			return itemRegistry.getItem(itemName);
		} catch (ItemNotFoundException e) {
			logger.error("couldn't find item for itemName '" + itemName + "'");
		}
		
		return null;
	}
	
	/**
	 * Returns a {@link State} which is inherited from the {@link Item}s
	 * accepted DataTypes. The call is delegated to the  {@link TypeParser}. If
	 * <code>item</code> is <code>null</code> the {@link StringType} is used.
	 *  
	 * @param item
	 * @param transformedResponse
	 * 
	 * @return a {@link State} which type is inherited by the {@link TypeParser}
	 * or a {@link StringType} if <code>item</code> is <code>null</code> 
	 */
	private State createState(Item item, String transformedResponse) {
		
		if (item != null) {
			return TypeParser.parseState(item.getAcceptedDataTypes(), transformedResponse);
		}
		else {
			return StringType.valueOf(transformedResponse);
		}
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
			HttpUtil.executeUrl(httpMethod, url, provider.getHttpHeaders(itemName, command), null, null, timeout);
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

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		
		if (config != null) {
			String timeoutString = (String) config.get("timeout");
			if (StringUtils.isNotBlank(timeoutString)) {
				timeout = Integer.parseInt(timeoutString);
			}
			
			String granularityString = (String) config.get("granularity");
			if (StringUtils.isNotBlank(granularityString)) {
				granularity = Integer.parseInt(granularityString);
			}
		}

	}
	

}
