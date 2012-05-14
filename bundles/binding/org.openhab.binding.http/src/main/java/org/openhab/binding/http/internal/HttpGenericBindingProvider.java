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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.http.HttpBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides HTTP binding information from it. It registers as a 
 * {@link HttpBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ http=">[ON:POST:http://www.domain.org/home/lights/23871/?status=on] >[OFF:POST:http://www.domain.org/home/lights/23871/?status=off]" }</code></li>
 * 	<li><code>{ http="<[http://www.domain.org/weather/openhabcity/daily:60000:REGEX(.*)]" }</code></li>
 * 	<li><code>{ http=">[ON:POST:http://www.domain.org/home/lights/23871/?status=on] >[OFF:POST:http://www.domain.org/home/lights/23871/?status=off] <[http://www.domain.org/weather/openhabcity/daily:60000:REGEX(.*)]" }</code></li>
 * </ul>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * 
 * @since 0.6.0
 */
public class HttpGenericBindingProvider extends AbstractGenericBindingProvider implements HttpBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(HttpGenericBindingProvider.class);

	/** 
	 * Artificial command for the http-in configuration (which has no command
	 * part by definition). Because we use this artificial command we can reuse
	 * the {@link HttpBindingConfig} for both in- and out-configuration.
	 */
	protected static final Command IN_BINDING_KEY = new Command() {
		public String format(String pattern) {
			throw new UnsupportedOperationException("format is not supported on the command IN_BINDING_KEY");
		}};
	
	/** {@link Pattern} which matches a binding configuration part */
	private static final Pattern BASE_CONFIG_PATTERN = Pattern.compile("(<|>)\\[(.*?)\\]");
	
	/** {@link Pattern} which matches an In-Binding */
	private static final Pattern IN_BINDING_PATTERN = Pattern.compile("(.*?):(?!//)(\\d*):(.*)");
	
	/** {@link Pattern} which matches an Out-Binding */
	private static final Pattern OUT_BINDING_PATTERN = Pattern.compile("(.*?):([A-Z]*):(.*)");
	

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "http";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig != null) {
			HttpBindingConfig config = parseBindingConfig(item, bindingConfig);
			addBindingConfig(item, config);
		}
		else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> process bindingConfig aborted!");
		}
	}
	
	/**
	 * Delegates parsing the <code>bindingConfig</code> with respect to the
	 * first character (<code>&lt;</code> or <code>&gt;</code>) to the 
	 * specialized parsing methods
	 * 
	 * @param item
	 * @param bindingConfig
	 * 
	 * @throws BindingConfigParseException
	 */
	protected HttpBindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		
		HttpBindingConfig config = new HttpBindingConfig();
		
		Matcher matcher = BASE_CONFIG_PATTERN.matcher(bindingConfig);
		
		if (!matcher.matches()) {
			throw new BindingConfigParseException("bindingConfig '" + bindingConfig + "' doesn't contain a valid binding configuration");
		}
		matcher.reset();
				
		while (matcher.find()) {
			String direction = matcher.group(1);
			String bindingConfigPart = matcher.group(2);
			
			if (direction.equals("<")) {
				config = parseInBindingConfig(item, bindingConfigPart, config);
			}
			else if (direction.equals(">")) {
				config = parseOutBindingConfig(item, bindingConfigPart, config);
			}
			else {
				throw new BindingConfigParseException("Unknown command given! Configuration must start with '<' or '>' ");
			}
		}
		
		return config;
	}

	/**
	 * Parses a http-in configuration by using the regular expression
	 * <code>(.*?):(\\d*):(.*)</code>. Where the groups should contain the
	 * following content:
	 * <ul>
	 * <li>1 - url</li>
	 * <li>2 - refresh interval</li>
	 * <li>3 - the transformation rule</li>
	 * </ul>
	 * @param item 
	 * 
	 * @param bindingConfig the config string to parse
	 * @param config
	 * @return the filled {@link HttpBindingConfig}
	 * 
	 * @throws BindingConfigParseException if the regular expression doesn't match
	 * the given <code>bindingConfig</code>
	 */
	protected HttpBindingConfig parseInBindingConfig(Item item, String bindingConfig, HttpBindingConfig config) throws BindingConfigParseException {
		
		Matcher matcher = IN_BINDING_PATTERN.matcher(bindingConfig);
		
		if (!matcher.matches()) {
			throw new BindingConfigParseException("bindingConfig '" + bindingConfig + "' doesn't represent a valid in-binding-configuration. A valid configuration is matched by the RegExp '(.*?):(?!//)(\\d*):(.*)'");
		}
		matcher.reset();
				
		HttpBindingConfigElement configElement;
		
		while (matcher.find()) {
			configElement = new HttpBindingConfigElement();
			configElement.url = matcher.group(1).replaceAll("\\\\\"", "");
			configElement.refreshInterval = Integer.valueOf(matcher.group(2)).intValue();
			configElement.transformation = matcher.group(3).replaceAll("\\\\\"", "\"");
			
			config.put(IN_BINDING_KEY, configElement);
		}
		
		return config;
	}

	/**
	 * Parses a http-out configuration by using the regular expression
	 * <code>([A-Z]*):([A-Z]*):(.*)</code>. Where the groups should contain the
	 * following content:
	 * <ul>
	 * <li>1 - command</li>
	 * <li>2 - http method</li>
	 * <li>3 - url</li>
	 * </ul>
	 * @param item 
	 * 
	 * @param bindingConfig the config string to parse
	 * @param config
	 * @return the filled {@link HttpBindingConfig}
	 * 
	 * @throws BindingConfigParseException if the regular expression doesn't match
	 * the given <code>bindingConfig</code>
	 */
	protected HttpBindingConfig parseOutBindingConfig(Item item, String bindingConfig, HttpBindingConfig config) throws BindingConfigParseException {
		
		Matcher matcher = OUT_BINDING_PATTERN.matcher(bindingConfig);
		
		if (!matcher.matches()) {
			throw new BindingConfigParseException("bindingConfig '" + bindingConfig + "' doesn't contain a valid out-binding-configuration. A valid configuration is matched by the RegExp '(.*?):([A-Z]*):(.*)'");
		}
		matcher.reset();
		
		HttpBindingConfigElement configElement;
		
		while (matcher.find()) {
			configElement = new HttpBindingConfigElement();
			
			String commandAsString = matcher.group(1);
			Command command = createCommandFromString(item, commandAsString);
			
			configElement.httpMethod = matcher.group(2);
			configElement.url = matcher.group(3).replaceAll("\\\\\"", "");
			config.put(command, configElement);
		}
		
		return config;
	}
	
	/**
	 * Creates a {@link Command} out of the given <code>commandAsString</code>
	 * incorporating the {@link TypeParser}.
	 *  
	 * @param item
	 * @param commandAsString
	 * 
	 * @return an appropriate Command (see {@link TypeParser} for more 
	 * information
	 * 
	 * @throws BindingConfigParseException if the {@link TypeParser} couldn't
	 * create a command appropriately
	 * 
	 * @see {@link TypeParser}
	 */
	private Command createCommandFromString(Item item, String commandAsString) throws BindingConfigParseException {
		
		Command command = TypeParser.parseCommand(
			item.getAcceptedCommandTypes(), commandAsString);
		
		if (command == null) {
			throw new BindingConfigParseException("couldn't create Command from '" + commandAsString + "' ");
		}
		
		return command;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getHttpMethod(String itemName, Command command) {
		HttpBindingConfig config = (HttpBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? config.get(command).httpMethod : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUrl(String itemName, Command command) {
		HttpBindingConfig config = (HttpBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? config.get(command).url : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getUrl(String itemName) {
		HttpBindingConfig config = (HttpBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).url : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getRefreshInterval(String itemName) {
		HttpBindingConfig config = (HttpBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).refreshInterval : 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getTransformation(String itemName) {
		HttpBindingConfig config = (HttpBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).transformation : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<String> getInBindingItemNames() {
		List<String> inBindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			HttpBindingConfig httpConfig = (HttpBindingConfig) bindingConfigs.get(itemName);
			if (httpConfig.containsKey(IN_BINDING_KEY)) {
				inBindings.add(itemName);
			}
		}
		return inBindings;
	}

	
	/**
	 * This is an internal data structure to map commands to 
	 * {@link HttpBindingConfigElement }. There will be map like 
	 * <code>ON->HttpBindingConfigElement</code>
	 */
	static class HttpBindingConfig extends HashMap<Command, HttpBindingConfigElement> implements BindingConfig {
		
        /** generated serialVersion UID */
		private static final long serialVersionUID = 6164971643530954095L;
		
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the HTTP binding 
	 * provider.
	 */
	static class HttpBindingConfigElement implements BindingConfig {
		
		public String httpMethod;
		public String url;
		public int refreshInterval;
		public String transformation;
		
		@Override
		public String toString() {
			return "HttpBindingConfigElement [httpMethod=" + httpMethod
					+ ", url=" + url + ", refreshInterval=" + refreshInterval
					+ ", transformation=" + transformation + "]";
		}
		
	}


}
