/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.http.internal;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.openhab.binding.http.internal.HttpGenericBindingProvider.CHANGED_COMMAND_KEY;

import java.util.Calendar;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.http.HttpBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
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
 * @author Pauli Anttila
 * @auther Ben Jones
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
	private static final Pattern EXTRACT_FUNCTION_PATTERN = 
			Pattern.compile("(.*?)\\((.*)\\)");

	/** RegEx to validate a cache config <code>'^(.*?)\\.(url|updateInterval)$'</code> */
	private static final Pattern EXTRACT_CACHE_CONFIG_PATTERN = 
			Pattern.compile("^(.*?)\\.(url|updateInterval)$");

	/** Map table to store cache data */
	private Map<String, CacheConfig> itemCache = new HashMap<String, CacheConfig>();
	private Object itemCacheLock = new Object();
	
	public HttpBinding() {
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
    
    @Override
    public void activate() {
    	super.activate();
    	setProperlyConfigured(true);
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
					
					String response = null;
					
					// check if special URL is used and data should get from
					// cache rather than directly from server
					if (isCacheConfig(url)) {
						logger.debug("item '{}' is fetched from cache", itemName);
						response = getCacheData(url);
					} else if (isValidUrl(url)) {
						logger.debug("item '{}' is about to be refreshed now", itemName);
						response = HttpUtil.executeUrl("GET", url, headers, null, null, timeout);
					} else {
						logger.debug("item '{}' is not a valid URL or is a cache id yet to be initialised ({})", itemName, url);
						continue;
					}
					
					if (response==null) {
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
						
						Class<? extends Item> itemType = provider.getItemType(itemName);
						State state = createState(itemType, transformedResponse);
						
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
	 * Returns a {@link State} which is inherited from the {@link Item}s
	 * accepted DataTypes. The call is delegated to the  {@link TypeParser}. If
	 * <code>item</code> is <code>null</code> the {@link StringType} is used.
	 *  
	 * @param itemType
	 * @param transformedResponse
	 * 
	 * @return a {@link State} which type is inherited by the {@link TypeParser}
	 * or a {@link StringType} if <code>item</code> is <code>null</code> 
	 */
	private State createState(Class<? extends Item> itemType, String transformedResponse) {
		try {
			if (itemType.isAssignableFrom(NumberItem.class)) {
				return DecimalType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(ContactItem.class)) {
				return OpenClosedType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(SwitchItem.class)) {
				return OnOffType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(RollershutterItem.class)) {
				return PercentType.valueOf(transformedResponse);
			} else {
				return StringType.valueOf(transformedResponse);
			}
		} catch (Exception e) {
			logger.debug("Couldn't create state of type '{}' for value '{}'", itemType, transformedResponse);
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
	 * Check a URL is a valid HTTP request
	 * 
	 * @param url
	 * @return true if a valid HTTP request, false otherwise
	 */
	private boolean isValidUrl(String url) {
		if (StringUtils.startsWithIgnoreCase(url, "http://"))
			return true;
		if (StringUtils.startsWithIgnoreCase(url, "https://"))
			return true;
		
		return false;
	}
	
	/**
	 * Synchronized access to the item cache. Do a quick check to see if this
	 * <code>cacheId</code> references a cached item.
	 * 
	 * @param cacheId
	 * @return true if this <code>cacheId</code> is a cached item, false 
	 * otherwise
	 */
	private boolean isCacheConfig(String cacheId) {
		synchronized(itemCacheLock) {
			return itemCache.containsKey(cacheId);
		}
	}

	/**
	 * Synchronized access to the item cache. Checks the <code>cacheId</code>
	 * is a cached item and returns the cached value. If the cache has 
	 * expired, refresh the cache value by making a new HTTP request.
	 * 
	 * @param cacheId
	 * @return the cached (or refreshed) dats
	 */
	private String getCacheData(String cacheId) {
		synchronized(itemCacheLock) {
			// check again in case the cache was cleared in between taking
			// the lock when checking in isCacheConfig() and now
			if (!itemCache.containsKey(cacheId))
				return null;
				
			CacheConfig cacheConfig = itemCache.get(cacheId);
			
			long cacheAge = System.currentTimeMillis() - cacheConfig.lastUpdate;
			boolean cacheNeedsUpdate = cacheAge >= cacheConfig.updateInterval;

			if (cacheNeedsUpdate) {

				// update and store data on cache
				logger.debug("updating cache for '{}' ('{}')", cacheId, cacheConfig.url);
				cacheConfig.data = HttpUtil.executeUrl("GET", cacheConfig.url, null, null, null, timeout);

				if (cacheConfig.data != null)
					cacheConfig.lastUpdate = System.currentTimeMillis();
			}

			return cacheConfig.data;
		}								
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		synchronized(itemCacheLock) {
			// clear any existing cache item configs
			itemCache.clear();
			
			if (config != null) {
				String timeoutString = (String) config.get("timeout");
				if (StringUtils.isNotBlank(timeoutString)) {
					timeout = Integer.parseInt(timeoutString);
				}
				
				String granularityString = (String) config.get("granularity");
				if (StringUtils.isNotBlank(granularityString)) {
					granularity = Integer.parseInt(granularityString);
				}
				
				// Parse page cache config
				
				@SuppressWarnings("unchecked")
				Enumeration<String> keys = config.keys();
				while (keys.hasMoreElements()) {
	
					String key = (String) keys.nextElement();
	
					// the config-key enumeration contains additional keys that we
					// don't want to process here ...
					if ("service.pid".equals(key)) {
						continue;
					}
	
					Matcher matcher = EXTRACT_CACHE_CONFIG_PATTERN.matcher(key);
	
					if (!matcher.matches()) {
						logger.error("given config key '"
								+ key
								+ "' does not follow the expected pattern '<id>.<url|updateInterval>'");
						continue;
					}
	
					matcher.reset();
					matcher.find();
	
					String cacheId = matcher.group(1);
					
					CacheConfig cacheConfig = itemCache.get(cacheId);
	
					if (cacheConfig == null) {
						cacheConfig = new CacheConfig(cacheId);
						itemCache.put(cacheId, cacheConfig);
					}
	
					String configKey = matcher.group(2);
					String value = (String) config.get(key);
	
					if ("url".equals(configKey)) {
						cacheConfig.url = value;
					} else if ("updateInterval".equals(configKey)) {
						cacheConfig.updateInterval = Integer.valueOf(value);
					} else {
						throw new ConfigurationException(configKey,
								"the given configKey '" + configKey
										+ "' is unknown");
					}
				}
	        }
		}
	}
	
	/**
	 * Internal data structure for data cache purposes
	 * 
	 */
	static class CacheConfig {

		/** Cache item id */
		String id;
		
		/** URL where data is fetched */
		String url;
		
		/** Update interval for cache */
		int updateInterval = 0;
		
		/** Variable to store cached data */
		String data;
		
		/** Last time when data is updated */
		long lastUpdate;
		
		public CacheConfig(String id) {
			this.id = id;
		}
		
		@Override
		public String toString() {
			return "CacheConfig [id=" + id + ", url=" + url + ", update interval="
					+ updateInterval + "]";
		}
	}
}
