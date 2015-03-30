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
import org.openhab.binding.ebus.internal.EBusTelegram;
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

//	private Map<String, Object> settings;

	// The configuration provider to parse the ebus telegram
	private EBusConfigurationProvider configurationProvider; 

	/**
	 * Constructor
	 * @param configurationProvider
	 */
	public EBusTelegramParser(EBusConfigurationProvider configurationProvider) {
		this.configurationProvider = configurationProvider;
	}

	/**
	 * @param byteBuffer
	 * @param settings 
	 * @param type
	 * @param pos
	 * @return
	 */
	private Object getValue(ByteBuffer byteBuffer, Map<String, Object> settings) {
		
		String type = ((String) settings.get("type")).toLowerCase();
		int pos = settings.containsKey("pos") ? ((Integer) settings.get("pos")).intValue() : -1;

		// load possible min, max, replace values and a factor from configuration
		BigDecimal valueMin = NumberUtils.toBigDecimal(settings.get("min"));
		BigDecimal valueMax = NumberUtils.toBigDecimal(settings.get("max"));
		BigDecimal replaceValue = NumberUtils.toBigDecimal(settings.get("replaceValue"));
		BigDecimal factor = NumberUtils.toBigDecimal(settings.get("factor"));
		
		
		Object value = null;
		byte hByte = 0;
		byte lByte = 0;

		BigDecimal repVal = null;

		// requested pos is greater as whole buffer
		if(pos > byteBuffer.position()) {
			//FIXME: Do something
			logger.warn("eBus buffer pos error! Can happen ...");
		}

		if(type.equals("data2b")) {
			hByte = byteBuffer.get(pos);
			lByte = byteBuffer.get(pos-1);
			repVal = BigDecimal.valueOf(-128);
			value = new BigDecimal(EBusUtils.decodeDATA2b(hByte, lByte));

		} else if(type.equals("data2c")) {
			hByte = byteBuffer.get(pos);
			lByte = byteBuffer.get(pos-1);
			repVal = BigDecimal.valueOf(-2048);
			value = new BigDecimal(EBusUtils.decodeDATA2c(hByte, lByte));

		} else if(type.equals("data1c")) {
			lByte = byteBuffer.get(pos-1);
			repVal = BigDecimal.valueOf(255);
			value = new BigDecimal(EBusUtils.decodeDATA1c(lByte));

		} else if(type.equals("data1b")) {
			lByte = byteBuffer.get(pos-1);
			repVal = BigDecimal.valueOf(-128);
			value = new BigDecimal(EBusUtils.decodeDATA1b(lByte));

		} else if(type.equals("bcd")) {
			lByte = byteBuffer.get(pos-1);
			repVal = BigDecimal.valueOf(266);
			value = new BigDecimal(EBusUtils.decodeBCD(lByte));

		} else if(type.equals("word")) {
			hByte = byteBuffer.get(pos);
			lByte = byteBuffer.get(pos-1);
			repVal = BigDecimal.valueOf(65535);
			value = new BigDecimal(EBusUtils.decodeWORD(hByte, lByte));

		} else if(type.equals("uchar") || type.equals("byte")) {
			repVal = BigDecimal.valueOf(255);
			value = new BigDecimal(byteBuffer.get(pos-1) & 0xFF);

		} else if(type.equals("char")) {
			repVal = BigDecimal.valueOf(255);
			value = new BigDecimal(byteBuffer.get(pos-1));

		} else if(type.equals("bit")) {
			int bit = ((Integer) settings.get("bit"));
			value = byteBuffer.get(pos-1);

			boolean isSet = ((Byte)value >> bit& 0x1) == 1;
			value = isSet;

		} else {
			logger.warn("Configuration Error: Unknown command type! {}", type);

		}

		// if replace value paramter set
		if(replaceValue != null) {
			repVal = replaceValue;
		}

		// if BigDecimal check for min, max and replace value
		if(value instanceof BigDecimal) {
			BigDecimal b = (BigDecimal)value;

			// equals replace value, than return null
			if(repVal != null && b.compareTo(repVal) == 0) {
				logger.trace("Replace value found, skip value ...");
				value = b = null;
			}
			
			// multiply before check min and max
			if(b != null && factor != null) {
				logger.trace("Value multiplied ...");
				value = b = b.multiply(factor);
			}
			
			// value is below min value, return null
			if(valueMin != null && b != null && b.compareTo(valueMin) == -1) {
				logger.trace("Minimal value reached, skip value ...");
				value = b = null;
				
			// value is above max value, return null
			} else if (valueMax != null && b != null && b.compareTo(valueMax) == 1) {
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
	private Object evaluateScript(Entry<String, Map<String, Object>> entry, Map<String, Object> scopeValues) throws ScriptException {
		
		Object value = null;
		
		// executes compiled script
		if(entry.getValue().containsKey("cscript")) {
			CompiledScript cscript = (CompiledScript) entry.getValue().get("cscript");

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

			Object word = i == data.length-1 ? "---" : EBusUtils.decodeWORD(data[i+1], data[i]);
			Object data2b = i == data.length-1 ? "---" : EBusUtils.decodeDATA2b(data[i+1], data[i]);
			Object data2c = i == data.length-1 ? "---" : EBusUtils.decodeDATA2c(data[i+1], data[i]);
			Object data1c = i == data.length-1 ? "---" : EBusUtils.decodeDATA1c(data[i+1]);
			
			int bcd = EBusUtils.decodeBCD(data[i]);
			int uint = data[i] & 0xFF;
			
			format = String.format("%-4s%-13s%-13s%-13s%-13s%-13s%-13s", i+6, word, uint, data2b, data2c, data1c, bcd);
			loggerBrutforce.trace("    " + format);
		}

		// Parse slave data
		if(telegram.getType() == EBusTelegram.MASTER_SLAVE) {
			data = telegram.getSlaveData();

			loggerBrutforce.trace("    ---------------------------------- Answer ----------------------------------");

			for (int i = 0; i < data.length; i++) {

				Object word = i == data.length-1 ? "---" : EBusUtils.decodeWORD(data[i+1], data[i]);
				Object data2b = i == data.length-1 ? "---" : EBusUtils.decodeDATA2b(data[i+1], data[i]);
				Object data2c = i == data.length-1 ? "---" : EBusUtils.decodeDATA2c(data[i+1], data[i]);
				Object data1c = i == data.length-1 ? "---" : EBusUtils.decodeDATA1c(data[i+1]);
				
				int bcd = EBusUtils.decodeBCD(data[i]);
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
			// FIXME: Return empty map
			return null;
		}

		// Secure null check
		if(telegram == null) {
			// FIXME: Return empty map
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
		final List<Map<String, Object>> matchedTelegramRegistry = configurationProvider.getCommandsByFilter(bufferString);

		loggerAnalyses.debug(bufferString);

		// No registry entries found, so this is a unknown telegram
		if(matchedTelegramRegistry.isEmpty()) {
			loggerAnalyses.debug("  >>> Unknown ----------------------------------------");
			if(loggerBrutforce.isTraceEnabled()) {
				loggerBrutforce.trace(bufferString);
				bruteforceEBusTelegram(telegram);
			}

			// FIXME: Return empty map
			return null;
		}

		// loop thru all matching telegrams from registry
		for (Map<String, Object> registryEntry : matchedTelegramRegistry) {

			int debugLevel = 0;
			
			// get class key if used
			String classKey = registryEntry.containsKey("class") ? 
					(String) registryEntry.get("class") : "";

			// load debug level for this configuration entry if available
			if(registryEntry.containsKey("debug")) {
				debugLevel = ((Integer)registryEntry.get("debug"));
			}

			// get values block of configuration
			@SuppressWarnings("unchecked")
			Map<String, Map<String, Object>> values = (Map<String, Map<String, Object>>) registryEntry.get("values");

			// debug
			loggerAnalyses.debug("  >>> {}", registryEntry.containsKey("comment") ? 
					registryEntry.get("comment") : "<No comment available>");

			Map<String, Object> settings = null;
			
			// loop over all entries
			for (Entry<String, Map<String, Object>> entry : values.entrySet()) {

				String uniqueKey = (classKey != "" ? classKey + "." : "") + entry.getKey();
				settings = entry.getValue();

				// Extract the value from byte buffer
				Object value = getValue(byteBuffer, entry.getValue());
				
				// If compiled script available for this key, execute it now
				if(settings.containsKey("cscript")) {
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
				String label = (String) (settings.containsKey("label") ? settings.get("label") : "");
				String format = String.format("%-35s%-10s%s", uniqueKey, value, label);
				if(debugLevel >= 2) {
					loggerAnalyses.debug("    >>> " + format);
				} else {
					loggerAnalyses.trace("    >>> " + format);
				}

				// Add result to registry
				valueRegistry.put(uniqueKey, value);
				
				// Add result to temp. short key registry, used for scripts
				valueRegistryShortKeys.put(entry.getKey(), value);
			}

			// computes values available? if not exit here
			if(!registryEntry.containsKey("computed_values"))
				continue;

			// post execute the computes_values block
			@SuppressWarnings("unchecked")
			Map<String, Map<String, Object>> cvalues = (Map<String, Map<String, Object>>) registryEntry.get("computed_values");
			for (Entry<String, Map<String, Object>> entry : cvalues.entrySet()) {
				
				String uniqueKey = (classKey != "" ? classKey + "." : "") + entry.getKey();
				
				// Add all values to script scope
				HashMap<String, Object> bindings = new HashMap<String, Object>();
				bindings.putAll(valueRegistryShortKeys);
				bindings.putAll(valueRegistry);
				
				Object value;
				try {
					// Evaluates script
					value = evaluateScript(entry, bindings);
					
					// Add result to registry
					valueRegistry.put(entry.getKey(), value);

					if(debugLevel >= 2) {
						String label = (String) (settings.containsKey("label") ? settings.get("label") : "");
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
