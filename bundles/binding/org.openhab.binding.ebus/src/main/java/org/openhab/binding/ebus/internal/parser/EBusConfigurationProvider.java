/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christian Sowada
 * @since 1.7.0
 */
public class EBusConfigurationProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(EBusConfigurationProvider.class);

	private ArrayList<Map<String, Object>> telegramRegistry = new ArrayList<>();

	private Compilable compEngine; 

	public boolean isEmpty() {
		return telegramRegistry.isEmpty();
	}
	
	/**
	 * Constructor
	 */
	public EBusConfigurationProvider() {
		final ScriptEngineManager mgr = new ScriptEngineManager();
		final ScriptEngine engine = mgr.getEngineByName("JavaScript");
		
		if(engine == null) {
			logger.warn("Unable to load \"JavaScript\" engine! Skip every eBus value calculated by JavaScript.");
			
		} else if (engine instanceof Compilable) {
			compEngine = (Compilable) engine;
			
		}
	}

	public void clear() {
		if(telegramRegistry != null) {
			telegramRegistry.clear();
		}
	}
	
	/**
	 * @param url
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public void loadConfigurationFile(URL url) throws IOException {

		final ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		final InputStream inputStream = url.openConnection().getInputStream();

		final ArrayList<Map<String, Object>> loadedTelegramRegistry = 
				(ArrayList<Map<String, Object>>) mapper.readValue(inputStream, List.class);
		
		for (Iterator<Map<String, Object>> iterator = loadedTelegramRegistry.iterator(); iterator.hasNext();) {
			Map<String, Object> object = iterator.next();
			transformDataTypes(object);
		}

		if(loadedTelegramRegistry != null && !loadedTelegramRegistry.isEmpty()) {
			telegramRegistry.addAll(loadedTelegramRegistry);
		}
	}

	/**
	 * @param configurationEntry
	 */
	@SuppressWarnings("unchecked")
	protected void transformDataTypes(Map<String, Object> configurationEntry) {
		
		if(configurationEntry.get("filter") instanceof String) {
			String filter = (String)configurationEntry.get("filter");
			filter = filter.replaceAll("\\?\\?", "[0-9A-Z]{2}");
			logger.trace("Compile RegEx filter: {}", filter);
			configurationEntry.put("cfilter", Pattern.compile(filter));
			
		} else {
			String filter = "[0-9A-Z]{2} [0-9A-Z]{2}";
			if(configurationEntry.containsKey("command")) {
				filter += " " + configurationEntry.get("command");
				filter += " [0-9A-Z]{2}";
			}
			
			if(configurationEntry.containsKey("data")) {
				filter += " " + configurationEntry.get("data");
			}
			
			filter += " .*";
			
			logger.trace("Compile RegEx filter: {}", filter);
			configurationEntry.put("cfilter", Pattern.compile(filter));
		}
		
		// compile scipt's if available
		if(configurationEntry.containsKey("values")) {
			Map<String, Map<String, Object>> values = (Map<String, Map<String, Object>>) configurationEntry.get("values");
			for (Entry<String, Map<String, Object>> entry : values.entrySet()) {
				if(entry.getValue().containsKey("script")) {
					String script = (String) entry.getValue().get("script");
					
					// check if engine is available
					if(StringUtils.isNotEmpty(script) && compEngine != null) {
						try {
							CompiledScript compile = compEngine.compile(script);
							entry.getValue().put("cscript", compile);
						} catch (ScriptException e) {
							logger.error("Error while compiling JavaScript!", e);
						}
					}
				}
			}
		}
		
		// compile scipt's if available
		if(configurationEntry.containsKey("computed_values")) {
			Map<String, Map<String, Object>> cvalues = (Map<String, Map<String, Object>>) configurationEntry.get("computed_values");
			for (Entry<String, Map<String, Object>> entry : cvalues.entrySet()) {
				if(entry.getValue().containsKey("script")) {
					String script = (String) entry.getValue().get("script");
					
					// check if engine is available
					if(StringUtils.isNotEmpty(script) && compEngine != null) {
						try {
							CompiledScript compile = compEngine.compile(script);
							entry.getValue().put("cscript", compile);
						} catch (ScriptException e) {
							logger.error("Error while compiling JavaScript!", e);
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * @param bufferString
	 * @return
	 */
	public List<Map<String, Object>> getCommandsByFilter(String bufferString) {

		final List<Map<String, Object>> matchedTelegramRegistry = new ArrayList<Map<String, Object>>();

		/** select matching telegram registry entries */
		for (Map<String, Object> registryEntry : telegramRegistry) {
			Pattern pattern = (Pattern) registryEntry.get("cfilter");
			Matcher matcher = pattern.matcher(bufferString);
			if(matcher.matches()) {
				matchedTelegramRegistry.add(registryEntry);
			}
		}

		return matchedTelegramRegistry;
	}

	/**
	 * @param commandId
	 * @param commandClass
	 * @return
	 */
	public Map<String, Object> getCommandById(String commandId, String commandClass) {
		for (Map<String, Object> entry : telegramRegistry) {
			if(entry.containsKey("id") && entry.get("id").equals(commandId) && 
					entry.containsKey("class") && entry.get("class").equals(commandClass)) {
				return entry;
			}
		}

		return null;
	}

}
