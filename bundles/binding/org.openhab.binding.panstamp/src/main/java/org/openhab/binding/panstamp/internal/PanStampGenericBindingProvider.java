/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panstamp.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.panstamp.PanStampBindingConfig;
import org.openhab.binding.panstamp.PanStampBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Gideon le Grange
 * @since 1.8.0
 */
public class PanStampGenericBindingProvider extends AbstractGenericBindingProvider implements PanStampBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(PanStampGenericBindingProvider.class);

	@Override
	public PanStampBindingConfig<?> getConfig(String itemName) {
		return (PanStampBindingConfig<?>) bindingConfigs.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		logger.debug("getBindingType");
		return "panstamp";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		logger.debug("validateItemType(item=" + item + ",bindingConfig=" + bindingConfig);
		// can't do much cause can't assume we have a running panStamp network so we can't
		// check item against endpoint for compatibility
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		PanStampBindingConfig<?> config;
		try {
			config = parse(item.getName(), bindingConfig);
			addBindingConfig(item, config);
			logger.debug("Created binding config '{}'", config);
		} catch (ValueException e) {
			throw new BindingConfigParseException(e.getMessage());
		}
		super.processBindingConfiguration(context, item, bindingConfig);
	}

	/*
	 * Parse the binding config string and return a new binding config.
	 */
	@SuppressWarnings("rawtypes")
	private PanStampBindingConfig<?> parse(String itemName, String config) throws BindingConfigParseException,
			ValueException {
		Map<String, String> confMap = makeConfigMap(config);
		String unit = confMap.get("unit");
		return new PanStampBindingConfig(itemName, PanStampConversions.asInt("device address", confMap.get("address"),
				1, 0xffff), parseManufacturerId(confMap.get("productCode")),
				parseProductId(confMap.get("productCode")), PanStampConversions.asInt("register",
						confMap.get("register"), 1, 0xff), parseEndpoint(confMap.get("endpoint")),
				(unit != null) ? parseUnit(unit) : "");
	}

	/*
	 * Create a map of key/value pairs from the binding configuration in config.
	 */
	private Map<String, String> makeConfigMap(String config) throws BindingConfigParseException {
		Map<String, String> map = new HashMap<String, String>();
		String parts[] = config.split(",");
		for (String part : parts) {
			String kvp[] = part.split("=");
			if (kvp.length != 2) {
				throw new BindingConfigParseException(String.format("Malformed key/value pair '%s'", part));
			}
			map.put(kvp[0].trim(), kvp[1].trim());
		}
		return map;
	}

	/*
	 * Parse the panStamp product code for the manufacturer ID
	 */
	private int parseManufacturerId(String val) throws BindingConfigParseException, ValueException {
		String pcp[] = val.split("/");
		if (pcp.length != 2) {
			throw new BindingConfigParseException(String.format("Malformed product code pair '%s'", val));
		}
		return PanStampConversions.asInt("manufacturer id", pcp[0].trim(), 1, 0xffff);
	}

	/*
	 * Parse the panStamp product code for the product ID
	 */
	private int parseProductId(String val) throws BindingConfigParseException, ValueException {
		String pcp[] = val.split("/");
		if (pcp.length != 2) {
			throw new BindingConfigParseException(String.format("Malformed product code pair '%s'", val));
		}
		return PanStampConversions.asInt("product id", pcp[1].trim(), 1, 0xffff);
	}

	/*
	 * Parse the binding endpoint name
	 */
	private String parseEndpoint(String val) throws BindingConfigParseException {
		if (val == null)
			throw new BindingConfigParseException(String.format("Undefined endpoint name"));
		if (val.startsWith("'") && val.endsWith("'")) {
			val = val.replace("'", "");
		}
		if (val.equals("")) {
			throw new BindingConfigParseException(String.format("Empty endpoint name"));
		}
		return val;
	}

	/* Parse the endpoint unit */
	private String parseUnit(String val) {
		if (val.startsWith("'") && val.endsWith("'")) {
			val = val.replace("'", "");
		}
		return val;
	}

}
