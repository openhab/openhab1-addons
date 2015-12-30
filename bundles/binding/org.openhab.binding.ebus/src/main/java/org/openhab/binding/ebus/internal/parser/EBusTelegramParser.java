/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.parser;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptException;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ebus.internal.EBusTelegram;
import org.openhab.binding.ebus.internal.configuration.TelegramConfiguration;
import org.openhab.binding.ebus.internal.configuration.TelegramValue;
import org.openhab.binding.ebus.internal.utils.EBusCodecUtils;
import org.openhab.binding.ebus.internal.utils.EBusUtils;
import org.openhab.binding.ebus.internal.utils.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class parses received eBus telegrams from eBus and convert them to
 * a result map with keys and resolved values.
 * 
 * @author Christian Sowada
 * @since 1.7.0
 */
public class EBusTelegramParser {

	private static final Logger logger = LoggerFactory
			.getLogger(EBusTelegramParser.class);

	private static final Logger loggerAnalyses = LoggerFactory
			.getLogger(EBusTelegramParser.class.getPackage().getName() + ".Analyses");

	private static final Logger loggerBrutforce = LoggerFactory
			.getLogger(EBusTelegramParser.class.getPackage().getName() + ".BruteForce");

	// The configuration provider to parse the ebus telegram
	private EBusConfigurationProvider configurationProvider;

	private EBusTelegramCSVWriter debugWriter;

	private String debugWriteMode; 

	/**
	 * Constructor
	 * @param configurationProvider
	 */
	public EBusTelegramParser(EBusConfigurationProvider configurationProvider) {
		this.configurationProvider = configurationProvider;
	}

	/**
	 * Sets the csv writer to log telegrams
	 * @param debugWriter
	 * @param debugWriteMode
	 */
	public void setDebugCSVWriter(EBusTelegramCSVWriter debugWriter, String debugWriteMode) {
		this.debugWriter = debugWriter;
		this.debugWriteMode = debugWriteMode;
	}

	/**
	 * @param byteBuffer
	 * @param telegramValue 
	 * @param type
	 * @param pos
	 * @return
	 */
	private Object getValue(ByteBuffer byteBuffer, TelegramValue telegramValue) {

		String type = telegramValue.getType().toLowerCase();
		int pos = telegramValue.getPos() != null ? telegramValue.getPos() : -1;

		Object value = null;

		// requested pos is greater as whole buffer
		if(pos > byteBuffer.position()) {
			logger.warn("eBus buffer pos error! Can happen ...");
		}

		// replace similar data types
		if(type.equals("uint"))
			type = "word";
		if(type.equals("byte"))
			type = "uchar";

		byte[] bytes = null;
		if(type.equals("data2b") || type.equals("data2c") || type.equals("word")) {
			bytes = new byte[] {byteBuffer.get(pos), byteBuffer.get(pos-1)};
		} else {
			bytes = new byte[] {byteBuffer.get(pos-1)};
		}

		if(type.equals("bit")) {
			int bit = telegramValue.getBit();
			value = bytes[0];

			boolean isSet = ((Byte)value >> bit& 0x1) == 1;
			value = isSet;

		} else {
			value = NumberUtils.toBigDecimal(EBusCodecUtils.decode(type, bytes, telegramValue.getReplaceValue()));
		}

		// if BigDecimal check for min, max and replace value
		if(value instanceof BigDecimal) {
			BigDecimal b = (BigDecimal)value;

			// multiply before check min and max
			if(b != null && telegramValue.getFactor() != null) {
				logger.trace("Value multiplied ...");
				value = b = b.multiply(telegramValue.getFactor());
			}

			// value is below min value, return null
			if(telegramValue.getMin() != null && b != null && b.compareTo(telegramValue.getMin()) == -1) {
				logger.trace("Minimal value reached, skip value ...");
				value = b = null;

				// value is above max value, return null
			} else if (telegramValue.getMax() != null && b != null && b.compareTo(telegramValue.getMax()) == 1) {
				logger.trace("Maximal value reached, skip value ...");
				value = b = null;
			}

		}

		return value;
	}

