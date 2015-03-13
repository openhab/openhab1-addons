/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.nest.NestBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * <p>
 * Nest bindings start with a &lt;, &gt; or =, to indicate if the item receives values from the API (in binding),
 * sends values to the API (out binding), or both (bidirectional binding), respectively.
 * 
 * <p>
 * The first character is then followed by BeanUtils-style property reference:
 * 
 * <p>
 * <code>&lt;property&gt;</code>
 * 
 * <p>
 * <code>property</code> is one of a long list of structures, thermostats or smoke_co_alarms properties 
 * that you can read and optionally change.
 *
 * @author John Cocula
 * @since 1.7.0
 */
public class NestGenericBindingProvider extends AbstractGenericBindingProvider implements NestBindingProvider {

	private static class NestBindingConfig implements BindingConfig {
		String property;
		boolean inBound = false;
		boolean outBound = false;

		public NestBindingConfig(final String property,
				final boolean inBound, final boolean outBound) {
			this.property = property;
			this.inBound = inBound;
			this.outBound = outBound;
		}

		@Override
		public String toString() {
			return "NestBindingConfig [property=" + this.property + ", inBound=" + this.inBound + ", outBound=" + this.outBound + "]";
		}
	}

	private static Logger logger = LoggerFactory.getLogger(NestGenericBindingProvider.class);

	private static final Pattern CONFIG_PATTERN = Pattern.compile(".\\[(.*)\\]");

	// the first character in the above pattern
	private static final String IN_BOUND = "<";
	private static final String OUT_BOUND = ">";
	private static final String BIDIRECTIONAL = "=";

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "nest";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProperty(final String itemName) {
		NestBindingConfig config = (NestBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.property : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInBound(final String itemName) {
		NestBindingConfig config = (NestBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.inBound : false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOutBound(final String itemName) {
		NestBindingConfig config = (NestBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.outBound : false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(final String context, final Item item, final String bindingConfig)
			throws BindingConfigParseException {
		logger.debug("Processing binding configuration: '{}'", bindingConfig);

		super.processBindingConfiguration(context, item, bindingConfig);

		boolean inBound = false;
		boolean outBound = false;

		if (bindingConfig.startsWith(IN_BOUND)) {
			inBound = true;
		} else if (bindingConfig.startsWith(OUT_BOUND)) {
			outBound = true;
		} else if (bindingConfig.startsWith(BIDIRECTIONAL)) {
			inBound = true;
			outBound = true;
		} else {
			throw new BindingConfigParseException("Item \"" + item.getName() + "\" does not start with " + IN_BOUND
					+ ", " + OUT_BOUND + " or " + BIDIRECTIONAL + ".");
		}

		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);

		if (!matcher.matches() || matcher.groupCount() != 1)
			throw new BindingConfigParseException("Config for item '" + item.getName() + "' could not be parsed.");

		String property = matcher.group(1);

		NestBindingConfig config = new NestBindingConfig(property, inBound, outBound);

		addBindingConfig(item, config);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {

		logger.trace("validateItemType called with bindingConfig={}", bindingConfig);

		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);

		if (!matcher.matches() || matcher.groupCount() != 1)
			throw new BindingConfigParseException("Config for item '" + item.getName() + "' could not be parsed.");

		String property = matcher.group(1);

		logger.trace("validateItemType called with property={}", property);
	}
}
