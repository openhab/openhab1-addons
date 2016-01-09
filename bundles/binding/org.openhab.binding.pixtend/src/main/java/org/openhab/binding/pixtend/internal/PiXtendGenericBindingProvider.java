/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pixtend.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.pixtend.PiXtendBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Michael Kolb
 * @since 1.7.1
 */
public class PiXtendGenericBindingProvider extends AbstractGenericBindingProvider implements PiXtendBindingProvider {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PiXtendGenericBindingProvider.class);

	private final Pattern itemBindingPattern = Pattern.compile("([A-Z0-9_]+)");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "pixtend";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		Matcher matcher = itemBindingPattern.matcher(bindingConfig);
		if (matcher.matches()) {

			String sPort = matcher.group(1);
			PiXtendPort port;
			try {
				port = PiXtendPort.valueOf(sPort);
			} catch (IllegalArgumentException e) {
				throw new BindingConfigParseException("Port '" + sPort + "' is unknown. Supported ports are " + PiXtendPort.getEnumNamesList() + " "
						+ e.getMessage());
			}
			addBindingConfig(item, new PiXtendBindingConfig(item, port));

		} else {
			throw new BindingConfigParseException("Item " + item.getName() + " in " + context + "has an invalid binding configuration");
		}

	}

	/**
	 * This is a helper class holding binding specific configuration details
	 *
	 * @author Michael Kolb
	 * @since 1.7.1
	 */
	public class PiXtendBindingConfig implements BindingConfig {
		private Item item;
		private PiXtendPort port;

		public PiXtendPort getPort() {
			return port;
		}

		public Item getItem() {
			return item;
		}

		public PiXtendBindingConfig(Item item, PiXtendPort port) {
			this.item = item;
			this.port = port;
		}
	}

	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// validation is done in processBindingConfiguration
	}

	public List<String> getItemsForDataPort(PiXtendPort port) {
		List<String> result = new LinkedList<String>();
		for (BindingConfig element : bindingConfigs.values()) {
			if (element instanceof PiXtendBindingConfig) {
				PiXtendBindingConfig config = (PiXtendBindingConfig) element;
				if (config.port == port) {
					result.add(config.getItem().getName());
				}
			}
		}
		return result;
	}

	public PiXtendPort getOutPort(String itemName) {
		BindingConfig config = bindingConfigs.get(itemName);
		if (config instanceof PiXtendBindingConfig) {
			return ((PiXtendBindingConfig) config).getPort();
		}
		return null;
	}

}