	/**
	 * Evaluates the compiled script of a entry.
	 * @param entry The configuration entry to evaluate
	 * @param scopeValues All known values for script scope
	 * @return The computed value
	 * @throws ScriptException
	 */
	private Object evaluateScript(Entry<String, TelegramValue> entry, Map<String, Object> scopeValues) throws ScriptException {

		Object value = null;

		// executes compiled script
		if(entry.getValue().getCsript() != null) {
			CompiledScript cscript = entry.getValue().getCsript();

			// Add global variables thisValue and keyName to JavaScript context
			Bindings bindings = cscript.getEngine().createBindings();
			bindings.putAll(scopeValues);
			value = cscript.eval(bindings);
		}

		// try to convert the returned value to BigDecimal
		value = ObjectUtils.defaultIfNull(
				NumberUtils.toBigDecimal(value), value);

		// round to two digits, maybe not optimal for any result
		if(value instanceof BigDecimal) {
			((BigDecimal)value).setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		return value;
	}

	/**
	 * A function show data from unknown telegrams
	 * @param telegram
	 */
	private void bruteforceEBusTelegram(EBusTelegram telegram) {

		byte[] data = telegram.getData();

		String format = String.format("%-4s%-13s%-13s%-13s%-13s%-13s%-13s", "Pos", "WORD", "UInt", "DATA2B", "DATA2C", "DATA1c", "BCD");
		loggerBrutforce.trace("    " + format);
		loggerBrutforce.trace("    -----------------------------------------------------------------------------");

		// Check all possible positions with known data types
		for (int i = 0; i < data.length; i++) {

			Object word = i == data.length-1 ? "---" : EBusCodecUtils.decodeWord(new byte[] {data[i+1], data[i]});
			Object data2b = i == data.length-1 ? "---" : EBusCodecUtils.decodeDATA2b(new byte[] {data[i+1], data[i]});
			Object data2c = i == data.length-1 ? "---" : EBusCodecUtils.decodeDATA2c(new byte[] {data[i+1], data[i]});
			Object data1c = i == data.length-1 ? "---" : EBusCodecUtils.decodeDATA1c(data[i+1]);

			int bcd = EBusCodecUtils.decodeBCD(data[i]);
			int uint = data[i] & 0xFF;

			format = String.format("%-4s%-13s%-13s%-13s%-13s%-13s%-13s", i+6, word, uint, data2b, data2c, data1c, bcd);
			loggerBrutforce.trace("    " + format);
		}

		// Parse slave data
		if(telegram.getType() == EBusTelegram.TYPE_MASTER_SLAVE) {
			data = telegram.getSlaveData();

			loggerBrutforce.trace("    ---------------------------------- Answer ----------------------------------");

			for (int i = 0; i < data.length; i++) {

				Object word = i == data.length-1 ? "---" : EBusCodecUtils.decodeWord(new byte[] {data[i+1], data[i]});
				Object data2b = i == data.length-1 ? "---" : EBusCodecUtils.decodeDATA2b(new byte[] {data[i+1], data[i]});
				Object data2c = i == data.length-1 ? "---" : EBusCodecUtils.decodeDATA2c(new byte[] {data[i+1], data[i]});
				Object data1c = i == data.length-1 ? "---" : EBusCodecUtils.decodeDATA1c(data[i+1]);

				int bcd = EBusCodecUtils.decodeBCD(data[i]);
				int uint = data[i] & 0xFF;

				format = String.format("%-4s%-13s%-13s%-13s%-13s%-13s%-13s", i+6, word, uint, data2b, data2c, data1c, bcd);
				loggerBrutforce.trace("    " + format);
			}

		}
	}

	/**
	 * Parses a valid eBus telegram and returns a map with key/values based on
	 * configuration registry.
	 * @param telegram The eBus telegram
	 * @return A Map with parsed key/values
	 */
	public Map<String, Object> parse(EBusTelegram telegram) {

		// Check if a configuration provider is set
		if(configurationProvider == null) {
			logger.error("Configuration not loaded, can't parse telegram!");
			return null;
		}

		// Secure null check
		if(telegram == null) {
			return null;
		}

		// All parsed values
		final Map<String, Object> valueRegistry = new HashMap<String, Object>();

		// All parsed values with short keys, used for script evaluation
		final Map<String, Object> valueRegistryShortKeys = new HashMap<String, Object>();

		// Get as byte buffer
		final ByteBuffer byteBuffer = telegram.getBuffer();

		// Get hex string for debugging
		final String bufferString = EBusUtils.toHexDumpString(byteBuffer).toString();

		// queries the configuration provider for matching registry entries
		final List<TelegramConfiguration> matchedTelegramRegistry = configurationProvider.getCommandsByFilter(bufferString);

		loggerAnalyses.debug(bufferString);

		// No registry entries found, so this is a unknown telegram
		if(matchedTelegramRegistry.isEmpty()) {

			if(debugWriter != null && (debugWriteMode.contains("unknown") || 
					debugWriteMode.contains("all"))) {
				debugWriter.writeTelegram(telegram, "<unknown>");
			}

			loggerAnalyses.debug("  >>> Unknown ----------------------------------------");
			if(loggerBrutforce.isTraceEnabled()) {
				loggerBrutforce.trace(bufferString);
				bruteforceEBusTelegram(telegram);
			}

			return null;
		}

		// loop thru all matching telegrams from registry
		for (TelegramConfiguration registryEntry : matchedTelegramRegistry) {

			int debugLevel = 0;

			// get id and class key if used
			String idKey = StringUtils.defaultString(registryEntry.getId());
			String classKey = StringUtils.defaultString(registryEntry.getClazz());

			// load debug level for this configuration entry if available
			if(registryEntry.getDebug() != null) {
				debugLevel = registryEntry.getDebug();

				if(debugWriter != null && debugWriteMode.contains("debug")) {
					debugWriter.writeTelegram(telegram, "DEBUG:" + (String) registryEntry.getComment());
				}
			}

			if(debugWriter != null && debugWriteMode.equals("all")) {
				debugWriter.writeTelegram(telegram, registryEntry.getComment());
			}

			// get values block of configuration
			Map<String, TelegramValue> values = registryEntry.getValues();

			// debug
			loggerAnalyses.debug("  >>> {}", StringUtils.defaultIfEmpty(
					registryEntry.getComment(), "<No comment available>"));

			TelegramValue settings = null;

			// loop over all entries
			for (Entry<String, TelegramValue> entry : values.entrySet()) {

				String uniqueKey = (classKey != "" ? classKey + "." : "") + (idKey != "" ? idKey + "." : "") + entry.getKey();
				settings = entry.getValue();

				// Extract the value from byte buffer
				Object value = getValue(byteBuffer, entry.getValue());

				// If compiled script available for this key, execute it now
				if(settings.getCsript() != null) {
					try {

						// Add global variables thisValue and keyName to JavaScript context
						HashMap<String, Object> bindings = new HashMap<String, Object>();
						bindings.put(entry.getKey(), value);	// short key
						bindings.put(uniqueKey, value);			// full key
						bindings.put("thisValue", value);		// alias thisValue

						// Evaluates script
						value = evaluateScript(entry, bindings);

					} catch (ScriptException e) {
						logger.error("Error on evaluating JavaScript!", e);
						break;
					}
				}

				// debug
				String label = StringUtils.defaultString(settings.getLabel());
				String format = String.format("%-35s%-10s%s", uniqueKey, value, label);
				String alias = null;

				if(settings.getMapping() != null) {
					Map<String, String> mapping = settings.getMapping();
					alias = mapping.get(value.toString());
				}

				if(debugLevel >= 2) {
					loggerAnalyses.debug("    >>> " + format);
					if(alias != null)
						loggerAnalyses.debug("      >>> " + alias);
				} else {
					loggerAnalyses.trace("    >>> " + format);
					if(alias != null)
						loggerAnalyses.trace("      >>> " + alias);
				}

				// Add result to registry
				valueRegistry.put(uniqueKey, value);

				// Add result to temp. short key registry, used for scripts
				valueRegistryShortKeys.put(entry.getKey(), value);
				
				// also use class.id as key as shortcut if we have only one value
				if(values.size() == 1) {
					if(!StringUtils.isEmpty(classKey) && !StringUtils.isEmpty(idKey)) {
						uniqueKey = classKey + "." + idKey;
						valueRegistry.put(uniqueKey, value);
					}
				}
			}
			
			// computes values available? if not exit here
			if(registryEntry.getComputedValues() == null)
				continue;

			// post execute the computes_values block
			Map<String, TelegramValue> cvalues = registryEntry.getComputedValues();
			for (Entry<String, TelegramValue> entry : cvalues.entrySet()) {

				String uniqueKey = (classKey != "" ? classKey + "." : "") + (idKey != "" ? idKey + "." : "") + entry.getKey();

				// Add all values to script scope
				HashMap<String, Object> bindings = new HashMap<String, Object>();
				bindings.putAll(valueRegistryShortKeys);
				bindings.putAll(valueRegistry);

				Object value;
				try {
					// Evaluates script
					value = evaluateScript(entry, bindings);

					// Add result to registry
					valueRegistry.put(uniqueKey, value);

					if(debugLevel >= 2) {
						String label = StringUtils.defaultString(settings.getLabel());
						String format = String.format("%-35s%-10s%s", uniqueKey, value, label);
						loggerAnalyses.debug("    >>> " + format);
					}

				} catch (ScriptException e) {
					logger.error("Error on evaluating JavaScript!", e);
				}
			}
		}

		return valueRegistry;
	}

}
