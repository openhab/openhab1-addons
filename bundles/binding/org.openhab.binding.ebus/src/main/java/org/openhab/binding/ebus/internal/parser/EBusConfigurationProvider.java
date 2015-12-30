/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import java.util.HashMap;
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
import org.codehaus.jackson.type.TypeReference;
import org.openhab.binding.ebus.internal.configuration.TelegramConfiguration;
import org.openhab.binding.ebus.internal.configuration.TelegramValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The configuration provider reads the vendors specific ebus protocol
 * information from the json configuration files. All placeholders (regex)
 * and javascript snippets will be compiled after loading to improve
 * runtime performance.
 * 
 * @author Christian Sowada
 * @since 1.7.0
 */
public class EBusConfigurationProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(EBusConfigurationProvider.class);

	// filter: ??
	private static Pattern P_PLACEHOLDER = Pattern.compile("\\?\\?");

	// filter: (00)
	private static Pattern P_BRACKETS_VALS = Pattern.compile("(\\([0-9A-Z]{2}\\))");

	// filter: (|)
	private static Pattern P_BRACKETS_CLEAN = Pattern.compile("(\\(|\\))");

	// The registry with all loaded configuration entries
	private ArrayList<TelegramConfiguration> telegramRegistry = new ArrayList<TelegramConfiguration>();

	private Map<String, String> loadedFilters = new HashMap<String, String>();

	// The script engine if available
	private Compilable compEngine; 

	/**
	 * Return if the provider is empty.
	 * @return
	 */
	public boolean isEmpty() {
		return telegramRegistry.isEmpty();
	}

	/**
	 * Constructor
	 */
	public EBusConfigurationProvider() {
		final ScriptEngineManager mgr = new ScriptEngineManager();

		// load script engine if available
		if(mgr != null) {
			final ScriptEngine engine = mgr.getEngineByName("JavaScript");

			if(engine == null) {
				logger.warn("Unable to load \"JavaScript\" engine! Skip every eBus value calculated by JavaScript.");

			} else if (engine instanceof Compilable) {
				compEngine = (Compilable) engine;

			}
		}
	}

	/**
	 * Clears all loaded configurations
	 */
	public void clear() {
		if(telegramRegistry != null) {
			telegramRegistry.clear();
		}
	}

	/**
	 * Loads a JSON configuration file by url
	 * @param url The url to a configuration file
	 * @throws IOException Unable to read configuration file
	 * @throws ParseException A invalid json file
	 */
	public void loadConfigurationFile(URL url) throws IOException {

		final ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		final InputStream inputStream = url.openConnection().getInputStream();

		final List<TelegramConfiguration> loadedTelegramRegistry = mapper.readValue(inputStream,
				new TypeReference<List<TelegramConfiguration>>() { } );

		for (Iterator<TelegramConfiguration> iterator = loadedTelegramRegistry.iterator(); iterator.hasNext();) {
			TelegramConfiguration object = iterator.next();
			transformDataTypes(object);

			// check if this filter pattern is already loaded
			String filter = object.getFilterPattern().toString();
			String fileComment = StringUtils.substringAfterLast(url.getFile(), "/") + 
					" >>> " + object.getComment();

			if(loadedFilters.containsKey(filter)) {
				logger.info("Identical filter already loaded ... {} AND {}", 
						loadedFilters.get(filter), fileComment);
			} else {
				loadedFilters.put(filter, fileComment);
			}
		}

		if(loadedTelegramRegistry != null && !loadedTelegramRegistry.isEmpty()) {
			telegramRegistry.addAll(loadedTelegramRegistry);
		}
	}

	/**
	 * @param configurationEntry
	 */
	protected void transformDataTypes(TelegramConfiguration configurationEntry) {

		// Use filter property if set
		if(StringUtils.isNotEmpty(configurationEntry.getFilter())) {
			String filter = configurationEntry.getFilter();
			filter = P_PLACEHOLDER.matcher(filter).replaceAll("[0-9A-Z]{2}");
			logger.trace("Compile RegEx filter: {}", filter);
			configurationEntry.setFilterPattern(Pattern.compile(filter));

		} else {
			// Build filter string

			// Always ignore first two hex bytes
			String filter = "[0-9A-Z]{2} [0-9A-Z]{2}";

			// Add command to filter string
			if(StringUtils.isNotEmpty(configurationEntry.getCommand())) {
				filter += " " + configurationEntry.getCommand();
				filter += " [0-9A-Z]{2}";
			}

			// Add data to filter string
			if(StringUtils.isNotEmpty(configurationEntry.getData())) {
				Matcher matcher = P_BRACKETS_VALS.matcher(configurationEntry.getData());
				filter += " " + matcher.replaceAll("[0-9A-Z]{2}");
			}

			// Finally add .* to end with everything
			filter += " .*";

			logger.trace("Compile RegEx filter: {}", filter);
			configurationEntry.setFilterPattern(Pattern.compile(filter));
		}

		// remove brackets if used
		if(StringUtils.isNotEmpty(configurationEntry.getData())) {
			Matcher matcher = P_BRACKETS_CLEAN.matcher(configurationEntry.getData());
			configurationEntry.setData(matcher.replaceAll(""));
		}

		// compile scipt's if available also once
		if(configurationEntry.getValues() != null && !configurationEntry.getValues().isEmpty()) {
			Map<String, TelegramValue> values = configurationEntry.getValues();
			for (Entry<String, TelegramValue> entry : values.entrySet()) {
				if(StringUtils.isNotEmpty(entry.getValue().getScript())) {
					String script = entry.getValue().getScript();

					// check if engine is available
					if(StringUtils.isNotEmpty(script) && compEngine != null) {
						try {
							CompiledScript compile = compEngine.compile(script);
							entry.getValue().setCsript(compile);
						} catch (ScriptException e) {
							logger.error("Error while compiling JavaScript!", e);
						}
					}
				}
			}
		}

		// compile scipt's if available
		if(configurationEntry.getComputedValues() != null && !configurationEntry.getComputedValues().isEmpty()) {
			Map<String, TelegramValue> cvalues = configurationEntry.getComputedValues();
			for (Entry<String, TelegramValue> entry : cvalues.entrySet()) {
				if(StringUtils.isNotEmpty(entry.getValue().getScript())) {
					String script = entry.getValue().getScript();

					// check if engine is available
					if(StringUtils.isNotEmpty(script) && compEngine != null) {
						try {
							CompiledScript compile = compEngine.compile(script);
							entry.getValue().setCsript(compile);
						} catch (ScriptException e) {
							logger.error("Error while compiling JavaScript!", e);
						}
					}
				}
			}
		}

	}

	/**
	 * Return all configuration which filter match the bufferString paramter
	 * @param bufferString The byte string to check against all loaded filters
	 * @return All configurations with matching filter
	 */
	public List<TelegramConfiguration> getCommandsByFilter(String bufferString) {

		final List<TelegramConfiguration> matchedTelegramRegistry = new ArrayList<TelegramConfiguration>();

		/** select matching telegram registry entries */
		for (TelegramConfiguration registryEntry : telegramRegistry) {
			Pattern pattern = registryEntry.getFilterPattern();
			Matcher matcher = pattern.matcher(bufferString);
			if(matcher.matches()) {
				matchedTelegramRegistry.add(registryEntry);
			}
		}

		return matchedTelegramRegistry;
	}

	/**
	 * Return all configurations by command id and class
	 * @param commandId The command id
	 * @return All matching configurations
	 */
	public TelegramConfiguration getCommandById(String commandId) {

		String[] idElements = StringUtils.split(commandId, ".");
		String commandClass = null;
		commandId = null;

		if(idElements.length > 1) {
			commandClass = idElements[0];
			commandId = idElements[1];
		}

		for (TelegramConfiguration entry : telegramRegistry) {
			if(StringUtils.equals(entry.getId(), commandId) && StringUtils.equals(entry.getClazz(), commandClass)) {
				return entry;
			}
		}

		return null;
	}
}
