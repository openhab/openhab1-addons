/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.snmp.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.snmp.SnmpBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * SNMP binding information from it. It registers as a
 * {@link SnmpBindingProvider} service as well.
 * </p>
 * 
 * <p>
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li>
 * <code>{ snmp="<[192.168.2.253:public:.1.3.6.1.2.1.2.2.1.10.10:10000]" }</code>
 * - receives status updates for the given OID</li>
 * <li>
 * <li>
 * <code>{ snmp="<[192.168.2.253:public:.1.3.6.1.2.1.2.2.1.10.10:10000:MAP(abc.map)]" }</code>
 * - receives status updates for the given OID and transforms the result with the MAP file</li>
 * <li>
 * <code>{ snmp="<[192.168.2.253:public:.1.3.6.1.2.1.2.2.1.10.10:0]" }</code> -
 * receives trap updates for the given OID</li>
 * <li>
 * <code>{snmp=">[OFF:192.168.2.252:private:.1.3.6.1.4.1.4526.11.16.1.1.1.3.1.2:2]" }</code>
 * - sets the command OFF to set an integer value 2 to the given OID
 * </ul>
 * </p>
 * 
 * The given config strings are only valid for {@link StringItem}s.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Chris Jackson - modified binding to support polling SNMP OIDs (SNMP
 *         GET) and setting values (SNMP SET).
 * @since 0.9.0
 */
public class SnmpGenericBindingProvider extends AbstractGenericBindingProvider implements SnmpBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(SnmpGenericBindingProvider.class);

	/**
	 * Artificial command for the snmp-in configuration
	 */
	protected static final Command IN_BINDING_KEY = StringType.valueOf("IN_BINDING");

	/** {@link Pattern} which matches a binding configuration part */
	private static final Pattern BASE_CONFIG_PATTERN = Pattern.compile("([<|>|\\*]\\[.*?\\])*");

	/** {@link Pattern} which matches an In-Binding */
	private static final Pattern IN_BINDING_PATTERN = Pattern
			.compile("<\\[([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9]+)\\]");
	private static final Pattern IN_BINDING_PATTERN_TRANSFORM = Pattern
			.compile("<\\[([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9]+):(.*)?\\]");

	/** {@link Pattern} which matches an In-Binding */
	private static final Pattern OUT_BINDING_PATTERN = Pattern
			.compile(">\\[([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9]+)\\]");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "snmp";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof StringItem || item instanceof NumberItem || item instanceof SwitchItem)) {
			throw new BindingConfigParseException(
					"Item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only StringItems, NumberItems and SwitchItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			SnmpBindingConfig newConfig = new SnmpBindingConfig();
			Matcher matcher = BASE_CONFIG_PATTERN.matcher(bindingConfig);

			if (!matcher.matches()) {
				throw new BindingConfigParseException("bindingConfig '" + bindingConfig
						+ "' doesn't contain a valid binding configuration");
			}
			matcher.reset();

			while (matcher.find()) {
				String bindingConfigPart = matcher.group(1);
				if (StringUtils.isNotBlank(bindingConfigPart)) {
					parseBindingConfig(newConfig, item, bindingConfigPart);
				}
			}

			addBindingConfig(item, newConfig);
		} else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
		}
	}

	/**
	 * Parses a SNMP-OUT configuration by using the regular expression
	 * <code>([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9]+)</code>.
	 * Where the groups should contain the following content:
	 * <ul>
	 * <li>1 - Command
	 * <li>2 - url</li>
	 * <li>3 - SNMP community</li>
	 * <li>4 - OID</li>
	 * <li>5 - Value</li>
	 * </ul>
	 * 
	 * Parses a SNMP-IN configuration by using the regular expression
	 * <code>([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9.a-zA-Z]+):([0-9]+)</code>.
	 * Where the groups should contain the following content:
	 * <ul>
	 * <li>1 - url</li>
	 * <li>2 - SNMP community</li>
	 * <li>3 - OID</li>
	 * <li>4 - Refresh interval (ms)</li>
	 * <li>5 - [Optional]transformation rule</li>
	 * </ul>
	 * 
	 * Setting refresh interval to 0 will only receive SNMP traps
	 * 
	 * @param config
	 *            - the Configuration that needs to be updated with the parsing
	 *            results
	 * @param item
	 *            - the Item that this configuration is intended for
	 * @param bindingConfig
	 *            - the configuration string that will be parsed
	 * @throws BindingConfigParseException
	 */
	private void parseBindingConfig(SnmpBindingConfig config, Item item, String bindingConfig)
			throws BindingConfigParseException {

		config.itemType = item.getClass();

		if (bindingConfig != null) {
			Matcher inMatcher = IN_BINDING_PATTERN.matcher(bindingConfig);
			Matcher outMatcher = OUT_BINDING_PATTERN.matcher(bindingConfig);

			// If no matched for the input, try the version with the transformation string
			if(!inMatcher.matches())
				inMatcher = IN_BINDING_PATTERN_TRANSFORM.matcher(bindingConfig);

			if (!outMatcher.matches() && !inMatcher.matches()) {
				throw new BindingConfigParseException(getBindingType()
						+ " binding configuration must consist of four [config=" + inMatcher
						+ "] or five parts [config=" + outMatcher + "]");
			} else {
				SnmpBindingConfigElement newElement = new SnmpBindingConfigElement();
				if (outMatcher.matches()) {
					String commandAsString = outMatcher.group(1).toString();
					newElement.address = GenericAddress.parse("udp:" + outMatcher.group(2).toString() + "/161");
					newElement.community = new OctetString(outMatcher.group(3).toString());
					newElement.oid = new OID(outMatcher.group(4).toString());

					// Only Integer commands accepted at this time.
					newElement.value = new Integer32(Integer.parseInt(outMatcher.group(5).toString()));

					Command command = TypeParser.parseCommand(item.getAcceptedCommandTypes(), commandAsString);
					if (command == null) {
						logger.error("SNMP can't resolve command {} for item {}", commandAsString, item);
					} else {
						config.put(command, newElement);
					}
				} else if (inMatcher.matches()) {
					newElement.address = GenericAddress.parse("udp:" + inMatcher.group(1).toString() + "/161");
					newElement.community = new OctetString(inMatcher.group(2).toString());
					newElement.oid = new OID(inMatcher.group(3).toString());
					newElement.refreshInterval = Integer.valueOf(inMatcher.group(4)).intValue();
					if(inMatcher.groupCount() == 5)
						newElement.setTransformationRule(inMatcher.group(5));

					config.put(IN_BINDING_KEY, newElement);
				}
			}
		} else {
			return;
		}
	}

	/**
	 * @{inheritDoc
	 */
	public List<String> getInBindingItemNames() {
		List<String> inBindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			SnmpBindingConfig snmpConfig = (SnmpBindingConfig) bindingConfigs.get(itemName);
			if (snmpConfig.containsKey(IN_BINDING_KEY)) {
				inBindings.add(itemName);
			}
		}
		return inBindings;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		SnmpBindingConfig config = (SnmpBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public OID getOID(String itemName) {
		SnmpBindingConfig config = (SnmpBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.get(IN_BINDING_KEY).oid : new OID("");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public OID getOID(String itemName, Command command) {
		SnmpBindingConfig config = (SnmpBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.get(command).oid : new OID("");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public Address getAddress(String itemName) {
		SnmpBindingConfig config = (SnmpBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.get(IN_BINDING_KEY).address : null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public Address getAddress(String itemName, Command command) {
		SnmpBindingConfig config = (SnmpBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.get(command).address : null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public Integer32 getValue(String itemName, Command command) {
		SnmpBindingConfig config = (SnmpBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.get(command).value : null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public OctetString getCommunity(String itemName) {
		SnmpBindingConfig config = (SnmpBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.get(IN_BINDING_KEY).community : new OctetString();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public OctetString getCommunity(String itemName, Command command) {
		SnmpBindingConfig config = (SnmpBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.get(command).community : new OctetString();
	}

	/**
	 * @{inheritDoc
	 */
	public TransformationService getTransformationService(String itemName) {
		SnmpBindingConfig config = (SnmpBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.get(IN_BINDING_KEY).transformationService : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRefreshInterval(String itemName) {
		SnmpBindingConfig config = (SnmpBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(IN_BINDING_KEY) != null ? config.get(IN_BINDING_KEY).refreshInterval : 0;
	}

	static class SnmpBindingConfig extends HashMap<Command, SnmpBindingConfigElement> implements BindingConfig {

		private static final long serialVersionUID = 4697146075427676116L;
		Class<? extends Item> itemType;

	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the SNMP binding
	 * provider.
	 */
	static class SnmpBindingConfigElement implements BindingConfig {
		public OID oid;
		public int refreshInterval;
		public OctetString community;
		public Address address;
		public Integer32 value;
		public TransformationService transformationService;
		public String transformationName;
		public String transformationParam;

		@Override
		public String toString() {
			return "SnmpBindingConfigElement [address=" + address.toString() + ", oid=" + oid.toString()
					+ ", refreshInterval=" + refreshInterval + ", community=" + community.toString() + "]";
		}

		public boolean setTransformationRule(String rule) {
			int pos = rule.indexOf('(');
			if (pos == -1)
				return false;

			// Split the transformation rule
			transformationName = rule.substring(0, pos);
			transformationParam = rule.substring(pos + 1, rule.length() - 1);

			BundleContext context = SnmpActivator.getContext();
			
			// Get the transformation service
			transformationService = TransformationHelper.getTransformationService(context, transformationName);
			if (transformationService == null) {
				logger.debug("No transformation service found for {}", transformationName);
				return false;
			}

			
			return true;
		}
		
		public String doTransformation(String value) throws TransformationException {
			if(transformationService == null)
				return value;
			
			return transformationService.transform(transformationParam, value);
		}
	}

	@Override
	public String doTransformation(String itemName, String value) throws TransformationException {
		SnmpBindingConfig config = (SnmpBindingConfig) bindingConfigs.get(itemName);
		if(config == null)
			return value;
		if(config.get(IN_BINDING_KEY) == null)
			return value;

		return config.get(IN_BINDING_KEY).doTransformation(value);
	}
}
